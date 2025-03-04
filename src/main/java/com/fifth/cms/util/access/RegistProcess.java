package com.fifth.cms.util.access;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class RegistProcess {
    
    public String shaHashing(String accessId, String uid) {

        String result = "";

        try {
            String input = accessId + uid;
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            result = hexString.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            result = "알고리즘 오류";
        }

        return result;
    }


}
