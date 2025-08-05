package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DatabaseConnection;


public class WFHRequestDAO {

    public static boolean checkWFHRequestExists(int userId, java.sql.Date wfhDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM WFHRequests WHERE userID = ? AND wfhDate = ?";
        boolean exists = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setDate(2, wfhDate);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                exists = true;
            }
        }
        return exists;
    }

    public static boolean addWFHRequest(int userId, java.sql.Date wfhDate, String wfhReason) throws SQLException {
        String query = "INSERT INTO WFHRequests (userID, wfhDate, wfhReason) VALUES (?, ?, ?)";
        boolean inserted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setDate(2, wfhDate);
            statement.setString(3, wfhReason);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                inserted = true;
            }
        }
        return inserted;
    }
}
