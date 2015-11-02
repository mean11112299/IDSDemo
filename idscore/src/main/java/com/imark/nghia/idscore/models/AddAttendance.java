package com.imark.nghia.idscore.models;

/**
 * Created by Imark-N on 10/31/2015.
 */
public class AddAttendance {
    private String type;
    private String latitude;
    private String longitude;

    public AddAttendance() {
    }

    public AddAttendance(String type, String latitude, String longitude) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
