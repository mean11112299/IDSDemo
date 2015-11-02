package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imark.nghia.idscore.data.models.Outlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghia-PC on 8/17/2015.
 * Cửa hàng Create, Read, Update, Delete
 * Lý do kế thừa: Sủ dụng lại KEY Không cần gọi cha
 */
public class OutletData extends BaseDataHandle {
    public OutletData(Context context) {
        super(context);
    }

    /** Chuyển Outlet => ContentValue */
    private static void outletToContentValues(Outlet outlet, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(outlet, values);

        values.put(KEY_OUTLET_NAME, outlet.getName());
        values.put(KEY_OUTLET_CITY, outlet.getCity());
        values.put(KEY_OUTLET_DISTRICT, outlet.getDistrict());
        values.put(KEY_OUTLET_ADDRESS, outlet.getAddress());
        values.put(KEY_OUTLET_LATITUDE, outlet.getLatitude());
        values.put(KEY_OUTLET_LONGITUDE, outlet.getLongitude());
    }

    /** Chuyển Cursor thành Outlet */
    private static void cursorToOutlet(Cursor cursor, Outlet outlet){
        BaseDataHandle.cursorToBaseDataObject(cursor, outlet);

        String name = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_NAME));
        String city = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_CITY));
        String district = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_DISTRICT));
        String address = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_ADDRESS));
        String latitude = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_LATITUDE));
        String longitude = cursor.getString(cursor.getColumnIndex(KEY_OUTLET_LONGITUDE));

        outlet.setName(name);
        outlet.setCity(city);
        outlet.setDistrict(district);
        outlet.setAddress(address);
        outlet.setLatitude(latitude);
        outlet.setLongitude(longitude);
    }

    /** Lấy bằng Query String */
    public static ArrayList<Outlet> getByQuery(Context context, String query) {
        ArrayList<Outlet> resultListOutlet = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){
                Outlet outlet = new Outlet();
                cursorToOutlet(cursor, outlet);
                resultListOutlet.add(outlet);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultListOutlet;
    }


    /** Tạo mới Outlet */
    public static long add(Context context, Outlet outlet) {

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            outletToContentValues(outlet, values);

            resultId = db.insert(TABLE_OUTLET, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }


    public static ArrayList<Outlet> getAll(Context context) {
        String strQuery = "SELECT * FROM " + TABLE_OUTLET;
        return getByQuery(context, strQuery);
    }

    public static Outlet getById(Context context, long outletId) {
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_OUTLET +
                " WHERE " + KEY_ID + "=" + outletId;
        ArrayList<Outlet> list = getByQuery(context, selectQuery);

        if (list == null || list.size() <= 0) {
            return null;  // không có giá trị
        } else {
            return list.get(0);  // lấy đầu tiên
        }
    }

    /** Lấy 5 Item qua tên */
    public static List<Outlet> search5byName(Context context, String name) {
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_OUTLET +
                " WHERE " + KEY_OUTLET_NAME + " LIKE '%" + name + "%' " +
                " LIMIT 0,5";
        return getByQuery(context, selectQuery);
    }


}
