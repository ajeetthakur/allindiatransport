package com.tpnagar.wrapper;

/**
 * Created by premprajapat on 12/27/16.
 */

public class DBTabsWrapper {



    String ID="";
    String Tab_Name="";
    String Tab_Desc="";
    String Seq_No="";
    String Target_URL="";
    String Tab_Icon="";

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getTenant() {
        return Tenant;
    }

    public void setTenant(String tenant) {
        Tenant = tenant;
    }

    public String getTab_Icon() {
        return Tab_Icon;
    }

    public void setTab_Icon(String tab_Icon) {
        Tab_Icon = tab_Icon;
    }

    public String getSeq_No() {
        return Seq_No;
    }

    public void setSeq_No(String seq_No) {
        Seq_No = seq_No;
    }

    public String getTab_Desc() {
        return Tab_Desc;
    }

    public void setTab_Desc(String tab_Desc) {
        Tab_Desc = tab_Desc;
    }

    public String getTarget_URL() {
        return Target_URL;
    }

    public void setTarget_URL(String target_URL) {
        Target_URL = target_URL;
    }

    public String getTab_Name() {
        return Tab_Name;
    }

    public void setTab_Name(String tab_Name) {
        Tab_Name = tab_Name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String IsActive="";
    String Version="";

    String Tenant="";
    String ActivityId="";


}
