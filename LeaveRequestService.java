//package services;
//
//import utils.DatabaseConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class LeaveRequestService {
//    public static boolean checkLeaveRequestExists(int userId, java.sql.Date leaveDate) throws SQLException {
//        String query = "SELECT COUNT(*) FROM LeaveRequest WHERE userID = ? AND leaveDate = ?";
//        boolean exists = false;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, userId);
//            statement.setDate(2, leaveDate);
//
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next() && resultSet.getInt(1) > 0) {
//                exists = true;
//            }
//        }
//        return exists;
//    }
//
//    public static boolean addLeaveRequest(int userId, java.sql.Date leaveDate, String leaveReason) throws SQLException {
//        String insertQuery = "INSERT INTO LeaveRequest (userID, leaveDate, leaveReason) VALUES (?, ?, ?)";
//        String updateUserQuery = "UPDATE Users SET userLeavesTaken = userLeavesTaken + 1, userPaidLeavesLeft = userPaidLeavesLeft - 1 WHERE userID = ?";
//
//        boolean inserted = false;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection()) {
//
//            connection.setAutoCommit(false);
//
//            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
//                statement.setInt(1, userId);
//                statement.setDate(2, leaveDate);
//                statement.setString(3, leaveReason);
//
//                int rowsInserted = statement.executeUpdate();
//                if (rowsInserted > 0) {
//
//                    try (PreparedStatement updateStatement = connection.prepareStatement(updateUserQuery)) {
//                        updateStatement.setInt(1, userId);
//                        int rowsUpdated = updateStatement.executeUpdate();
//                        if (rowsUpdated > 0) {
//
//                            connection.commit();
//                            inserted = true;
//                        } else {
//                            connection.rollback();
//                            throw new SQLException("Failed to update the User table.");
//                        }
//                    }
//                } else {
//                    connection.rollback();
//                    throw new SQLException("Failed to insert the leave request.");
//                }
//            } catch (SQLException e) {
//                connection.rollback();
//                throw e;
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error creating leave request and updating user: " + e.getMessage());
//            throw e;
//        }
//
//        return inserted;
//    }
//}
