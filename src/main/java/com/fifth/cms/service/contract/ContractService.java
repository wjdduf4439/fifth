package com.fifth.cms.service.contract;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.contract.ContractMapper;
import com.fifth.cms.model.contract.ContractVO;	

@Service
public class ContractService {

	private final ContractMapper contractMapper;
	
	public ContractService(ContractMapper contractMapper) {
		this.contractMapper = contractMapper;
	}

	public List<ContractVO> selectContractList(HashMap<String, String> stringJson) {

		HashMap<String, Object> ObjectJson = new HashMap<String, Object>();
		ObjectJson.put("startPoint", Integer.parseInt(stringJson.get("startPoint")) );
		ObjectJson.put("limit", Integer.parseInt(stringJson.get("limit")) );

		return contractMapper.selectContractList(ObjectJson);
	}

	public Integer selectContractCount(HashMap<String, String> stringJson) {
		return contractMapper.selectContractCount(stringJson);	
	}

	public String selectContractMaxCode() {
		return contractMapper.selectContractMaxCode();
	}	

	public Integer insertContract(HashMap<String, String> stringJson) {
		return contractMapper.insertContract(stringJson);
	}
	
}
