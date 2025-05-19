package com.xuka.model;

public class Admin extends User {
    public Admin(String userId, String userName, String userPassword, String RegisterTime, String Role) {
        super(userId, userName, userPassword, RegisterTime, Role);
    }
    public Admin() {
        super();
        this.Role = "Admin";
        this.userPassword ="admin";
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", userName='" + getUserName() + '\'' +
                ", userPassword='" + getUserPassword() + '\'' +
                ", RegisterTime='" + getRegisterTime() + '\'' +
                ", Role='" + getRole() + '\'' +
                '}';
    }
}
