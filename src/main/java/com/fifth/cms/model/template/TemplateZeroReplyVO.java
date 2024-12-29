package com.fifth.cms.model.template;

import java.util.Date;

public class TemplateZeroReplyVO {

	private Integer uid;
	private Integer pid;
	private String writerNick;	
	// private String tagRepCode;
	private Integer momRepUid;
	private Integer tagRepUid;
	private String context;
	private int like;
	private int dislike;
	private String likeOptionPath;
	private Date frstRegistPnttm;
	private Date lastUpdtPnttm;

	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

	public Integer getPid() { return pid; }
	public void setPid(Integer pid) { this.pid = pid; }

	public String getWriterNick() { return writerNick; }
	public void setWriterNick(String writerNick) { this.writerNick = writerNick; }

	public Integer getMomRepUid() { return momRepUid; }
	public void setMomRepUid(Integer momRepUid) { this.momRepUid = momRepUid; }

	public Integer getTagRepUid() { return tagRepUid; }
	public void setTagRepUid(Integer tagRepUid) { this.tagRepUid = tagRepUid; }

	public String getContext() { return context; }
	public void setContext(String context) { this.context = context; }

	public int getLike() { return like; }
	public void setLike(int like) { this.like = like; }

	public int getDislike() { return dislike; }
	public void setDislike(int dislike) { this.dislike = dislike; }

	public String getLikeOptionPath() { return likeOptionPath; }
	public void setLikeOptionPath(String likeOptionPath) { this.likeOptionPath = likeOptionPath; }

	public Date getFrstRegistPnttm() { return frstRegistPnttm; }
	public void setFrstRegistPnttm(Date frstRegistPnttm) { this.frstRegistPnttm = frstRegistPnttm; }

	public Date getLastUpdtPnttm() { return lastUpdtPnttm; }
	public void setLastUpdtPnttm(Date lastUpdtPnttm) { this.lastUpdtPnttm = lastUpdtPnttm; }

	@Override
	public String toString() {
		return "TemplateZeroReplyVO{" +
				"uid='" + uid + '\'' +
				", pid='" + pid + '\'' +
				", writerNick='" + writerNick + '\'' +
				", momRepUid='" + momRepUid + '\'' +
				", tagRepUid='" + tagRepUid + '\'' +
				", context='" + context + '\'' +
				", like=" + like +
				", dislike=" + dislike +
				", likeOptionPath='" + likeOptionPath + '\'' +
				", frstRegistPnttm=" + frstRegistPnttm +
				", lastUpdtPnttm=" + lastUpdtPnttm +
				'}';
	}
	
}
