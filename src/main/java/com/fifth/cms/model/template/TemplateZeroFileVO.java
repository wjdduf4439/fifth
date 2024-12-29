package com.fifth.cms.model.template;

public class TemplateZeroFileVO {
	
	private Integer	uid;
	private String	code;
	private String	pid;
	private int		fsign;
	private String	fpath;
	private String	savingFname;
	private String	fname;

	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public String getPid() { return pid; }
	public void setPid(String pid) { this.pid = pid; }

	public int getFsign() { return fsign; }
	public void setFsign(int fsign) { this.fsign = fsign; }

	public String getFpath() { return fpath; }
	public void setFpath(String fpath) { this.fpath = fpath; }

	public String getSavingFname() { return savingFname; }
	public void setSavingFname(String savingFname) { this.savingFname = savingFname; }

	public String getFname() { return fname; }
	public void setFname(String fname) { this.fname = fname; }

	@Override
	public String toString() {
		return "TemplateZeroFileVO{" +
				"uid=" + uid +
				", code='" + code + '\'' +
				", pid='" + pid + '\'' +
				", fsign=" + fsign +
				", fpath='" + fpath + '\'' +
				", savingFname='" + savingFname + '\'' +
				", fname='" + fname + '\'' +
				'}';
	}

}
