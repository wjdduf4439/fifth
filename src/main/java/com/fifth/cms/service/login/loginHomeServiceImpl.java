package com.fifth.cms.service.login;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.login.AccessMapper;
import com.fifth.cms.model.login.AccessVO;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

/*

	UserDetailsService 인터페이스:
	
	UserDetailsService는 사용자의 정보를 로드하는 데 사용되는 인터페이스입니다.
	loadUserByUsername(String username) 메서드를 구현하여 사용자 이름(또는 ID)을 기반으로 사용자 정보를 가져와 UserDetails 객체로 반환합니다.
	반환된 UserDetails 객체는 사용자의 자격 증명을 포함하고 있습니다. (UserDetails는 주로 User 클래스를 구현하게 됩니다.)
	주로 사용자의 정보를 데이터베이스나 다른 소스에서 가져와 반환하는 역할을 합니다.
	UserDetailsService를 이용하여 사용자 정보를 검색하고, AuthenticationProvider에서는 이 정보를 기반으로 실제 인증을 수행합니다.
	
	간단히 말해서, AuthenticationProvider는 사용자의 인증을 처리하고, UserDetailsService는 사용자 정보를 로드하는 역할을 합니다. 
		일반적으로, AuthenticationProvider는 사용자가 입력한 자격 증명을 확인하고, 
		UserDetailsService는 해당 사용자의 세부 정보를 가져옵니다. 
	두 인터페이스를 함께 사용하여 전체적인 인증 프로세스를 완성할 수 있습니다.

*/
@Service
@RequiredArgsConstructor
public class loginHomeServiceImpl implements UserDetailsService {
	
	AccessMapper accessMapper;
	
	public loginHomeServiceImpl(AccessMapper accessMapper) {
		this.accessMapper = accessMapper;
	}
	
	public AccessVO accLoginHome( AccessVO accessVO ) throws Exception {
		
		System.out.println("access Login Service");
		AccessVO registedLoginHomeVO = accessMapper.selectAccessOne(accessVO);
		
		return registedLoginHomeVO;
	}

	/*
		AuthenticationProvider 구현을 하지 않으면 비밀번호 인증을 못하고, BadCredentialsException이 발생하게 된다
		인증 절차 인터페이스 구현
		https://to-dy.tistory.com/87
	*/
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		System.out.println("access loadUserByUsername");
		
		
		AccessVO accessVO = new AccessVO();
		accessVO.setId(id);
		
		System.out.println("id : " + id);
		//System.out.println("accessVO : " + accessVO);
		
		AccessVO loginHomeVO = new AccessVO();
		try {
			loginHomeVO = accessMapper.selectAccessOne(accessVO);

			if(loginHomeVO == null) {
				throw new UsernameNotFoundException( loginHomeVO.getId() );
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("loadUserByUsername 종료 : " + loginHomeVO);
		return loginHomeVO;
	}
	
	
}
