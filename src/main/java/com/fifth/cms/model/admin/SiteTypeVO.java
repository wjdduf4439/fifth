package com.fifth.cms.model.admin;

public class SiteTypeVO {

	
	private Integer uid;
	private String formName;
	private String code;
	private String comment;

	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

	public String getFormName() { return formName; }
	public void setFormName(String formName) { this.formName = formName; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }

	@Override
	public String toString() {
		return "SiteTypeVO{" +
				"uid=" + uid +
				", formName='" + formName + '\'' +
				", code='" + code + '\'' +
				", comment='" + comment + '\'' +
				'}';
	}

}




 