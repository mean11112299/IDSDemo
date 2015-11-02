package com.imark.nghia.idscore.data.models;

import android.content.Context;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AttendanceData;
import com.imark.nghia.idscore.data.ImageData;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/31/2015.
 */
public class AttendanceImg extends BaseDataObject {
    private long attendanceId;
    private long imageId;

    private Attendance attendance;
    private Image image;

    public AttendanceImg() {
    }

    public AttendanceImg(long _id, int serverId, String sessionCode, long createBy, Calendar createAt, UploadStatus uploadStatus, long attendanceId, long imageId) {
        super(_id, serverId, sessionCode, createBy, createAt, uploadStatus);
        this.attendanceId = attendanceId;
        this.imageId = imageId;
    }

    public long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }


    public Attendance getAttendance(Context context){
        if (attendance == null)
            attendance = AttendanceData.getById(context, attendanceId);
        return attendance;
    }

    public Image getImage(Context context){
        if (image == null){
            image = ImageData.getById(context, imageId);
        }
        return image;
    }
}
