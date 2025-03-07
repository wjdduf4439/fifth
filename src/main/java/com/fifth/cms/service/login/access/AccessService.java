package com.fifth.cms.service.login.access;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.login.AccessMapper;
import com.fifth.cms.model.login.AccessVO;

@Service
public class AccessService {

	private final AccessMapper accessMapper;

	public AccessService(AccessMapper accessMapper) {
		this.accessMapper = accessMapper;
	}

	public List<AccessVO> selectAccessList(HashMap<String, String> stringJson) {
		return accessMapper.selectAccessList(stringJson);
	}

	public Integer checkAccess(HashMap<String, String> accessInfoMap) {
		return accessMapper.checkAccess(accessInfoMap);
	}

	public AccessVO selectAccessOne(HashMap<String, String> stringJson) {
		return accessMapper.selectAccessOne(stringJson);
	}

	public AccessVO selectAccessOneApproved(HashMap<String, String> accessInfoMap) {
		AccessVO accessVO = new AccessVO();

		accessVO.setId(accessInfoMap.get("id"));
		accessVO.setApprove(accessInfoMap.get("approve"));

		return accessMapper.selectAccessOneApproved(accessVO);
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

	public AccessVO selectAccessOneforUid(String uid) {
		return accessMapper.selectAccessOneforUid(uid);
	}

	public Integer insertAccount(HashMap<String, String> stringJson) {
		return accessMapper.insertAccount(stringJson);
	}

	public Integer updateRefreshToken(AccessVO accessVO) {
		return accessMapper.updateRefreshToken(accessVO);
	}

	public Integer updateApprove(HashMap<String, Object> approveMap) {
		return accessMapper.updateApprove(approveMap);
	}

	public Integer updateAccount(HashMap<String, String> stringJson) {
		return accessMapper.updateAccount(stringJson);
	}

	public Integer updateBlankRefreshToken(HashMap<String, String> stringJson) {
		return accessMapper.updateBlankRefreshToken(stringJson);
	}

	public Integer updateAccessCode(HashMap<String, String> stringJson) {
		return accessMapper.updateAccessCode(stringJson);
	}

	public Integer disableApprove(HashMap<String, String> stringJson) {
		return accessMapper.disableApprove(stringJson);
	}

	public Integer deleteAccount(HashMap<String, String> stringJson) {
		return accessMapper.deleteAccount(stringJson);
	}

}
