
package domain;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LunchSchedule {
    private int adderID;
    private int lunchID; // AUTO_INCREMENT
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lunchDate;

    private String lunchMenu;

    public LunchSchedule() {
        adderID = 0;
        lunchID = 0;
        lunchDate = new Date();
        lunchMenu = "";
    }

    public LunchSchedule(int adderID, int lunchID, Date lunchDate, String lunchMenu) {
        this.adderID = adderID;
        this.lunchID = lunchID;
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

    public int getAdderID() {
        return adderID;
    }

    public void setAdderID(int adderID) {
        this.adderID = adderID;
    }

}
