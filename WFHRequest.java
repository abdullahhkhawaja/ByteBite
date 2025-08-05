package domain;

import java.util.Date;

public class WFHRequest {
    private int wfhID; // AUTO_INCREMENT
    private int userID;
    private Date wfhDate;
    private String wfhReason;

    public WFHRequest() {
        wfhID = 0;
        userID = 0;
        wfhDate = new Date();
        wfhReason = "";
    }

    public WFHRequest(int userId, Date wfhDate, String wfhReason) {
        this.userID = userId;
        this.wfhDate = wfhDate;
        this.wfhReason = wfhReason;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userId;
    }

    public int getWfhID() {
        return wfhID;
    }

    public void setWfhID(int wfhId) {
        this.wfhID = wfhId;
    }

    public Date getWfhDate() {
        return wfhDate;
    }

    public void setWfhDate(Date wfhDate) {
        this.wfhDate = wfhDate;
    }

    public String getWfhReason() {
        return wfhReason;
    }

    public void setWfhReason(String wfhReason) {
        this.wfhReason = wfhReason;
    }
}
