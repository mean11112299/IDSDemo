package com.imark.nghia.idsdemo.ui.productshown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.windyroad.nghia.common.BitmapUtil;
import com.windyroad.nghia.common.FileUtil;
import com.windyroad.nghia.common.service.LocationService;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.ProductShownImgData;
import com.imark.nghia.idscore.helper.AppConfig;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idscore.helper.Global;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.data.models.Product;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idscore.data.models.ProductShown;
import com.imark.nghia.idscore.data.ProductShownData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.data.models.ProductShownImg;
import com.imark.nghia.idsdemo.ui.product.AddProductActivity;
import com.imark.nghia.idsdemo.adapters.ProductAdapter;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.network.UploadStatus;

import java.util.ArrayList;
import java.util.Calendar;

public class EditProductShownActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_SHOWN_ID = "extra_product_shown_id";
    public static String EXTRA_ASSIGN_ID = "assign_id";

    private static final int REQUEST_CODE_ADD_PRODUCT = 200;
    private static final String EXTRA_RESULT_PRODUCT_ID = "extra_product_id";

    ProgressDialog mDialog;
    ArrayList<Product> mListProduct;
    ProductAdapter mAdapterProduct;
    Product mProductAddNew;

    private Toolbar mToolbar;
    private Spinner mSpinner_Product;
    private TextInputLayout mInputLayout_Number;
    private TextInputLayout mInputLayout_RetailPrice;
    private long mAssignId;
    private ProductShown mProductShown;
    private boolean isAddNew; // AddNew vs Update
    private Uri mUriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_shown);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product_shown, menu);

        // Sửa mới cho chụp hình
        if (isAddNew){
            menu.findItem(R.id.action_capture).setVisible(false);
        } else {
            menu.findItem(R.id.action_capture).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                saveProductShownHandle();
                return true;
            case R.id.action_capture:
                mUriImage = Global.startCameraForResult(this, Constants.REQUEST_CODE_CAPTURE_IMAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_PRODUCT){
            // Tạo Product
            if (resultCode == RESULT_OK){

                long productId = data.getLongExtra(AddProductActivity.EXTRA_RESULT_PRODUCT_ID, 0);
                spinner_Product_init();
                spinner_Product_setSelection(productId);

            }
        }

        if (requestCode == Constants.REQUEST_CODE_CAPTURE_IMAGE){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // Lấy ảnh resize + save
                    Bitmap bitmap = BitmapFactory.decodeFile(mUriImage.getPath());
                    BitmapUtil.resizeAndSave(bitmap, AppConfig.APP_IMAGE_MAX_SIZE, mUriImage.getPath());

                    savePSImageHandle();
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
        mSpinner_Product = (Spinner) findViewById(R.id.spinner_Product);
        mInputLayout_Number = (TextInputLayout) findViewById(R.id.textInputLayout_Number);
        mInputLayout_RetailPrice = (TextInputLayout) findViewById(R.id.textInputLayout_RetailPrice);
    }

    private void initVars() {
        mDialog = new ProgressDialog(this);

        mProductAddNew = new Product(){{
            set_id(ProductAdapter.ACTION_ID_ADD);
            setName(getString(R.string.action_add_product));
        }};

        //--- Product ---
        spinner_Product_init();

        // khởi tạo giá trị qua Intent
        mAssignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, -1);

        long mPSId = getIntent().getLongExtra(EXTRA_PRODUCT_SHOWN_ID, -1);
        if (mPSId == -1){
            isAddNew = true;
        }
        else {
            // Hiển thị Cập nhật
            updateUIAddOrEdit(false);
            isAddNew = false;

            mProductShown = ProductShownData.getById(this, mPSId);
            spinner_Product_setSelection(mProductShown.getProductId());
            mInputLayout_Number.getEditText().setText(mProductShown.getNumber()+"");
            mInputLayout_RetailPrice.getEditText().setText(mProductShown.getRetailPrice()+"");
        }
    }

    private void setEvents() {

        mSpinner_Product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_Product_itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //endregion


    //region Sự kiện
    private void saveProductShownHandle(){
        AppAlertDialog.showWaitingDialog(this, mDialog);

        if (validateViews()){
            if (isAddNew) {
                // save add new

                long productId = addProductShown();

                if (productId > -1) {
                    // Thành công
                    Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                    AssignData.changeStatus(this, mAssignId, Assign.WorkStatus.DOING);
                    finishWithResultOK(productId);

                } else {
                    // Thất bại
                    Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
                }
            } else{
                // save Update

                long row = updateProductShown();

                if (row > 0) {
                    // Thành công
                    Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                    finishWithResultOK(mProductShown.get_id());

                } else {
                    // Thất bại
                    Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
                }
            }
        }

        mDialog.cancel();
    }

    /** Thay đổi lựa chọn Product */
    private void spinner_Product_itemSelected() {
        Product selectProduct = (Product) mSpinner_Product.getSelectedItem();
        if (selectProduct == mProductAddNew){
            // chọn add New Product

            startAddProductActivity();
        }
    }

    /** Chọn Product qua Id */
    private void spinner_Product_setSelection(long productId) {
        for(int i=0; i<mListProduct.size(); i++){
            if (mListProduct.get(i).get_id() == productId){
                mSpinner_Product.setSelection(i);
                break;
            }
        }
    }

    /** Save Product Shown Image */
    private void savePSImageHandle() {
        if (!isAddNew) {
            // Cập nhật mới thêm Ảnh

            long aiId = savePSImage();
            if (aiId != -1) {
                // thành công
                Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();

            } else {
                // Thất bại
                Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
            }
        }
    }

    private long savePSImage() {

        String latitude = new LocationService(this).getLatitude()+"";
        String longitude = new LocationService(this).getLongitude()+"";

        //-----save image------
        Image image = new Image();

        String filePath = mUriImage.getPath();
        image.setFileName(FileUtil.getFileName(filePath));
        image.setFilePath(filePath);
        image.setLatitude(latitude);
        image.setLongitude(longitude);

        image.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        image.setCreateBy(UserPref.getUserPrefId(this));
        image.setCreateAt(Calendar.getInstance());
        image.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        //-----save Attendance image------
        ProductShownImg psi = new ProductShownImg();

        psi.setProductShownId(mProductShown.get_id());

        psi.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        psi.setCreateBy(UserPref.getUserPrefId(this));
        psi.setCreateAt(Calendar.getInstance());
        psi.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        return ProductShownImgData.add(this, image, psi);
    }

    //endregion


    //region Hỗ trợ

    private boolean validateViews() {
        View errorView = null;

        // kiểm tra số dương, không rỗng
        if (!ValidateUtil.hasText(mInputLayout_Number, getString(R.string.error_field_required))
                || !ValidateUtil.isPositiveNumber(mInputLayout_Number, getString(R.string.error_field_positive_number))){
            errorView = mInputLayout_Number;
        }
        if (!ValidateUtil.hasText(mInputLayout_RetailPrice, getString(R.string.error_field_required))
                || !ValidateUtil.isPositiveNumber(mInputLayout_RetailPrice, getString(R.string.error_field_positive_number))){
            errorView = mInputLayout_RetailPrice;
        }

        // trả về kết quả
        if (errorView != null){
            errorView.requestFocus();
            return false;
        }

        return true;
    }

    private long addProductShown() {
        // lấy giá trị
        Product product = (Product) mSpinner_Product.getSelectedItem();
        long productId = product.get_id();
        int number = Integer.valueOf(mInputLayout_Number.getEditText().getText() + "");
        long retailPrice = Long.valueOf(mInputLayout_RetailPrice.getEditText().getText() + "");

        // gán giá trị
        mProductShown = new ProductShown();

        mProductShown.setAssignId(mAssignId);
        mProductShown.setProductId(productId);
        mProductShown.setNumber(number);
        mProductShown.setRetailPrice(retailPrice);

        mProductShown.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        mProductShown.setCreateBy(UserPref.getUserPrefId(this));
        mProductShown.setCreateAt(Calendar.getInstance());
        mProductShown.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        long id = ProductShownData.add(this, mProductShown);
        mProductShown.set_id(id);

        return id;
    }

    private long updateProductShown() {
        // lấy giá trị
        Product product = (Product) mSpinner_Product.getSelectedItem();
        long productId = product.get_id();
        int number = Integer.valueOf(mInputLayout_Number.getEditText().getText() + "");
        long retailPrice = Long.valueOf(mInputLayout_RetailPrice.getEditText().getText() + "");

        // gán giá trị
        mProductShown.setAssignId(mAssignId);
        mProductShown.setProductId(productId);
        mProductShown.setNumber(number);
        mProductShown.setRetailPrice(retailPrice);

        mProductShown.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        mProductShown.setCreateBy(UserPref.getUserPrefId(this));
        mProductShown.setCreateAt(Calendar.getInstance());
        mProductShown.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        return ProductShownData.updateAllById(this, mProductShown.get_id(), mProductShown);
    }

    /** Hiện Thêm Product */
    private void startAddProductActivity() {
        startActivityForResult(new Intent(this, AddProductActivity.class), REQUEST_CODE_ADD_PRODUCT);
    }

    /** Khởi tạo lại Product */
    private void spinner_Product_init() {
        mListProduct = ProductData.getAll(this);
        mListProduct.add(mProductAddNew);  // Chọn để thêm mới Product
        mAdapterProduct = new ProductAdapter(this, R.layout.list_item_product, mListProduct);
        mSpinner_Product.setAdapter(mAdapterProduct);
    }

    /** Trả về với kết quả */
    private void finishWithResultOK(long productId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_RESULT_PRODUCT_ID, productId);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /** Cập nhật giao diện Thêm mới hay Cập nhật */
    private void updateUIAddOrEdit(boolean isAddNew) {
        String title = "";

        if (isAddNew){
            title = getString(R.string.title_activity_add_product_shown);
        } else {
            title = getString(R.string.title_activity_update_product_shown);
        }

        getSupportActionBar().setTitle(title);
    }

    //endregion
}
