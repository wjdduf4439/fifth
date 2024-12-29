package com.fifth.cms.mapper.contract;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fifth.cms.model.contract.ContractVO;

@Mapper
public interface ContractMapper {
	
	public List<ContractVO> selectContractList(HashMap<String, Object> ObjectJson);

	public String selectContractMaxCode();

	public Integer selectContractCount(HashMap<String, String> stringJson);

	public Integer insertContract(HashMap<String, String> stringJson);

}
