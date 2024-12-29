package com.fifth.cms.model.admin;

public class SkinTypeVO {

	
	private Integer uid;
	private String skinName;
	private String code;
	private String tCode;
	private String comment;

	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

	public String getSkinName() { return skinName; }
	public void setSkinName(String skinName) { this.skinName = skinName; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public String getTCode() { return tCode; }
	public void setTCode(String tCode) { this.tCode = tCode; }

	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }

	@Override
	public String toString() {
		return "SkinTypeVO{" +
				"uid=" + uid +
				", skinName='" + skinName + '\'' +
				", code='" + code + '\'' +
				", tCode='" + tCode + '\'' +
				", comment='" + comment + '\'' +
				'}';
	}

}




 