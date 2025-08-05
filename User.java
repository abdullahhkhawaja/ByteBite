package domain;

public class User {
    private int userID;                   // auto-incremented
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userRole;              // employee or admin
    private int userLeavesTaken;
    private int userPaidLeavesLeft;

    public User() {
        this.userID = 0;
        this.userName = "";
        this.userEmail = "";
        this.userPassword = "";
        this.userRole = "";
        this.userLeavesTaken = 0;
        this.userPaidLeavesLeft = 21;
    }

    public User(int userID, String userName, String userEmail, String userPassword, String userRole,
                int userLeavesTaken, int userPaidLeavesLeft) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userLeavesTaken = userLeavesTaken;
        this.userPaidLeavesLeft = userPaidLeavesLeft;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getUserLeavesTaken() {
        return userLeavesTaken;
    }

    public void setUserLeavesTaken(int userLeavesTaken) {
        this.userLeavesTaken = userLeavesTaken;
    }

    public int getUserPaidleavesleft() {
        return userPaidLeavesLeft;
    }

    public void setUserPaidleavesleft(int userPaidleavesleft) {
        this.userPaidLeavesLeft = userPaidleavesleft;
    }

    @Override
    public String toString() {
        return ("Welcome" + '\n' + "UserID:  " + userID + '\n' + "User Name: " + userName);
    }
}

