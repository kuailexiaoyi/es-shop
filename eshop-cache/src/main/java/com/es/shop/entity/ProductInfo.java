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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
