package com.imark.nghia.idscore.presenters.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.AttendanceData;
import com.imark.nghia.idscore.data.AttendanceImgData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.Attendance;
import com.imark.nghia.idscore.data.models.AttendanceImg;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.helper.AppConfig;
import com.imark.nghia.idscore.helper.Constants;
import com.imark.nghia.idscore.helper.Global;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.models.AddAttendance;
import com.imark.nghia.idscore.models.AddAttendanceImg;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetAttendanceTimeWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceImgWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceTrackingWSResult;
import com.imark.nghia.idscore.tasks.GetAssignAttendanceTimeWSTask;
import com.imark.nghia.idscore.tasks.GetAttendanceTimeWSTask;
import com.imark.nghia.idscore.tasks.PostAssignAttendanceImgWSTask;
import com.imark.nghia.idscore.tasks.PostAssignAttendanceWSTask;
import com.imark.nghia.idscore.tasks.PostAttendanceImgWSTask;
import com.imark.nghia.idscore.tasks.PostAttendanceWSTask;
import com.windyroad.nghia.common.BitmapUtil;
import com.windyroad.nghia.common.DatabaseUtil;
import com.windyroad.nghia.common.FileUtil;
import com.windyroad.nghia.common.ValidateUtil;
import com.windyroad.nghia.common.activity.ActivityUtil;
import com.windyroad.nghia.common.activity.IActivityResultListener;
import com.windyroad.nghia.common.network.ConnectionType;
import com.windyroad.nghia.common.network.NetworkUtil;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.service.LocationService;

import java.util.Calendar;

/**
 * Created by Imark-N on 10/27/2015.
 */
public class AttendancePresenter implements IAttendancePresenter {

    private final IAttendanceView mView;
    private Assign mAssign = null;  // assign == null -> chấm công ra vào
    private long mAttendanceId;
    private Uri mUriImage;

    public AttendancePresenter(IAttendanceView view, long assignId) {
        this.mView = view;

        // assign
        if (assignId != Constants.DEFAULT_LONG)
            mAssign = AssignData.getById(mView.getContext(), assignId);

        // lấy thông tin chấm công
        if (mAssign == null) {
            // Chấm công ra vào
            mView.setUI_userAttendance();

            GetAttendanceTimeWSTask getInfoTask2 = new GetAttendanceTimeWSTask(
                    mView.getContext(),
                    UserPref.getUserPrefId(mView.getContext()),
                    new GetAttendanceTimeWSTask.ResultListener() {
                        @Override
                        public void onPostExecuteResult(GetAttendanceTimeWSResult wsResult) {
                            getAttendanceTimeTask_result(wsResult);
                        }
                    });
            getInfoTask2.execute();

        } else {
            // Chấm công Phân công
            mView.setUI_assignAttendance(mAssign.getOutletName());

            GetAssignAttendanceTimeWSTask getInfoTask1 = new GetAssignAttendanceTimeWSTask(
                    mView.getContext(),
                    mAssign.getServerId(),
                    new GetAssignAttendanceTimeWSTask.ResultListener() {
                        @Override
                        public void onPostExecuteResult(GetAttendanceTimeWSResult wsResult) {
                            getAttendanceTimeTask_result(wsResult);
                        }
                    });
            getInfoTask1.execute();
        }

        // lấy vị trí
        getCoordinatesHandle(false);
    }

    /**
     * Lấy kết quả Thông tin chấm công từ Server
     */
    private void getAttendanceTimeTask_result(GetAttendanceTimeWSResult wsResult) {
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            // Thành công
            mView.setServerInfo(wsResult);

        } else {
            // server lỗi
            mView.setError_server(wsResult.getDescription());
        }
    }


    @Override
    public void getCoordinatesHandle(boolean showError) {
        LocationService location = new LocationService(mView.getContext());
        if (!location.canGetLocation()) {
            // Không lấy được vị trí
            if (showError) {
                mView.setError_location();
            }
        } else {
            // lấy được vị trí
            mView.setLocation(location.getLocation());
        }
    }


    @Override
    public void openOtherMap(String latitude, String longitude) {
        ActivityUtil.startOtherMap(mView.getContext(), latitude, longitude);
    }


    @Override
    public void saveAttendanceHandle(final AddAttendance model) {
        if (validateAttendance(model)) {

            mView.showWaiting();

            final String sessionCode = DatabaseUtil.getCodeGenerationByTime();
            final long userId = UserPref.getUserPrefId(mView.getContext());
            final Calendar attendanceTime = Calendar.getInstance();

            if (mAssign == null) {
                // Chấm công ngày

                final PostAttendanceWSTask postAttendanceTask = new PostAttendanceWSTask(mView.getContext(),
                        sessionCode, userId, model.getType(), attendanceTime, model.getLatitude(), model.getLongitude(),
                        new PostAttendanceWSTask.IAddAttendanceListener() {
                            @Override
                            public void onPostExecuteResult(PostAttendanceTrackingWSResult wsResult) {
                                postAttendanceTask_result(wsResult, sessionCode, userId, model.getType(), attendanceTime);
                            }
                        });

                postAttendanceTask.execute();

            } else {
                // Chấm công theo Assign

                PostAssignAttendanceWSTask task = new PostAssignAttendanceWSTask(mView.getContext(),
                        sessionCode, userId, mAssign.get_id(), model.getType(), attendanceTime, model.getLatitude(), model.getLongitude(),
                        new PostAssignAttendanceWSTask.IAddAttendanceListener() {
                            @Override
                            public void onPostExecuteResult(PostAttendanceTrackingWSResult wsResult) {
                                postAttendanceTask_result(wsResult, sessionCode, userId, model.getType(), attendanceTime);
                            }
                        });
                task.execute();
            }
        }
    }

    /**
     * Kiểm tra trước khi chấm công
     */
    private boolean validateAttendance(AddAttendance model) {
        // Kiểm tra mạng
        if (NetworkUtil.getConnectivityStatus(mView.getContext()) == ConnectionType.NOT_CONNECTED){
            mView.setError_network();
            return false;
        }
        // kiểm tra GPS
        if (!new LocationService(mView.getContext()).canGetLocation()) {
            mView.setError_location();
            return false;
        }

        // Kiểm tra Giá trị tọa độ
        if (!ValidateUtil.hasText(model.getType()) || !ValidateUtil.hasText(model.getLatitude())
                || !ValidateUtil.hasText(model.getLongitude())){
            mView.setError_allNotNull();
            return false;
        }

        return true;
    }

    /**
     * Post Attendance Kết thúc
     * @param wsResult
     * @param sessionCode
     * @param userId
     * @param attendanceType
     * @param attendanceTime
     */
    private void postAttendanceTask_result(PostAttendanceTrackingWSResult wsResult, String sessionCode, long userId, String attendanceType, Calendar attendanceTime) {
        mView.hideWaiting();

        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            // Post Thành công => Lấy id Save, Cho chụp hình chấm công, đổi Assign thành DOING

            long serverId = wsResult.getAttendanceTrackingID();
            mAttendanceId = saveLocalAttendance(sessionCode, userId, attendanceType,
                    attendanceTime, serverId,
                    UploadStatus.UPLOADED);

            if (mAssign != null){
                AssignData.changeStatus(mView.getContext(), mAssign.get_id(), Assign.WorkStatus.DOING);
            }

            mView.setUI_captureImage(true);

        } else {
            // Thất bại
            mView.setError_server(wsResult.getDescription());
        }
    }

    /**
     * Lưu Attendance ở SQLite
     *
     * @param sessionCode
     * @param userId
     * @param attendanceType
     * @param attendanceTime
     */
    private long saveLocalAttendance(
            String sessionCode, long userId, String attendanceType,
            Calendar attendanceTime, long serverId, UploadStatus uploadStatus) {

        Attendance at = new Attendance();
        at.setCreateAt(attendanceTime);
        at.setCreateBy(userId);
        at.setServerId(serverId);
        at.setSessionCode(sessionCode);
        at.setUploadStatus(uploadStatus);
        at.setAttendanceType(attendanceType);

        return AttendanceData.add(mView.getContext(), at);
    }


    @Override
    public void saveAttendanceImgHandle(Activity activity, int requestCode, final AddAttendanceImg model) {
        mUriImage = Global.startCameraForResult(activity, requestCode);
        mView.setCameraResultListener(new IActivityResultListener() {

            @Override
            public void onResult(int resultCode, Intent data) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Lấy ảnh resize + save
                        Bitmap bitmap = BitmapFactory.decodeFile(mUriImage.getPath());
                        BitmapUtil.resizeAndSave(bitmap, AppConfig.APP_IMAGE_MAX_SIZE, mUriImage.getPath());

                        long aiId = saveAttendanceImage(model);
                        if (aiId != -1) {
                            // thành công
                            postAttendanceImage(aiId);

                        } else {
                            // Thất bại
                            mView.setError_unknown();
                        }
                        break;
                }
            }
        });
    }

    private long saveAttendanceImage(AddAttendanceImg model) {
        //-----save image------
        Image image = new Image();

        String filePath = mUriImage.getPath();
        image.setFileName(FileUtil.getFileName(filePath));
        image.setFilePath(filePath);
        image.setLatitude(model.getLatitude());
        image.setLongitude(model.getLongitude());

        image.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        image.setCreateBy(UserPref.getUserPrefId(mView.getContext()));
        image.setCreateAt(Calendar.getInstance());
        image.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        //-----save Attendance image------
        AttendanceImg attendanceImage = new AttendanceImg();

        attendanceImage.setAttendanceId(mAttendanceId);

        attendanceImage.setSessionCode(DatabaseUtil.getCodeGenerationByTime());
        attendanceImage.setCreateBy(UserPref.getUserPrefId(mView.getContext()));
        attendanceImage.setCreateAt(Calendar.getInstance());
        attendanceImage.setUploadStatus(UploadStatus.WAITING_UPLOAD);

        return AttendanceImgData.add(mView.getContext(), image, attendanceImage);
    }

    /**
     * Post Attendance Image lên Webservice
     */
    private void postAttendanceImage(long attendanceImageId) {
        mView.showWaiting();

        if (mAssign == null) {
            // chấm công theo ngày

            PostAttendanceImgWSTask task1 = new PostAttendanceImgWSTask(
                    mView.getContext(),
                    attendanceImageId,
                    new PostAttendanceImgWSTask.IResultListener() {
                        @Override
                        public void onPostExecute(PostAttendanceImgWSResult wsResult) {
                            postImage_result(wsResult);
                        }
                    });
            task1.execute();

        } else {
            // theo Assign

            PostAssignAttendanceImgWSTask task2 = new PostAssignAttendanceImgWSTask(
                    mView.getContext(),
                    attendanceImageId,
                    new PostAssignAttendanceImgWSTask.IResultListener() {
                @Override
                public void onPostExecute(PostAttendanceImgWSResult wsResult) {
                    postImage_result(wsResult);
                }
            });
            task2.execute();
        }
    }

    /**
     * Lấy kết quả khi post image
     * @param wsResult
     */
    private void postImage_result(PostAttendanceImgWSResult wsResult) {
        mView.hideWaiting();

        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            mView.setSuccess_postAttendanceImg();
        } else {
            mView.setError_server(wsResult.getDescription());
        }
    }

}
