package com.smartcity.model;

/**
 * Created by mobintia-android-developer-1 on 21/9/15.
 */
public class NotificationsItem
{
    String id;
    String notification_message;
    String notification_date;
    String notification_subject;
    String notification_sortMessage;
    String notification_complaintId;
    String notification_address;
    String notification_contactNumber;

    public NotificationsItem(String id, String notification_message, String notification_date, String notification_subject, String notification_sortMessage, String notification_complaintId, String notification_address, String notification_contactNumber) {
        this.id = id;
        this.notification_message = notification_message;
        this.notification_date = notification_date;
        this.notification_subject = notification_subject;
        this.notification_sortMessage = notification_sortMessage;
        this.notification_complaintId = notification_complaintId;
        this.notification_address = notification_address;
        this.notification_contactNumber = notification_contactNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotification_message() {
        return notification_message;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getNotification_subject() {
        return notification_subject;
    }

    public void setNotification_subject(String notification_subject) {
        this.notification_subject = notification_subject;
    }

    public String getNotification_sortMessage() {
        return notification_sortMessage;
    }

    public void setNotification_sortMessage(String notification_sortMessage) {
        this.notification_sortMessage = notification_sortMessage;
    }

    public String getNotification_complaintId() {
        return notification_complaintId;
    }

    public void setNotification_complaintId(String notification_complaintId) {
        this.notification_complaintId = notification_complaintId;
    }

    public String getNotification_address() {
        return notification_address;
    }

    public void setNotification_address(String notification_address) {
        this.notification_address = notification_address;
    }

    public String getNotification_contactNumber() {
        return notification_contactNumber;
    }

    public void setNotification_contactNumber(String notification_contactNumber) {
        this.notification_contactNumber = notification_contactNumber;
    }

    public NotificationsItem() {
    }


}
