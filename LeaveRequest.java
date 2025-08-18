package domain;

import java.util.Date;

public class LeaveRequest {
    private int leaveRequestID;
    private int userID;
    private Date leaveDate;
    private String leaveReason;

    public LeaveRequest() {}

    public LeaveRequest(int leaveRequestId, int userId, Date leaveDate, String leaveReason) {
        this.leaveRequestID = leaveRequestId;
        this.userID = userId;
        this.leaveDate = leaveDate;
        this.leaveReason = leaveReason;
    }

    public int getLeaveRequestId() {
        return leaveRequestID;
    }

    public void setLeaveRequestId(int leaveRequestId) {
        this.leaveRequestID = leaveRequestId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userId;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }
}
