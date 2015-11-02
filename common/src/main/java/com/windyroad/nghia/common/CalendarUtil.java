package com.windyroad.nghia.common;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/3/2015.
 */
public class CalendarUtil {

    public static final String DATE_TIME_FORMAT_COMMON = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_SYNC_CODE = "yyMMddHHmmssSSS";

    /** lấy ngày giờ hiện tại **/
    public static Calendar getNow(){
        return Calendar.getInstance();
    }

    /**
     * Đặt lại giá trị dựa trên Calendar cũ
     * @param calendar
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static Calendar setValue(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar;
    }

    /**
     * Đặt lại giá trị dựa trên Calendar cũ
     */
    public static Calendar setValue(Calendar calendar, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }
}
