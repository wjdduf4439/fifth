package com.fifth.cms.util;	

public class ValidatingValue {

	//true == 통과
	//false == 막힘
	
	public boolean validatingStringIsNotNull(String mstringValue) {
		boolean resultBoolean = false;
		
		if( null !=  mstringValue ) resultBoolean = true;
		
		return resultBoolean;
	}
	
	public boolean validatingStringIsNull(String mstringValue) {
		boolean resultBoolean = false;
		
		if( null ==  mstringValue) resultBoolean = true;
		
		return resultBoolean;
	}
	
	public boolean validatingStringIsNotBlank(String mstringValue) {
		boolean resultBoolean = false;
		
		if( !"".equals(mstringValue) ) resultBoolean = true;
			
		
		return resultBoolean;
	}
	
	public boolean validatingStringIsBlank(String mstringValue) {
		boolean resultBoolean = false;
		
		if( "".equals(mstringValue) ) resultBoolean = true;
		
		return resultBoolean;
	}	
	
	public Integer validatingStringIndexOf(String mString, String mregString) {
		Integer result = -1;
		
		result = mString.indexOf(mregString);
		
		return result;
	}
	
	public Integer validatingStringIndexOf(String mString, String[] mregString) {
		Integer result = -1;
		
		for(int i_mregString = 0; i_mregString < mregString.length; i_mregString++) {

			result = mString.indexOf(mregString[i_mregString]);
			if(result > 0 ){ return result; }
			
		}
		
		return result;
	}
	
}
