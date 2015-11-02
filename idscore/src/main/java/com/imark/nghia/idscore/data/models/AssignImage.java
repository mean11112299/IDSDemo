package com.imark.nghia.idscore.data.models;

import android.content.Context;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.ImageData;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class AssignImage extends BaseDataObject {
    private long assignId;
    private long imageId;
    private String imageType;

    private Assign assign;
    private Image image;

    public AssignImage() {
    }

    public AssignImage(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, long assignId, long imageId, String imageType) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.assignId = assignId;
        this.imageId = imageId;
        this.imageType = imageType;
    }

    public long getAssignId() {
        return assignId;
    }

    public void setAssignId(long assignId) {
        this.assignId = assignId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Assign getAssign(Context context) {
        if (assign == null){
            assign = AssignData.getById(context, assignId);
        }
        return assign;
    }

    public Image getImage(Context context){
        if (image == null){
            image = ImageData.getById(context, imageId);
        }
        return image;
    }
}
