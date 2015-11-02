package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.Assign;
import com.windyroad.nghia.common.ConvertUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/20/2015.
 * Lớp truy xuất dữ liệu cho Assign
 */
public class AssignData extends BaseDataHandle {
    public AssignData(Context context) {
        super(context);
    }

    /** Đổi Assign to ContentValues */
    private static void assignToContentValues(Assign assign, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(assign, values);

        String strExpiredAt = ConvertUtil.Calendar2String(assign.getExpiredAt(), null, null);

        values.put(KEY_ASSIGN_EXPIRED_AT, strExpiredAt);
        values.put(KEY_ASSIGN_WORK_STATUS, assign.getWorkStatus().name());
        values.put(KEY_ASSIGN_OUTLET_NAME, assign.getOutletName());
        values.put(KEY_ASSIGN_OUTLET_CITY, assign.getOutletCity());
        values.put(KEY_ASSIGN_OUTLET_DISTRICT, assign.getOutletDistrict());
        values.put(KEY_ASSIGN_OUTLET_AREA_ID, assign.getOutletAreaId());
        values.put(KEY_ASSIGN_OUTLET_ADDRESS, assign.getOutletAddress());
        values.put(KEY_ASSIGN_OUTLET_LATITUDE, assign.getOutletLatitude());
        values.put(KEY_ASSIGN_OUTLET_LONGITUDE, assign.getOutletLongitude());
    }

    /** Đổi Cursor to Assign */
    private static void cursorToAssign(Cursor cursor, Assign assign) {
        BaseDataHandle.cursorToBaseDataObject(cursor, assign);

        String strExpiredAt = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_EXPIRED_AT));
        String strWorkStatus = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_WORK_STATUS));

        Calendar expiredAt = ConvertUtil.String2Canendar(strExpiredAt, null, null, Calendar.getInstance());
        Assign.WorkStatus workStatus = Assign.WorkStatus.valueOf(strWorkStatus);
        String outletName = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_NAME));
        String outletCity = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_CITY));
        String outletDistrict = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_DISTRICT));
        long outletAreaId = cursor.getLong(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_AREA_ID));
        String outletAddress = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_ADDRESS));
        String outletLatitude = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_LATITUDE));
        String outletLongitude = cursor.getString(cursor.getColumnIndex(KEY_ASSIGN_OUTLET_LONGITUDE));

        assign.setExpiredAt(expiredAt);
        assign.setWorkStatus(workStatus);
        assign.setOutletName(outletName);
        assign.setOutletCity(outletCity);
        assign.setOutletCity(outletCity);
        assign.setOutletDistrict(outletDistrict);
        assign.setOutletAreaId(outletAreaId);
        assign.setOutletAddress(outletAddress);
        assign.setOutletLatitude(outletLatitude);
        assign.setOutletLongitude(outletLongitude);
    }

    /** Query Thông qua điều kiện */
    public static ArrayList<Assign> query(Context context, String[] columns, String where,
                                           String groupBy, String having, String orderBy, String limit){

        ArrayList<Assign> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_ASSIGN, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                Assign assign = new Assign();
                cursorToAssign(cursor, assign);
                resultList.add(assign);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }

    /** Tạo mới Assign */
    public static long add(Context context, Assign assign) {
        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            assignToContentValues(assign, values);

            resultId = db.insert(TABLE_ASSIGN, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /**
     * Lấy Assign Từ Id
     * @param assignId
     * @return
     */
    public static Assign getById(Context context, long assignId) {
        Assign resultAssign = null;
        SQLiteDatabase db = null;
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_ASSIGN +
                " WHERE " + KEY_ID + "=" + assignId;
        try {

            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()){
                resultAssign = new Assign();
                cursorToAssign(cursor, resultAssign);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultAssign;
    }

    /** lấy bằng WorkStatus */
    public static ArrayList<Assign> getByWorkStatus(Context context, long createById, Assign.WorkStatus workStatus){
        ArrayList<Assign> resultListAssign = new ArrayList<>();
        SQLiteDatabase db = null;
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_ASSIGN +
                " WHERE " + KEY_CREATE_BY + "=" + createById +
                " AND " + KEY_ASSIGN_WORK_STATUS + " like '" + workStatus.name() + "'";
        try {

            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Assign assign = new Assign();
                cursorToAssign(cursor, assign);

                resultListAssign.add(assign);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultListAssign;
    }


    /**
     * Tìm kiếm theo key, ràng buộc nhiều thứ
     * @param context
     * @param userId
     * @param workStatus
     * @param key
     * @return
     */
    public static ArrayList<Assign> getSearchByWorkStatus(Context context, long userId, Assign.WorkStatus workStatus, String key) {
        String where = KEY_CREATE_BY +'='+ userId +
                " AND " + KEY_ASSIGN_WORK_STATUS + " like '" + workStatus.name() + "'" +
                " AND (" +
                    KEY_ASSIGN_OUTLET_NAME + " like '%" + key +"%'" +
                    " OR " + KEY_ASSIGN_OUTLET_ADDRESS + " like '%" + key +"%'" +
                    " OR " + KEY_ASSIGN_OUTLET_CITY + " like '%" + key +"%'" +
                    " OR " + KEY_ASSIGN_OUTLET_DISTRICT + " like '%" + key +"%'" +
                    " OR " + KEY_ASSIGN_EXPIRED_AT + " like '%" + key +"%'" +
                ")";
        return query(context, null, where, null, null, null, null);
    }

    /** Đổi trạng thái để kiểm tra
     * return dòng ảnh hưởng*/
    public static long changeStatus(Context context, long assignId, Assign.WorkStatus status) {

        ContentValues values = new ContentValues();
        values.put(KEY_ASSIGN_WORK_STATUS, status.name());

        String condition = KEY_ID + "=" + assignId;

        return update(context, TABLE_ASSIGN, values, condition);
    }

    public static Assign getByServerId(Context context, long serverId) {
        Assign resultAssign = null;
        SQLiteDatabase db = null;
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_ASSIGN +
                " WHERE " + KEY_SERVER_ID + "=" + serverId;
        try {

            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()){
                resultAssign = new Assign();
                cursorToAssign(cursor, resultAssign);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultAssign;

    }

    /** Cập nhật Assign tất cả giá trị qua Id */
    public static long updateAllById(Context context, long id, Assign assign) {
        ContentValues values = new ContentValues();
        assignToContentValues(assign, values);

        String where = KEY_ID + "=" + id;

        return update(context, TABLE_ASSIGN, values, where);
    }

    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_ASSIGN, id, uploadStatus, serverId);
    }

    /**
     * Xóa bằng Work Status
     * @param context
     * @param workStatus
     * @return row effect
     */
    public static long deleteBy_WorkStatus(Context context, Assign.WorkStatus workStatus) {
        String where = KEY_ASSIGN_WORK_STATUS +"='" + workStatus.name() +"'";
        return BaseDataHandle.delete(context, TABLE_ASSIGN, where);
    }
}
