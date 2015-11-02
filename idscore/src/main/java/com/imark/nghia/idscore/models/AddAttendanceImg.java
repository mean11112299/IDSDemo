package com.imark.nghia.idscore.models;

/**
 * Created by Imark-N on 11/2/2015.
 */
public class AddAttendanceImg {
    private String latitude;
    private String longitude;

    public AddAttendanceImg(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
