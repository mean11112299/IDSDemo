package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class PostAttendanceTrackingWSResult extends BaseWSResult {
    private int AttendanceTrackingID;

    public PostAttendanceTrackingWSResult() {
    }

    public PostAttendanceTrackingWSResult(int status, String description, int attendanceTrackingID) {
        super(status, description);
        AttendanceTrackingID = attendanceTrackingID;
    }

    public int getAttendanceTrackingID() {
        return AttendanceTrackingID;
    }

    public void setAttendanceTrackingID(int attendanceTrackingID) {
        AttendanceTrackingID = attendanceTrackingID;
    }
}
