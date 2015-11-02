package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.Image;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class ImageData extends BaseDataHandle {
    public ImageData(Context context) {
        super(context);
    }

    /**
     * Chuyển Outlet => ContentValue
     */
    private static void imageToContentValues(Image image, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(image, values);

        values.put(KEY_IMAGE_FILE_NAME, image.getFileName());
        values.put(KEY_IMAGE_FILE_PATH, image.getFilePath());
        values.put(KEY_IMAGE_LATITUDE, image.getLatitude());
        values.put(KEY_IMAGE_LONGITUDE, image.getLongitude());
    }

    /**
     * Chuyển Cursor thành Image
     */
    private static void cursorToImage(Cursor cursor, Image image) {
        BaseDataHandle.cursorToBaseDataObject(cursor, image);

        String fileName = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_FILE_NAME));
        String filePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_FILE_PATH));
        String latitude = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_LATITUDE));
        String longitude = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_LONGITUDE));

        image.setFileName(fileName);
        image.setFilePath(filePath);
        image.setLatitude(latitude);
        image.setLongitude(longitude);
    }


    /**
     * Tạo mới Image
     */
    public static long add(Context context, Image image) {

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            imageToContentValues(image, values);

            resultId = db.insert(TABLE_IMAGE, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /** Lấy Image qua Id */
    public static Image getById(Context context, long id) {
        Image resultImage = null;
        SQLiteDatabase db = null;
        try{

            db = new BaseDataHandle(context).getWritableDatabase();

            String condition = KEY_ID + "=" + id;
            Cursor cursor = db.query(TABLE_IMAGE, null, condition, null, null, null, null);
            if (cursor.moveToNext()){
                resultImage = new Image();
                cursorToImage(cursor, resultImage);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultImage;
    }

    /** Đổi Status, Server Id qua Id
     *
     * @param context
     * @param id
     * @param uploadStatus
     * @param serverId
     * @return Dòng ảnh hưởng
     */
    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_IMAGE, id, uploadStatus, serverId);
    }
}
