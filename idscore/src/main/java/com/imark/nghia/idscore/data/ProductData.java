package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghia-PC on 8/22/2015.
 * Hỗ trợ CRUD Product
 */
public class ProductData extends BaseDataHandle {
    public ProductData(Context context) {
        super(context);
    }

    private static void productToContentValues(Product product, ContentValues values) {
        baseDataObjectToContentValues(product, values);

        values.put(KEY_PRODUCT_NAME, product.getName());
        values.put(KEY_PRODUCT_COLOR, product.getColor());
        values.put(KEY_PRODUCT_PRICE, product.getPrice());
    }

    private static void cursorToProduct(Cursor cursor, Product product){
        cursorToBaseDataObject(cursor, product);

        String name = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME));
        String color = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_COLOR));
        long price = cursor.getLong(cursor.getColumnIndex(KEY_PRODUCT_PRICE));

        product.setName(name);
        product.setColor(color);
        product.setPrice(price);
    }

    /** Query Thông qua điều kiện */
    public static List<Product> query(Context context, String[] columns, String where,
                                      String groupBy, String having, String orderBy, String limit){

        ArrayList<Product> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                Product product = new Product();
                cursorToProduct(cursor, product);
                resultList.add(product);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }

    /**Tạo Product */
    public static long add(Context context, Product product){

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            productToContentValues(product, values);

            resultId = db.insert(TABLE_PRODUCT, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    /** Lấy tất cả Product */
    public static ArrayList<Product> getAll(Context context) {
        ArrayList<Product> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null);
            while (cursor.moveToNext()){
                Product product = new Product();
                cursorToProduct(cursor, product);
                resultList.add(product);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }

    public static Product getById(Context context, long id) {
        Product resultProduct = null;
        SQLiteDatabase db = null;
        String where = KEY_ID + "=" + id;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT, null, where, null, null, null, null);
            while (cursor.moveToNext()){
                resultProduct = new Product();
                cursorToProduct(cursor, resultProduct);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultProduct;
    }


    public static List<Product> getByUploadStatus(Context context, UploadStatus status) {
        String where = KEY_UPLOAD_STATUS + "='" + status.name() + "'";
        return query(context,null, where, null, null, null, null);
    }

    /** Cập nhật lại khi gởi xong server */
    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_PRODUCT, id, uploadStatus, serverId);
    }
}
