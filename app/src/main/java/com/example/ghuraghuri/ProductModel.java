package com.example.ghuraghuri;

public class ProductModel {
    String product_name,quantity,price,features,description;

    public ProductModel() {
    }

    public ProductModel(String product_name, String quantity, String price, String features, String description) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.features = features;
        this.description = description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
