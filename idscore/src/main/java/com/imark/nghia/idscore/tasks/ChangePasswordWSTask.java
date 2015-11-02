package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.AccountWS;
import com.windyroad.nghia.common.models.ActionResult;

/**
 * Created by Nghia-PC on 8/24/2015.
 * Task vụ Đổi mật khẩu
 */
public class ChangePasswordWSTask extends AsyncTask<Void, Void, ActionResult> {

    private final Context mContext;
    private final long mUserId;
    private final String mOldPassword;
    private final String mNewPassword;
    private final ITaskResultListener mResultListener;

    public ChangePasswordWSTask(Context context, long username, String oldPassword, String newPassword,
                                ITaskResultListener resultListener){

        this.mContext = context;
        this.mUserId = username;
        this.mOldPassword = oldPassword;
        this.mNewPassword = newPassword;
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

            BaseWSResult wsResult = AccountWS.changePassword(mUserId, mOldPassword, mNewPassword);
            if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
                // Đổi mật khẩu thành công
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
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ActionResult actionResult) {
        super.onPostExecute(actionResult);

        mResultListener.onPostExecuteResult(actionResult);
    }
}
