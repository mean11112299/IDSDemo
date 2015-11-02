package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.ProductShown;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/1/2015.
 */
public class ProductShownData extends BaseDataHandle {
    public ProductShownData(Context context) {
        super(context);
    }

    private static void productShownToContentValues(ProductShown productShown, ContentValues values) {
        baseDataObjectToContentValues(productShown, values);

        values.put(KEY_PS_ASSIGN_ID, productShown.getAssignId());
        values.put(KEY_PS_PRODUCT_ID, productShown.getProductId());
        values.put(KEY_PS_NUMBER, productShown.getNumber());
        values.put(KEY_PS_RETAIL_PRICE, productShown.getRetailPrice());
    }

    private static void cursorToProductShown(Cursor cursor, ProductShown productShown){
        cursorToBaseDataObject(cursor, productShown);

        long assignId = cursor.getLong(cursor.getColumnIndex(KEY_PS_ASSIGN_ID));
        long productId = cursor.getLong(cursor.getColumnIndex(KEY_PS_PRODUCT_ID));
        int number = cursor.getInt(cursor.getColumnIndex(KEY_PS_NUMBER));
        long retailPrice = cursor.getLong(cursor.getColumnIndex(KEY_PS_RETAIL_PRICE));

        productShown.setAssignId(assignId);
        productShown.setProductId(productId);
        productShown.setNumber(number);
        productShown.setRetailPrice(retailPrice);
    }

    /**Tạo Product */
    public static long add(Context context, ProductShown productShown){

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            productShownToContentValues(productShown, values);

            resultId = db.insert(TABLE_PRODUCT_SHOWN, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /** Query Thông qua điều kiện */
    public static ArrayList<ProductShown> query(Context context, String[] columns, String where,
                                               String groupBy, String having, String orderBy, String limit){

        ArrayList<ProductShown> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT_SHOWN, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                ProductShown ps = new ProductShown();
                cursorToProductShown(cursor, ps);
                resultList.add(ps);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }

    public static ArrayList<ProductShown> getByAssignId(Context context, long assignId) {
        ArrayList<ProductShown> resultListProductShown = new ArrayList<>();
        SQLiteDatabase db = null;
        String where = KEY_PS_ASSIGN_ID + "=" + assignId;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT_SHOWN, null, where, null, null, null, null);
            while (cursor.moveToNext()){
                ProductShown productShown = new ProductShown();
                cursorToProductShown(cursor, productShown);
                resultListProductShown.add(productShown);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultListProductShown;
    }

    public static ProductShown getById(Context context, long id) {
        ProductShown productShown = null;
        SQLiteDatabase db = null;
        String where = KEY_ID + "=" + id;
        try {

            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT_SHOWN, null, where, null, null, null, null);
            if (cursor.moveToNext()){
                productShown = new ProductShown();
                cursorToProductShown(cursor, productShown);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return productShown;
    }

    public static long updateAllById(Context context, long id, ProductShown productShown) {
        ContentValues values = new ContentValues();
        productShownToContentValues(productShown, values);

        String where = KEY_ID + "=" + id;

        return update(context, TABLE_PRODUCT_SHOWN, values, where);
    }

    public static ArrayList<ProductShown> getBy_AssignId_UploadStatus(Context context, long assignId, UploadStatus uploadStatus) {
        String strWhere = KEY_PS_ASSIGN_ID + "='" + assignId + "'" +
                " AND " + KEY_UPLOAD_STATUS + "='" + uploadStatus.name() + "'";
        return query(context, null, strWhere, null, null, null, null);
    }

    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_PRODUCT_SHOWN, id, uploadStatus, serverId);
    }
}
