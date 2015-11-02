package com.imark.nghia.idsdemo.ui.assign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.helper.AppConfig;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.imark.nghia.idscore.tasks.PostAssignWSTask;
import com.imark.nghia.idsdemo.ui.assignimage.AddAssignImageActivity;
import com.imark.nghia.idsdemo.ui.comment.EditCommentActivity;
import com.imark.nghia.idsdemo.ui.comment.ListCommentActivity;
import com.imark.nghia.idsdemo.ui.productshown.EditProductShownActivity;
import com.imark.nghia.idsdemo.ui.attendance.AttendanceActivity;
import com.windyroad.nghia.common.ConvertUtil;
import com.imark.nghia.idsdemo.ui.productshown.ListProductShownActivity;

public class ViewAssignActivity extends AppCompatActivity {

    public static final String EXTRA_ASSIGN_ID = "assign_id";
    Assign mAssign;
    Toolbar mToolbar;
    ProgressDialog mDialogProgress;
    private PostAssignWSTask mTaskPostAssign;

    ViewGroup mViewGroup_AssignFunction;
    TextView mTextView_ExpiredDate;
    TextView mTextView_OutletName;
    TextView mTextView_OutletAddress;
    TextView mTextView_OutletArea;
    TextView mTextView_Coordinates;
    Button mButton_StartAssignImage;
    Button mButton_StartAttendance;
    Button mButton_StartListProductShown;
    Button mButton_StartListComment;
    Button mButton_Upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assign);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assign_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Khởi tạo
    /**
     * Khởi tạo Toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // hiển thị Home/ Up
        }
    }

    private void findViews() {
        mViewGroup_AssignFunction = (ViewGroup) findViewById(R.id.viewGroup_AssignFunction);
        mTextView_ExpiredDate = (TextView) findViewById(R.id.textView_ExpiredDate);
        mTextView_OutletName = (TextView) findViewById(R.id.textView_OutletName);
        mTextView_OutletAddress = (TextView) findViewById(R.id.textView_OutletAddress);
        mTextView_OutletArea = (TextView) findViewById(R.id.textView_OutletArea);
        mTextView_Coordinates = (TextView) findViewById(R.id.textView_Coordinates);
        mButton_StartAssignImage = (Button) findViewById(R.id.button_StartAssign);
        mButton_StartAttendance = (Button) findViewById(R.id.button_StartAttendance);
        mButton_StartListProductShown = (Button) findViewById(R.id.button_StartAddProductShown);
        mButton_StartListComment = (Button) findViewById(R.id.button_StartAddComment);
        mButton_Upload = (Button) findViewById(R.id.button_Upload_Finish);
    }

    private void initVars() {
        mDialogProgress = new ProgressDialog(this);

        // Đưa thông tin Assign lên Views
        mAssign = getAssignFromIntent();
        if (mAssign != null) {
            String strExpiredDate = ConvertUtil
                    .Calendar2String(mAssign.getExpiredAt(), AppConfig.APP_DATE_FORMAT, null);
            String strCoordinates = mAssign.getOutletLatitude() + ", " + mAssign.getOutletLongitude();

            mTextView_ExpiredDate.setText(strExpiredDate);
            mTextView_OutletName.setText(mAssign.getOutletName());
            mTextView_OutletAddress.setText(mAssign.getOutletAddress());
            mTextView_OutletArea.setText(mAssign.getOutletCity());
            mTextView_Coordinates.setText(strCoordinates);

            getSupportActionBar().setTitle(mAssign.getOutletName());  // đặt lại Title
        } else {
            // không có Assign => ẩn chức năng
            mViewGroup_AssignFunction.setVisibility(View.GONE);
        }
    }

    private void setEvents() {
        mButton_StartAssignImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAssignImageActivity();
            }
        });
        mButton_StartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAttendanceActivity();
            }
        });
        mButton_StartListComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListCommentActivity();
            }
        });
        mButton_StartListProductShown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListProductShownActivity();
            }
        });
        mButton_Upload.setOnClickListener(buttonUpload_click);

        mButton_StartListComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startAddCommentActivity();
                return true;
            }
        });
        mButton_StartListProductShown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startAddProductShownActivity();
                return true;
            }
        });
    }
    //endregion

    //region Sự kiện
    private void startAssignImageActivity(){
        Intent intent = new Intent(this, AddAssignImageActivity.class);
        intent.putExtra(AddAssignImageActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private void startAttendanceActivity() {
        Intent intent = new Intent(this, AttendanceActivity.class);
        intent.putExtra(AttendanceActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private void startListCommentActivity() {
        Intent intent = new Intent(this, ListCommentActivity.class);
        intent.putExtra(ListCommentActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private void startAddCommentActivity() {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private void startListProductShownActivity() {
        Intent intent = new Intent(this, ListProductShownActivity.class);
        intent.putExtra(ListProductShownActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private void startAddProductShownActivity() {
        Intent intent = new Intent(this, EditProductShownActivity.class);
        intent.putExtra(EditProductShownActivity.EXTRA_ASSIGN_ID, mAssign.get_id());
        startActivity(intent);
    }

    private View.OnClickListener buttonUpload_click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (validateUpload()) {

                AppAlertDialog.showPostDataDialog(getBaseContext(), mDialogProgress);

                mTaskPostAssign = new PostAssignWSTask(
                        getBaseContext(), mAssign.get_id(),
                        UserPref.getUserPrefId(getBaseContext()), postAssignListener);
                mTaskPostAssign.execute();
            }
        }
    };

    /** lắng nghe Post Assign trả về */
    private ITaskResultListener postAssignListener = new ITaskResultListener() {
        @Override
        public void onPostExecuteResult(ActionResult result) {

            mDialogProgress.dismiss();
            mTaskPostAssign = null;

            if (result.getResult() == ActionResult.ResultStatus.SUCCESS){
                Toast.makeText(getBaseContext(), getString(R.string.message_success), Toast.LENGTH_LONG).show();
            } else {
                String errMsg = getString(R.string.message_error_server_plus) + result.getMessage();
                Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_LONG).show();
            }
        }
    };
    //endregion

    //region Hỗ trợ

    /**
     * Kiểm tra cho phép Upload
     * @return
     */
    private boolean validateUpload() {
        if (mTaskPostAssign != null){
            return false;
        }

        // kiểm tra mạng
        if (NetworkUtil.getConnectivityStatus(this) == ConnectionType.NOT_CONNECTED){
            AppAlertDialog.showNetworkSettingAlert(this);
            return false;
        }

        return true;
    }


    /**
     * Lấy Assign từ Intent
     * @return assign
     */
    private Assign getAssignFromIntent(){
        // Lấy Id từ Intend
        // Tìm Id trong Database
        Assign resultAssign = null;

        Intent intent = getIntent();
        if (intent != null){
            long assignId = intent.getLongExtra(EXTRA_ASSIGN_ID, -1);
            resultAssign = AssignData.getById(this, assignId);
        }

        return resultAssign;
    }
    //endregion
}
