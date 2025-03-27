package com.example.vhh;

public class Product {
    private String name;
    private String category;
    private String description;
    private double price;
    private double discountedprice;
    private int imageResId;
    private boolean isAvailable;
    private int quantity;

    public Product(){}
    public Product(String name,double price, int imageResId,int quantity) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = quantity;
    }

    public Product(String name, String category, String description, double price, int imageResId, boolean isAvailable,int quantity) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
    public double getDiscountedPrice() {
        return discountedprice;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
