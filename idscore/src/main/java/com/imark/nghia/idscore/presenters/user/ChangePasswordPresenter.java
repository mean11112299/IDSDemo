package com.imark.nghia.idscore.presenters.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.imark.nghia.idscore.R;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.models.ChangePassword;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.tasks.ChangePasswordWSTask;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;

/**
 * Created by Imark-N on 10/26/2015.
 */
public class ChangePasswordPresenter implements IChangePasswordPresenter {

    ChangePasswordWSTask mChangePassTask;

    private IChangePasswordView mView;

    public ChangePasswordPresenter(IChangePasswordView mView) {
        this.mView = mView;

        viewInit();
    }

    private void viewInit() {
        UserInfo userInfo = UserPref.getUserPref(mView.getContext());
        mView.setUserInfo(userInfo);
    }


    @Override
    public void changePasswordHandle(ChangePassword model) {
        if (validateModel(model)){
            changePassword(model);
        }
    }

    private boolean validateModel(ChangePassword model) {
        mView.clearError();

        // đang chạy
        if (mChangePassTask != null) {
            // Task Đang sử dụng => hủy
            mView.setError_running();
            return false;
        }

        // không có mạng
        if (NetworkUtil.getConnectivityStatus(mView.getContext()) == ConnectionType.NOT_CONNECTED){
            // không có mạng => hủy lệnh
            //AppAlertDialog.showNetworkSettingAlert(this);
            mView.setError_network_requite();
            return false;
        }

        // Old password
        if (TextUtils.isEmpty(model.getOldPassword()+"")){
            mView.setError_oldPass_empty();
            return false;
        }

        // new password
        if (TextUtils.isEmpty(model.getNewPassword()+"")){
            mView.setError_newPass_empty();
            return false;
        }

        // confirm password
        if (!ValidateUtil.isTextEquals(model.getNewPassword()+"", model.getConfirmNewPassword()+"")){
            mView.setError_newPassword_notEqual();
            return false;
        }

        return true;
    }


    /**
     * Đổi mật khẩu
     */
    private void changePassword(ChangePassword model){
        mView.showWaiting();

        mChangePassTask = new ChangePasswordWSTask(mView.getContext(),
                UserPref.getUserPrefId(mView.getContext()),
                model.getOldPassword(),
                model.getNewPassword(),
                new ITaskResultListener() {
                    @Override
                    public void onPostExecuteResult(ActionResult actionResult) {

                        mChangePassTask = null;
                        mView.hideWaiting();

                        if (actionResult.getResult() == ActionResult.ResultStatus.SUCCESS){
                            // Thành công
                            mView.setSuccess();
                            mView.close();

                        } else {
                            // Thất bại
                            mView.setError_server(actionResult.getMessage());
                        }
                    }
                });
        mChangePassTask.execute();
    }

}
