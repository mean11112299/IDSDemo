package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.RawHeaderType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceImgWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class AttendanceImgWS {

    private static final String TAG = AttendanceImgWS.class.getName();

    public static PostAttendanceImgWSResult postAttendanceImg
            (final String sessionCode, final long attendanceServerId, final long imageServerId) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", sessionCode));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pAttendanceID", attendanceServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pImageID", imageServerId+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ATTENDANCE_IMAGE,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostAttendanceImgWSResult.class);
    }

    public static PostAttendanceImgWSResult postAssignAttendanceImg
            (final String sessionCode, final long attendanceServerId, final long imageServerId) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", sessionCode));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pAttendanceID", attendanceServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pImageID", imageServerId+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ASSIGN_ATTENDANCE_IMAGE,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostAttendanceImgWSResult.class);
    }
}
