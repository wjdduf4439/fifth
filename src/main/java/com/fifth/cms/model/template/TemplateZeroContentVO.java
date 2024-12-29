package com.fifth.cms.model.template;

public class TemplateZeroContentVO {

	private Integer pid;
	private Integer uid;
    private String context;

    public Integer getPid() { return pid; }
    public void setPid(Integer pid) { this.pid = pid; }

    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    @Override
    public String toString() {
        return "TemplateZeroContentVO{" +
                "pid=" + pid +
                ", uid=" + uid +
                ", context='" + context + '\'' +
                '}';
    }

}
