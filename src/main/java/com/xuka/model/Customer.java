package com.xuka.model;

public class Customer extends User {
    private String userEmail;
    private String userMobile;

    public Customer(String userId, String userName, String userPassword, String RegisterTime, String Role, String userEmail, String userMobile) {
        super(userId, userName, userPassword, RegisterTime, Role);
        this.userEmail = userEmail;
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Customer() {
        super();
        this.userEmail = "xuankhoa@example.com";
        this.userMobile = "0302948572";
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId='" + getUserId() + '\'' +
                ", userName='" + getUserName() + '\'' +
                ", userPassword='" + getUserPassword() + '\'' +
                ", RegisterTime='" + getRegisterTime() + '\'' +
                ", Role='" + getRole() + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userMobile='" + userMobile + '\'' +
                '}';
    }
}

