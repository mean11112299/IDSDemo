package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/22/2015.
 * Model Sản Phẩm
 */
public class Product extends BaseDataObject {
    private String name;
    private String color;
    private long price;

    public Product() {
    }

    public Product(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, String name, String color, long price) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.name = name;
        this.color = color;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
