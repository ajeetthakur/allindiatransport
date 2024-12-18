package com.tpnagar.wrapper;

public class NotificationsWrapper{
	private String recordId;
	private String msgType;
	private String crDate;
	private String title;
	private String message;
	private String CompanyId;
	private String strdate;

	public String getStrdate() {
		return strdate;
	}

	public void setStrdate(String strdate) {
		this.strdate = strdate;
	}

	public String getStrtime() {
		return strtime;
	}

	public void setStrtime(String strtime) {
		this.strtime = strtime;
	}

	private String strtime;


	public PostGoodsWrapper getPostGoodsWrapper() {
		return postGoodsWrapper;
	}

	public void setPostGoodsWrapper(PostGoodsWrapper postGoodsWrapper) {
		this.postGoodsWrapper = postGoodsWrapper;
	}

	public PostVehicleWrapper getPostVehicleWrapper() {
		return postVehicleWrapper;
	}

	public void setPostVehicleWrapper(PostVehicleWrapper postVehicleWrapper) {
		this.postVehicleWrapper = postVehicleWrapper;
	}

	PostGoodsWrapper postGoodsWrapper = new PostGoodsWrapper();

	PostVehicleWrapper postVehicleWrapper = new PostVehicleWrapper();

	public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String CompanyName;
	private  int id;


	public String getIsRead() {
		return IsRead;
	}

	public void setIsRead(String isRead) {
		IsRead = isRead;
	}

	private String IsRead;

	public void setRecordId(String recordId){
		this.recordId = recordId;
	}

	public String getRecordId(){
		return recordId;
	}

	public void setMsgType(String msgType){
		this.msgType = msgType;
	}

	public String getMsgType(){
		return msgType;
	}

	public void setCrDate(String crDate){
		this.crDate = crDate;
	}

	public String getCrDate(){
		return crDate;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsWrapper{" + 
			"recordId = '" + recordId + '\'' + 
			",msgType = '" + msgType + '\'' + 
			",crDate = '" + crDate + '\'' + 
			",title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}
