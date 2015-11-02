package com.imark.nghia.idscore.presenters.user;

import android.content.Context;

import com.imark.nghia.idscore.models.UserLogin;
import com.imark.nghia.idscore.presenters.BasePresenter;
import com.imark.nghia.idscore.presenters.BaseView;

/**
 * Created by Imark-N on 10/24/2015.
 */
public interface ILoginPresenter extends BasePresenter {

    /**
     * Resume = Kiểm tra bỏ qua login
     */
    void onResume();

    /**
     * Tiến hành login
     */
    void loginHandle(UserLogin userLogin);

    /**
     * Kết thúc ứng dụng
     */
    void exitHandle();

    /**
     * View dùng cho Presenter
     */
    interface ILoginView extends BaseView {

        /**
         * Xóa toàn bộ lỗi
         */
        void clearError();

        /**
         * Đặt Username lỗi rỗng
         */
        void setError_userName_empty();

        /**
         * Đặt Password lỗi rỗng
         */
        void setError_password_empty();

        /**
         * Lỗi đang chạy
         */
        void setError_task_running();

        /**
         * Lỗi kết nối mạng
         */
        void setError_network();

        /**
         * Lỗi do server trả về
         */
        void setError_server(String error);

        /**
         * Chuyển đến UserInfo
         */
        void navigate_toUserInfo();

        /**
         * Chuyển đến khởi tạo dữ liệu lần đầu
         */
        void navigate_toInitData();

        /**
         * Thoát, kết thúc
         */
        void exit();
    }
}
