package domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product {
    private int productid;
    private String productname;
    private double price;
    private int stockQuantity;


    public Product() {}

    public Product(ResultSet rs) throws SQLException {
       this.productid = rs.getInt("id");
        this.productname = rs.getString("productname");
        this.price = rs.getDouble("price");
        this.stockQuantity = rs.getInt("stock_quantity");
    }

    public Product(int productid, String name, double price, int stockQuantity) {
        this.productid = productid;
        this.productname = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    public int getId() {
        return productid;
    }

    public void setId(int id) { this.productid = id;}

    public String getName() {
        return productname;
    }

    public void setName(String name) {
        this.productname = productname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productid +
                ", name='" + productname + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
