package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/28/2015.
 * Image: hình ảnh
 */
public class Image extends BaseDataObject {
    private String fileName;
    private String filePath;
    private String latitude;
    private String longitude;

    public Image() {
    }

    public Image(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, String fileName, String filePath, String latitude, String longitude) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.fileName = fileName;
        this.filePath = filePath;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
