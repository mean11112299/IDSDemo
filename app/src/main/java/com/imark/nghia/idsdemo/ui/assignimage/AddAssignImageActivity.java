package com.imark.nghia.idsdemo.ui.assignimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.helper.AppConfig;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.AssignImage;
import com.imark.nghia.idscore.data.AssignImageData;
import com.imark.nghia.idscore.data.models.CodeDetail;
import com.imark.nghia.idscore.data.CodeDetailData;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.data.ImageData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.Global;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idsdemo.adapters.AssignImageTypeAdapter;
import com.windyroad.nghia.common.BitmapUtil;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.FileUtil;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.service.LocationService;

import java.util.ArrayList;
import java.util.Calendar;

public class AddAssignImageActivity extends AppCompatActivity {

    public static final String EXTRA_ASSIGN_ID = "assign_id";
    private Toolbar mToolbar;

    private long mAssignId;
    private ListView mListView_AssignImageType;
    private ArrayList<CodeDetail> mListAssignImageType;
    private AssignImageTypeAdapter mAdapterAssignImageType;
    private Uri mImageUri;  // File Uri
    private int mPositionImageType;  // Vị trí chọn trong list
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assign_image);

        initToolbar();
        findViews();
        initVars();
        initEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_assign_image, menu);
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
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Lưu file URL sẽ null Sau khi Camera return
        outState.putParcelable("file_uri", mImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Lấy lại Uri
        mImageUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Camera trả về kết quả
        if (requestCode == Constants.REQUEST_CODE_CAPTURE_IMAGE) {
            switch (resultCode){
                case Activity.RESULT_OK:
                    // resize Image, lưu chỗ cũ
                    Bitmap resultImage = BitmapFactory.decodeFile(mImageUri.getPath());
                    BitmapUtil.resizeAndSave(resultImage, AppConfig.APP_IMAGE_MAX_SIZE, mImageUri.getPath());

                    saveAssignImageHandle();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, getString(R.string.message_cancel), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
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
        }
    }

    private void findViews() {
        mListView_AssignImageType = (ListView) findViewById(R.id.listView_AssignImageType);
    }

    private void initVars() {
        mProgressDialog = new ProgressDialog(this);
        mAssignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, -1);

        // Khởi tạo List
        mListAssignImageType = CodeDetailData
                .getByGroupCode(this, Constants.GROUP_CODE_IMAGE_TYPE);
        mAdapterAssignImageType = new AssignImageTypeAdapter(
                this,
                R.layout.list_item_assign_image_type,
                mListAssignImageType,
                mAssignId);
        mListView_AssignImageType.setAdapter(mAdapterAssignImageType);
    }

    private void initEvent() {
        mListView_AssignImageType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCameraHandle(position);
            }
        });
    }
    //endregion


    //region Hàm sự kiện
    private void openCameraHandle(int position) {
        mPositionImageType = position;
        mImageUri = Global.startCameraForResult(this, Constants.REQUEST_CODE_CAPTURE_IMAGE);
    }

    private void saveAssignImageHandle(){
        AppAlertDialog.showWaitingDialog(this, mProgressDialog);

        if (validateViews()){

            long id = saveAssignImage();
            if (id > -1){
                // thành công, Đổi Assign => Working

                Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                mAdapterAssignImageType.notifyDataSetChanged();

                AssignData.changeStatus(this, mAssignId, Assign.WorkStatus.DOING);

            } else {
                // thất bại
                Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
            }
        }

        mProgressDialog.cancel();
    }

    /** Kiểm tra hợp lệ */
    private boolean validateViews() {

        // Image Type
        if (mPositionImageType <= -1){
            Toast.makeText(this, getString(R.string.error_image_type_unselected), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    //endregion


    //region Hàm hỗ trợ
    /** Lưu Image & Assign Image*/
    private long saveAssignImage(){

        // lấy biến
        String filePath = mImageUri.getPath();
        String latitude = new LocationService(this).getLatitude() +"";
        String longitude = new LocationService(this).getLongitude()+"";
        String imageType = mListAssignImageType.get(mPositionImageType).getValue();

        //-----save image------
        // gán biến
        Image image = new Image();

        image.setFileName(FileUtil.getFileName(filePath));
        image.setFilePath(filePath);
        image.setLatitude(latitude);
        image.setLongitude(longitude);

        image.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        image.setCreateBy(UserPref.getUserPrefId(this));
        image.setCreateAt(Calendar.getInstance());
        image.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        long imageId = ImageData.add(this, image);

        if (imageId > -1){
            // thành công

            //-----save assign image------
            AssignImage assignImage = new AssignImage();

            assignImage.setAssignId(mAssignId);
            assignImage.setImageId(imageId);
            assignImage.setImageType(imageType);

            assignImage.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
            assignImage.setCreateBy(UserPref.getUserPrefId(this));
            assignImage.setCreateAt(Calendar.getInstance());
            assignImage.setUploadStatus(UploadStatus.WAITING_UPLOAD);

            return AssignImageData.add(this, assignImage);
        }
        return -1;
    }
    //endregion
}
