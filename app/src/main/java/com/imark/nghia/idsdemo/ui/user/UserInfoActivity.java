package com.imark.nghia.idsdemo.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imark.nghia.idscore.data.models.Attendance;
import com.imark.nghia.idscore.models.UserLogin;
import com.imark.nghia.idscore.presenters.user.IUserInfoPresenter;
import com.imark.nghia.idscore.presenters.user.UserInfoPresenter;
import com.imark.nghia.idsdemo.ui.attendance.AttendanceActivity;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.imark.nghia.idscore.tasks.InitFirstDataWSTask;
import com.imark.nghia.idsdemo.ui.assign.ListAssignActivity;
import com.windyroad.nghia.common.fragment.YesNoDialogFragment;
import com.windyroad.nghia.common.models.ActionResult;

public class UserInfoActivity extends AppCompatActivity {

    private IUserInfoPresenter mUserInfoPresenter;

    private ProgressDialog mDialog;
    private ImageView mImageView_UserImage;
    private TextView mTextView_UserDisplayName;
    private TextView mTextView_TotalAssign;
    private TextView mTextView_FinishAssign;
    private TextView mTextView_Version;
    private ProgressBar mProgressBar_Assign;
    private Button mButton_ChangePassword;
    private Button mButton_Logout;
    private Button mButton_StartListAssign;
    private Button mButton_StartAttendance;
    private Button mButton_ResetAllData;
    private Toolbar mToolbar;

    private IUserInfoPresenter.IUserInfoView mView = new IUserInfoPresenter.IUserInfoView() {

        @Override
        public Context getContext() {
            return UserInfoActivity.this;
        }

        @Override
        public void showWaiting() {
            AppAlertDialog.showWaitingDialog(getBaseContext(), mDialog);
        }

        @Override
        public void hideWaiting() {
            mDialog.dismiss();
        }

        @Override
        public void setUserInfo(UserInfo userInfo) {
            mTextView_UserDisplayName.setText(userInfo.getDisplayName());
        }

        @Override
        public void navigateToLogin() {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
        }

        @Override
        public void navigateToListAssign() {
            startActivity(new Intent(getBaseContext(), ListAssignActivity.class));
        }

        @Override
        public void navigateToAttendance() {
            startActivity(new Intent(getBaseContext(), AttendanceActivity.class));
        }

        @Override
        public void navigateToChangePassword() {
            startActivity(new Intent(getBaseContext(), ChangePasswordActivity.class));
        }

        @Override
        public void setError_netWork() {
            AppAlertDialog.showNetworkSettingAlert(getBaseContext());
        }

        @Override
        public void setSuccess_resetData() {
            Toast.makeText(getBaseContext(), getString(R.string.message_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setError_server(String errorMessage) {
            String message = getString(R.string.message_error_server_plus) + errorMessage;
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Khởi tạo
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);  // có thể hiện icon
            actionBar.setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        }
    }

    private void findViews(){
        mImageView_UserImage = (ImageView) findViewById(R.id.imageView_UserImage);
        mTextView_UserDisplayName = (TextView) findViewById(R.id.textView_UserDisplayName);
        mTextView_TotalAssign = (TextView) findViewById(R.id.textView_TotalAssign);
        mTextView_FinishAssign = (TextView) findViewById(R.id.textView_FinishAssign);
        mTextView_Version = (TextView) findViewById(R.id.textView_Version);
        mProgressBar_Assign = (ProgressBar) findViewById(R.id.progressBar_Assign);
        mButton_ChangePassword = (Button) findViewById(R.id.button_ChangePassword);
        mButton_Logout = (Button) findViewById(R.id.button_Logout);
        mButton_StartListAssign = (Button) findViewById(R.id.button_StartListAssign);
        mButton_ResetAllData = (Button) findViewById(R.id.button_ResetAllData);
        mButton_StartAttendance = (Button) findViewById(R.id.button_StartAttendance);
    }

    private void initVars(){
        mDialog = new ProgressDialog(this);
        mUserInfoPresenter = new UserInfoPresenter(mView);
    }

    private void setEvents() {
        mButton_ChangePassword.setOnClickListener(mButton_ChangePassword_click);
        mButton_Logout.setOnClickListener(mButton_Logout_click);
        mButton_StartListAssign.setOnClickListener(mButton_StartListAssign_click);
        mButton_ResetAllData.setOnClickListener(mButton_ResetAllData_click);
        mButton_StartAttendance.setOnClickListener(mButton_StartAttendance_click);
    }

    //endregion
    View.OnClickListener mButton_Logout_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logoutHandle();
        }
    };
    View.OnClickListener mButton_ChangePassword_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.navigateChangePasswordHandle();
        }
    };
    View.OnClickListener mButton_ResetAllData_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetAllDataHandle();
        }
    };
    View.OnClickListener mButton_StartListAssign_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.navigateListAssignHandle();
        }
    };

    View.OnClickListener mButton_StartAttendance_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mUserInfoPresenter.navigateAttendanceHandle();
        }
    };
    //region Sự kiện


    /** Đăng xuất */
    private void logoutHandle() {
        // Tạo câu hỏi
        YesNoDialogFragment yesNoDialog = YesNoDialogFragment.newInstance(
                getString(R.string.title_dialog_question, ""),
                getString(R.string.message_dialog_logout, ""),
                getString(R.string.action_yes, ""),
                getString(R.string.action_no, "")
        );
        yesNoDialog.setListener(new YesNoDialogFragment.IDialogListener() {
            @Override
            public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
                mUserInfoPresenter.logoutHandle();
            }

            @Override
            public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {
                dialog.dismiss();
            }
        });
        yesNoDialog.show(getSupportFragmentManager(), "");
    }

    private void resetAllDataHandle(){
        // Thông báo
        YesNoDialogFragment dialog = YesNoDialogFragment.newInstance(
                getString(R.string.title_dialog_question),
                getString(R.string.message_dialog_reset_data),
                getString(R.string.action_yes),
                getString(R.string.action_no)
        );
        dialog.setListener(new YesNoDialogFragment.IDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                mUserInfoPresenter.resetAllDataHandle();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }
    //endregion


    //region Hỗ trợ

    //endregion
}
