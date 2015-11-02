package com.imark.nghia.idscore.data.models;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class Attendance extends BaseDataObject {
    /** Giờ vào */
    public static final String IN_TIME = "IN_TIME";
    /** Giờ ra */
    public static final String OUT_TIME = "IN_TIME";

    private String attendanceType;

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getAttendanceType() {
        return attendanceType;
    }
}
