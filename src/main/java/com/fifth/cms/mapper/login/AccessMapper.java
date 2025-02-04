package com.fifth.cms.mapper.login;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.login.AccessVO;

import java.util.HashMap;

//springboot는 mapper어노테이션으로 별도로 db와 연동할 수 있다.
@Mapper
public interface AccessMapper {

	public Integer accIdCount(AccessVO accessVO);

	public Integer accIdCount(HashMap<String, String> stringJson);

	public String accCodeMax(AccessVO accessVO);

	public String accCodeMax(HashMap<String, String> stringJson);

	public AccessVO selectAccessOne(AccessVO accessVO);

	public AccessVO selectAccessOneforCode(String code);

	public Integer checkAccess(HashMap<String, String> accessInfoMap);

	public AccessVO checkAdminOne(HashMap<String, String> accessInfoMap);

	public Integer insertAccount(AccessVO accessVO);

	public Integer insertAccount(HashMap<String, String> stringJson);

	public Integer updateRefreshToken(AccessVO accessVO);

	public Integer updateBlankRefreshToken(HashMap<String, String> stringJson);
}
