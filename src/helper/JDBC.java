package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.Division;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    public static Connection connection;  // Connection Interface
    public static int currentUser;

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            // Password
            String password = "Passw0rd!";
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Get current user
     * @return currentUser
     */
    public static int getCurrentUser() {
        return currentUser;
    }

    /** Method to get current user's name
     * @return String current user's name */
    public static String getCurrentUserName(int userId) throws SQLException {
        String userName = null;
        String sql = "SELECT User_Name FROM users WHERE User_ID = " + userId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while (rs.next()) {
            userName = rs.getString("User_Name");
        }
        return userName;
    }

    /**
     * Sets the current user
     * @param user ID of the user who just logged in
     */
    public static void setCurrentUser(int user) {
        currentUser = user;
    }

    /**
     * Return name of state (division) when given ID
     * @return String divisionName */
    public static String stateNameFromId(int id) throws SQLException {
        String divisionName = null;
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = " + id;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while (rs.next()) {
            divisionName = rs.getString("Division");
        }
        return divisionName;
    }

    /**
     * Return ID of state (division) when given name
     * @return int divisionId */
    public static int stateIdFromName(String name) throws SQLException {
        int divisionId = 0;
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = '" + name + "'";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while (rs.next()) {
            divisionId = rs.getInt("Division_ID");
        }
        return divisionId;
    }

    /**
     * Return name of Country from Division ID
     * @return string countryName */
    public static String countryFromDivisionId(int id) throws SQLException {
      int countryId = 0;
      String countryName = null;
      String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + id;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while (rs.next()) {
            countryId = rs.getInt("Country_ID");
        }
        switch (countryId) {
            case 1 -> countryName = "U.S";
            case 2 -> countryName = "UK";
            case 3 -> countryName = "Canada";
        }
        return countryName;
    }

    /**
     * Method to add customer to database */
    public static void addCustomer(String customerName, String address, String postal,
                                      String phone, int divisionID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, "+
                "Create_Date, Created_By, Last_Update, Last_Updated_by, Division_ID)"+
                "VALUES (?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setString(5, getCurrentUserName(currentUser));
        ps.setInt(6, getCurrentUser());
        ps.setInt(7, divisionID);
        ps.executeUpdate();
    }

    /**
     * Method to modify existing customer */
    public static void updateCustomer(int customerId, String customerName, String address, String postal,
                                   String phone, int divisionID) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "+
                "Last_Update = CURRENT_TIMESTAMP, Last_Updated_by = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setInt(5, getCurrentUser());
        ps.setInt(6, divisionID);
        ps.setInt(7, customerId);
        ps.executeUpdate();
    }

    /***
     * Method to delete customer from database */
    public static void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.executeUpdate();
    }

    /**
     * Method to loop through users table and look for a username/password match
     * @return true if user/password match is found, false otherwise */
    public static boolean checkLogin(String userName, String password) throws SQLException {
        boolean match = false;
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            String user = rs.getString("User_Name");
            String userPassword = rs.getString("Password");
            if((Objects.equals(user, userName)) && (Objects.equals(userPassword, password))) {
                setCurrentUser(rs.getInt("User_ID"));
                match = true;
            }
        }
        return match;
    }

    /**
     * Retrieve data from customers table to fill Customers tableview in CustomerForm.java
     * @return ObservableList customers */
    public static ObservableList<Customer> getCustomers() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            int customerDivision = rs.getInt("Division_ID");
            String customerPostal = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            Customer customer = new Customer(customerId, customerName, customerAddress, customerDivision, customerPostal, customerPhone);
            customers.add(customer);
        }
        return customers;
    }

    /**
     * Retrieve Appointments associated with Customer_ID */
    // TODO: Might need to also add one using USER_ID instead/also
    public static ObservableList<String> getDivisionsById(String id) throws SQLException {
        String sql = "SELECT Division_ID, Division FROM first_level_divisions WHERE Country_ID=" + id;
        ObservableList<String> divisions = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            String divisionName = rs.getString("Division");
            divisions.add(divisionName);
        }
        return divisions;
    }

    /**
     * Retrieve Appointments associated with Customer_ID */
    // TODO: Might need to also add one using USER_ID instead/also
    public static ObservableList<Appointment> getAppointmentsById(String id) throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS WHERE Customer_ID=" + id;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            Appointment appointment = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Retrieve All Divisions
     * @return ObservableList of all divisions info */
    // TODO: Might need to also add one using USER_ID instead/also
    public static ObservableList<Division> getAllDivisions() throws SQLException {
        String sql = "SELECT * FROM first_level_divisions";
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, id);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            Division division = new Division(divisionId, divisionName, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            divisions.add(division);
        }
        return divisions;
    }

    /**
     * Method to delete appointment from database */
    public static void deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.executeUpdate();
    }

    /**
     * Method to retrieve all contacts from database */
    public static ObservableList<Contact> getContacts() throws SQLException {
        String sql = "SELECT * FROM CONTACTS";
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");
            Contact contact = new Contact(contactId, contactName, contactEmail);
            contacts.add(contact);
        }
        return contacts;
    }

    /** Method to retrieve and return all contact names */
    public static ObservableList<String> getContactNames() throws SQLException {
        String sql = "SELECT Contact_Name FROM CONTACTS";
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            String contactName = rs.getString("Contact_Name");
            contactNames.add(contactName);
        }
        return contactNames;
    }

    /** Method to retrieve and return all customer names */
    public static ObservableList<String> getCustomerNames() throws SQLException {
        String sql = "SELECT Customer_Name FROM CUSTOMERS";
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            String customerName = rs.getString("Customer_Name");
            customerNames.add(customerName);
        }
        return customerNames;
    }

    /** Method to retrieve and return all user names */
    public static ObservableList<String> getUserNames() throws SQLException {
        String sql = "SELECT User_Name FROM USERS";
        ObservableList<String> userNames = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            String userName = rs.getString("User_Name");
            userNames.add(userName);
        }
        return userNames;
    }

    /** Method to add appointment to database */
    public static void addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomer_id());
        ps.setInt(8, appointment.getUser_id());
        ps.setInt(9, appointment.getContact_id());
        ps.executeUpdate();
    }

    /** Method to update appointment in database */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6, appointment.getEnd());
        ps.setInt(7, appointment.getCustomer_id());
        ps.setInt(8, appointment.getUser_id());
        ps.setInt(9, appointment.getContact_id());
        ps.setInt(10, appointment.getId());
        ps.executeUpdate();
    }

    // ********Methods to be used for Appointment form*********

    /**
     * Method to retrieve contact name using contact id */
    public static String getContactName(int contactId) throws SQLException {
        String sql = "SELECT Contact_Name FROM CONTACTS WHERE Contact_ID = " + contactId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        String contactName = "";
        while(rs.next()) {
            contactName = rs.getString("Contact_Name");
        }
        return contactName;
    }

    /** 
     * Method to retrieve customer name using customer id */
    public static String getCustomerName(int customerId) throws SQLException {
        String sql = "SELECT Customer_Name FROM CUSTOMERS WHERE Customer_ID = " + customerId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        String customerName = "";
        while(rs.next()) {
            customerName = rs.getString("Customer_Name");
        }
        return customerName;
    }
}
