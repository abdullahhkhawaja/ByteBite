//package services;
//
//import utils.DatabaseConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Date;
//
//public class HeadcountService {
//
//    // Method to get the total number of employees
//    public static int getTotalEmployees() throws SQLException {
//        String query = "SELECT COUNT(*) FROM Users";
//        int totalEmployees = 0;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                totalEmployees = resultSet.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error retrieving total employees count");
//            throw e;
//        }
//
//        return totalEmployees;
//    }
//
//    // Method to get the total number of WFH requests for a specific date
//    public static int getWFHRequestsForDay(java.sql.Date date) throws SQLException {
//        String query = "SELECT COUNT(*) FROM WFHRequests WHERE wfhDate = ?";
//        int wfhRequests = 0;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setDate(1, date);  // Set the date to query against
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                wfhRequests = resultSet.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error retrieving WFH requests for date: " + date);
//            throw e;
//        }
//
//        return wfhRequests;
//    }
//
//    // Method to get the total number of Leave requests for a specific date
//    public static int getLeaveRequestsForDay(java.sql.Date date) throws SQLException {
//        String query = "SELECT COUNT(*) FROM LeaveRequests WHERE leaveDate = ?";
//        int leaveRequests = 0;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setDate(1, date);  // Set the date to query against
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                leaveRequests = resultSet.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error retrieving Leave requests for date: " + date);
//            throw e;
//        }
//
//        return leaveRequests;
//    }
//
//    // Method to calculate the available headcount for the day
//    public static int calculateHeadcountForDay(java.util.Date date) throws SQLException {
//        int totalEmployees = getTotalEmployees();  // Get total employees
//        int wfhRequests = getWFHRequestsForDay(new java.sql.Date(date.getTime()));  // Convert to java.sql.Date and get WFH requests
//        int leaveRequests = getLeaveRequestsForDay(new java.sql.Date(date.getTime()));  // Convert to java.sql.Date and get Leave requests
//
//        // Subtract WFH and leave requests from total employees to get headcount
//        return totalEmployees - wfhRequests - leaveRequests;
//    }
//}
