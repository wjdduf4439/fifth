package com.fifth.cms.model.template;

import java.util.Date;
import java.util.List;

public class TemplateZeroVO {

	private Integer uid;
    private String code;
	private String codeHead;
    private String del_chk;
    private String notice_chk;
    private String title;
    private String writerNick;
    private int viewNum;
    private int like;
    private int dislike;
    private Date frstRegistPnttm;
    private String frstRegistNm;
    private Date lastUpdtPnttm;
    private String lastUpdtNm;

	private TemplateZeroContentVO contentVO;
	private List<TemplateZeroFileVO> fileVO;

	public TemplateZeroVO() {
		this.del_chk = "N";
		this.notice_chk = "N";
	}

	public TemplateZeroVO(Integer uid, String code, String codeHead, String del_chk, String notice_chk, String title, String writerNick, int viewNum, int like, int dislike, Date frstRegistPnttm, String frstRegistNm, Date lastUpdtPnttm, String lastUpdtNm, TemplateZeroContentVO contentVO) {
		this.uid = uid;
		this.code = code;
		this.codeHead = codeHead;
		this.del_chk = del_chk;
		this.notice_chk = notice_chk;
		this.title = title;
		this.writerNick = writerNick;
		this.viewNum = viewNum;
		this.like = like;
		this.dislike = dislike;
		this.frstRegistPnttm = frstRegistPnttm;
		this.frstRegistNm = frstRegistNm;
		this.lastUpdtPnttm = lastUpdtPnttm;
		this.lastUpdtNm = lastUpdtNm;
		this.contentVO = contentVO;
	}

	public TemplateZeroVO(Integer uid, String code, String codeHead, String del_chk, String notice_chk, String title, String writerNick, int viewNum, int like, int dislike, Date frstRegistPnttm, String frstRegistNm, Date lastUpdtPnttm, String lastUpdtNm, TemplateZeroContentVO contentVO, List<TemplateZeroFileVO> fileVO) {
		this.uid = uid;
		this.code = code;
		this.codeHead = codeHead;
		this.del_chk = del_chk;
		this.notice_chk = notice_chk;
		this.title = title;
		this.writerNick = writerNick;
		this.viewNum = viewNum;
		this.like = like;
		this.dislike = dislike;
		this.frstRegistPnttm = frstRegistPnttm;
		this.frstRegistNm = frstRegistNm;
		this.lastUpdtPnttm = lastUpdtPnttm;
		this.lastUpdtNm = lastUpdtNm;
		this.contentVO = contentVO;
		this.fileVO = fileVO;	
	}

	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

	public String getCodeHead() { return codeHead; }
	public void setCodeHead(String codeHead) { this.codeHead = codeHead; }

    public String getDel_chk() { return del_chk; }
    public void setDel_chk(String del_chk) { this.del_chk = del_chk; }

    public String getNotice_chk() { return notice_chk; }
    public void setNotice_chk(String notice_chk) { this.notice_chk = notice_chk; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getWriterNick() { return writerNick; }
    public void setWriterNick(String writerNick) { this.writerNick = writerNick; }

    public int getViewNum() { return viewNum; }
    public void setViewNum(int viewNum) { this.viewNum = viewNum; }

    public int getLike() { return like; }
    public void setLike(int like) { this.like = like; }

    public int getDislike() { return dislike; }
    public void setDislike(int dislike) { this.dislike = dislike; }

    public Date getFrstRegistPnttm() { return frstRegistPnttm; }
    public void setFrstRegistPnttm(Date frstRegistPnttm) { this.frstRegistPnttm = frstRegistPnttm; }

    public String getFrstRegistNm() { return frstRegistNm; }
    public void setFrstRegistNm(String frstRegistNm) { this.frstRegistNm = frstRegistNm; }

    public Date getLastUpdtPnttm() { return lastUpdtPnttm; }
    public void setLastUpdtPnttm(Date lastUpdtPnttm) { this.lastUpdtPnttm = lastUpdtPnttm; }

    public String getLastUpdtNm() { return lastUpdtNm; }
    public void setLastUpdtNm(String lastUpdtNm) { this.lastUpdtNm = lastUpdtNm; }

	public TemplateZeroContentVO getContentVO() { return contentVO; }
	public void setContentVO(TemplateZeroContentVO contentVO) { this.contentVO = contentVO; }

	public List<TemplateZeroFileVO> getFileVO() { return fileVO; }
	public void setFileVO(List<TemplateZeroFileVO> fileVO) { this.fileVO = fileVO; }

	@Override
	public String toString() {
		return "TemplateZeroVO{" +
				"code='" + code + '\'' +
				", codeHead='" + codeHead + '\'' +
				", del_chk='" + del_chk + '\'' +
				", notice_chk='" + notice_chk + '\'' +
				", title='" + title + '\'' +
				", writerNick='" + writerNick + '\'' +
				", viewNum=" + viewNum +
				", like=" + like +
				", dislike=" + dislike +
				", frstRegistPnttm=" + frstRegistPnttm +
				", frstRegistNm='" + frstRegistNm + '\'' +
				", lastUpdtPnttm=" + lastUpdtPnttm +
				", lastUpdtNm='" + lastUpdtNm + '\'' +
				", contentVO=" + contentVO +
				", fileVO=" + fileVO +
				'}';
	}
}
