package com.smartcity.model;

/**
 * Created by mobintia-android-developer-1 on 21/9/15.
 */
public class ComplaintItem
{
    String userComplaintId;
    String address;
    String link;
    String contactNumber;
    String date;

    public String getUserComplaintId() {
        return userComplaintId;
    }

    public void setUserComplaintId(String userComplaintId) {
        this.userComplaintId = userComplaintId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ComplaintItem(String userComplaintId, String address, String link, String contactNumber, String date) {
        this.userComplaintId = userComplaintId;
        this.address = address;
        this.link = link;
        this.contactNumber = contactNumber;
        this.date = date;
    }

    public ComplaintItem() {
    }


}
