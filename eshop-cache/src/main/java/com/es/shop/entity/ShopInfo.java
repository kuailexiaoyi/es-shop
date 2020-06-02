package com.es.shop.entity;

import java.io.Serializable;

/**
 * @Description: 商品信息服务类
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
public class ShopInfo implements Serializable {

    private static final long serialVersionUID = 2081297096046606698L;

    private int id;

    private String name;

    private String level;

    private Double goodCommentRate;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(Double goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }
}
