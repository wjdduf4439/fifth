package com.fifth.cms.filter;

public class MybatisXSSFilter {
	

	// 필터링할 문자와 예약어를 정규식으로 정의합니다.
    private static final String[] regexMybatisXSSFilter = {
		"'", "\"", "=", "&", "|", "!", "(", ")", "{", "}", "$", "%", "@", "#", 
        "UNION", "SELECT", "THEN", "IF", "INSTANCE", "END", "COLUMN",
        "DATABASE()", "CONCAT()", "COUNT()", "LOWER()"	
    };

	public static String sanitizeDDL(String param) {
        if (param == null || param.isEmpty()) {
            return "";
        }
        
        for(String filterTargetString : regexMybatisXSSFilter ) param = param.replace(filterTargetString, "");
        
        // 정규식을 사용하여 필터링합니다.
        return param;
    }
    		
	
    public static String sanitizeOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isEmpty()) {
            return "";
        }
        
        for(String filterTargetString : regexMybatisXSSFilter ) orderBy = orderBy.replace(filterTargetString, "");
        
        // 정규식을 사용하여 필터링합니다.
        return orderBy;
    }
    
    public static String sanitizeLimit(String limit) {
        if (limit == null || limit.isEmpty()) {
            return "";
        }
        
        // 필터링할 문자와 예약어를 정규식으로 정의합니다.
        for(String filterTargetString : regexMybatisXSSFilter ) limit = limit.replace(filterTargetString, "");
        
        // 정규식을 사용하여 필터링합니다.
        return limit;
    }
    
}