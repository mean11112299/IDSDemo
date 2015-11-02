package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imark.nghia.idscore.data.models.Attendance;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class AttendanceData extends BaseDataHandle {
    public AttendanceData(Context context) {
        super(context);
    }

    /** Chuyển thành ContentValues */
    private static void attendanceTrackingToContentValues(Attendance attendanceTracking, ContentValues values) {
        baseDataObjectToContentValues(attendanceTracking, values);

        values.put(KEY_AT_ATTENDANCE_TYPE, attendanceTracking.getAttendanceType());
    }

    /** Chuyển Cursor to Attendance */
    private static void cursorToAttendance(Cursor cursor, Attendance attendance) {
        cursorToBaseDataObject(cursor, attendance);

        String atType = cursor.getString(cursor.getColumnIndex(KEY_AT_ATTENDANCE_TYPE));

        attendance.setAttendanceType(atType);
    }

    /** Thêm */
    public static long add(Context context, Attendance attendanceTracking) {
        ContentValues values = new ContentValues();
        attendanceTrackingToContentValues(attendanceTracking, values);

        return BaseDataHandle.insert(context, TABLE_ATTENDANCE_TRACKING, values);
    }

    public static Attendance getById(Context context, long id) {
        Attendance resultAT = null;
        SQLiteDatabase db = null;
        try{

            db = new BaseDataHandle(context).getWritableDatabase();

            String condition = KEY_ID + "=" + id;
            Cursor cursor = db.query(TABLE_ATTENDANCE_TRACKING, null, condition, null, null, null, null);
            if (cursor.moveToNext()){
                resultAT = new Attendance();
                cursorToAttendance(cursor, resultAT);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultAT;
    }

}
