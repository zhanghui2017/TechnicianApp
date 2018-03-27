package com.gengli.technician.bean;

import android.content.Intent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Fitting implements Serializable {

    private String id;
    private String imgUrl;
    private String name;
    /**
     * 库存数量
     */
    private int lastCount;
    /**
     * 当前添加数量
     */
    private int count;

    /**
     * 申请数量
     */
    private int chooseCount;

    /**
     * 价格
     */
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getChooseCount() {
        return chooseCount;
    }

    public void setChooseCount(int chooseCount) {
        this.chooseCount = chooseCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastCount() {
        return lastCount;
    }

    public void setLastCount(int lastCount) {
        this.lastCount = lastCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
