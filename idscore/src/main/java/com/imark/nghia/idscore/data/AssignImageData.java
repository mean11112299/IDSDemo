package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.AssignImage;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class AssignImageData extends BaseDataHandle {
    public AssignImageData(Context context) {
        super(context);
    }


    /**
     * Chuyển Outlet => ContentValue
     */
    private static void assignImageToContentValues(AssignImage assignImage, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(assignImage, values);

        values.put(KEY_AI_ASSIGN_ID, assignImage.getAssignId());
        values.put(KEY_AI_IMAGE_ID, assignImage.getImageId());
        values.put(KEY_AI_IMAGE_TYPE, assignImage.getImageType());
    }

    /**
     * Chuyển Cursor thành Image
     */
    private static void cursorToAssigImage(Cursor cursor, AssignImage assignImage) {
        BaseDataHandle.cursorToBaseDataObject(cursor, assignImage);

        long assignId = cursor.getLong(cursor.getColumnIndex(KEY_AI_ASSIGN_ID));
        long imageId = cursor.getLong(cursor.getColumnIndex(KEY_AI_IMAGE_ID));
        String imageType = cursor.getString(cursor.getColumnIndex(KEY_AI_IMAGE_TYPE));

        assignImage.setAssignId(assignId);
        assignImage.setImageId(imageId);
        assignImage.setImageType(imageType);
    }

    /** Query Thông qua điều kiện */
    public static ArrayList<AssignImage> query(Context context, String[] columns, String where,
                                      String groupBy, String having, String orderBy, String limit){

        ArrayList<AssignImage> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_ASSIGN_IMAGE, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                AssignImage ai = new AssignImage();
                cursorToAssigImage(cursor, ai);
                resultList.add(ai);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }


    /**
     * Tạo mới Assign Image
     */
    public static long add(Context context, AssignImage assignImage) {

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            assignImageToContentValues(assignImage, values);

            resultId = db.insert(TABLE_ASSIGN_IMAGE, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /** Đếm ảnh */
    public static int countBy_AssignId_ImageType(Context context, long assignId, String imageType) {
        String condition = KEY_AI_ASSIGN_ID + "=" + assignId +
                " AND " + KEY_AI_IMAGE_TYPE + "=" + imageType;
        return count(context, TABLE_ASSIGN_IMAGE, condition);
    }

    public static ArrayList<AssignImage> getBy_AssignId_UploadStatus(Context context, long assignId, UploadStatus uploadStatus) {
        String strWhere = KEY_AI_ASSIGN_ID + "='" + assignId + "'" +
                " AND " + KEY_UPLOAD_STATUS + "='" + uploadStatus.name() + "'";
        return query(context, null, strWhere, null, null, null, null);
    }

    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_ASSIGN_IMAGE, id, uploadStatus, serverId);
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
