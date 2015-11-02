package com.imark.nghia.idscore.presenters.attendance;

import android.app.Activity;
import android.location.Location;
import android.widget.ActionMenuView;

import com.imark.nghia.idscore.models.AddAttendance;
import com.imark.nghia.idscore.models.AddAttendanceImg;
import com.imark.nghia.idscore.network.webservices.models.GetAttendanceTimeWSResult;
import com.imark.nghia.idscore.presenters.BasePresenter;
import com.imark.nghia.idscore.presenters.BaseView;
import com.windyroad.nghia.common.activity.IActivityResultListener;

/**
 * Created by Imark-N on 10/26/2015.
 */
public interface IAttendancePresenter extends BasePresenter {

    /**
     * Lấy vị trí
     */
    void getCoordinatesHandle(boolean showError);

    /**
     * Mở map ngoài
     */
    void openOtherMap(String latitude, String longitude);

    /**
     * Lưu chấm công
     */
    void saveAttendanceHandle(AddAttendance model);

    /**
     * Mở chụp ảnh
     */
    void saveAttendanceImgHandle(Activity activity, int requestCode, AddAttendanceImg model);


    interface IAttendanceView extends BaseView{

        /**
         * Đặt giao diện Chấm công theo User
         */
        void setUI_userAttendance();

        /**
         * Đặt giao diện chấm công theo Assign
         */
        void setUI_assignAttendance(String outletName);

        /**
         * cập nhật giao diện chụp hình
         */
        void setUI_captureImage(boolean allowCapture);

        /**
         * Camera Result
         * @param listener
         */
        void setCameraResultListener(IActivityResultListener listener);

        /**
         * Đặt thông tin Server
         */
        void setServerInfo(GetAttendanceTimeWSResult info);

        /**
         * Đặt lỗi do Server
         */
        void setError_server(String error);

        /**
         * Đặt lỗi lấy vị trí
         */
        void setError_location();

        /**
         * Lỗi kết nối mạng
         */
        void setError_network();

        /**
         * Lỗi tất cả phải không rỗng
         */
        void setError_allNotNull();

        /**
         * Lỗi không xác định
         */
        void setError_unknown();

        /**
         * Đặt location
         * @param location
         */
        void setLocation(Location location);

        /**
         * Post ảnh thành công
         */
        void setSuccess_postAttendanceImg();
    }
}
