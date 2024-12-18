
package com.tpnagar.fcm;

public class NotificationModel {
    private String Status;

    public String getStatus() { return this.Status; }

    public void setStatus(String Status) { this.Status = Status; }

    private int ReferralPatientID;

    public int getReferralPatientID() { return this.ReferralPatientID; }

    public void setReferralPatientID(int ReferralPatientID) { this.ReferralPatientID = ReferralPatientID; }

    private boolean show_in_foreground;

    public boolean getShowInForeground() { return this.show_in_foreground; }

    public void setShowInForeground(boolean show_in_foreground) { this.show_in_foreground = show_in_foreground; }

    private String sound;

    public String getSound() { return this.sound; }

    public void setSound(String sound) { this.sound = sound; }

    private String ReferralID;

    public String getReferralID() { return this.ReferralID; }

    public void setReferralID(String ReferralID) { this.ReferralID = ReferralID; }

    private String notificationtype;

    public String getNotificationtype() { return this.notificationtype; }

    public void setNotificationtype(String notificationtype) { this.notificationtype = notificationtype; }

    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private String body;

    public String getBody() { return this.body; }

    public void setBody(String body) { this.body = body; }

    private String Badge;

    public String getBadge() { return this.Badge; }

    public void setBadge(String Badge) { this.Badge = Badge; }

    private String priority;

    public String getPriority() { return this.priority; }

    public void setPriority(String priority) { this.priority = priority; }
}