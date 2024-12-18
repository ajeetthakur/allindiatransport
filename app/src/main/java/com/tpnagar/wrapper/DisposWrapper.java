package com.tpnagar.wrapper;

/**
 * Created by premprajapat on 12/27/16.
 */

public class DisposWrapper {


    String Id="";

    public String getDisp_Name() {
        return Disp_Name;
    }

    public void setDisp_Name(String disp_Name) {
        Disp_Name = disp_Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDisp_Category() {
        return Disp_Category;
    }

    public void setDisp_Category(String disp_Category) {
        Disp_Category = disp_Category;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    String Disp_Name="";
    String Disp_Category="";
    String IsActive="";

}
