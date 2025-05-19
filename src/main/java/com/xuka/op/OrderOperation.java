package com.xuka.op;

public class OrderOperation {
    private static OrderOperation instance = null;

    private OrderOperation() {
        // Private constructor to prevent instantiation
    }

    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    public void A(){

    }
}
