package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.data.models.ProductShownImg;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/17/2015.
 */
public class ProductShownImgData extends BaseDataHandle {
    public ProductShownImgData(Context context) {
        super(context);
    }

    /**
     * Chuyển Attendance Image => ContentValue
     */
    private static void productShownImgToContentValues(ProductShownImg productShownImg, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(productShownImg, values);

        values.put(KEY_PSI_IMAGE_ID, productShownImg.getImageId());
        values.put(KEY_PSI_PRODUCT_SHOW_ID, productShownImg.getProductShownId());
    }

    /**
     * Chuyển Cursor thành Attendance Image
     */
    private static void cursorToProductShownImg(Cursor cursor, ProductShownImg productShownImg) {
        BaseDataHandle.cursorToBaseDataObject(cursor, productShownImg);

        long imageId = cursor.getLong(cursor.getColumnIndex(KEY_PSI_IMAGE_ID));
        long productShownId = cursor.getLong(cursor.getColumnIndex(KEY_PSI_PRODUCT_SHOW_ID));

        productShownImg.setImageId(imageId);
        productShownImg.setProductShownId(productShownId);
    }

    /** Query Thông qua điều kiện */
    public static ArrayList<ProductShownImg> query(Context context, String[] columns, String where,
                                                String groupBy, String having, String orderBy, String limit){

        ArrayList<ProductShownImg> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_PRODUCT_SHOWN_IMAGE, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                ProductShownImg psi = new ProductShownImg();
                cursorToProductShownImg(cursor, psi);
                resultList.add(psi);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }


    /**
     * Tạo mới Image
     * Explain: add image, add ProductShownImage
     */
    public static long add(Context context, Image image, ProductShownImg productShownImg) {
        long resultId = -1;

        long imageId = ImageData.add(context, image);
        if (imageId > -1) {
            productShownImg.setImageId(imageId);

            ContentValues values = new ContentValues();
            productShownImgToContentValues(productShownImg, values);

            resultId = insert(context, TABLE_PRODUCT_SHOWN_IMAGE, values);
        }

        return resultId;
    }

    /** Lấy PROduct shown Img qua Id
     *
     * @param context
     * @param id
     * @return null: không có
     */
    public static ProductShownImg getById(Context context, long id) {
        ProductShownImg result = null;
        SQLiteDatabase db = null;
        try{

            db = new BaseDataHandle(context).getWritableDatabase();

            String condition = KEY_ID + "=" + id;
            Cursor cursor = db.query(TABLE_PRODUCT_SHOWN_IMAGE, null, condition, null, null, null, null);
            if (cursor.moveToNext()){
                result = new ProductShownImg();
                cursorToProductShownImg(cursor, result);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return result;
    }

    /** Cập nhật lại khi gởi xong server */
    public static long updateStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, int serverId) {
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOAD_STATUS, uploadStatus.name());
        values.put(KEY_SERVER_ID, serverId);

        String strWhere = KEY_ID + "=" + id;

        return BaseDataHandle.update(context, TABLE_PRODUCT_SHOWN, values, strWhere);
    }

    public static ArrayList<ProductShownImg> getBy_AssignId_UploadStatus(Context context, long productShownId, UploadStatus uploadStatus) {
        String strWhere = KEY_PSI_PRODUCT_SHOW_ID + "='" + productShownId + "'" +
                " AND " + KEY_UPLOAD_STATUS + "='" + uploadStatus.name() + "'";
        return query(context, null, strWhere, null, null, null, null);
    }

    public static void update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        update_UploadStatus_ServerId_byId(context, TABLE_PRODUCT_SHOWN_IMAGE, id, uploadStatus, serverId);
    }
}
