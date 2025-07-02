package services;

import domain.Order;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderDAO {

    public static boolean createOrder(Order order) {

        String insertOrderQuery = "INSERT INTO Orders (productid, order_quantity, customer_username) VALUES (?, ?, ?)";
        String updateStockQuery = "UPDATE Products SET stock_quantity = stock_quantity - ? WHERE id = ?";



        try (Connection connection = DatabaseConnection.getDataSource().getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(insertOrderQuery)) {
                statement.setInt(1, order.getProductId());
                statement.setInt(2, order.getOrderquantity());
                statement.setString(3, order.getCustomerusername());

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted <= 0) {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(updateStockQuery)) {
                statement.setInt(1, order.getOrderquantity());
                statement.setInt(2, order.getProductId());


                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated <= 0) {
                    connection.rollback(); // Rollback the transaction if stock update failed
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {

                DatabaseConnection.getDataSource().getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

        public static List<Order> getOrdersByUsername(String username) {
            List<Order> orders = new ArrayList<>();
            String query = "SELECT * FROM Orders WHERE customer_username = ?";


            try (Connection connection = DatabaseConnection.getDataSource().getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, username);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        int orderid = resultSet.getInt("orderid");
                        int productid = resultSet.getInt("productid");
                        int orderquantity = resultSet.getInt("order_quantity");
                        String customerusername = resultSet.getString("customer_username");

                        Order order = new Order(orderid, productid, orderquantity, customerusername);
                        orders.add(order);
                    }
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }

            return orders;
        }
}



