package com.fifth.cms.config;

import java.io.File;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fifth.cms.filter.XssFilter;
import com.fifth.cms.interceptor.AccessInterCeptor;
import com.fifth.cms.interceptor.AdminInterCeptor;
import com.fifth.cms.service.login.access.AccessService;

/*스프링 컨테이너는 @Configuration이 붙어있는 클래스를 자동으로 빈으로 등록해두고, 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈을 생성해준다.
하지만 어떤 임의의 클래스를 만들어서 @Bean 어노테이션을 붙인다고 되는 것이 아니고,
@Bean을 사용하는 클래스에는 반드시 @Configuration 어노테이션을 활용하여 해당 클래스에서 Bean을 등록하고자 함을 명시해주어야 한다.
스프링 빈으로 등록된 다른 클래스 안에서 @Bean으로 직접 빈을 등록해주는 것도 동작은 한다. 
하지만 @Configuration 안에서 @Bean을 사용해야 싱글톤을 보장받을 수 있으므로 @Bean 어노테이션은 반드시 @Configuration과 함께 사용해주어야 한다.
@Bean은 반드시 @Configuration 안에서 사용되어야 한다. 
*/
@Configuration
public class servletConfig implements WebMvcConfigurer {

	// Interceptor에서 제외되는 URL 주소
	private static final String[] EXCLUDE_PATHS = { "/contract/**", "/log/**" };
	private final Environment environment;

	private final AccessService accessService;

	public servletConfig(Environment environment, AccessService accessService) {
		this.environment = environment;
		this.accessService = accessService;
	}

	//ApplicationRunner는 Spring Boot에서 애플리케이션이 시작될 때 특정 작업을 수행하기 위해 사용하는 인터페이스입니다.
	//ApplicationRunner를 구현하거나 반환하는 메서드를 정의하면, 애플리케이션 컨텍스트가 완전히 초기화된 후에 실행할 수 있는 코드를 작성할 수 있습니다.
	@Bean
	public ApplicationRunner showLoggingEnvPoint() {
		return args -> {
			String envPoint = environment.getProperty("ENVPOINT");
			System.out.println("SERVER ENVPOINT: " + envPoint);
			
			// 환경 변수에서 STORAGEPATH 가져오기
            String basePath = environment.getProperty("STORAGEPATH", "/home/storage/");
			String[] directories = {"template", "codehead/options"};

			// 각 디렉터리 존재 여부 확인 및 생성
			for (String dir : directories) {
				File directory = new File(basePath + dir);
				if (!directory.exists()) {
					boolean created = directory.mkdirs();
					if (created) {
						System.out.println("디렉터리 생성됨: " + directory.getPath());
					} else {
						System.out.println("디렉터리 생성 실패: " + directory.getPath());
					}
				} else {
					System.out.println("디렉터리 이미 존재함: " + directory.getPath());
				}
			}

		};
	}

	@Bean
	FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean searchFilterRegistrationBean = new FilterRegistrationBean();
		searchFilterRegistrationBean.setFilter(new XssFilter());
		searchFilterRegistrationBean.addUrlPatterns("/*");
		searchFilterRegistrationBean.setOrder(1);

		return searchFilterRegistrationBean;
	}

	@Bean
	RequestMappingHandlerMapping mappingHandlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	// 인터셉터 등록 빈
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccessInterCeptor(accessService))
				.addPathPatterns("/admin/**")
				.excludePathPatterns(EXCLUDE_PATHS);
		registry.addInterceptor(new AdminInterCeptor(accessService))
				.addPathPatterns("/admin/**")
				.excludePathPatterns(EXCLUDE_PATHS);
	}

	@Bean
	MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}

}