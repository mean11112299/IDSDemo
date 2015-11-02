package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.models.PostAttendanceTrackingWSResult;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.AttendanceWS;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 8/29/2015.
 */
public class PostAttendanceWSTask extends AsyncTask<Void, Void, PostAttendanceTrackingWSResult> {

    private final Context mContext;
    private final IAddAttendanceListener mListener;
    private final String mSessionCode;
    private final long mUserId;
    private final String mTimePointType;
    private final String mLatitude;
    private final String mLongitude;
    private final Calendar mAttendanceTime;

    public PostAttendanceWSTask(
            Context context, String sessionCode, long userId, String timePointType, Calendar attendanceTime,
            String latitude, String longitude, IAddAttendanceListener resultListener){

        this.mContext = context;
        this.mListener = resultListener;
        this.mAttendanceTime = attendanceTime;
        this.mSessionCode = sessionCode;
        this.mUserId = userId;
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

            return AttendanceWS.postAttendance(mUserId, mSessionCode, mAttendanceTime,
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

    /** Trả về */
    public interface IAddAttendanceListener {
        void onPostExecuteResult(PostAttendanceTrackingWSResult result);
    }
}
