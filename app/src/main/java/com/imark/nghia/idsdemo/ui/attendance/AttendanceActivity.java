package com.imark.nghia.idsdemo.ui.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.imark.nghia.idscore.models.AddAttendance;
import com.imark.nghia.idscore.models.AddAttendanceImg;
import com.imark.nghia.idscore.presenters.attendance.AttendancePresenter;
import com.imark.nghia.idscore.presenters.attendance.IAttendancePresenter;
import com.windyroad.nghia.common.activity.ActivityUtil;
import com.windyroad.nghia.common.activity.IActivityResultListener;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.network.webservices.models.GetAttendanceTimeWSResult;

/**
 * Chấm công => upload, save local => upload image, save local
 */
public class AttendanceActivity extends AppCompatActivity
        implements com.google.android.gms.location.LocationListener {

    public static String EXTRA_ASSIGN_ID = "assign_id";

    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(33)    // 33ms = 30fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private GoogleApiClient mGoogleApiClient;
    private static ProgressDialog mDialogProgress;
    private boolean firstMoveMapCamera = true;  // di chuyển camera đầu tiên

    private IAttendancePresenter mPresenter;

    private TextView mTextView_Latitude;
    private TextView mTextView_Longitude;
    private TextView mTextView_ServerTime;
    private TextView mTextView_AttendanceType;
    private View mButton_RefreshCoordinate;
    private View mView_OpenMap;
    private GoogleMap mGoogleMap;
    private Menu mMenu;

    private IActivityResultListener mCameraResultListener;
    private IAttendancePresenter.IAttendanceView mView = new IAttendancePresenter.IAttendanceView() {
        @Override
        public void setServerInfo(GetAttendanceTimeWSResult info) {
            mTextView_ServerTime.setText(info.getDateTimeServer());
            mTextView_AttendanceType.setText(info.getTypeAttendance());
        }

        @Override
        public void setError_server(String error) {
            String err = getString(R.string.message_error_server_plus) + error;
            Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
        }

        @Override
        public void setUI_userAttendance() {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(getString(R.string.title_activity_attendance));
        }

        @Override
        public void setUI_assignAttendance(String outletName) {
            String title = getString(R.string.title_activity_assign_attendance, outletName);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
        }

        @Override
        public void setError_location() {
            AppAlertDialog.showLocationSettingAlert(AttendanceActivity.this);
        }

        @Override
        public void setLocation(Location location) {
            mTextView_Latitude.setText(String.valueOf(location.getLatitude()));
            mTextView_Longitude.setText(String.valueOf(location.getLongitude()));
        }

        @Override
        public void setSuccess_postAttendanceImg() {
            Toast.makeText(mView.getContext(), getString(R.string.message_success), Toast.LENGTH_LONG).show();
        }

        @Override
        public void setError_unknown() {
            Toast.makeText(getBaseContext(), getString(R.string.message_error_unknown), Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void setError_network() {
            AppAlertDialog.showNetworkSettingAlert(AttendanceActivity.this);
        }

        @Override
        public void setError_allNotNull() {
            Toast.makeText(getBaseContext(), getString(R.string.error_coordinate_required), Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void setUI_captureImage(boolean allowCapture) {
            mMenu.findItem(R.id.action_add_attendance).setVisible(!allowCapture);
            mMenu.findItem(R.id.action_add_attendance_image).setVisible(allowCapture);
        }

        @Override
        public void setCameraResultListener(IActivityResultListener listener) {
            mCameraResultListener = listener;
        }

        @Override
        public Context getContext() {
            return AttendanceActivity.this;
        }

        @Override
        public void showWaiting() {
            AppAlertDialog.showWaitingDialog(getBaseContext(), mDialogProgress);
        }

        @Override
        public void hideWaiting() {
            mDialogProgress.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        buildMap();
        ActivityUtil.setupToolbar(this, R.id.toolbar_Main,
                true, R.drawable.ic_close_32dp,
                false, null);
        findViews();
        initVars();
        setEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();  // kết nối trở lại
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();  // ngừng kết nối
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendance, menu);

        this.mMenu = menu;

        // ẩn trong menu
        mView.setUI_captureImage(false);

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
            case R.id.action_add_attendance:
                mPresenter.saveAttendanceHandle(new AddAttendance(
                        mTextView_AttendanceType.getText() + "",
                        mTextView_Latitude.getText() + "",
                        mTextView_Longitude.getText() + ""
                        )
                );
                return true;
            case R.id.action_location:
                refreshGoogleLocationHandle();
                return true;
            case R.id.action_add_attendance_image:
                mPresenter.saveAttendanceImgHandle(
                        AttendanceActivity.this,
                        Constants.REQUEST_CODE_CAPTURE_IMAGE,
                        new AddAttendanceImg(mTextView_Latitude.getText()+"", mTextView_Longitude.getText()+""));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Camera trả về kết quả
        if (requestCode == Constants.REQUEST_CODE_CAPTURE_IMAGE) {
            mCameraResultListener.onResult(resultCode, data);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // vị trí thay đổi

        if (firstMoveMapCamera) {
            firstMoveMapCamera = false;
            // chuyển camera đến vị trí 1 lần
            updateMapCamera(mGoogleMap, location);
        }

        mTextView_Latitude.setText(String.valueOf(location.getLatitude()));
        mTextView_Longitude.setText(String.valueOf(location.getLongitude()));
    }


    //region Khởi tạo

    private void buildMap() {
        // Đồng bộ map
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = mapFragment.getMap();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);  // hiện vị trí
            }
        });

        GoogleApiClient.Builder mapBuilder = new GoogleApiClient.Builder(this);
        mapBuilder.addApi(LocationServices.API);
        mapBuilder.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                // Cập nhật vị trí qua Map
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(mGoogleApiClient, REQUEST, AttendanceActivity.this);
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        mapBuilder.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Toast.makeText(getBaseContext(), getString(R.string.message_connection_map_fail), Toast.LENGTH_LONG).show();
            }
        });

        mGoogleApiClient = mapBuilder.build();
    }

    private void findViews() {
        mTextView_Latitude = (TextView) findViewById(R.id.textView_Latitude);
        mTextView_Longitude = (TextView) findViewById(R.id.textView_Longitude);
        mTextView_ServerTime = (TextView) findViewById(R.id.textView_ServerTime);
        mTextView_AttendanceType = (TextView) findViewById(R.id.textView_AttendanceType);
        mView_OpenMap = findViewById(R.id.button_OpenMap);
        mButton_RefreshCoordinate = findViewById(R.id.button_RefreshCoordinates);
    }

    private void initVars() {
        mDialogProgress = new ProgressDialog(this);

        long assignId = getIntent().getLongExtra(EXTRA_ASSIGN_ID, Constants.DEFAULT_LONG);
        mPresenter = new AttendancePresenter(mView, assignId);
    }

    private void setEvents() {
        mView_OpenMap.setOnClickListener(openMap_onClick);
        mButton_RefreshCoordinate.setOnClickListener(refreshCoordinates_onClick);
    }

    //endregion


    //region Sự kiện
    private void refreshGoogleLocationHandle() {
        if (mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateMapCamera(mGoogleMap, location);
        }
    }

    /**
     * mở bản đồ trong máy
     */
    View.OnClickListener openMap_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPresenter.openOtherMap(
                    mTextView_Latitude.getText().toString(),
                    mTextView_Longitude.getText().toString());
        }
    };

    View.OnClickListener refreshCoordinates_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPresenter.getCoordinatesHandle(true);
        }
    };
    //endregion

    /**
     * Cập nhật camera
     */
    private void updateMapCamera(GoogleMap googleMap, Location location) {
        CameraPosition currentPos = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(16f)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(currentPos);
        googleMap.animateCamera(cameraUpdate, 500, null);  // chạy 1000ms
        //mMap.moveCamera(update);  // không có hiệu ứng
    }
    //endregion
}
