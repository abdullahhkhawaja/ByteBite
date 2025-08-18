package services;

import domain.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public static boolean SignUp(String userName, String userEmail, String userPassword) throws SQLException {

        if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            return false;
        }

        String query = "INSERT INTO Users (userName, userEmail, userPassword) VALUES (?, ?, ?)";
        boolean inserted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userName);
            statement.setString(2, userEmail);
            statement.setString(3, userPassword);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                inserted = true;
                System.out.println("User successfully created: " + userName);
            }

        } catch (SQLException e) {
            System.out.println("Error creating user: " + userName
            );
            throw e;
        }

        return inserted;
    }

    public static boolean CheckIfEmailAlreadyUsed(String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE userEmail = ?";
        boolean EmailExists = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            // logger.info("Checking availability for email: {}", email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    //   logger.warn("Username already exists: {}", username);
                    EmailExists = true;
                }
            } catch (SQLException e) {
                //logger.error("Error checking username availability for: {}", username, e);
                throw e;
            }

        }
        return EmailExists;
    }

    public static User validateLogin(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return null;
        }

        String query = "SELECT * FROM Users WHERE userEmail = ?";
        User user = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            //logger.info("Attempting to validate login for user: " + username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("userPassword");

                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        int id = resultSet.getInt("userID");
                        String userName = resultSet.getString("userName");
                        String  userEmail = resultSet.getString("userEmail");
                        String userPassword = resultSet.getString("userPassword");
                        String userRole = resultSet.getString("userRole");
                        int UserLeavesTaken = resultSet.getInt("userLeavesTaken");
                        int UserPaidLeavesLeft = resultSet.getInt("userPaidLeavesLeft");

                        user = new User(id,userName,userEmail,userPassword,userRole,UserLeavesTaken,UserPaidLeavesLeft);
                        //logger.info("User validated successfully: {}", username);
                    } else {
                        //logger.warn("Invalid password for user: {}", username);
                    }
                } else {
                    //logger.warn("No user found with username: {}", username);
                }
            }

        } catch (SQLException e) {
           // logger.error("Error during login validation for user: {}", username, e);
        }

        return user;
    }


    public static int getUserIDFromDatabase(String email) throws SQLException {
        String query = "SELECT userID FROM Users WHERE userEmail = ?";
        int userID = -1;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();

             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setString(1, email);
             ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userID = resultSet.getInt("userID");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving user ID for email: " + email);
            throw e;
        }

        return userID;
    }

    public static String getUserNameFromID(int userID) throws SQLException {
        String query = "SELECT userName FROM Users WHERE userID = ?";
        String userName = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userName = resultSet.getString("userName");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching userName for userID: " + userID);
            throw e;
        }
        return userName;
    }

}
