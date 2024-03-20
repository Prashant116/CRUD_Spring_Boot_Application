package com.storeSite.storeSite.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String brand;


    private String catagory;
    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Date createdAt;
    private String imageFileName;

    public Product(int id, String name, String brand, String catagory, double price, String description, Date createdAt, String imageFileName) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.catagory = catagory;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.imageFileName = imageFileName;
    }

    public Product(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
