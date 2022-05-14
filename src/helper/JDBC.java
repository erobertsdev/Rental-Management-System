package helper;

import java.sql.*;
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

    // ---------------CRUD OPERATIONS---------------
    // Create/insert a record into a table, just an example
    public static int insert(String customerName, int customerId) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Customer_ID) VALUES (?, ?)"; // ? are bind variables, indexed starting at 1
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName); // assigns customerName to the first bind variable in the sql string
        ps.setInt(2, customerId); // assigned customerId to the second bind variable
        int rowsAffected = ps.executeUpdate(); // returns the number of rows affected, in this case should be 1
        return rowsAffected;
    }

    // Update a record, example
    public static int update(int customerId, String customerName) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ? WHERE Customer_Id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName); // assigns customerName to the first bind variable in the sql string
        ps.setInt(2, customerId); // assigned customerId to the second bind variable
        int rowsAffected = ps.executeUpdate(); // returns the number of rows affected, in this case should be 1
        return rowsAffected;
    }

    // Delete a record, example
    public static int delete(int customerId) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_Id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId); // Customer ID to be deleted
        int rowsAffected = ps.executeUpdate(); // returns the number of rows affected, in this case should be 1
        return rowsAffected;
    }

    // -----------------RUNNING QUERIES-----------------------
    // Retrieve Customer ID and customer Name, for example
    public static void select() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while(rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            System.out.print(customerId + " | " + customerName + "\n");
        }
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
                match = true;
            }
        }
        return match;
    }
}