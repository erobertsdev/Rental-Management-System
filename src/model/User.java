package model;

public class User {
    private int Id;
    private String userName;

    public User(int id, String userName) {
        Id = id;
        this.userName = userName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
