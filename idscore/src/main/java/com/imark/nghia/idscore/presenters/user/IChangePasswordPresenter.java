package com.imark.nghia.idscore.presenters.user;

import com.imark.nghia.idscore.models.ChangePassword;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.presenters.BasePresenter;
import com.imark.nghia.idscore.presenters.BaseView;

/**
 * Created by Imark-N on 10/26/2015.
 */
public interface IChangePasswordPresenter extends BasePresenter {

    /**
     * Đổi mật khẩu
     * @param model
     */
    void changePasswordHandle(ChangePassword model);

    /**
     * Tương tác hiển thị
     */
    interface IChangePasswordView extends BaseView {

        /**
         * Hiển thị thông tin người dùng
         * @param userInfo
         */
        void setUserInfo(UserInfo userInfo);

        /**
         * Xóa mọi lỗi
         */
        void clearError();

        /**
         * Lỗi đang chạy
         */
        void setError_running();

        /**
         * Lỗi không có mạng
         */
        void setError_network_requite();

        /**
         * Lỗi mật khẩu cũ rỗng
         */
        void setError_oldPass_empty();

        /**
         * Lỗi mật khẩu mới rỗng
         */
        void setError_newPass_empty();

        /**
         * Lỗi mật khẩu không trùng
         */
        void setError_newPassword_notEqual();

        /**
         * Lỗi do Server trả về
         * @param error
         */
        void setError_server(String error);

        /**
         * Thành công
         */
        void setSuccess();

        /**
         * Đóng hiện tại
         */
        void close();
    }
}
