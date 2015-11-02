package com.imark.nghia.idscore.presenters.user;

import android.text.TextUtils;

import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.models.UserLogin;
import com.imark.nghia.idscore.tasks.UserLoginWSTask;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;

/**
 * Created by Imark-N on 10/23/2015.
 * Điều kiển login
 */
public class LoginPresenter implements ILoginPresenter {

    private final ILoginView mView;
    private UserLoginWSTask mAuthTask;

    public LoginPresenter(ILoginView loginView) {
        this.mView = loginView;
    }

    @Override
    public void onResume() {
        //logged: Bỏ qua Đăng nhập, khi có User
        if (UserPref.getUserPrefId(mView.getContext()) > -1) {
            mView.navigate_toUserInfo();
        }
    }

    @Override
    public void loginHandle(UserLogin userLogin) {
        if (loginValidate(userLogin)) {
            login(userLogin);
        }
    }

    @Override
    public void exitHandle() {
        mView.exit();
    }

    private boolean loginValidate(UserLogin userLogin) {
        mView.clearError();

        // kiểm tra mạng
        if (NetworkUtil.getConnectivityStatus(mView.getContext()) == ConnectionType.NOT_CONNECTED){
            mView.setError_network();
            return false;
        }

        // kiểm tra đang chạy
        if (mAuthTask != null) {
            mView.setError_task_running();
            return false;
        }

        // check username
        if (TextUtils.isEmpty(userLogin.getUsername())) {
            mView.setError_userName_empty();
            return false;
        }

        // check password
        if (TextUtils.isEmpty(userLogin.getPassword())) {
            mView.setError_password_empty();
            return false;
        }

        return true;
    }

    private void login(UserLogin userLogin) {
        mView.showWaiting();

        mAuthTask = new UserLoginWSTask(mView.getContext(), userLogin.getUsername(), userLogin.getPassword(),
                new UserLoginWSTask.IResultListener() {
                    @Override
                    public void onPostExecute(ActionResult actionResult, UserInfo userInfo) {
                        mAuthTask = null;
                        mView.hideWaiting();

                        if (actionResult.getResult() == ActionResult.ResultStatus.SUCCESS) {

                            // Thành công => MainActivity
                            UserPref.saveUserPref(mView.getContext(), userInfo);
                            mView.navigate_toInitData();

                        } else {
                            // Thất bại => báo lỗi
                            mView.setError_server(actionResult.getMessage());
                        }
                    }
                });

        mAuthTask.execute((Void) null);
    }

}
