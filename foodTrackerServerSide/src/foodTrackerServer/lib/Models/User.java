package foodTrackerServer.lib.Models;

import com.sun.istack.internal.NotNull;

public class User {
    private int UserId;
    private String UserName;
    private String Password;

    @Override
    public String toString() {
        return "User{" +
                "Guid=" + UserId +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
    public User(){}
    public User(@NotNull String userName, @NotNull String password){
        UserName = userName;
        Password = password;
    }

    public int getUserId() {
        return UserId;
    }
    public String getUserName() {
        return UserName;
    }
    public String getPassword() {
        return Password;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public void setUserId(int userId) {
        UserId = userId;
    }
    public void setPassword(String password) {
        Password = password;
    }
}