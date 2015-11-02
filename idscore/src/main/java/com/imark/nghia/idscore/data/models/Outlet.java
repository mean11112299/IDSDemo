package com.imark.nghia.idscore.data.models;

import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/19/2015.
 * Lớp Outlet truy vấn Data
 */
public class Outlet extends BaseDataObject {
    private String name;
    private String district;
    private String city;
    private String address;
    private String latitude;
    private String longitude;

    public Outlet() {
    }

    public Outlet(long _id, long serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, String name, String district, String city, String address, String latitude, String longitude) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.name = name;
        this.district = district;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    //region Method
    //TODO: Tạo hàm lấy ListAssign từ OutletId
    //endregion
}
