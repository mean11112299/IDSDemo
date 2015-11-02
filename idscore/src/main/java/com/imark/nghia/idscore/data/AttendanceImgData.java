package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.AttendanceImg;
import com.imark.nghia.idscore.data.models.Image;

/**
 * Created by Nghia-PC on 8/31/2015.
 */
public class AttendanceImgData extends BaseDataHandle {
    public AttendanceImgData(Context context) {
        super(context);
    }

    /**
     * Chuyển Attendance Image => ContentValue
     */
    private static void attendanceTrackingImageToContentValues(AttendanceImg attendanceImage, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(attendanceImage, values);

        values.put(KEY_ATI_ATTENDANCE_TRACKING_ID, attendanceImage.getAttendanceId());
        values.put(KEY_ATI_IMAGE_ID, attendanceImage.getImageId());
    }

    /**
     * Chuyển Cursor thành Attendance Image
     */
    private static void cursorToAttendanceTrackingImage(Cursor cursor, AttendanceImg attendanceImg) {
        BaseDataHandle.cursorToBaseDataObject(cursor, attendanceImg);

        long attendanceId = cursor.getLong(cursor.getColumnIndex(KEY_ATI_ATTENDANCE_TRACKING_ID));
        long imageId = cursor.getLong(cursor.getColumnIndex(KEY_ATI_IMAGE_ID));

        attendanceImg.setAttendanceId(attendanceId);
        attendanceImg.setImageId(imageId);
    }


    /**
     * Tạo mới Image
     * Explain: add image, add addAttendanceImage
     */
    public static long add(Context context, Image image, AttendanceImg attendanceImage) {
        long resultId = -1;

        long imageId = ImageData.add(context, image);
        if (imageId > -1) {
            attendanceImage.setImageId(imageId);

            ContentValues values = new ContentValues();
            attendanceTrackingImageToContentValues(attendanceImage, values);

            resultId = insert(context, TABLE_ATTENDANCE_TRACKING_IMAGE, values);
        }

        return resultId;
    }

    /** Lấy Tracking Img qua Id
     *
     * @param context
     * @param id
     * @return null: không có
     */
    public static AttendanceImg getById(Context context, long id) {
        AttendanceImg result = null;
        SQLiteDatabase db = null;
        try{

            db = new BaseDataHandle(context).getWritableDatabase();

            String condition = KEY_ID + "=" + id;
            Cursor cursor = db.query(TABLE_ATTENDANCE_TRACKING_IMAGE, null, condition, null, null, null, null);
            if (cursor.moveToNext()){
                result = new AttendanceImg();
                cursorToAttendanceTrackingImage(cursor, result);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return result;
    }

    /** Cập nhật lại khi gởi xong server */
    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_ATTENDANCE_TRACKING_IMAGE, id, uploadStatus, serverId);
    }


    /*public static ArrayList<Outlet> getAll(Context context) {
        ArrayList<Outlet> resultListOutlet = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHelper(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_OUTLET, null, null, null, null, null, null);
            while (cursor.moveToNext()){
                Outlet outlet = new Outlet();
                cursorToOutlet(cursor, outlet);
                resultListOutlet.add(outlet);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHelper.close(db);
        }
        return resultListOutlet;
    }*/
}
