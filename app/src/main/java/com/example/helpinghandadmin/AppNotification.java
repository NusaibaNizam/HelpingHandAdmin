package com.example.helpinghandadmin;

import java.io.Serializable;

public class AppNotification implements Serializable {
    String fromID;
    String toID;
    String text;
    String type;
    String notifID;
    String workType;

    public AppNotification() {
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getNotifID() {
        return notifID;
    }

    public void setNotifID(String notifID) {
        this.notifID = notifID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public AppNotification(String fromID, String toID, String text, String type, String notifID, String workType) {
        this.fromID = fromID;
        this.toID = toID;
        this.text = text;
        this.type = type;
        this.notifID = notifID;
        this.workType = workType;
    }
}
