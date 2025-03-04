package  com.fifth.cms.model.login;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccessVO implements UserDetails {

	private Integer uid;
	private String id;
	private String pw;
	private String nick;
	/*
		1. Role 기반 접근 제어:
		특정 URL에 대한 접근을 제한할 때 사용됩니다. 예를 들어, 관리자만 접근할 수 있는 페이지를 설정할 수 있습니다.
		2. Authority 기반 접근 제어:
		더 세밀한 접근 제어를 위해 사용됩니다. 특정 작업이나 리소스에 대한 접근을 제한할 때 사용됩니다.
	*/
	private String email;
	private String accessCode;
	private String role;
	private String authority;
	private String refreshToken;
	private String approve;
	
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	/*

	db에 auth에 대응되는 값을 설정하고 해당 값을 조회할 수 있도록 할것
	
	*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		System.out.println("UserDetails authority 설정 : " + authority);
		auth.add(new SimpleGrantedAuthority(authority));
		return auth;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return pw;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return id;
	}
	//false 설정시 User account has expired 에러 발생
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//false 설정시 User account is locked 에러 발생
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	//false 설정시 Bad credentials 에러 발생
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//false 설정시 User is disabled 에러 발생
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public String toString() {
		return "AccessVO{" +
				"id='" + id + '\'' +
				", pw='" + pw + '\'' +
				", nick='" + nick + '\'' +
				", role='" + role + '\'' +
				", authority='" + authority + '\'' +
				", refreshToken='" + refreshToken + '\'' +
				", approve='" + approve + '\'' +
				'}';
	}
	
}
