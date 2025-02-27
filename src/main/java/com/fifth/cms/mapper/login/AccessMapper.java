package com.fifth.cms.mapper.login;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.login.AccessVO;

//springboot는 mapper어노테이션으로 별도로 db와 연동할 수 있다.
@Mapper
public interface AccessMapper {

	public List<AccessVO> selectAccessList(HashMap<String, String> stringJson);

	public Integer accIdCount(AccessVO accessVO);

	public Integer accIdCount(HashMap<String, String> stringJson);

	public AccessVO selectAccessOne(HashMap<String, String> stringJson);

	public AccessVO selectAccessOneApproved(AccessVO accessVO);

	public AccessVO selectAccessOneforUid(String uid);

	public Integer checkAccess(HashMap<String, String> accessInfoMap);

	public AccessVO checkAdminOne(HashMap<String, String> accessInfoMap);

	public AccessVO checkNickName(HashMap<String, String> stringJson);

	public AccessVO checkEmail(HashMap<String, String> stringJson);

	public Integer insertAccount(AccessVO accessVO);

	public Integer insertAccount(HashMap<String, String> stringJson);

	public Integer updateRefreshToken(AccessVO accessVO);

	public Integer updateBlankRefreshToken(HashMap<String, String> stringJson);

	public Integer updateApprove(HashMap<String, Object> approveMap);

	public Integer updateAccount(HashMap<String, String> stringJson);

	public Integer disableApprove(HashMap<String, String> stringJson);

	public Integer deleteAccount(HashMap<String, String> stringJson);
}
