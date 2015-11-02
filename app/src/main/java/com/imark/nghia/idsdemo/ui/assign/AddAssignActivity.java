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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AreaData;
import com.imark.nghia.idscore.data.models.CodeDetail;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idscore.data.models.Area;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.models.Outlet;
import com.imark.nghia.idscore.data.OutletData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.AppConfig;
import com.imark.nghia.idscore.helper.Global;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idsdemo.adapters.OutletAdapter;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.DialogUtil;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.activity.ActivityUtil;
import com.windyroad.nghia.common.fragment.DatePickerFragment;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.service.LocationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAssignActivity extends AppCompatActivity {
    private static final String TAG = AddAssignActivity.class.getName();

    private Toolbar mToolbar;
    /**
     * Ngày hết hạn
     */
    private Calendar mCalExpiredDate;  // Đánh dấu cho chọn ngày Hết hạn
    private ArrayList<Outlet> mListOutlet;
    private OutletAdapter mAdapterOutlet;
    private ProgressDialog mDialog;
    private List<Area> mListCity;
    private List<Area> mListDistrict;
    private ArrayAdapter<Area> mAdapterCity;
    private ArrayAdapter<Area> mAdapterDistrict;

    private ViewGroup mViewGroup_NewOutlet;
    private Button mButton_ExpiredDate;
    private ImageButton mButton_OpenMap;
    private EditText mEditText_OutletAddress;
    private EditText mEditText_OutletLatitude;
    private EditText mEditText_OutletLongitude;
    private ImageButton mButton_RefreshOutletCoordinates;
    private AutoCompleteTextView mAutoComplete_OutletName;
    private Spinner mSpinner_City;
    private Spinner mSpinner_District;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assign);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_assign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                saveAssignHandle();
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
        mButton_ExpiredDate = (Button) findViewById(R.id.button_ExpiredDate);
        mEditText_OutletAddress = (EditText) findViewById(R.id.editText_OutletAddress);
        mEditText_OutletLatitude = (EditText) findViewById(R.id.editText_OutletLatitude);
        mEditText_OutletLongitude = (EditText) findViewById(R.id.editText_OutletLongitude);
        mButton_RefreshOutletCoordinates = (ImageButton) findViewById(R.id.button_RefreshOutletCoordinates);
        mViewGroup_NewOutlet = (ViewGroup) findViewById(R.id.viewGroup_NewOutlet);
        mAutoComplete_OutletName = (AutoCompleteTextView) findViewById(R.id.autoComplete_OutletName);
        mSpinner_City = (Spinner) findViewById(R.id.spinner_City);
        mSpinner_District = (Spinner) findViewById(R.id.spinner_District);
        mButton_OpenMap = (ImageButton) findViewById(R.id.button_OpenMap);
    }

    private void initVars() {
        mDialog = new ProgressDialog(this);

        //----- Expired Date -----
        mCalExpiredDate = Calendar.getInstance();

        mButton_ExpiredDate.setText(ConvertUtil.Calendar2String(
                mCalExpiredDate, AppConfig.APP_DATE_FORMAT, null));

        //------ Auto complete Outlet------
        mListOutlet = OutletData.getAll(this);
        mAdapterOutlet = new OutletAdapter(this, R.layout.list_item_outlet, android.R.id.text1, mListOutlet);
        mAutoComplete_OutletName.setAdapter(mAdapterOutlet);

        //------ City - District ---------
        initListCity();

        initListDistrict();

        // ---- Get location ----
        refreshCoordinatesHandle(false);
    }

    /**
     * Khởi tạo Sự kiện
     */
    private void setEvents() {
        mButton_RefreshOutletCoordinates.setOnClickListener(mButton_RefreshOutletCoordinates_onClick);
        mButton_ExpiredDate.setOnClickListener(mButton_ExpiredDate_onClick);
        mSpinner_City.setOnItemSelectedListener(mSpinner_City_onItemSelected);
        mButton_OpenMap.setOnClickListener(mButton_OpenMap_onClick);
    }
    //endregion


    //region Sự kiện
    /**
     * Mở bản đồ
     */
    View.OnClickListener mButton_OpenMap_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ActivityUtil.startOtherMap(
                    getBaseContext(),
                    mEditText_OutletLatitude.getText() + "",
                    mEditText_OutletLongitude.getText() + "");
        }
    };

    /** thử lưu Assign, kiểm tra... */
    private void saveAssignHandle() {
        AppAlertDialog.showWaitingDialog(this, mDialog);

        if (validateViews()) {
            // không lỗi

            long assignId = saveAssign();
            if (assignId > -1) {
                // thành công
                startAssignInfoActivity(assignId);

            } else {
                // thất bại
                Toast.makeText( this,getResources().getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show();
            }
        }

        mDialog.cancel();
    }

    /** TODO: nếu chọn lại, Khi outlet được chọn lại */
    OutletAdapter.OnItemSelectedListener onOutletSelected = new OutletAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(Outlet outlet) {
            // đẩy dữ liệu vào View
            mEditText_OutletAddress.setText(outlet.getAddress());
            mEditText_OutletLatitude.setText(outlet.getLatitude());
            mEditText_OutletLongitude.setText(outlet.getLongitude());
            mSpinner_City_setSelect(outlet.getCity());
        }
    };


    private void mSpinner_City_setSelect(String cityName) {
        // tìm theo tên
        for (int i=0; i<mSpinner_City.getCount(); i++){
            CodeDetail city = (CodeDetail) mSpinner_City.getItemAtPosition(i);
            if (cityName.equals(city.getName())){
                mSpinner_City.setSelection(i);
                break;
            }
        }
    }


    private View.OnClickListener mButton_ExpiredDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Làm 1 Button Chọn ngày trong Common
            DatePickerFragment datePicker = DatePickerFragment.newInstance(mCalExpiredDate);
            datePicker.setListener(new DatePickerFragment.IListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Đặt lại Cal - Text
                    mCalExpiredDate.set(year, monthOfYear, dayOfMonth);
                    mButton_ExpiredDate.setText(ConvertUtil.Calendar2String(
                            mCalExpiredDate, AppConfig.APP_DATE_FORMAT, null));
                }
            });
            datePicker.show(getSupportFragmentManager(), "tag");
        }
    };

    private View.OnClickListener mButton_RefreshOutletCoordinates_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshCoordinatesHandle(true);
        }
    };

    /** Chọn city => đổi district*/
    private AdapterView.OnItemSelectedListener mSpinner_City_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            initListDistrict();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    //endregion


    //region Hỗ trợ

    /** Tạo city */
    private void initListCity() {
        mListCity = AreaData.getBy_Level_ParentId(this, Constants.AREA_LEVEL_CITY, null);
        mAdapterCity = new ArrayAdapter<>(this, R.layout.list_item_simple_spinner, mListCity);
        mSpinner_City.setAdapter(mAdapterCity);
    }

    /** Tạo District */
    private void initListDistrict() {
        Area city = (Area) mSpinner_City.getSelectedItem();
        long cityServerId = city.getServerId();

        mListDistrict = AreaData.getBy_Level_ParentId(this, Constants.AREA_LEVEL_DISTRICT, cityServerId);
        mAdapterDistrict = new ArrayAdapter<>(this, R.layout.list_item_simple_spinner, mListDistrict);
        mSpinner_District.setAdapter(mAdapterDistrict);
    }

    private boolean validateViews(){
        View focusView = null;

        // ngày hết hạn
        if (!ValidateUtil.hasText(mButton_ExpiredDate, getString(R.string.error_field_required))){
            focusView = mButton_ExpiredDate;
        }

        // outlet name
        if (!ValidateUtil.hasText(mAutoComplete_OutletName, getString(R.string.error_field_required))){
            focusView = mAutoComplete_OutletName;
        }

        // lấy kết quả
        if (focusView != null){
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Lưu, lấy id
     */
    private long saveAssign() {

        Area city = (Area) mSpinner_City.getSelectedItem();
        Area district = (Area) mSpinner_District.getSelectedItem();

        Assign assign = new Assign();

        assign.setExpiredAt(mCalExpiredDate);
        assign.setOutletName(mAutoComplete_OutletName.getText() + "");
        assign.setOutletCity(city.getName());
        assign.setOutletDistrict(district.getName());
        assign.setOutletAreaId(district.getServerId());
        assign.setOutletAddress(mEditText_OutletAddress.getText() + "");
        assign.setOutletLatitude(mEditText_OutletLatitude.getText() + "");
        assign.setOutletLongitude(mEditText_OutletLongitude.getText()+"");

        assign.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        assign.setCreateBy(UserPref.getUserPrefId(this));
        assign.setCreateAt(Calendar.getInstance());
        assign.setUploadStatus(UploadStatus.WAITING_UPLOAD);
        assign.setWorkStatus(Assign.WorkStatus.NEW);

        return AssignData.add(this, assign);
    }

    private void startAssignInfoActivity(long assignId){
        Intent intent = new Intent(this, ViewAssignActivity.class);
        intent.putExtra(ViewAssignActivity.EXTRA_ASSIGN_ID, assignId);
        startActivity(intent);
        finish();
    }

    /**
     * Cập nhật thay đổi lên giao diện
     */
    private void updateUIAddNewOutlet(boolean isAddNewOutlet) {
        // Cập nhật giao diện thêm mới Outlet
        if (isAddNewOutlet) {
            mViewGroup_NewOutlet.setVisibility(View.VISIBLE);
        } else {
            mViewGroup_NewOutlet.setVisibility(View.GONE);
        }
    }

    /**
     * Refresh Tọa độ
     **/
    private void refreshCoordinatesHandle(boolean askOpenSetting) {
        LocationService location = new LocationService(this);
        if (!location.canGetLocation()) {
            // Không lấy được vị trí
            if (askOpenSetting) {
                DialogUtil.showLocationSettingAlert(
                        this,
                        getString(R.string.title_dialog_gps),
                        getString(R.string.message_dialog_open_gps),
                        getString(R.string.action_settings),
                        getString(R.string.action_cancel)
                );
            }
        } else {
            // lấy được vị trí
            mEditText_OutletLatitude.setText(location.getLatitude() + "");
            mEditText_OutletLongitude.setText(location.getLongitude() + "");
        }
    }

    //endregion
}
