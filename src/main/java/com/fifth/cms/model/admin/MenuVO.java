package com.fifth.cms.model.admin;

import java.util.List;

public class MenuVO {

	private Integer uid;
	private String codeHead;
	private String pCode;
	private Integer depth;
	private Integer sort;
	private String path;
	private String title;
	private String del_chk;
	private String visible;
	private String pathType;
	private String targetOption;

	private String frstRegistPnttm;
	private String frstRegistNm;
	private String lastUpdtPnttm;
	private String lastUpdtNm;
	
	private List<MenuVO> childNode;

	public MenuVO() {
		this.uid = 0;
	}

	public MenuVO(Integer uid, String codeHead, String pCode, Integer depth, Integer sort, String path, String title, String del_chk, String visible, String pathType, String targetOption, String frstRegistPnttm, String frstRegistNm, String lastUpdtPnttm, String lastUpdtNm) {
		this.uid = uid;
		this.codeHead = codeHead;
		this.pCode = pCode;
		this.depth = depth;
		this.sort = sort;
		this.path = path;
		this.title = title;
		this.del_chk = del_chk;
		this.visible = visible;
		this.pathType = pathType;
		this.targetOption = targetOption;
		this.frstRegistPnttm = frstRegistPnttm;
		this.frstRegistNm = frstRegistNm;
		this.lastUpdtPnttm = lastUpdtPnttm;
		this.lastUpdtNm = lastUpdtNm;
	}
	
	public Integer getUid() { return uid; }
	public void setUid(Integer uid) { this.uid = uid; }

	public String getCodeHead() { return codeHead; }
	public void setCodeHead(String codeHead) { this.codeHead = codeHead; }

	public String getPCode() { return pCode; }
	public void setPCode(String pCode) { this.pCode = pCode; }

	public Integer getDepth() { return depth; }
	public void setDepth(Integer depth) { this.depth = depth; }

	public Integer getSort() { return sort; }
	public void setSort(Integer sort) { this.sort = sort; }

	public String getPath() { return path; }
	public void setPath(String path) { this.path = path; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDel_chk() { return del_chk; }
	public void setDel_chk(String del_chk) { this.del_chk = del_chk; }

	public String getVisible() { return visible; }
	public void setVisible(String visible) { this.visible = visible; }

	public String getPathType() { return pathType; }
	public void setPathType(String pathType) { this.pathType = pathType; }

	public String getTargetOption() { return targetOption; }
	public void setTargetOption(String targetOption) { this.targetOption = targetOption; }

	public String getFrstRegistPnttm() { return frstRegistPnttm; }
	public void setFrstRegistPnttm(String frstRegistPnttm) { this.frstRegistPnttm = frstRegistPnttm; }

	public String getFrstRegistNm() { return frstRegistNm; }
	public void setFrstRegistNm(String frstRegistNm) { this.frstRegistNm = frstRegistNm; }

	public String getLastUpdtPnttm() { return lastUpdtPnttm; }
	public void setLastUpdtPnttm(String lastUpdtPnttm) { this.lastUpdtPnttm = lastUpdtPnttm; }

	public String getLastUpdtNm() { return lastUpdtNm; }
	public void setLastUpdtNm(String lastUpdtNm) { this.lastUpdtNm = lastUpdtNm; }

	public List<MenuVO> getChildNode() { return childNode; }
	public void setChildNode(List<MenuVO> childNode) { this.childNode = childNode; }

    public int calculateMaxFloor() {
        int maxFloor = 0;
        maxFloor = calculateDepthRecursive(this, maxFloor);
        return maxFloor;
    }

    private int calculateDepthRecursive(MenuVO menu, int currentDepth) {
        if (menu.getChildNode() == null || menu.getChildNode().isEmpty()) {
            return currentDepth;
        }
        int maxFloor = currentDepth;
        for (MenuVO child : menu.getChildNode()) {
            maxFloor = Math.max(maxFloor, calculateDepthRecursive(child, currentDepth + 1));
        }
        return maxFloor;
    }

	@Override
	public String toString() {
		return "MenuVO{" +
				"uid=" + uid +
				", codeHead='" + codeHead + '\'' +
				", pCode='" + pCode + '\'' +
				", depth=" + depth +
				", sort=" + sort +
				", path='" + path + '\'' +
				", title='" + title + '\'' +
				", del_chk='" + del_chk + '\'' +
				", visible='" + visible + '\'' +
				", pathType='" + pathType + '\'' +
				", targetOption='" + targetOption + '\'' +
				", frstRegistPnttm='" + frstRegistPnttm + '\'' +
				", frstRegistNm='" + frstRegistNm + '\'' +
				", lastUpdtPnttm='" + lastUpdtPnttm + '\'' +
				", lastUpdtNm='" + lastUpdtNm + '\'' +
				", childNode=" + childNode +
				'}';
	}
	
}
