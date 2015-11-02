package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/17/2015.
 */
public class PostProductWSResult extends BaseWSResult {
    private long ProductID;

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        this.ProductID = productID;
    }
}
