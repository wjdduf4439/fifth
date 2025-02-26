package com.fifth.cms.model.login;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class FifthAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private Integer uid;
	private String nick;

	public FifthAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities, String nickname, Integer uid) {
		super(principal, credentials, authorities);
		this.nick = nickname;
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public Integer getUid() {
		return uid;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("Principal=").append(getPrincipal()).append(", ");
		sb.append("Credentials=[PROTECTED], ");
		sb.append("Authenticated=").append(isAuthenticated()).append(", ");
		sb.append("Details=").append(getDetails()).append(", ");
		sb.append("Granted Authorities=").append(this.getAuthorities()).append(", ");
		sb.append("nickname=").append(nick).append(", ");
		sb.append("uid=").append(uid);
		sb.append("]");

		return sb.toString();
	}
}
