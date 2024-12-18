package com.tpnagar.wrapper;

import java.io.Serializable;

public class GetallowedbranchWrapper implements Serializable{


	private boolean status;


	private String branchName;


	private int branchId;


	private int id;

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	public void setBranchName(String branchName){
		this.branchName = branchName;
	}

	public String getBranchName(){
		return branchName;
	}

	public void setBranchId(int branchId){
		this.branchId = branchId;
	}

	public int getBranchId(){
		return branchId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"GetallowedbranchWrapper{" + 
			"status = '" + status + '\'' + 
			",branch_Name = '" + branchName + '\'' + 
			",branch_Id = '" + branchId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}