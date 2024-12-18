package com.tpnagar.wrapper;

public class NotificationCompnay{
	private String companyName;
	private String companyId;

	public int getUnReadMsgCount() {
		return UnReadMsgCount;
	}

	public void setUnReadMsgCount(int unReadMsgCount) {
		UnReadMsgCount = unReadMsgCount;
	}

	int UnReadMsgCount=0;

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean blocked) {
		isBlocked = blocked;
	}

	boolean isBlocked;

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}
}
