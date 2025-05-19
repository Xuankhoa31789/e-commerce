package com.xuka.model;

public class Product {
    private String proID;
    private String proModel;
    private String proCategory;
    private String proName;
    private double proCurrentPrice;
    private double proRawPrice;
    private double proDiscount;
    private int proLikesCount;

    public Product(String proID, String proModel, String proCategory, String proName,
                   double proCurrentPrice, double proRawPrice, double proDiscount, int proLikesCount) {
        this.proID = proID;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }
    public String getProID() {
        return proID;
    }
    public void setProID(String proID) {
        this.proID = proID;
    }
    public String getProModel() {
        return proModel;
    }
    public void setProModel(String proModel) {
        this.proModel = proModel;
    }
    public String getProCategory() {
        return proCategory;
    }
    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }
    public String getProName() {
        return proName;
    }
    public void setProName(String proName) {
        this.proName = proName;
    }
    public double getProCurrentPrice() {
        return proCurrentPrice;
    }
    public void setProCurrentPrice(double proCurrentPrice) {
        this.proCurrentPrice = proCurrentPrice;
    }
    public double getProRawPrice() {
        return proRawPrice;
    }
    public void setProRawPrice(double proRawPrice) {
        this.proRawPrice = proRawPrice;
    }
    public double getProDiscount() {
        return proDiscount;
    }
    public void setProDiscount(double proDiscount) {
        this.proDiscount = proDiscount;
    }
    public int getProLikesCount() {
        return proLikesCount;
    }
    public void setProLikesCount(int proLikesCount) {
        this.proLikesCount = proLikesCount;
    }
    public Product(){
        this.proID = "p_123456789";
        this.proModel = "Model_1";
        this.proCategory = "Category_1";
        this.proName = "Product_1";
        this.proCurrentPrice = 0.0;
        this.proRawPrice = 0.0;
        this.proDiscount = 0.0;
        this.proLikesCount = 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "proID='" + proID + '\'' +
                ", proModel='" + proModel + '\'' +
                ", proCategory='" + proCategory + '\'' +
                ", proName='" + proName + '\'' +
                ", proCurrentPrice=" + proCurrentPrice +
                ", proRawPrice=" + proRawPrice +
                ", proDiscount=" + proDiscount +
                ", proLikesCount=" + proLikesCount +
                '}';
    }
}