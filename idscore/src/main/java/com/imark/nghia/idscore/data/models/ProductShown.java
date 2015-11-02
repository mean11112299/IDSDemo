package com.imark.nghia.idscore.data.models;

import android.content.Context;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.ProductData;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 9/1/2015.
 */
public class ProductShown extends BaseDataObject {

    private long assignId;
    private long productId;
    private int number;
    private long retailPrice;

    private Product product;
    private Assign assign;

    public ProductShown() {
    }

    public ProductShown(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, long assignId, long productId, int number, long retailPrice) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.assignId = assignId;
        this.productId = productId;
        this.number = number;
        this.retailPrice = retailPrice;
    }

    public long getAssignId() {
        return assignId;
    }

    public void setAssignId(long assignId) {
        this.assignId = assignId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }


    public Product getProduct(Context context){
        if (product == null){
            product = ProductData.getById(context, productId);
        }
        return product;
    }

    public Assign getAssign(Context context) {
        if (assign == null){
            assign = AssignData.getById(context, assignId);
        }
        return assign;
    }
}
