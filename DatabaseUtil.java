package utils;
import domain.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    public static List<Product> executeQueryy(String query) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
              while (resultSet.next()) {
                  Product product = new Product(resultSet);
                  products.add(product);
              }
            }

            return products;
        } catch (SQLException e) {

            e.printStackTrace();
            throw e;
        }
    }

    public static Product executeQueryy(String query, int productid) throws SQLException {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productid);

            Product product = null;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product(resultSet);
                }
            }
            return product;

        }  catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
