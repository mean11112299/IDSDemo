package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class PostAttendanceImgWSResult extends BaseWSResult {
    private long Attendance_ImageID;

    public PostAttendanceImgWSResult(int status, String description, long attendance_ImageID) {
        super(status, description);
        Attendance_ImageID = attendance_ImageID;
    }

    public long getAttendance_ImageID() {
        return Attendance_ImageID;
    }

    public void setAttendance_ImageID(long attendance_ImageID) {
        Attendance_ImageID = attendance_ImageID;
    }
}
