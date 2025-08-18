package services;

import domain.LunchSchedule;
import utils.DatabaseConnection;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ArrayList;


import java.sql.*;

public class LunchScheduleService {

    public static Response addLunchSchedule(LunchSchedule lunch) throws SQLException {
        java.util.Date date = lunch.getLunchDate();
        String menu = lunch.getLunchMenu();
        int adderID = lunch.getAdderID();

        Response response = null;

        String query = "INSERT INTO LunchSchedule (adderID, lunchDate, lunchMenu) VALUES (?,?,?)";

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, adderID);
            statement.setDate(2, new Date(date.getTime()));
            statement.setString(3, menu);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                response = Response.status(Response.Status.CREATED)
                        .entity("Lunch schedule added successfully.")
                        .build();
                System.out.println("Menu: " + menu + " has been added to the database for date: " + date);
            }

            else
            {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to add lunch schedule.")
                    .build();
            }

        } catch (SQLException e) {
            System.out.println("Error adding lunch schedule: " + e.getMessage());
            throw e;
        }

        return response;
    }

    public static Response checkLunchScheduleAvailability(LunchSchedule lunch) throws SQLException {

        Date lunchDate = new java.sql.Date(lunch.getLunchDate().getTime());
        String query = "SELECT COUNT(*) FROM LunchSchedule WHERE lunchDate = ?";
        Response response = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, new Date(lunchDate.getTime()));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    response = null;
                }
                else
                {
                    response = Response.status(Response.Status.BAD_REQUEST).entity("A lunch schedule already exists for entry!").build();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking lunch schedule availability: " + e.getMessage());
            throw e;
        }
        return response;
    }

    public static List<LunchSchedule> viewLunchScheduleForThisMonth() throws SQLException {

        java.util.Date now = new java.util.Date();
        int currentMonth = now.getMonth();
        int currentYear = now.getYear() + 1900;

        String query = "SELECT * FROM LunchSchedule WHERE YEAR(lunchDate) = ? AND MONTH(lunchDate) = ?";

        List<LunchSchedule> lunchSchedules = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, currentYear);
            statement.setInt(2, currentMonth + 1);  // Add 1 because SQL months are 1-based

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int adderId = resultSet.getInt("adderID");
                int lunchId = resultSet.getInt("lunchID");
                Date lunchDate = resultSet.getDate("lunchDate");
                String lunchMenu = resultSet.getString("lunchMenu");

                LunchSchedule lunchSchedule = new LunchSchedule(adderId, lunchId, lunchDate, lunchMenu);
                lunchSchedules.add(lunchSchedule);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching lunch schedule for the month: " + e.getMessage());
            throw e;
        }

        return lunchSchedules;
    }


    public static Response validateLunch(LunchSchedule lunch)
    {
        Response response = null;
        java.util.Date date = lunch.getLunchDate();
        String menu = lunch.getLunchMenu();
        int adderID = lunch.getAdderID();

        if (date == null || menu == null || menu.isEmpty() || adderID <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date, menu and adderID cannot be empty!")
                    .build();
        }

        java.util.Date today = new java.util.Date();
        if (date.before(today)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be in the past!")
                    .build();
        }
        return response;
    }

    public static Response validateDate(LunchSchedule lunchSchedule)
    {
        Response response = null;
        java.util.Date date = lunchSchedule.getLunchDate();

        if (date == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be empty!")
                    .build();
        }

        java.util.Date today = new java.util.Date();
        if (date.before(today)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be in the past!")
                    .build();
        }
        return response;
    }

    public static Response deleteLunchSchedule(LunchSchedule lunchSchedule) throws SQLException {

        Date lunchDate = new java.sql.Date(lunchSchedule.getLunchDate().getTime());
        String query = "DELETE FROM LunchSchedule WHERE lunchDate = ?";
        Response response = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, lunchDate);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                response = Response.status(Response.Status.OK)
                        .entity("Lunch schedule deleted successfully.")
                        .build();
            }
            else {
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Failed to delete lunch schedule.")
                            .build();
            }

        } catch (SQLException e) {
            throw e;
        }
        return response;
    }

    public static Response validateRole(LunchSchedule lunchSchedule) throws SQLException {
        Response response = null;
        int adderID = lunchSchedule.getAdderID();

        String query = "SELECT userID FROM Users WHERE userID = ? AND userRole = 'admin'";

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, adderID);
            ResultSet resultSet = statement.executeQuery();  // Use executeQuery instead of executeUpdate

            if (resultSet.next()) {
               return response;
            } else {
                response = Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Lunch can only be added by admins!").build();
            }

        } catch (SQLException e) {
            System.out.println("Error searching for role for user ID: " + adderID);
            throw e;
        }

        return response;
    }

}
