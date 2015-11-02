package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/20/2015.
 * Lớp Assign, truy vấn Data
 */
public class Assign extends BaseDataObject{
    private Calendar expiredAt;
    private WorkStatus workStatus;
    private String outletName;
    private String outletCity;
    private String outletDistrict;
    private long outletAreaId;
    private String outletAddress;
    private String outletLatitude;
    private String outletLongitude;

    public Assign() {
    }

    public Assign(long _id, long serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, Calendar expiredAt, WorkStatus workStatus, String outletName, String outletCity, String outletDistrict, long outletAreaId, String outletAddress, String outletLatitude, String outletLongitude) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.expiredAt = expiredAt;
        this.workStatus = workStatus;
        this.outletName = outletName;
        this.outletCity = outletCity;
        this.outletDistrict = outletDistrict;
        this.outletAreaId = outletAreaId;
        this.outletAddress = outletAddress;
        this.outletLatitude = outletLatitude;
        this.outletLongitude = outletLongitude;
    }

    public Assign(Calendar expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Calendar getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Calendar expiredAt) {
        this.expiredAt = expiredAt;
    }

    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletCity() {
        return outletCity;
    }

    public void setOutletCity(String outletCity) {
        this.outletCity = outletCity;
    }

    public String getOutletDistrict() {
        return outletDistrict;
    }

    public void setOutletDistrict(String outletDistrict) {
        this.outletDistrict = outletDistrict;
    }

    public long getOutletAreaId() {
        return outletAreaId;
    }

    public void setOutletAreaId(long outletAreaId) {
        this.outletAreaId = outletAreaId;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getOutletLatitude() {
        return outletLatitude;
    }

    public void setOutletLatitude(String outletLatitude) {
        this.outletLatitude = outletLatitude;
    }

    public String getOutletLongitude() {
        return outletLongitude;
    }

    public void setOutletLongitude(String outletLongitude) {
        this.outletLongitude = outletLongitude;
    }

    //endregion

    /** Trạng thái hoàn thành công việc */
    public enum WorkStatus {
        NEW,
        DOING,
        FINISHED
    }
}
