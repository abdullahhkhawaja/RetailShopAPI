package services.sql;

public class CustomerSQL {
    public static String SELECT_USER_PASS = "SELECT * FROM Customers WHERE username = ? AND pass = ?";
    public static String INSERT_USER_PASS = "INSERT INTO Customers (username, pass) VALUES (?, ?)";

}
