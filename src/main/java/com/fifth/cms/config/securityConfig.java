package com.fifth.cms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fifth.cms.security.loginAuthenticationEntryPoint;
import com.fifth.cms.security.loginDeniedHandler;
import com.fifth.cms.security.loginFailedHandler;
import com.fifth.cms.security.loginSuccessHandler;
import com.fifth.cms.security.filter.JsonAuthentication;
import com.fifth.cms.security.filter.JwtAuthenticationFilter;
import com.fifth.cms.service.login.CustomAuthenticationProvider;
import com.fifth.cms.service.login.loginHomeServiceImpl;
import com.fifth.cms.service.login.access.AccessService;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class securityConfig {

    private final loginHomeServiceImpl loginHomeService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final loginSuccessHandler loginSuccessHandler;
    private final loginFailedHandler loginFailedHandler;
    private final loginDeniedHandler loginDeniedHandler;
    private final loginAuthenticationEntryPoint loginAuthenticationEntryPoint;
    private final CorsFilter corsFilter;
	private final AccessService accessService;

    public securityConfig(loginHomeServiceImpl loginHomeService,
                          CustomAuthenticationProvider customAuthenticationProvider,
                          loginSuccessHandler loginSuccessHandler,
                          loginFailedHandler loginFailedHandler,
                          loginDeniedHandler loginDeniedHandler,
                          loginAuthenticationEntryPoint loginAuthenticationEntryPoint,
                          CorsFilter corsFilter,
						  AccessService accessService) {
        this.loginHomeService = loginHomeService;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailedHandler = loginFailedHandler;
        this.loginDeniedHandler = loginDeniedHandler;
        this.loginAuthenticationEntryPoint = loginAuthenticationEntryPoint;
        this.corsFilter = corsFilter;
		this.accessService = accessService;
    }

    //json 형식으로 로그인 요청을 처리하는 필터, request 본문에서 데이터를 읽어와 인증 요청을 처리
	/*
	해당 형식과 같이 ben으로 등록하는 경우 모든 요청에 대하여 해당 bean이 호출되어 인증 처리가 되는 문제가 있음.
    @Bean
    public JsonAuthentication jsonAuthentication(
        @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
        BCryptPasswordEncoder passwordEncoder
    ) throws Exception {
        return new JsonAuthentication(authenticationManager, passwordEncoder);
    }
 	*/

	 private void configureCommonSecurity(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.cors(cors -> cors.configurationSource(corsFilter.corsConfigurationSource())) // CORS 설정 활성화
			// 모든 경로에 security 검증을 적용할지 수행한다. 이 설정은 formLogin과 같은 메소드에만 적용되며, 필터와는 무관하다.
			// authorizeRequests 경로 설정을 추가해도 addFilterBefore와 같은 필터 경로 검증을 수행하게 된다.
			.authorizeHttpRequests(authorizeRequests -> 
				authorizeRequests
					.requestMatchers("/api/auth.go").hasAuthority("admin") // /auth.go 경로에 대해 admin 권한 필요
					.requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
					.anyRequest().authenticated()
			)
			//axios 요청 시 로그인 인증 처리
			.addFilterBefore(new JsonAuthentication(authenticationManager, bCryptPasswordEncoder(), accessService), UsernamePasswordAuthenticationFilter.class)
			//인증 필요한 사이트 접속시, jwt 요청 시 인증 처리
			.addFilterBefore(new JwtAuthenticationFilter(accessService), UsernamePasswordAuthenticationFilter.class)
			.formLogin(form -> {
				form.usernameParameter("id");
				form.passwordParameter("pw");
				form.loginProcessingUrl("/api/accLogin.go");
				form.successHandler(loginSuccessHandler); // 로그인 성공 핸들러 설정
				form.failureHandler(loginFailedHandler);
                // form.defaultSuccessUrl("/home", true); // 로그인 성공 시 리다이렉트할 URL 설정 (제거)
			})
			.exceptionHandling(exception -> exception
				.accessDeniedHandler(loginDeniedHandler)
			)
			.userDetailsService(loginHomeService)
			//Axios 인증 요청에서도 customAuthenticationProvider가 호출되는 이유는 Spring Security의 인증 흐름 때문입니다.
			//Axios 인증 요청은 기본적으로 폼 로그인 프로세스를 거치지 않기 때문에, 
			//customAuthenticationProvider가 호출되어 인증 처리를 수행합니다.
			.authenticationProvider(customAuthenticationProvider);
	}

    // 인증 관리자를 주입하여 인증 관리자 빈을 생성
	@Bean
	@Profile("dev")
	public SecurityFilterChain devSecurityFilterChain(HttpSecurity http, @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) throws Exception {
		configureCommonSecurity(http, authenticationManager);
		// dev 환경에 특화된 설정 추가 가능
		return http.build();
	}

	@Bean
	@Profile("aws")
	public SecurityFilterChain awsSecurityFilterChain(HttpSecurity http, @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) throws Exception {
		configureCommonSecurity(http, authenticationManager);
		http.requiresChannel(channel -> channel.anyRequest().requiresSecure()); // AWS 환경에 특화된 설정
		return http.build();
	}

    // 인증 관리자를 주입하여 인증 관리자 빈을 생성
    @Primary
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 비밀번호 인코더를 생성하여 비밀번호 인코더 빈을 생성
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
