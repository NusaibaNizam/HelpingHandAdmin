package com.example.helpinghandadmin;

public class Notification {
    String id;
    String notification;

    public Notification() {
    }

    public Notification(String id, String notification) {
        this.id = id;
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
