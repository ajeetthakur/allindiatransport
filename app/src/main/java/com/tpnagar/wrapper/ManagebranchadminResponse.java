package com.tpnagar.wrapper;


public class ManagebranchadminResponse{


	private int companyId;


	private boolean canEditBranch;

	private boolean canDeleteBranch;

	private int branchId;

	private boolean canAddBranch;

	public void setCompanyId(int companyId){
		this.companyId = companyId;
	}

	public int getCompanyId(){
		return companyId;
	}

	public void setCanEditBranch(boolean canEditBranch){
		this.canEditBranch = canEditBranch;
	}

	public boolean isCanEditBranch(){
		return canEditBranch;
	}

	public void setCanDeleteBranch(boolean canDeleteBranch){
		this.canDeleteBranch = canDeleteBranch;
	}

	public boolean isCanDeleteBranch(){
		return canDeleteBranch;
	}

	public void setBranchId(int branchId){
		this.branchId = branchId;
	}

	public int getBranchId(){
		return branchId;
	}

	public void setCanAddBranch(boolean canAddBranch){
		this.canAddBranch = canAddBranch;
	}

	public boolean isCanAddBranch(){
		return canAddBranch;
	}
}