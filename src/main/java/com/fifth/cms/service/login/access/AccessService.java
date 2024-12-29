package com.fifth.cms.service.login.access;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.login.AccessMapper;
import com.fifth.cms.model.login.AccessVO;

@Service
public class AccessService {

	private final AccessMapper accessMapper;

	public AccessService(AccessMapper accessMapper) {
		this.accessMapper = accessMapper;
	}

	public Integer checkAccess(HashMap<String, String> accessInfoMap) {
		return accessMapper.checkAccess(accessInfoMap);
	}

	public AccessVO checkAdminOne(HashMap<String, String> accessInfoMap) {
		return accessMapper.checkAdminOne(accessInfoMap);
	}

	public AccessVO selectAccessOne(AccessVO accessVO) {
		return accessMapper.selectAccessOne(accessVO);
	}

	public AccessVO selectAccessOneforCode(String code) {
		return accessMapper.selectAccessOneforCode(code);
	}

	public void updateRefreshToken(AccessVO accessVO) {
		accessMapper.updateRefreshToken(accessVO);
	}

	public Integer updateBlankRefreshToken(HashMap<String, String> stringJson) {
		return accessMapper.updateBlankRefreshToken(stringJson);
	}

}
