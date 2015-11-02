package com.imark.nghia.idscore.presenters.user;

import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.imark.nghia.idscore.tasks.InitFirstDataWSTask;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;

/**
 * Created by Imark-N on 10/26/2015.
 */
public class UserInfoPresenter implements IUserInfoPresenter {

    IUserInfoView mView;

    public UserInfoPresenter(IUserInfoView view) {
        this.mView = view;

        viewInit();
    }

    private void viewInit() {
        // Hiện thông tin người dùng
        UserInfo userInfo = UserPref.getUserPref(mView.getContext());
        mView.setUserInfo(userInfo);
    }

    @Override
    public void logoutHandle() {
        // xóa thông tin
        UserPref.clear(mView.getContext());

        // về trang login
        mView.navigateToLogin();
    }

    @Override
    public void navigateListAssignHandle() {
        mView.navigateToListAssign();
    }

    @Override
    public void navigateAttendanceHandle() {
        mView.navigateToAttendance();
    }

    @Override
    public void navigateChangePasswordHandle() {
        mView.navigateToChangePassword();
    }

    @Override
    public void resetAllDataHandle() {
        if (NetworkUtil.getConnectivityStatus(mView.getContext()) == ConnectionType.NOT_CONNECTED){
            // không có mạng => hủy lệnh
            mView.setError_netWork();
            return;
        }

        mView.showWaiting();

        // chạy task Reset
        final InitFirstDataWSTask initFirstDataTask = new InitFirstDataWSTask(
                mView.getContext(), UserPref.getUserPrefId(mView.getContext()),
                new ITaskResultListener() {
                    @Override
                    public void onPostExecuteResult(ActionResult result) {
                        mView.hideWaiting();

                        if (result.getResult() == ActionResult.ResultStatus.SUCCESS) {
                            mView.setSuccess_resetData();
                        } else {
                            mView.setError_server(result.getMessage());
                        }
                    }
                });
        initFirstDataTask.execute();
    }
}
