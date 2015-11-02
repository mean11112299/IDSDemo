package com.imark.nghia.idsdemo.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imark.nghia.idscore.models.ChangePassword;
import com.imark.nghia.idscore.models.UserInfo;
import com.imark.nghia.idscore.presenters.user.ChangePasswordPresenter;
import com.imark.nghia.idscore.presenters.user.IChangePasswordPresenter;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.tasks.ChangePasswordWSTask;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.models.ActionResult;

public class ChangePasswordActivity extends AppCompatActivity {

    private ChangePasswordWSTask mChangePassTask;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private IChangePasswordPresenter mChangePasswordPresenter;

    private TextView mTextView_DisplayName;
    private EditText mEditText_OldPassword;
    private EditText mEditText_NewPassword;
    private EditText mEditText_ConfirmPassword;

    private IChangePasswordPresenter.IChangePasswordView mChangePasswordView = new IChangePasswordPresenter.IChangePasswordView() {
        @Override
        public void setUserInfo(UserInfo userInfo) {
            mTextView_DisplayName.setText(userInfo.getDisplayName());
        }

        @Override
        public void clearError() {
            mEditText_OldPassword.setError(null);
            mEditText_NewPassword.setError(null);
            mEditText_ConfirmPassword.setError(null);
        }

        @Override
        public void setError_running() {
            Toast.makeText(getBaseContext(), R.string.message_error_task_running, Toast.LENGTH_LONG).show();
        }

        @Override
        public void setError_network_requite() {
            AppAlertDialog.showNetworkSettingAlert(getBaseContext());
        }

        @Override
        public void setError_oldPass_empty() {
            mEditText_OldPassword.setError(getString(R.string.error_field_required));
            mEditText_OldPassword.requestFocus();
        }

        @Override
        public void setError_newPass_empty() {
            mEditText_NewPassword.setError(getString(R.string.error_field_required));
            mEditText_NewPassword.requestFocus();
        }

        @Override
        public void setError_newPassword_notEqual() {
            mEditText_ConfirmPassword.setError(getString(R.string.error_new_password_equals));
            mEditText_ConfirmPassword.requestFocus();
        }

        @Override
        public void setError_server(String error) {
            Toast.makeText(
                    getBaseContext(),
                    getString(R.string.message_error_unknown) + error,
                    Toast.LENGTH_LONG
            ).show();
        }

        @Override
        public void setSuccess() {
            Toast.makeText(getBaseContext(), getString(R.string.message_success), Toast.LENGTH_LONG).show();
        }

        @Override
        public void close() {
            finish();
        }

        @Override
        public Context getContext() {
            return ChangePasswordActivity.this;
        }

        @Override
        public void showWaiting() {
            AppAlertDialog.showWaitingDialog(getBaseContext(), mProgressDialog);
        }

        @Override
        public void hideWaiting() {
            mProgressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                changePasswordHandle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region Khởi tạo
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // hiển thị Home/ Up
        }
    }

    private void findViews() {
        mTextView_DisplayName = (TextView) findViewById(R.id.textView_DisplayName);
        mEditText_OldPassword = (EditText) findViewById(R.id.editText_OldPassword);
        mEditText_NewPassword = (EditText) findViewById(R.id.editText_NewPassword);
        mEditText_ConfirmPassword = (EditText) findViewById(R.id.editText_ConfirmPassword);
    }

    private void initVars() {
        mChangePasswordPresenter = new ChangePasswordPresenter(mChangePasswordView);
        mProgressDialog = new ProgressDialog(this);
    }

    private void setEvents() {

    }
    //endregion


    //region Sự kiện

    /**
     * Thử Đổi mật khẩu
     */
    private void changePasswordHandle() {
        mChangePasswordPresenter.changePasswordHandle(getChangePassword_fromView());
    }

    private ChangePassword getChangePassword_fromView() {
        return new ChangePassword(
                mEditText_OldPassword.getText().toString(),
                mEditText_NewPassword.getText().toString(),
                mEditText_ConfirmPassword.getText().toString()
        );
    }
    //endregion


    //region Hỗ trợ

    //endregion
}
