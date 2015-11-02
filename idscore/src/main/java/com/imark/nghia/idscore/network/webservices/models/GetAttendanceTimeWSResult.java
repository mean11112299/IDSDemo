package com.imark.nghia.idscore.network.webservices.models;

/**
 * Created by Nghia-PC on 9/11/2015.
 */
public class GetAttendanceTimeWSResult extends BaseWSResult {
    private String DateTimeServer;
    private String TypeAttendance;

    public String getDateTimeServer() {
        return DateTimeServer;
    }

    public void setDateTimeServer(String dateTimeServer) {
        DateTimeServer = dateTimeServer;
    }

    public String getTypeAttendance() {
        return TypeAttendance;
    }

    public void setTypeAttendance(String typeAttendance) {
        TypeAttendance = typeAttendance;
    }
}
