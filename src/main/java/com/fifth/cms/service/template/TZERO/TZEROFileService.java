package com.fifth.cms.service.template.TZERO;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fifth.cms.mapper.template.TZERO.TZEROFileMapper;
import com.fifth.cms.model.template.TemplateZeroFileVO;

@Service
public class TZEROFileService {
	

	private final TZEROFileMapper tzeroFileMapper;

	public TZEROFileService(TZEROFileMapper tzeroFileMapper) {
		this.tzeroFileMapper = tzeroFileMapper;
	}
	
	public TemplateZeroFileVO getFile(String codeHead, String uid) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("codeHead", codeHead);
		valueMap.put("uid", uid);

		return tzeroFileMapper.getFile(valueMap);
	}
	
	public TemplateZeroFileVO getFile(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.getFile(stringJson);
	}

	public List<TemplateZeroFileVO> getContextImageFileList(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.getContextImageFileList(stringJson);
	}
	
	public Integer getUploadedFileCount( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.getUploadedFileCount(stringJson);
	}
	
	public String selectFileRecordListMax(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub 
		return tzeroFileMapper.selectFileRecordListMax(stringJson);
	}
	
	public Integer insertFileRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.insertFileRecord( stringJson );
	}

	public Integer insertContextImageFile(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.insertContextImageFile(stringJson);
	}
	
	public Integer deleteFileRecord( HashMap<String, String> stringJson ) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.deleteFileRecord( stringJson );
	}

	public Integer deleteContextImageFile(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.deleteContextImageFile(stringJson);
	}

	public Integer deletePostedContextImageFile(HashMap<String, String> stringJson) throws Exception {
		// TODO Auto-generated method stub
		return tzeroFileMapper.deletePostedContextImageFile(stringJson);
	}

}
