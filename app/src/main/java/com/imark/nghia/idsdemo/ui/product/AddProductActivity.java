package com.imark.nghia.idsdemo.ui.product;

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
import com.imark.nghia.idscore.data.models.Product;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.network.UploadStatus;

import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_PRODUCT_ID = "extra_product_id";

    private ProgressDialog mDialog;

    private Toolbar mToolbar;
    private TextInputLayout mInputLayout_Name;
    private TextInputLayout mInputLayout_Color;
    private TextInputLayout mInputLayout_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
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
                saveProductHandle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region khởi tạo

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
        mInputLayout_Name = (TextInputLayout) findViewById(R.id.textInputLayout_Name);
        mInputLayout_Color = (TextInputLayout) findViewById(R.id.textInputLayout_Color);
        mInputLayout_Price = (TextInputLayout) findViewById(R.id.textInputLayout_Price);
    }

    private void initVars() {
        mDialog = new ProgressDialog(this);
    }

    private void setEvents() {

    }

    //endregion


    //region Sự kiện
    private void saveProductHandle() {
        AppAlertDialog.showWaitingDialog(this, mDialog);

        if (validateViews()){
            long productId = saveProduct();
            if (productId > -1){
                // thành công

                Toast.makeText(this, getString(R.string.message_success), Toast.LENGTH_LONG).show();
                finishWithResultOK(productId);

            } else {
                // thất bại

                Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
            }
        }

        mDialog.cancel();
    }

    //endregion


    //region Hỗ trợ

    private boolean validateViews() {
        View errorView = null;

        // name
        if (!ValidateUtil.hasText(mInputLayout_Name, getString(R.string.error_field_required))){
            errorView = mInputLayout_Name;
        }

        // price dương
        if (!ValidateUtil.isPositiveNumber(mInputLayout_Price, getString(R.string.error_field_positive_number))){
            errorView = mInputLayout_Price;
        }

        // trả về kết quả
        if (errorView != null){
            errorView.requestFocus();
            return false;
        }

        return true;
    }

    private long saveProduct() {
        // lấy biến
        long price = Long.valueOf(mInputLayout_Price.getEditText().getText()+"");

        // gán biến
        Product product = new Product();

        product.setName(mInputLayout_Name.getEditText().getText()+"");
        product.setColor(mInputLayout_Color.getEditText().getText() + "");
        product.setPrice(price);

        product.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        product.setCreateBy(UserPref.getUserPrefId(this));
        product.setCreateAt(Calendar.getInstance());
        product.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        // save
        return ProductData.add(this, product);
    }

    /** Finish Activity trả về kết quả */
    private void finishWithResultOK(long productId) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_RESULT_PRODUCT_ID, productId);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    //endregion
}
