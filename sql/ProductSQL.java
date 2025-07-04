package services.sql;

public class ProductSQL {
    public static final String GET_ALL_PRODUCTS = "SELECT * FROM Products";
    public static final String GET_STOCK_QUANTITY = "SELECT stock_quantity FROM Products WHERE id = ?";
    public static final String SELECTPRODUCTFROMID = "SELECT * FROM Products WHERE id = ?";
}
