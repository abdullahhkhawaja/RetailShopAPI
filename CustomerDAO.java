package services;
import org.mindrot.jbcrypt.BCrypt;

import domain.Customer;
import utils.DatabaseConnection;

import static services.sql.CustomerSQL.SELECT_USER_PASS;
import static services.sql.CustomerSQL.INSERT_USER_PASS;

import java.sql.*;

public class CustomerDAO {

    public static Customer validateLogin(String username, String password) {
        String query = "SELECT * FROM Customers WHERE username = ?";
        Customer customer = null;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                        String storedHashedPassword = resultSet.getString("pass");

                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        int id = resultSet.getInt("customerid");
                        String user = resultSet.getString("username");

                        customer = new Customer(id, user, storedHashedPassword);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public static boolean SignUp(String username, String hashedPassword) throws SQLException {
        String query = "INSERT INTO Customers (username, pass) VALUES (?, ?)";
        boolean inserted = false;

        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                inserted = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Rethrow the exception if you want to propagate it up the stack
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
