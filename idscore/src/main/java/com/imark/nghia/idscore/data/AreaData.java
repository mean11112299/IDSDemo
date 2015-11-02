package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.imark.nghia.idscore.data.models.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghia-PC on 9/5/2015.
 */
public class AreaData extends BaseDataHandle {
    public AreaData(Context context) {
        super(context);
    }

    /**
     * Đổi Area to ContentValues
     */
    private static void areaToContentValues(Area area, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(area, values);

        values.put(KEY_AREA_NAME, area.getName());
        values.put(KEY_AREA_PARENT_ID, area.getParentID());
        values.put(KEY_AREA_LEVEL, area.getAreaLevel());
    }

    /**
     * Đổi Area to Assign
     */
    private static void cursorToArea(Cursor cursor, Area area) {
        BaseDataHandle.cursorToBaseDataObject(cursor, area);

        String name = cursor.getString(cursor.getColumnIndex(KEY_AREA_NAME));
        int parentId = cursor.getInt(cursor.getColumnIndex(KEY_AREA_PARENT_ID));
        int areaLevel = cursor.getInt(cursor.getColumnIndex(KEY_AREA_LEVEL));

        area.setName(name);
        area.setParentID(parentId);
        area.setAreaLevel(areaLevel);
    }


    /**
     * Tạo mới Area
     */
    public static long add(Context context, Area area) {

        ContentValues values = new ContentValues();
        areaToContentValues(area, values);

        return BaseDataHandle.insert(context, TABLE_AREA, values);
    }

    /** Lấy bằng Level, parent id nếu có */
    public static List<Area> getBy_Level_ParentId(Context context, int areaLevel, @Nullable Long parentId) {
        List<Area> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        String query = "SELECT * " +
                " FROM " + TABLE_AREA +
                " WHERE " + KEY_AREA_LEVEL + "=" + areaLevel;

        if (parentId != null){
            query += " AND " + KEY_AREA_PARENT_ID + "=" + parentId;
        }

        try {

            db = new BaseDataHandle(context).getReadableDatabase();

            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()){

                Area area = new Area();
                cursorToArea(cursor, area);

                resultList.add(area);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultList;
    }
}
