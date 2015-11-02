package com.imark.nghia.idscore.data.models;

import android.content.Context;

import com.imark.nghia.idscore.data.ImageData;
import com.imark.nghia.idscore.data.ProductShownData;

/**
 * Created by Nghia-PC on 9/17/2015.
 */
public class ProductShownImg extends BaseDataObject {
    private long productShownId;
    private long imageId;

    private ProductShown productShown;
    private Image image;

    public long getProductShownId() {
        return productShownId;
    }

    public void setProductShownId(long productShownId) {
        this.productShownId = productShownId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }


    public ProductShown getProductShow(Context context){
        if (productShown == null){
            productShown = ProductShownData.getById(context, productShownId);
        }
        return productShown;
    }

    public Image getImage(Context context){
        if (image == null){
            image = ImageData.getById(context, imageId);
        }
        return image;
    }
}
