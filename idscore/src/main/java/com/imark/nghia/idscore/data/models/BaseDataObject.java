package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/19/2015.
 * Lớp mẫu cho Data - Chứa thuộc tính chung
 */
public class BaseDataObject {
    private long _id;
    private long serverId;
    private String sessionCode;
    private long createBy;
    private Calendar createAt;
    private UploadStatus uploadStatus;

    public BaseDataObject() {
    }

    public BaseDataObject(long _id, long serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus) {
        this._id = _id;
        this.serverId = serverId;
        this.sessionCode = sessionCode;
        this.createBy = createBy;
        this.createAt = createAt;
        this.uploadStatus = uploadStatus;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }

    public Calendar getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Calendar createAt) {
        this.createAt = createAt;
    }

    public UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
