package com.imark.nghia.idsdemo.ui.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.Comment;
import com.imark.nghia.idscore.data.CommentData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.service.LocationService;

import java.util.Calendar;

public class EditCommentActivity extends AppCompatActivity {

    private static final String EXTRA_RESULT_COMMENT_ID = "comment_id";
    public static String EXTRA_ASSIGN_ID = "assign_id";
    public static String EXTRA_COMMENT_ID = "comment_id";

    private ProgressDialog mDialog;
    private long mAssignId;
    private Comment mComment;
    private boolean isAddNew; // Add New vs Update

    private Toolbar mToolbar;
    TextInputLayout mInputLayout_Advantage;
    TextInputLayout mInputLayout_Disadvantage;
    TextInputLayout mInputLayout_Suggestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                saveCommentHandle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mInputLayout_Advantage = (TextInputLayout) findViewById(R.id.textInputLayout_Advantage);
        mInputLayout_Disadvantage = (TextInputLayout) findViewById(R.id.textInputLayout_Disadvantage);
        mInputLayout_Suggestion = (TextInputLayout) findViewById(R.id.textInputLayout_Suggestion);
    }

    private void initVars() {
        mDialog = new ProgressDialog(this);

        // --- Khởi tạo qua Intent ----
        mAssignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, -1);

        long commentId = getIntent().getLongExtra(EXTRA_COMMENT_ID, -1);
        if (commentId == -1){
            isAddNew = true;
        }
        else {
            // Cập nhật giao diện Update Comment
            updateUIAddOrEdit(false);
            isAddNew = false;

            mComment = CommentData.getById(this, commentId);
            mInputLayout_Advantage.getEditText().setText(mComment.getAdvantage());
            mInputLayout_Disadvantage.getEditText().setText(mComment.getDisadvantage());
            mInputLayout_Suggestion.getEditText().setText(mComment.getSuggestion());
        }

    }

    private void setEvents() {

    }
    //endregion


    //region Sự kiện

    private void saveCommentHandle() {
        AppAlertDialog.showWaitingDialog(this, mDialog);

        if (validateViews()){

            if (isAddNew) {
                // Thêm mới comment

                long id = addComment();

                // xuất kết quả
                if (id != -1) {
                    Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                    AssignData.changeStatus(this, mAssignId, Assign.WorkStatus.DOING);
                    finishWithResultOK(mComment.get_id());
                } else {
                    Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
                }

            } else {
                // update comment

                long row = updateComment();

                // xuất kết quả
                if (row > 0) {
                    Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                    finishWithResultOK(mComment.get_id());
                } else {
                    Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
                }
            }


        }

        mDialog.cancel();
    }

    //endregion


    //region Hỗ trợ
    private boolean validateViews() {
        View focusViews = null;

        // 1 trong 3 phải có giá trị
        String messageRequired = getString(R.string.error_field_required);
        if (!ValidateUtil.hasText(mInputLayout_Suggestion, messageRequired)
                && !ValidateUtil.hasText(mInputLayout_Disadvantage, messageRequired)
                && !ValidateUtil.hasText(mInputLayout_Advantage, messageRequired)){

            focusViews = mInputLayout_Suggestion;
        }

        // trả về giá trị
        if (focusViews != null){
            focusViews.requestFocus();
            return false;
        }
        return true;
    }
    private long addComment() {
        long assignId = mAssignId;
        String advantage = mInputLayout_Advantage.getEditText().getText()+"";
        String disadvantage = mInputLayout_Disadvantage.getEditText().getText()+"";
        String suggestion = mInputLayout_Suggestion.getEditText().getText()+"";
        String latitude = new LocationService(this).getLatitude()+"";
        String longitude = new LocationService(this).getLongitude()+"";

        mComment = new Comment();

        mComment.setAssignId(assignId);
        mComment.setAdvantage(advantage);
        mComment.setDisadvantage(disadvantage);
        mComment.setSuggestion(suggestion);
        mComment.setLatitude(latitude);
        mComment.setLongitude(longitude);

        mComment.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        mComment.setCreateBy(UserPref.getUserPrefId(this));
        mComment.setCreateAt(Calendar.getInstance());
        mComment.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        long id = CommentData.add(this, mComment);
        mComment.set_id(id);

        return id;
    }

    private long updateComment() {
        long assignId = mAssignId;
        String advantage = mInputLayout_Advantage.getEditText().getText()+"";
        String disadvantage = mInputLayout_Disadvantage.getEditText().getText()+"";
        String suggestion = mInputLayout_Suggestion.getEditText().getText()+"";
        String latitude = new LocationService(this).getLatitude()+"";
        String longitude = new LocationService(this).getLongitude()+"";

        mComment.setAssignId(assignId);
        mComment.setAdvantage(advantage);
        mComment.setDisadvantage(disadvantage);
        mComment.setSuggestion(suggestion);
        mComment.setLatitude(latitude);
        mComment.setLongitude(longitude);

        mComment.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        mComment.setCreateBy(UserPref.getUserPrefId(this));
        mComment.setCreateAt(Calendar.getInstance());
        mComment.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        return CommentData.updateAllById(this, mComment.get_id(), mComment);
    }

    private void updateUIAddOrEdit(boolean isAddNew) {

        String title = "";  // đổi title

        if (isAddNew){
            title = getString(R.string.title_activity_add_comment);
        } else {
            title = getString(R.string.title_activity_update_comment);
        }

        getSupportActionBar().setTitle(title);
    }

    /** Trả về với kết quả */
    private void finishWithResultOK(long commentId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_RESULT_COMMENT_ID, commentId);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
    //endregion

}
