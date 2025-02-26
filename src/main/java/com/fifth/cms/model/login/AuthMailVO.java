package com.fifth.cms.model.login;

import java.util.Date;

public class AuthMailVO {
    
    private Integer uid;
    private String authkey;
    private String email;
    private Date signdate;

    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }

    public String getAuthkey() { return authkey; }
    public void setAuthkey(String authkey) { this.authkey = authkey; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getSigndate() { return signdate; }
    public void setSigndate(Date signdate) { this.signdate = signdate; }
    
}
