package services;

import domain.LunchSchedule;
import utils.DatabaseConnection;
import java.util.List;
import java.util.ArrayList;


import java.sql.*;


public class LunchScheduleDAO {
    public static boolean addLunchSchedule(Date date, String menu) throws SQLException {
        String query = "INSERT INTO LunchSchedule (lunchDate, lunchMenu) VALUES (?, ?)";
        boolean inserted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Convert java.util.Date to java.sql.Date
            statement.setDate(1, new Date(date.getTime()));  // Correct Date conversion
            statement.setString(2, menu);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                inserted = true;
                System.out.println("Menu: " + menu + " has been added to the database for date: " + date);
            }

        } catch (SQLException e) {
            System.out.println("Error adding lunch schedule: " + e.getMessage());
            throw e;
        }

        return inserted;
    }

    public static boolean checkLunchScheduleAvailability(Date lunchDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM LunchSchedule WHERE lunchDate = ?";
        boolean isAvailable = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, new Date(lunchDate.getTime()));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Get the number of matching rows
                if (count == 0) {
                    isAvailable = true; // No schedule found for this date
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking lunch schedule availability: " + e.getMessage());
            throw e;
        }
        return isAvailable;
    }

    public static List<LunchSchedule> viewLunchScheduleForThisMonth() throws SQLException {
        // Get the current date
        java.util.Date now = new java.util.Date();
        int currentMonth = now.getMonth();  // Month is 0-based (0 = January, 11 = December)
        int currentYear = now.getYear() + 1900; // Year is 1900-based, so add 1900 to get the current year

        // SQL query to get all lunch schedules for the current month and year
        String query = "SELECT * FROM LunchSchedule WHERE YEAR(lunchDate) = ? AND MONTH(lunchDate) = ?";

        List<LunchSchedule> lunchSchedules = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameters for the current year and month
            statement.setInt(1, currentYear);
            statement.setInt(2, currentMonth + 1);  // Add 1 because SQL months are 1-based

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and create LunchSchedule objects
            while (resultSet.next()) {
                int lunchId = resultSet.getInt("lunchID");
                Date lunchDate = resultSet.getDate("lunchDate");
                String lunchMenu = resultSet.getString("lunchMenu");

                LunchSchedule lunchSchedule = new LunchSchedule(lunchId, lunchDate, lunchMenu);
                lunchSchedules.add(lunchSchedule);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching lunch schedule for the month: " + e.getMessage());
            throw e;
        }

        return lunchSchedules;
    }

    public static boolean deleteLunchSchedule(java.sql.Date date) throws SQLException {
        String query = "DELETE FROM LunchSchedule WHERE lunchDate = ?";
        boolean deleted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, date);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                deleted = true;
                System.out.println("Lunch schedule for " + date + " has been deleted.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting lunch schedule for date " + date);
            throw e;
        }

        return deleted;
    }
}
