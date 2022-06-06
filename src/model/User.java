package model;

public class User {
    private int Id;
    private static String userId;
    private String userName;

    public User(int id, String userId, String userName) {
        Id = id;
        User.userId = userId;
        this.userName = userName;
    }

    /**
     * Get user ID
     * @return
     */
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public static String getCurrentId() {
        return userId;
    }

    /**
     * Get current user name
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
