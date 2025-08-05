package domain;

import java.util.Date;

public class LunchSchedule {
    private int lunchID; // AUTO_INCREMENT
    private Date lunchDate;
    private String lunchMenu;

    public LunchSchedule() {
        lunchID = 0;
        lunchDate = new Date();
        lunchMenu = "";
    }

    public LunchSchedule(Date lunchDate, String lunchMenu) {
        this.lunchDate = lunchDate;
        this.lunchMenu = lunchMenu;
    }

    public int getLunchID() {
        return lunchID;
    }

    public void setLunchID(int lunchID) {
        this.lunchID = lunchID;
    }

    public Date getLunchDate() {
        return lunchDate;
    }

    public void setLunchDate(Date lunchDate) {
        this.lunchDate = lunchDate;
    }

    public String getLunchMenu() {
        return lunchMenu;
    }

    public void setLunchMenu(String lunchMenu) {
        this.lunchMenu = lunchMenu;
    }
}
