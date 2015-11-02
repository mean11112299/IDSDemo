package com.imark.nghia.idsdemo.ui.others;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.imark.nghia.idscore.tasks.InitFirstDataWSTask;
import com.imark.nghia.idsdemo.ui.user.UserInfoActivity;
import com.windyroad.nghia.common.models.ActionResult;

/**
 * Activity Khởi tạo Dữ liệu ban đầu
 */
public class InitFirstDataActivity extends AppCompatActivity {

    InitFirstDataWSTask mInitTask;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_first_data);

        initVars();

    }

    private void initVars() {
        mProgressDialog = new ProgressDialog(this);

        if (getPreferences(MODE_PRIVATE).getBoolean(Constants.PREF_KEY_FIRST_INIT, true)){

            AppAlertDialog.showGetDataDialog(this, mProgressDialog);

            // Ứng dụng chạy lần đầu tiên
            mInitTask = new InitFirstDataWSTask(this,
                    UserPref.getUserPrefId(this),
                    new ITaskResultListener() {
                @Override
                public void onPostExecuteResult(ActionResult result) {

                    mInitTask = null;
                    mProgressDialog.cancel();

                    if (result.getResult() == ActionResult.ResultStatus.SUCCESS){
                        // thành công => Không chạy nữa, khởi động Activity khác
                        getPreferences(MODE_PRIVATE).edit().putBoolean(Constants.PREF_KEY_FIRST_INIT, false).commit();
                        startUserInfoActivity();
                    } else {
                        Toast.makeText(getBaseContext(),
                                getString(R.string.message_error_server_plus) + result.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            mInitTask.execute((Void) null);

        } else {
            // đã chạy lần đầu => không chạy nữa
            startUserInfoActivity();
        }
    }

    private void startUserInfoActivity(){
        startActivity(new Intent(this, UserInfoActivity.class));
        finish();
    }
}
