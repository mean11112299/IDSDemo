package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/15/2015.
 */
public class PostAttendanceImgWSRequest {
    private String pAppCode;
    private String pSessionCode;
    private String AttendanceID;
    private String ImageID;

    public PostAttendanceImgWSRequest() {
    }

    public PostAttendanceImgWSRequest(String pAppCode, String pSessionCode, String attendanceID, String imageID) {
        this.pAppCode = pAppCode;
        this.pSessionCode = pSessionCode;
        AttendanceID = attendanceID;
        ImageID = imageID;
    }

    public String getpAppCode() {
        return pAppCode;
    }

    public void setpAppCode(String pAppCode) {
        this.pAppCode = pAppCode;
    }

    public String getpSessionCode() {
        return pSessionCode;
    }

    public void setpSessionCode(String pSessionCode) {
        this.pSessionCode = pSessionCode;
    }

    public String getAttendanceID() {
        return AttendanceID;
    }

    public void setAttendanceID(String attendanceID) {
        AttendanceID = attendanceID;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }
}
