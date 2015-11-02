package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.network.webservices.models.GetAttendanceTimeWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceTrackingWSResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class AttendanceWS {

    private static final String TAG = AttendanceWS.class.getName();

    /** Tạo chấm công */
    public static PostAttendanceTrackingWSResult postAttendance(long userId, String sessionCode, Calendar attendanceTime, String timePointType, String latitude, String longitude) {

        String strAttendanceTime = ConvertUtil.Calendar2String(attendanceTime, WSConfig.FORMAT_POST_TIME, null);

        List<UrlParam> listParams = new ArrayList<>();
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", sessionCode));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", userId+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pAttendanceDateTime", strAttendanceTime));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pTimePointType", timePointType+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pLatGPS", latitude+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pLongGPS", longitude + ""));

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ATTENDANCE,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);


        return new Gson().fromJson(strData, PostAttendanceTrackingWSResult.class);
    }

    /** Tạo chấm công */
    public static PostAttendanceTrackingWSResult postAssignAttendance(long userId, long assignServerId, String sessionCode, Calendar attendanceTime, String timePointType, String latitude, String longitude) {

        String strAttendanceTime = ConvertUtil.Calendar2String(attendanceTime, WSConfig.FORMAT_POST_TIME, null);

        List<UrlParam> listParams = new ArrayList<>();
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", sessionCode));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", userId+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pAttendanceDateTime", strAttendanceTime));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pTimePointType", timePointType+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pLatGPS", latitude+""));
        listParams.add(new UrlParam(UrlParam.ParamType.TEXT, "pLongGPS", longitude + ""));

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ASSIGN_ATTENDANCE,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);


        return new Gson().fromJson(strData, PostAttendanceTrackingWSResult.class);
    }

    public static GetAttendanceTimeWSResult getAttendanceTime(final long userId){
        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", userId+""));
        }};
        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_GET_ATTENDANCE_TIME,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG + " getAttendanceTime", strData);

        return new Gson().fromJson(strData, GetAttendanceTimeWSResult.class);
    }

    public static GetAttendanceTimeWSResult getAssignAttendanceTime(final long assignServerId){
        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
        }};
        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_GET_ATTENDANCE_ASSIGN_TIME,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG + " getAttendanceTime", strData);

        return new Gson().fromJson(strData, GetAttendanceTimeWSResult.class);
    }


}
