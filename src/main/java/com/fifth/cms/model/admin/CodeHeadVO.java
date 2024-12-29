package com.fifth.cms.model.admin;

import java.util.Date;

public class CodeHeadVO {

    private int uid; // 기본 키
    private String code; // 코드 헤드
    private String name; // 코드명
    private String comment; // 주석
	private String del_chk; // 삭제 여부
	private String templateType; // 템플릿 타입
	private String skinType; // 템플릿 타입

	// private String placeRow; //내부 게시판 표기 행
	// private String placeName; //내부 게시판 표기 이름
	// private String placeWidth; //내부 게시판 표기 너비
	// private Integer maxFileUploadNumber; // 최대 파일 업로드 수
	// private String fileUploadType; // 파일 업로드 타입

	private String optionPath; // 옵션 파라미터 저장파일명
	private String optionContent; // 옵션 파라미터 내용
    private Date signdate; // 작성일자

    // 매개변수 있는 생성자
    public CodeHeadVO(int uid, String code, String name, String comment, String del_chk, String templateType, String skinType, String optionPath, Date signdate) {
        this.uid = uid;
        this.code = code;
        this.name = name;
        this.comment = comment;
		this.del_chk = del_chk;
		this.templateType = templateType;
		this.skinType = skinType;
		this.optionPath = optionPath;
		this.signdate = signdate;
    }

	public int getUid() { return uid; }
	public void setUid(int uid) { this.uid = uid; }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getComment() { return comment; }
	public void setComment(String comment) { this.comment = comment; }

	public String getDel_chk() { return del_chk; }
	public void setDel_chk(String del_chk) { this.del_chk = del_chk; }

	public String getTemplateType() { return templateType; }
	public void setTemplateType(String templateType) { this.templateType = templateType; }

	public String getSkinType() { return skinType; }
	public void setSkinType(String skinType) { this.skinType = skinType; }

	public String getOptionPath() { return optionPath; }
	public void setOptionPath(String optionPath) { this.optionPath = optionPath; }

	public String getOptionContent() { return optionContent; }
	public void setOptionContent(String optionContent) { this.optionContent = optionContent; }

	public Date getSigndate() { return signdate; }
	public void setSigndate(Date signdate) { this.signdate = signdate; }

    @Override
    public String toString() {
        return "CodeHeadVO{" +
                "uid=" + uid +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
				", del_chk='" + del_chk + '\'' +
				", templateType='" + templateType + '\'' +
				", skinType='" + skinType + '\'' +
				", optionPath='" + optionPath + '\'' +
				", optionContent='" + optionContent + '\'' +
                ", signdate=" + signdate +
                '}';
    }
}