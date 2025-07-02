package services;


import domain.Customer;
import utils.DatabaseConnection;

import java.sql.*;

public class CustomerDAO {

    public static Customer validateLogin(String username, String password) {
        String query = "SELECT * FROM Customers WHERE username = ? AND pass = ?";
        Customer customer = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, username);
            statement.setString(2, password);


            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("customerid");
                    String user = resultSet.getString("username");
                    String pass = resultSet.getString("pass");

                    customer = new Customer(id, user, pass);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;

    }

    public static boolean SignUp(String username, String password) throws SQLException {
        String query = "INSERT INTO Customers (username, pass) VALUES (?, ?)";
        boolean inserted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, username);
            statement.setString(2, password);


            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                inserted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return inserted;
    }

    public static boolean checkUsernameAvailability(String username) throws SQLException {
        String query = "SELECT * FROM Customers WHERE username = ?";
        boolean usernamecheck = false;
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("customerid");
                    String user = resultSet.getString("username");
                    usernamecheck = true;
                }

            }
             catch  (SQLException e) {
                e.printStackTrace();
                throw e;
             }

            return usernamecheck;
        }
    }
}
