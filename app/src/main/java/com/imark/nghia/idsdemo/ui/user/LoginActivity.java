package com.imark.nghia.idsdemo.ui.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imark.nghia.idscore.models.UserLogin;
import com.imark.nghia.idscore.presenters.user.ILoginPresenter;
import com.imark.nghia.idscore.presenters.user.LoginPresenter;
import com.imark.nghia.idsdemo.ui.others.InitFirstDataActivity;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.tasks.UserLoginWSTask;

public class LoginActivity extends AppCompatActivity {

    private UserLoginWSTask mAuthTask = null;
    private ILoginPresenter mLoginPresenter;

    // UI references.
    private View mView_WaitingProgress;
    private View mView_LoginForm;
    private EditText mEditText_Username;
    private EditText mEditText_Password;
    private Button mButton_SignIn;
    private Button mButton_Exit;

    private ILoginPresenter.ILoginView mLoginView = new LoginPresenter.ILoginView() {

        @Override
        public void navigate_toUserInfo() {
            startActivity(new Intent(getBaseContext(), UserInfoActivity.class));
            finish();
        }

        @Override
        public void navigate_toInitData() {
            startActivity(new Intent(getBaseContext(), InitFirstDataActivity.class));
            finish();
        }

        @Override
        public void exit() {
            finish();
        }

        @Override
        public void clearError() {
            mEditText_Username.setError(null);
            mEditText_Password.setError(null);
        }

        @Override
        public void setError_network() {
            AppAlertDialog.showNetworkSettingAlert(getBaseContext());
        }

        @Override
        public void setError_userName_empty() {
            mEditText_Username.setError(getString(R.string.error_field_required));
        }

        @Override
        public void setError_password_empty() {
            mEditText_Password.setError(getString(R.string.error_field_required));
        }

        @Override
        public void setError_task_running() {
            Toast.makeText(getBaseContext(),
                    getString(R.string.message_error_server_plus),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void setError_server(String error) {
            Toast.makeText(getBaseContext(),
                    getString(R.string.message_error_server_plus) + error,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public Context getContext() {
            return getBaseContext();
        }

        @Override
        public void showWaiting() {
            updateUIWaiting(true);
        }

        @Override
        public void hideWaiting() {
            updateUIWaiting(false);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initVars();
        setEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLoginPresenter.onResume();
    }


    //region Khởi tạo
    private void findViews() {
        mView_WaitingProgress = findViewById(R.id.layout_Waiting);
        mView_LoginForm = findViewById(R.id.layout_LoginForm);

        mEditText_Username = (EditText) findViewById(R.id.editText_Username);
        mEditText_Password = (EditText) findViewById(R.id.editText_Password);

        mButton_SignIn = (Button) findViewById(R.id.button_SignIn);
        mButton_Exit = (Button) findViewById(R.id.button_Exit);
    }


    private void initVars() {
        mLoginPresenter = new LoginPresenter(mLoginView);
    }

    private void setEvents() {
        mButton_SignIn.setOnClickListener(mButton_SignIn_click);
        mButton_Exit.setOnClickListener(mButton_Exit_click);
    }
    //endregion


    //region Sự kiện
    View.OnClickListener mButton_SignIn_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginPresenter.loginHandle(getUserInfo_fromView());
        }
    };

    View.OnClickListener mButton_Exit_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginPresenter.exitHandle();
        }
    };

    //endregion


    //region Hỗ trợ

    /**
     * Hiện Chờ đợi, ẩn Form nhập liệu
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void updateUIWaiting(final boolean isWaiting) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mView_LoginForm.setVisibility(isWaiting ? View.GONE : View.VISIBLE);
            mView_LoginForm.animate().setDuration(shortAnimTime).alpha(
                    isWaiting ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView_LoginForm.setVisibility(isWaiting ? View.GONE : View.VISIBLE);
                }
            });

            mView_WaitingProgress.setVisibility(isWaiting ? View.VISIBLE : View.GONE);
            mView_WaitingProgress.animate().setDuration(shortAnimTime).alpha(
                    isWaiting ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView_WaitingProgress.setVisibility(isWaiting ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mView_WaitingProgress.setVisibility(isWaiting ? View.VISIBLE : View.GONE);
            mView_LoginForm.setVisibility(isWaiting ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Lấy UserInfo từ giao diện
     *
     * @return
     */
    private UserLogin getUserInfo_fromView() {
        return new UserLogin(
                mEditText_Username.getText().toString(),
                mEditText_Password.getText().toString()
        );
    }

    /**
     * Buộc dữ liệu vào View
     */
    private void bindingDataToView(UserLogin userLogin) {
        // // TODO: 10/24/2015 lớp mẫu
    }
    //endregion
}
