package com.es.shop.entity;

import java.io.Serializable;

/**
 * @Description: 商品类
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 2025247550096367457L;

    private int id;

    private String name;

    private int price;

    private String picturelist;

    private String specification;

    private String service;

    private String modifiedTime;

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public int getId() {
        return id;
    }

    private String color;

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPicturelist() {
        return picturelist;
    }

    public void setPicturelist(String picturelist) {
        this.picturelist = picturelist;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
