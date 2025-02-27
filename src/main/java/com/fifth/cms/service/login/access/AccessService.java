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

	public AccessVO checkNickName(HashMap<String, String> stringJson) {
		return accessMapper.checkNickName(stringJson);
	}

	public AccessVO checkEmail(HashMap<String, String> stringJson) {
		return accessMapper.checkEmail(stringJson);
	}

	public AccessVO selectAccessOne(AccessVO accessVO) {
		return accessMapper.selectAccessOne(accessVO);
	}

	public AccessVO selectAccessOneforUid(String uid) {
		return accessMapper.selectAccessOneforUid(uid);
	}

	public Integer insertAccount(HashMap<String, String> stringJson) {
		return accessMapper.insertAccount(stringJson);
	}

	public void updateRefreshToken(AccessVO accessVO) {
		accessMapper.updateRefreshToken(accessVO);
	}

	public Integer updateBlankRefreshToken(HashMap<String, String> stringJson) {
		return accessMapper.updateBlankRefreshToken(stringJson);
	}

}
