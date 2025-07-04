package domain;

public class Customer {
    private int customerid;
    private String username;
    private String password;

    public Customer() {
        customerid = 0;
        username = "";
        password = "";
    }

    public Customer(int id, String username, String password) {
        this.customerid = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return customerid;
    }

    public void setId(int id) {
        this.customerid = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "id = " + customerid + ", username = '" + username;
    }
}
