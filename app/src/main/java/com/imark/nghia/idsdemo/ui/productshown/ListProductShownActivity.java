package com.imark.nghia.idsdemo.ui.productshown;

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
import com.imark.nghia.idscore.data.ProductShownData;
import com.imark.nghia.idscore.data.models.ProductShown;
import com.imark.nghia.idsdemo.adapters.ProductShownAdapter;

import java.util.ArrayList;

public class ListProductShownActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT_PRODUCT = 200;
    public static String EXTRA_ASSIGN_ID = "assign_id";

    private ArrayList<ProductShown> mListProductShown;
    private ProductShownAdapter mAdapterProductShown;
    private long mAssignId;

    private Toolbar mToolbar;
    private ListView mListView_ProductShown;
    private View mView_NoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_shown);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            // Thêm mới
            startEditProductShownActivity(null);
            return true;
        }
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Chỉnh sửa trả về
        if (requestCode == REQUEST_CODE_EDIT_PRODUCT){
            switch (resultCode){
                case RESULT_OK:
                    //refresh list
                    listView_ProductShown_init();
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
        }
    }

    private void findViews() {
        mListView_ProductShown = (ListView) findViewById(R.id.listView_ProductShown);
        mView_NoData = findViewById(R.id.view_NoData);
    }

    private void initVars() {
        mAssignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, -1);

        listView_ProductShown_init();
    }

    private void setEvents() {
        mListView_ProductShown.setOnItemClickListener(listView_ProductShown_itemClick);
    }
    //endregion


    //region Sự kiện
    private void listView_ProductShown_init() {
        mListProductShown = ProductShownData.getByAssignId(this, mAssignId);
        mAdapterProductShown = new ProductShownAdapter(this, R.layout.list_item_product_show, mListProductShown);
        mListView_ProductShown.setAdapter(mAdapterProductShown);

        if (mListProductShown.size() > 0){
            updateUINoData(false);
        } else {
            updateUINoData(true);
        }
    }

    private AdapterView.OnItemClickListener listView_ProductShown_itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
            ProductShown ps = mListProductShown.get(postion);
            if (ps.getUploadStatus() == UploadStatus.WAITING_UPLOAD) {
                // Chỉ cho chỉnh sửa Waiting Upload
                startEditProductShownActivity(ps.get_id());
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
    /** Qua trang add/update ProductShown */
    private void startEditProductShownActivity(@Nullable Long productShownId) {
        Intent intent = new Intent(this, EditProductShownActivity.class);
        intent.putExtra(EditProductShownActivity.EXTRA_ASSIGN_ID, mAssignId);
        intent.putExtra(EditProductShownActivity.EXTRA_PRODUCT_SHOWN_ID, productShownId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT);
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
