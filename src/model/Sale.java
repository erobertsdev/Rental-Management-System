package model;

public class Sale {
    private int saleID;
    private double salePrice;
    private int customerID;
    private int userID;
    private int productID;
    private String productName;

    public Sale(int saleID, double salePrice, int customerID, int userID, int productID, String productName) {
        this.saleID = saleID;
        this.salePrice = salePrice;
        this.customerID = customerID;
        this.userID = userID;
        this.productID = productID;
        this.productName = productName;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
