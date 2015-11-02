package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imark.nghia.idscore.data.models.BaseDataObject;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/17/2015.
 * Hỗ trợ tạo dữ liệu.
 */
public class BaseDataHandle extends SQLiteOpenHelper {
    // server default = 0
    // client default = -1
    public static final String LOG = BaseDataHandle.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ids_manager";

    //----- Tables Name-----
    public static final String TABLE_IMAGE = "image";
    public static final String TABLE_CODE_DETAIL = "code_detail";
    public static final String TABLE_AREA = "area";
    public static final String TABLE_ATTENDANCE_TRACKING = "attendance_tracking";
    public static final String TABLE_ATTENDANCE_TRACKING_IMAGE = "attendance_tracking_image";
    public static final String TABLE_OUTLET = "outlet";
    public static final String TABLE_ASSIGN = "assign";
    public static final String TABLE_ASSIGN_IMAGE = "assign_image";
    public static final String TABLE_COMMENT = "comment";
    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_PRODUCT_SHOWN = "product_shown";
    public static final String TABLE_PRODUCT_SHOWN_IMAGE = "product_shown_image";


    //----- Table Column, Create Statement -----

    // Table Code Detail
    public static final String KEY_CODE_DETAIL_GROUP_CODE = "group_code";
    public static final String KEY_CODE_DETAIL_NAME = "name";
    public static final String KEY_CODE_DETAIL_VALUE = "value";
    public static final String KEY_CODE_DETAIL_ORDINAL = "ordinal";
    public static final String KEY_CODE_DETAIL_REMARK = "remark";
    public static final String CODE_DETAIL_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_CODE_DETAIL + "( " +
                    KEY_CODE_DETAIL_GROUP_CODE + " TEXT, " +
                    KEY_CODE_DETAIL_NAME + " TEXT, " +
                    KEY_CODE_DETAIL_VALUE + " TEXT, " +
                    KEY_CODE_DETAIL_ORDINAL + " INTEGER, " +
                    KEY_CODE_DETAIL_REMARK + " TEXT, " +
                    " CONSTRAINT PK_CodeDetail_1 PRIMARY KEY (" +
                    KEY_CODE_DETAIL_GROUP_CODE + ", " + KEY_CODE_DETAIL_NAME + ")" +
                    ")";

    //---- Column Chung----
    public static final String KEY_ID = "_id";
    public static final String KEY_SERVER_ID = "server_id";
    public static final String KEY_SESSION_CODE = "session_code";
    public static final String KEY_CREATE_BY = "create_by";
    public static final String KEY_CREATE_AT = "create_at";
    public static final String KEY_UPLOAD_STATUS = "upload_status";
    public static final String CREATE_COMMON_COLUMNS =
            KEY_ID + " INTEGER  PRIMARY KEY  AUTOINCREMENT, " +
                    KEY_SERVER_ID + " INTEGER DEFAULT -1,  " +
                    KEY_SESSION_CODE + " TEXT,  " +
                    KEY_CREATE_BY + " INTEGER,  " +
                    KEY_CREATE_AT + " DATETIME,  " +
                    KEY_UPLOAD_STATUS + " TEXT";

    public static final String KEY_AREA_NAME = "name";
    public static final String KEY_AREA_PARENT_ID = "parent_id";
    public static final String KEY_AREA_LEVEL = "area_level";
    public static final String AREA_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_AREA + " ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_AREA_NAME + " TEXT,  " +
                    KEY_AREA_PARENT_ID + " INTEGER,  " +
                    KEY_AREA_LEVEL + " INTEGER  " +
                    ")";


    //TABLE_IMAGE
    public static final String KEY_IMAGE_FILE_NAME = "file_name";
    public static final String KEY_IMAGE_FILE_PATH = "file_path";
    public static final String KEY_IMAGE_LATITUDE = "latitude";
    public static final String KEY_IMAGE_LONGITUDE = "longitude";
    public static final String IMAGE_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_IMAGE + " ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_IMAGE_FILE_NAME + " TEXT,  " +
                    KEY_IMAGE_FILE_PATH + " TEXT,  " +
                    KEY_IMAGE_LATITUDE + " TEXT,  " +
                    KEY_IMAGE_LONGITUDE + " TEXT  " +
                    ")";

    //TABLE_ATTENDANCE_TRACKING
    public static final String KEY_AT_ATTENDANCE_TYPE = "attendance_type";
    public static final String ATTENDANCE_TRACKING_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_ATTENDANCE_TRACKING + " ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_AT_ATTENDANCE_TYPE + " TEXT " +
                    ")";

    //TABLE_ATTENDANCE_TRACKING_IMAGE
    public static final String KEY_ATI_ATTENDANCE_TRACKING_ID = "attendance_tracking_id";
    public static final String KEY_ATI_IMAGE_ID = "image_id";
    public static final String ATTENDANCE_TRACKING_IMAGE_CREATE_STATEMENT =
            "CREATE TABLE "+TABLE_ATTENDANCE_TRACKING_IMAGE+" ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_ATI_ATTENDANCE_TRACKING_ID + " INTEGER, " +
                    KEY_ATI_IMAGE_ID + " INTEGER " +
                    ")";

    //TABLE_OUTLET
    public static final String KEY_OUTLET_NAME = "outlet_name";
    public static final String KEY_OUTLET_CITY = "city";
    public static final String KEY_OUTLET_DISTRICT = "district";
    public static final String KEY_OUTLET_ADDRESS = "address";
    public static final String KEY_OUTLET_LATITUDE = "latitude";
    public static final String KEY_OUTLET_LONGITUDE = "longitude";
    public static final String OUTLET_CREATE_STATEMENT =
            "CREATE TABLE "+TABLE_OUTLET+" ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_OUTLET_NAME + " TEXT, " +
                    KEY_OUTLET_CITY + " TEXT, " +
                    KEY_OUTLET_DISTRICT + " TEXT, " +
                    KEY_OUTLET_ADDRESS + " TEXT, " +
                    KEY_OUTLET_LATITUDE + " TEXT, " +
                    KEY_OUTLET_LONGITUDE + " TEXT " +
                    ")";

    //TABLE_ASSIGN
    public static final String KEY_ASSIGN_EXPIRED_AT = "expired_at";
    public static final String KEY_ASSIGN_WORK_STATUS = "work_status";
    public static final String KEY_ASSIGN_OUTLET_NAME = "outlet_name";
    public static final String KEY_ASSIGN_OUTLET_CITY = "outlet_city";
    public static final String KEY_ASSIGN_OUTLET_DISTRICT = "outlet_district";
    public static final String KEY_ASSIGN_OUTLET_AREA_ID = "outlet_area_id";
    public static final String KEY_ASSIGN_OUTLET_ADDRESS = "outlet_address";
    public static final String KEY_ASSIGN_OUTLET_LATITUDE = "outlet_latitude";
    public static final String KEY_ASSIGN_OUTLET_LONGITUDE = "outlet_longitude";
    public static final String ASSIGN_CREATE_STATEMENT =
            "CREATE TABLE "+TABLE_ASSIGN+" ( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_ASSIGN_WORK_STATUS + " TEXT, " +
                    KEY_ASSIGN_EXPIRED_AT + " DATETIME, " +
                    KEY_ASSIGN_OUTLET_NAME + " TEXT, " +
                    KEY_ASSIGN_OUTLET_CITY + " TEXT, " +
                    KEY_ASSIGN_OUTLET_DISTRICT + " TEXT, " +
                    KEY_ASSIGN_OUTLET_AREA_ID + " INTEGER, " +
                    KEY_ASSIGN_OUTLET_ADDRESS + " TEXT, " +
                    KEY_ASSIGN_OUTLET_LATITUDE + " TEXT, " +
                    KEY_ASSIGN_OUTLET_LONGITUDE + " TEXT " +
                    ")";

    //TABLE_ASSIGN_IMAGE
    public static final String KEY_AI_ASSIGN_ID = "assign_id";
    public static final String KEY_AI_IMAGE_ID = "image_id";
    public static final String KEY_AI_IMAGE_TYPE = "image_type";
    public static final String ASSIGN_IMAGE_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_ASSIGN_IMAGE + "( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_AI_ASSIGN_ID + " INTEGER, " +
                    KEY_AI_IMAGE_ID + " INTEGER, " +
                    KEY_AI_IMAGE_TYPE + " TEXT" +
                    ")";

    //TABLE_COMMENT
    public static final String KEY_COMMENT_ASSIGN_ID = "assign_id";
    public static final String KEY_COMMENT_ADVANTAGE = "advantage";
    public static final String KEY_COMMENT_DISADVANTAGE = "disadvantage";
    public static final String KEY_COMMENT_SUGGESTION = "suggestion";
    public static final String KEY_COMMENT_LONGITUDE = "longitude";
    public static final String KEY_COMMENT_LATITUDE = "latitude";
    public static final String COMMENT_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_COMMENT + "( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_COMMENT_ASSIGN_ID + " INTEGER, " +
                    KEY_COMMENT_ADVANTAGE + " TEXT, " +
                    KEY_COMMENT_DISADVANTAGE +" TEXT, " +
                    KEY_COMMENT_SUGGESTION + " TEXT, " +
                    KEY_COMMENT_LONGITUDE + " TEXT, " +
                    KEY_COMMENT_LATITUDE + " TEXT" +
                    ")";

    //TABLE_PRODUCT
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_COLOR = "color";
    public static final String KEY_PRODUCT_PRICE = "price";
    public static final String PRODUCT_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_PRODUCT + "( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_PRODUCT_NAME + " TEXT, " +
                    KEY_PRODUCT_COLOR + " TEXT, " +
                    KEY_PRODUCT_PRICE + " TEXT" +
                    ")";

    //TABLE_PRODUCT_SHOWN
    public static final String KEY_PS_ASSIGN_ID = "assign_id";
    public static final String KEY_PS_PRODUCT_ID = "product_id";
    public static final String KEY_PS_NUMBER = "number";
    public static final String KEY_PS_RETAIL_PRICE = "retail_price";
    public static final String PRODUCT_SHOWN_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_PRODUCT_SHOWN + "( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_PS_ASSIGN_ID + " INTEGER, " +
                    KEY_PS_PRODUCT_ID + " INTEGER, " +
                    KEY_PS_NUMBER + " INTEGER, " +
                    KEY_PS_RETAIL_PRICE + " INTEGER" +
                    ")";

    //TABLE_PRODUCT_SHOWN_IMAGE
    public static final String KEY_PSI_PRODUCT_SHOW_ID = "product_show_id";
    public static final String KEY_PSI_IMAGE_ID = "image_id";
    public static final String PRODUCT_SHOWN_IMAGE_CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_PRODUCT_SHOWN_IMAGE + "( " +
                    CREATE_COMMON_COLUMNS + ", " +
                    KEY_PSI_PRODUCT_SHOW_ID + " INTEGER, " +
                    KEY_PSI_IMAGE_ID + " INTEGER "+
                    ")";
    

    public BaseDataHandle(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CODE_DETAIL_CREATE_STATEMENT);
        db.execSQL(AREA_CREATE_STATEMENT);
        db.execSQL(IMAGE_CREATE_STATEMENT);
        db.execSQL(ATTENDANCE_TRACKING_CREATE_STATEMENT);
        db.execSQL(ATTENDANCE_TRACKING_IMAGE_CREATE_STATEMENT);
        db.execSQL(OUTLET_CREATE_STATEMENT);
        db.execSQL(ASSIGN_CREATE_STATEMENT);
        db.execSQL(ASSIGN_IMAGE_CREATE_STATEMENT);
        db.execSQL(COMMENT_CREATE_STATEMENT);
        db.execSQL(PRODUCT_CREATE_STATEMENT);
        db.execSQL(PRODUCT_SHOWN_CREATE_STATEMENT);
        db.execSQL(PRODUCT_SHOWN_IMAGE_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // XÓA
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SHOWN_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SHOWN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGN_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_TRACKING_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_TRACKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CODE_DETAIL);

        // TẠO LẠI
        onCreate(db);
    }


    /** Chuyển BaseDataObject => Content Values; Không Put Id */
    public static void baseDataObjectToContentValues(BaseDataObject object, ContentValues values){
        String createAt = ConvertUtil.Calendar2String(object.getCreateAt(), null, null);

        //values.put(KEY_ID, object.get_id());
        values.put(KEY_SERVER_ID, object.getServerId());
        values.put(KEY_SESSION_CODE, object.getSessionCode());
        values.put(KEY_CREATE_BY, object.getCreateBy());
        values.put(KEY_CREATE_AT, createAt);
        values.put(KEY_UPLOAD_STATUS, object.getUploadStatus().name());
    }

    /** Chuyển Cursor => BaseDataObject */
    public static void cursorToBaseDataObject(Cursor cursor, BaseDataObject object){
        // Lấy dữ liệu
        long id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
        long serverId = cursor.getLong(cursor.getColumnIndex(KEY_SERVER_ID));
        String sessionCode = cursor.getString(cursor.getColumnIndex(KEY_SESSION_CODE));
        long createBy = cursor.getLong(cursor.getColumnIndex(KEY_CREATE_BY));
        Calendar createAt = ConvertUtil.String2Canendar(
                cursor.getString(cursor.getColumnIndex(KEY_CREATE_AT)),
                null, null, Calendar.getInstance());
        
        String strUploadStatus = cursor.getString(cursor.getColumnIndex(KEY_UPLOAD_STATUS));
        UploadStatus uploadStatus = UploadStatus.valueOf(strUploadStatus);
        
        // Truyền dữ liệu
        object.set_id(id);
        object.setServerId(serverId);
        object.setSessionCode(sessionCode);
        object.setCreateBy(createBy);
        object.setCreateAt(createAt);
        object.setUploadStatus(uploadStatus);
    }


    /** Đóng Database */
    public static void close(SQLiteDatabase db) {
        if (db != null)
            if (db.isOpen())
                db.close();
    }

    /** Reset Database: Xóa hết rồi tạo lại. True: thành công */
    public static boolean resetAllTable(Context context){
        boolean isSuccess = false;
        SQLiteDatabase db = null;
        try {
            BaseDataHandle dataHelper = new BaseDataHandle(context);
            db = dataHelper.getWritableDatabase();
            dataHelper.onUpgrade(db, 0, 0);
            isSuccess = true;

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            close(db);
        }
        return isSuccess;
    }

    /** Tạo mới
     *
     * @param context
     * @param tableName
     * @param values
     * @return new id, -1 nếu fail
     */
    static long insert(Context context, String tableName, ContentValues values) {
        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            resultId = db.insert(tableName, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /** Cập nhật
     *
     * @param context
     * @param tableName
     * @param values
     * @param strWhere
     * @return dòng ảnh hưởng
     */
    static long update(Context context, String tableName, ContentValues values, String strWhere) {
        long resultRowAffect = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            resultRowAffect = db.update(tableName, values, strWhere, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultRowAffect;
    }

    /** Xóa
     *
     * @param context
     * @param tableName
     * @param strWhere
     * @return dòng ảnh hưởng
     */
    static long delete(Context context, String tableName, String strWhere) {
        long resultRowAffect = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            resultRowAffect = db.delete(tableName, strWhere, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultRowAffect;
    }

    /** Đếm số dòng */
    static int count(Context context, String tableName, String strWhere) {
        int resultRow = -1;
        SQLiteDatabase db = null;
        String query = "SELECT COUNT(*) as count " +
                " FROM " + tableName +
                " WHERE " + strWhere;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            resultRow = cursor.getInt(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultRow;
    }

    /** Cập nhật lại khi gởi xong server */
    static long update_UploadStatus_ServerId_byId(
            Context context, String tableName, long id, UploadStatus uploadStatus, long serverId) {

        ContentValues values = new ContentValues();
        values.put(KEY_UPLOAD_STATUS, uploadStatus.name());
        values.put(KEY_SERVER_ID, serverId);

        String strWhere = KEY_ID + "=" + id;

        return BaseDataHandle.update(context, tableName, values, strWhere);
    }
}
