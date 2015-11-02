package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.AssignWS;
import com.windyroad.nghia.common.models.ActionResult;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.network.webservices.AttendanceWS;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceTrackingWSResult;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 9/28/2015.
 */
public class PostAssignAttendanceWSTask extends AsyncTask<Void, Void, PostAttendanceTrackingWSResult> {

    private final Context mContext;
    private final IAddAttendanceListener mListener;
    private final String mSessionCode;
    private final long mUserId;
    private final String mTimePointType;
    private final String mLatitude;
    private final String mLongitude;
    private final Calendar mAttendanceTime;
    private final long mAssignId;

    public PostAssignAttendanceWSTask(
            Context context, String sessionCode, long userId, long assignId, String timePointType, Calendar attendanceTime,
            String latitude, String longitude, IAddAttendanceListener resultListener) {

        this.mContext = context;
        this.mListener = resultListener;
        this.mAttendanceTime = attendanceTime;
        this.mSessionCode = sessionCode;
        this.mUserId = userId;
        this.mAssignId = assignId;
        this.mTimePointType = timePointType;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected PostAttendanceTrackingWSResult doInBackground(Void... params) {
        try {

            Assign assign = AssignData.getById(mContext, mAssignId);
            // Post Assign trước
            if (assign.getServerId() == 0){
                // không có assing thì post lên
                ActionResult paResult = AssignWS.postAndUpdate(mContext, assign);
                if (paResult.getResult() == ActionResult.ResultStatus.SUCCESS){
                    // Thành công, load lại Assign
                    assign = AssignData.getById(mContext, mAssignId);
                } else {
                    // thất bại
                    return new PostAttendanceTrackingWSResult(
                            BaseWSResult.STATUS_UNKNOWN,
                            paResult.getMessage(), 0);
                }
            }

            return AttendanceWS.postAssignAttendance(mUserId, assign.getServerId(), mSessionCode, mAttendanceTime,
                    mTimePointType, mLatitude, mLongitude);

        } catch (Exception ex) {
            ex.printStackTrace();

            PostAttendanceTrackingWSResult result = new PostAttendanceTrackingWSResult();
            result.setStatus(BaseWSResult.STATUS_UNKNOWN);
            result.setDescription(ex.getMessage());

            return result;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(PostAttendanceTrackingWSResult actionResult) {
        super.onPostExecute(actionResult);

        mListener.onPostExecuteResult(actionResult);
    }

    /**
     * Trả về
     */
    public interface IAddAttendanceListener {
        void onPostExecuteResult(PostAttendanceTrackingWSResult result);
    }
}