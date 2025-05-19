package com.xuka.model;

public class Order {
    private String orderId;
    private String userId;
    private String proId;
    private String orderTime;

    public Order ( String orderId, String userId, String proId, String orderTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.proId = proId;
        this.orderTime = orderTime;
    }
    public String getOrderId() {

        return orderId;
    }
    public void setOrderId(String orderId) {

        this.orderId = orderId;
    }
    public String getUserId() {

        return userId;
    }
    public void setUserId(String userId) {

        this.userId = userId;
    }
    public String getProId()
    {
        return proId;
    }
    public void setProId(String proId) {

        this.proId = proId;
    }
    public String getOrderTime() {
        return orderTime;
    }

    public Order(){
        this.orderId = "o_123451234";
        this.userId = "u_123451234";
        this.proId = "p_123451234";
        this.orderTime = "01-01-2024_00:00:00";
    }
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", proId='" + proId + '\'' +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}

