package com.imark.nghia.idscore.presenters.user;

import android.content.Context;

import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.presenters.BasePresenter;
import com.imark.nghia.idscore.presenters.BaseView;

/**
 * Created by Imark-N on 10/26/2015.
 */
public interface IUserInfoPresenter extends BasePresenter{

    /**
     * Đăng xuất
     */
    void logoutHandle();

    /**
     * Đến list Assign
     */
    void navigateListAssignHandle();

    /**
     * Đến change password
     */
    void navigateAttendanceHandle();

    /**
     * Đến đổi mật khẩu
     */
    void navigateChangePasswordHandle();

    /**
     * Xóa mọi dữ liệu
     */
    void resetAllDataHandle();

    /**
     * Giao diện tương tác
     */
    interface IUserInfoView extends BaseView{

        /**
         * Đến trang login
         */
        void navigateToLogin();

        /**
         * Đến list Assign
         */
        void navigateToListAssign();

        /**
         * Đến chấm công
         */
        void navigateToAttendance();

        /**
         * Đến đổi mật khẩu
         */
        void navigateToChangePassword();

        /**
         * Hiển thị User Info
         * @param userInfo
         */
        void setUserInfo(UserInfo userInfo);

        /**
         * Lỗi do mạng
         */
        void setError_netWork();

        /**
         * Lỗi server
         * @param message
         */
        void setError_server(String message);

        /**
         * reset data thành công
         */
        void setSuccess_resetData();


    }
}
