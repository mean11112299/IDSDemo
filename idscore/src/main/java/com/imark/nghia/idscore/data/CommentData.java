package com.imark.nghia.idscore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.models.Comment;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 8/28/2015.
 */
public class CommentData extends BaseDataHandle {
    public CommentData(Context context) {
        super(context);
    }

    /**
     * Chuyển Outlet => ContentValue
     */
    private static void commentToContentValues(Comment comment, ContentValues values) {
        BaseDataHandle.baseDataObjectToContentValues(comment, values);

        values.put(KEY_COMMENT_ASSIGN_ID, comment.getAssignId());
        values.put(KEY_COMMENT_ADVANTAGE, comment.getAdvantage());
        values.put(KEY_COMMENT_DISADVANTAGE, comment.getDisadvantage());
        values.put(KEY_COMMENT_SUGGESTION, comment.getSuggestion());
        values.put(KEY_COMMENT_LATITUDE, comment.getLatitude());
        values.put(KEY_COMMENT_LONGITUDE, comment.getLongitude());
    }

    /**
     * Chuyển Cursor thành Image
     */
    private static void cursorToComment(Cursor cursor, Comment comment) {
        BaseDataHandle.cursorToBaseDataObject(cursor, comment);

        long assignId = cursor.getLong(cursor.getColumnIndex(KEY_COMMENT_ASSIGN_ID));
        String advantage = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_ADVANTAGE));
        String disadvantage = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_DISADVANTAGE));
        String suggestion = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_SUGGESTION));
        String latitude = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_LATITUDE));
        String longitude = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_LONGITUDE));

        comment.setAssignId(assignId);
        comment.setAdvantage(advantage);
        comment.setDisadvantage(disadvantage);
        comment.setSuggestion(suggestion);
        comment.setLatitude(latitude);
        comment.setLongitude(longitude);
    }

    /** Query Thông qua điều kiện */
    public static ArrayList<Comment> query(Context context, String[] columns, String where,
                                               String groupBy, String having, String orderBy, String limit){

        ArrayList<Comment> resultList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_COMMENT, columns, where, null, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()){
                Comment comment = new Comment();
                cursorToComment(cursor, comment);
                resultList.add(comment);
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
     */
    public static long add(Context context, Comment comment) {

        long resultId = -1;
        SQLiteDatabase db = null;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            ContentValues values = new ContentValues();
            commentToContentValues(comment, values);

            resultId = db.insert(TABLE_COMMENT, null, values);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultId;
    }

    public static ArrayList<Comment> getByAssignId(Context context, long assignId) {
        ArrayList<Comment> resultListComment = new ArrayList<>();
        SQLiteDatabase db = null;
        String where = KEY_COMMENT_ASSIGN_ID + "=" + assignId;
        try {
            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_COMMENT, null, where, null, null, null, null);
            while (cursor.moveToNext()){
                Comment comment = new Comment();
                cursorToComment(cursor, comment);
                resultListComment.add(comment);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultListComment;
    }

    public static Comment getById(Context context, long id) {
        Comment resultComment = null;
        SQLiteDatabase db = null;
        String where = KEY_ID + "=" + id;
        try {

            db = new BaseDataHandle(context).getWritableDatabase();

            Cursor cursor = db.query(TABLE_COMMENT, null, where, null, null, null, null);
            if (cursor.moveToNext()){
                resultComment = new Comment();
                cursorToComment(cursor, resultComment);
            }

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            BaseDataHandle.close(db);
        }
        return resultComment;
    }

    public static long updateAllById(Context context, long id, Comment comment) {
        ContentValues values = new ContentValues();
        commentToContentValues(comment, values);

        String where = KEY_ID + "=" + id;

        return update(context, TABLE_COMMENT, values, where);
    }

    public static ArrayList<Comment> getBy_AssignId_UploadStatus(Context context, long assignId, UploadStatus uploadStatus) {
        String strWhere = KEY_COMMENT_ASSIGN_ID + "='" + assignId + "'" +
                " AND " + KEY_UPLOAD_STATUS + "='" + uploadStatus.name() + "'";
        return query(context, null, strWhere, null, null, null, null);
    }

    public static long update_UploadStatus_ServerId_byId(Context context, long id, UploadStatus uploadStatus, long serverId) {
        return update_UploadStatus_ServerId_byId(context, TABLE_COMMENT, id, uploadStatus, serverId);
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
