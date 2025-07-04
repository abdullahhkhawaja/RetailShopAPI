package services;

import services.sql.ProductSQL;
import domain.Product;
import utils.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import utils.DatabaseUtil;

import static services.sql.ProductSQL.GET_ALL_PRODUCTS;
import static services.sql.ProductSQL.SELECTPRODUCTFROMID;

public class ProductDAO {

    public static List<Product> GetAllProducts() throws SQLException {
        return DatabaseUtil.executeQueryy(GET_ALL_PRODUCTS);
    }

    public static Product GetSpecificProduct(int productid) throws SQLException {
        return DatabaseUtil.executeQueryy(SELECTPRODUCTFROMID, productid);
    }

//    public static int getProductStockQuantity(int productId) throws SQLException {
//
//        String query = "SELECT stock_quantity FROM Products WHERE id = ?";
//        int stockQuantity = 0;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//
//            statement.setInt(1, productId);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//
//                    stockQuantity = resultSet.getInt("stock_quantity");
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return stockQuantity;
// }

//    public static boolean DoesProductExist(int productId) throws SQLException {
//        String query = "SELECT * FROM Products WHERE id = ?";
//        boolean result = false;
//
//        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setInt(1, productId);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    result = true;
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//        return result;
//    }

}
