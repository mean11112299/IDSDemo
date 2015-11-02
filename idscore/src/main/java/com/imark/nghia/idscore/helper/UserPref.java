package com.imark.nghia.idscore.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.imark.nghia.idscore.models.UserInfo;

/**
 * Created by Nghia-PC on 8/24/2015.
 * Xử lý lưu, lấy UserInfo trong references
 */
public class UserPref {
    private static final String PREF_NAME_USER = "user_info";
    private static final String PREF_KEY_USER_ID = "user_id";
    private static final String PREF_KEY_USER_CODE = "user_code";
    private static final String PREF_KEY_USER_NAME = "user_name";
    private static final String PREF_KEY_USER_DISPLAY_NAME = "user_display_name";
    private static final String PREF_KEY_USER_ICON = "user_icon";

    /**
     * Lưu user Đăng nhập vào Pref
     * @param userInfo
     */
    public static void saveUserPref(Context context, UserInfo userInfo) {
        SharedPreferences spfInfo = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spfInfo.edit();
        editor.putLong(PREF_KEY_USER_ID, userInfo.getUserID());
        editor.putString(PREF_KEY_USER_CODE, userInfo.getUserCode());
        editor.putString(PREF_KEY_USER_NAME, userInfo.getUserName());
        editor.putString(PREF_KEY_USER_DISPLAY_NAME, userInfo.getDisplayName());
        editor.putString(PREF_KEY_USER_ICON, userInfo.getIconUrl());
        editor.apply();
    }

    /**
     * Lấy User Info trong Pref
     * @param context
     * @return
     */
    public static UserInfo getUserPref(Context context){
        SharedPreferences spfInfo = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE);

        long userId = spfInfo.getLong(PREF_KEY_USER_ID, -1);
        String userCode = spfInfo.getString(PREF_KEY_USER_CODE, "");
        String userName  = spfInfo.getString(PREF_KEY_USER_NAME, "");
        String userDisplayName = spfInfo.getString(PREF_KEY_USER_DISPLAY_NAME, "");
        String userIcon = spfInfo.getString(PREF_KEY_USER_ICON, "");

        return new UserInfo(userId, userCode, userName, userDisplayName, userIcon);
    }

    /**
     * Lấy User Id trong UserPref
     * @param context
     * @return -1: không có dữ liệu
     */
    public static long getUserPrefId(Context context){
        SharedPreferences spfInfo = context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE);
        return spfInfo.getLong(PREF_KEY_USER_ID, -1);
    }

    /**
     * Xóa thông tin User
     */
    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME_USER, Context.MODE_PRIVATE)
                .edit().clear().commit();
    }
}
