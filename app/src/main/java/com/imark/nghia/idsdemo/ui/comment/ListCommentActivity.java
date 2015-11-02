package com.imark.nghia.idsdemo.ui.comment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.CommentData;
import com.imark.nghia.idscore.data.models.Comment;
import com.imark.nghia.idsdemo.adapters.CommentAdapter;

import java.util.ArrayList;

public class ListCommentActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT_COMMENT = 200;
    public static String EXTRA_ASSIGN_ID = "assign_id";

    private ArrayList<Comment> mListComment;
    private CommentAdapter mAdapterComment;
    private long mAssignId;

    private Toolbar mToolbar;
    private ListView mListView_Comment;
    private View mView_NoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comment);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startEditCommentActivity(null);
            return true;
        } else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_COMMENT){
            switch (resultCode){
                case RESULT_OK:
                    //refresh list
                    listView_Comment_init();
                    break;
                default:
                    break;
            }
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_32dp);
        }
    }

    private void findViews() {
        mListView_Comment = (ListView) findViewById(R.id.listView_Comment);
        mView_NoData = findViewById(R.id.view_NoData);
    }

    private void initVars() {
        mAssignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, -1);

        listView_Comment_init();
    }

    private void setEvents() {
        mListView_Comment.setOnItemClickListener(listView_Comment_itemClick);
    }
    //endregion


    //region Sự kiện
    private void listView_Comment_init() {
        mListComment = CommentData.getByAssignId(this, mAssignId);
        mAdapterComment = new CommentAdapter(this, R.layout.list_item_comment, mListComment);
        mListView_Comment.setAdapter(mAdapterComment);

        if (mListComment == null || mListComment.size() <= 0){
            updateUINoData(true);
        } else {
            updateUINoData(false);
        }
    }

    private AdapterView.OnItemClickListener listView_Comment_itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
            Comment comment = mListComment.get(postion);
            if (comment.getUploadStatus() == UploadStatus.WAITING_UPLOAD) {
                // Chỉ cho chỉnh sửa Waiting Upload
                startEditCommentActivity(comment.get_id());
            } else {
                Toast.makeText(
                        getBaseContext(),
                        getString(R.string.message_not_allow_edit_because_uploaded),
                        Toast.LENGTH_LONG).show();
            }
        }
    };
    //endregion


    //region Hỗ trợ
    /** Qua trang add/update comment */
    private void startEditCommentActivity(@Nullable Long commentId) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.EXTRA_ASSIGN_ID, mAssignId);
        intent.putExtra(EditCommentActivity.EXTRA_COMMENT_ID, commentId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_COMMENT);
    }

    /**
     * Hiển thị không dữ liệu
     * @param noData
     */
    private void updateUINoData(boolean noData) {
        mView_NoData.setVisibility(View.VISIBLE);
        if (!noData){
            //có dữ liệu
            mView_NoData.setVisibility(View.GONE);
        }
    }

    //endregion
}
