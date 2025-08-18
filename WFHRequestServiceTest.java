package services;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import utils.DatabaseConnection;

public class WFHRequestServiceTest {

    private int testUserId = 9999; // some unique test user ID
    private Date testDate = new Date(System.currentTimeMillis());
    private String testReason = "Testing WFH request";

    @Test
    public void testCheckWFHRequestExistsFalse() throws SQLException {
        boolean exists = WFHRequestService.checkWFHRequestExists(testUserId, testDate);
        assertFalse(exists);
    }

    @Test
    public void testAddWFHRequest() throws SQLException {// needs to be part of user table
        int testUserId = 2;
        boolean inserted = WFHRequestService.addWFHRequest(testUserId, testDate, testReason);
        assertTrue(inserted);
        boolean exists = WFHRequestService.checkWFHRequestExists(testUserId, testDate);
        assertTrue(exists);
    }

    @Test
    public void testCheckWFHRequestExistsTrue() throws SQLException {
        int testUserId = 2;
        WFHRequestService.addWFHRequest(testUserId, testDate, testReason);
        boolean exists = WFHRequestService.checkWFHRequestExists(testUserId, testDate);
        assertTrue(exists);
    }


}
