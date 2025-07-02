package domain;

public class Order {

    private int orderid;
    private int productId;
    private int orderquantity;
    private String customerusername;


    public Order()
    {
        orderid = 0;
        productId = 0;
        orderquantity = 0;
        customerusername = "";
    }


    public Order(int orderid, int productId, int orderquantity, String customerusername) {
        this.orderid = orderid;
        this.productId = productId;
        this.orderquantity = orderquantity;
        this.customerusername = customerusername;
    }


    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderquantity() {
        return orderquantity;
    }

    public void setOrderquantity(int orderquantity) {
        this.orderquantity = orderquantity;
    }

    public String getCustomerusername() {
        return customerusername;
    }

    public void setCustomerusername(String customerusername) {
        this.customerusername = customerusername;
    }
}
