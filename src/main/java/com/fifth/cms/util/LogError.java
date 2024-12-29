package com.fifth.cms.util;

public class LogError {

    public void writeErrorLog(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(this.getClass().getName()) && element.getMethodName().equals("accIdCount")) {
                int lineNumber = element.getLineNumber();
                String errorMessage = e.getMessage().split("\n")[0];
                System.err.println("Error in method accIdCount at line " + lineNumber + ": " + errorMessage);
                // 여기서 에러 정보를 파일이나 데이터베이스에 저장할 수 있습니다.
                break;
            }
        }
    }
}
