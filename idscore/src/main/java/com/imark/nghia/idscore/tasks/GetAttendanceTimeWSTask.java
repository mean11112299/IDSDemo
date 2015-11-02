package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.AttendanceWS;
import com.imark.nghia.idscore.network.webservices.models.GetAttendanceTimeWSResult;

/**
 * Created by Nghia-PC on 9/11/2015.
 */
public class GetAttendanceTimeWSTask extends AsyncTask<Void, Void, GetAttendanceTimeWSResult> {


    private final Context mContext;
    private final long userId;
    private final ResultListener mListener;

    public GetAttendanceTimeWSTask(Context context, long userId, ResultListener listener){
        this.mContext = context;
        this.userId = userId;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected GetAttendanceTimeWSResult doInBackground(Void... voids) {
        try{

            return AttendanceWS.getAttendanceTime(userId);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return new GetAttendanceTimeWSResult();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(GetAttendanceTimeWSResult attendanceTimeGet) {
        if(mListener != null)
            mListener.onPostExecuteResult(attendanceTimeGet);
    }


    public interface ResultListener {
        public void onPostExecuteResult(GetAttendanceTimeWSResult attendanceTimeGet);
    }
}
