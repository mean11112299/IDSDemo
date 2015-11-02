package com.imark.nghia.idscore.network.webservices;

/**
 * Created by Nghia-PC on 9/4/2015.
 */
public class WSConfig {
    /** Tên app Dùng gởi WS */
    public static final String APP_CODE = "IDS";
    public static final String FORMAT_POST_TIME = "MM/dd/yyyy hh:mm:ss";
    public static final String WS_DOMAIN = "http://idsn2015.imark.com.vn:8001/WS/api";

    public static final String PATH_GET_OUTLET = WS_DOMAIN + "/GetAllOutlet";
    public static final String PATH_GET_ASSIGN_OUTLET = WS_DOMAIN + "/LoadAssignOutlet";
    public static final String PATH_GET_CODE_DETAIL = WS_DOMAIN + "/CodeDetail";
    public static final String PATH_GET_PRODUCT = WS_DOMAIN + "/AllProduct";
    public static final String PATH_GET_AREA = WS_DOMAIN + "/AllArea";
    public static final String PATH_GET_ATTENDANCE_TIME = WS_DOMAIN + "/GetAttendanceTime";
    public static final String PATH_GET_ATTENDANCE_ASSIGN_TIME = WS_DOMAIN + "/GetAttendanceTimeForAssign";

    public static final String PATH_POST_ATTENDANCE = WS_DOMAIN + "/AttendanceTracking";
    public static final String PATH_POST_ASSIGN_ATTENDANCE = WS_DOMAIN + "/AttendanceTrackingForOutlet";
    public static final String PATH_POST_ATTENDANCE_IMAGE = WS_DOMAIN + "/AddImageForAttenadence";
    public static final String PATH_POST_ASSIGN_ATTENDANCE_IMAGE = WS_DOMAIN + "/AddImageAttendanceForAssign";
    public static final String PATH_POST_LOGIN = WS_DOMAIN + "/Login";
    public static final String PATH_POST_CHANGE_PASSWORD = WS_DOMAIN + "/ChangePassWord";
    public static final String PATH_POST_ADD_ASSIGN_IMAGE = WS_DOMAIN + "/AddImageForAssign";
    public static final String PATH_POST_ADD_ASSIGN_OUTLET = WS_DOMAIN + "/AddOutlet";
    public static final String PATH_POST_ADD_PRODUCT = WS_DOMAIN + "/AddProduct";
    public static final String PATH_POST_ADD_PRODUCT_SHOWN = WS_DOMAIN + "/AddProductShown";
    public static final String PATH_POST_ADD_PRODUCT_SHOWN_IMG = WS_DOMAIN + "/AddImageForProductShow";
    public static final String PATH_POST_ADD_COMMENT = WS_DOMAIN + "/AddComment";
    public static final String PATH_POST_ASSIGN_COMPLETE = WS_DOMAIN + "/Complete";


    public static final String PATH_POST_IMAGE = "http://idsn2015.imark.com.vn:8001/WS/UploadImage/Uploadfile";
}
