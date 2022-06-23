package model;

/**
 * Contains all methods having to do with customer model
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private int division;
    private String postalCode;
    private String phoneNumber;
    private String isVIP;

    public Customer(int id, String name, String address, int division, String postalCode, String phoneNumber, String isVIP) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.division = division;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.isVIP = isVIP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsVIP() { return isVIP; }
}
