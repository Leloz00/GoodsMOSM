package com.pojo;

public class Type {

	private String typeId;				//分类ID
	private String typeName;			//分类名称
	private String note;				//备注
	private String timeRenew;			//更新时间
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTimeRenew() {
		return timeRenew;
	}
	public void setTimeRenew(String timeRenew) {
		this.timeRenew = timeRenew;
	}	
}
