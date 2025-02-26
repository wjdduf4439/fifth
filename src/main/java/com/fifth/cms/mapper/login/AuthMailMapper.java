package com.fifth.cms.mapper.login;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.login.AuthMailVO;

@Mapper
public interface AuthMailMapper {
    public AuthMailVO getAuthMailInfo(HashMap<String, String> stringJson);

    public Integer insertAuthMail(AuthMailVO authMailVO);

    public Integer deleteAuthMail(String uid);
}
