package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.LoginWSResult;
import com.imark.nghia.idscore.network.webservices.AccountWS;
import com.imark.nghia.idscore.models.UserInfo;
import com.windyroad.nghia.common.models.ActionResult;

/**
 * Created by Nghia-PC on 8/26/2015.
 * Task Đồng bộ User
 */
public class UserLoginWSTask extends AsyncTask<Void, Void, ActionResult> {


    private final String mUsername;
    private final String mPassword;
    private final Context mContext;

    private final IResultListener mResultListener;
    private UserInfo mUserInfo;

    public UserLoginWSTask(Context context, String username, String password, IResultListener resultListener) {
        this.mContext = context;
        this.mUsername = username;
        this.mPassword = password;
        this.mResultListener = resultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ActionResult doInBackground(Void... params) {
        ActionResult resultValue;
        try {

            LoginWSResult wsResult = AccountWS.login(mUsername, mPassword);
            if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
                // Đăng nhập thành công => save userid
                mUserInfo = new UserInfo();
                mUserInfo.setUserID(Long.valueOf(wsResult.getUserInfo().getUserTeam_ID()));
                mUserInfo.setDisplayName(wsResult.getUserInfo().getDisplayName());

                resultValue = new ActionResult(ActionResult.ResultStatus.SUCCESS, wsResult.getDescription());

            } else {
                // Thất bại
                resultValue = new ActionResult(ActionResult.ResultStatus.FAIL, wsResult.getDescription());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            resultValue = new ActionResult(ActionResult.ResultStatus.FAIL, ex.getMessage());
        }
        return resultValue;
    }

    @Override
    protected void onPostExecute(final ActionResult actionResult) {
        mResultListener.onPostExecute(actionResult, mUserInfo);
    }

    @Override
    protected void onCancelled() {
        /*mAuthTask = null;
        updateUI(false);*/
    }

    /**
     * Kết quả trả về
     */
    public interface IResultListener {
        void onPostExecute(ActionResult actionResult, UserInfo userInfo);
    }
}
