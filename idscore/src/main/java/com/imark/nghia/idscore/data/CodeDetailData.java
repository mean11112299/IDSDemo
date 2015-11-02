package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imark.nghia.idscore.data.models.CodeDetail;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 8/21/2015.
 * Giúp truy cập dữ liệu Code Detail
 */
public class CodeDetailData extends BaseDataHandle {
    public CodeDetailData(Context context) {
        super(context);
    }

    /**
     * Chuyển Cursor thành CodeDetail
     */
    private static void cursorToCodeDetail(Cursor cursor, CodeDetail codeDetail){
        String groupCode = cursor.getString(cursor.getColumnIndex(KEY_CODE_DETAIL_GROUP_CODE));
        String name = cursor.getString(cursor.getColumnIndex(KEY_CODE_DETAIL_NAME));
        String value = cursor.getString(cursor.getColumnIndex(KEY_CODE_DETAIL_VALUE));
        int ordinal = cursor.getInt(cursor.getColumnIndex(KEY_CODE_DETAIL_ORDINAL));
        String remark = cursor.getString(cursor.getColumnIndex(KEY_CODE_DETAIL_REMARK));

        codeDetail.setGroupCode(groupCode);
        codeDetail.setName(name);
        codeDetail.setValue(value);
        codeDetail.setOrdinal(ordinal);
        codeDetail.setRemark(remark);
    }

    /**
     * Đổ CodeDetail vào ContentValues
     */
    private static void codeDetailToContentValues(CodeDetail codeDetail, ContentValues values){
        values.put(KEY_CODE_DETAIL_GROUP_CODE, codeDetail.getGroupCode());
        values.put(KEY_CODE_DETAIL_NAME, codeDetail.getName());
        values.put(KEY_CODE_DETAIL_VALUE, codeDetail.getValue());
        values.put(KEY_CODE_DETAIL_ORDINAL, codeDetail.getOrdinal());
        values.put(KEY_CODE_DETAIL_REMARK, codeDetail.getRemark());
    }

    /**
     * Lấy Qua Group Code
     */
    public static ArrayList<CodeDetail> getByGroupCode(Context context, String groupCode) {
        ArrayList<CodeDetail> resultListCodeDetail = new ArrayList<>();
        SQLiteDatabase db = null;
        String selectQuery = "SELECT * " +
                " FROM " + TABLE_CODE_DETAIL +
                " WHERE " + KEY_CODE_DETAIL_GROUP_CODE + " like '" + groupCode + "'";
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()){
                CodeDetail codeDetail = new CodeDetail();
                cursorToCodeDetail(cursor, codeDetail);
                resultListCodeDetail.add(codeDetail);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultListCodeDetail;
    }

    public static long add(Context context, CodeDetail codeDetail) {

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            codeDetailToContentValues(codeDetail, values);

            resultId = db.insert(TABLE_CODE_DETAIL, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }
}
