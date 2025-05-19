package com.xuka.model;

public class User {
    protected String userId;
    protected String userName;
    protected String userPassword;
    protected String RegisterTime;
    protected String Role;

    public User (String userId, String userName, String userPassword, String RegisterTime, String Role) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.RegisterTime = RegisterTime;
        this.Role = Role;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getRegisterTime() {
        return RegisterTime;
    }
    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }
    public String getRole() {
        return Role;
    }
    public void setRole(String role) {
        this.Role = role;
    }
    public User (){
        this.userId = "u_123451234";
        this.userName = "defaultUser";
        this.userPassword = "default";
        this.RegisterTime = "01-01-2024_00:00:00";
        this.Role = "Customer";
    }

    @Override
    public String toString(){
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", RegisterTime='" + RegisterTime + '\'' +
                ", Role='" + Role + '\'' +
                '}';
    }
}

