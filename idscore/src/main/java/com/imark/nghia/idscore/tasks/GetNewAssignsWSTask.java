package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.imark.nghia.idscore.helper.Constants;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetNewAssignsWSResult;
import com.imark.nghia.idscore.network.webservices.AssignWS;
import com.imark.nghia.idscore.network.webservices.WSConfig;

import java.util.Calendar;

/**
 * Created by Nghia-PC on 9/15/2015.
 */
public class GetNewAssignsWSTask extends AsyncTask<Void, Void, ActionResult> {

    private static final String TAG = GetNewAssignsWSTask.class.getName();
    private final ITaskResultListener mListener;
    private Context mContext;
    private long mUserId;

    public GetNewAssignsWSTask(Context mContext, long mUserId, ITaskResultListener listener) {
        this.mContext = mContext;
        this.mUserId = mUserId;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ActionResult doInBackground(Void... voids) {
        /* Flow:
        Gọi WS, lấy về Assign
        Kiểm tra từng Assign mới:
            nếu: Đã có ServerID:
                Nếu Local UPLOADED => DOING
                Cập nhật lại dòng hiện tại
            nếu: Ngược lại:
                Thêm mới Assign (NEW)
         */

        ActionResult resultAction = new ActionResult(ActionResult.ResultStatus.FAIL, "");
        try{

            GetNewAssignsWSResult wsResult = AssignWS.getNewAssigns(mUserId);
            if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE){

                resultAction.setResult(ActionResult.ResultStatus.SUCCESS);  // trả về thành công

                // So trùng ServerId
                for (GetNewAssignsWSResult.SimpleOutlet newAssign: wsResult.getListSimpleOutlet()){
                    Assign oldAssign = AssignData.getByServerId(mContext, newAssign.getAssignID());

                    if (oldAssign != null){
                        // Assign Exist => Update, (UPLOADED => DOING)

                        long rowUpdate = updateAssign(newAssign, oldAssign);
                        Log.e(TAG, "row update =" + rowUpdate);

                    } else {
                        // Assign not Exist => Add New (NEW)

                        Calendar expiredAt = ConvertUtil.String2Canendar(
                                newAssign.getDateAssign(),
                                WSConfig.FORMAT_POST_TIME,
                                null, Calendar.getInstance());
                        Assign assign = new Assign(
                                Constants.DEFAULT_ID,
                                newAssign.getAssignID(),
                                Constants.DEFAULT_SESSION_CODE,
                                mUserId,
                                Calendar.getInstance(),
                                UploadStatus.UPLOADED,
                                expiredAt,
                                Assign.WorkStatus.NEW,
                                newAssign.getOutletName(),
                                newAssign.getCity(),
                                newAssign.getDistrict(),
                                newAssign.getAreaID(),
                                newAssign.getAddress(),
                                Constants.DEFAULT_LATITUDE,
                                Constants.DEFAULT_LONGITUDE
                        );
                        long addId = AssignData.add(mContext, assign);
                        Log.e(TAG, "add id =" + addId);
                    }
                }
            }
            resultAction.setMessage(wsResult.getDescription());

        } catch (Exception ex){
            ex.printStackTrace();
            resultAction.setMessage(ex.getMessage());
        }
        return resultAction;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ActionResult actionResult) {
        super.onPostExecute(actionResult);

        mListener.onPostExecuteResult(actionResult);
    }



    /** Cập nhật Assign từ newAssign, đổi trạng thái FINISH => DOING */
    private long updateAssign(GetNewAssignsWSResult.SimpleOutlet newAssign, Assign oldAssign) {

        Calendar expiredAt = ConvertUtil.String2Canendar(
                newAssign.getDateAssign(),
                WSConfig.FORMAT_POST_TIME,
                null, Calendar.getInstance());

        oldAssign.setCreateBy(mUserId);
        oldAssign.setExpiredAt(expiredAt);
        oldAssign.setOutletName(newAssign.getOutletName());
        oldAssign.setOutletCity(newAssign.getCity());
        oldAssign.setOutletDistrict(newAssign.getDistrict());
        oldAssign.setOutletAreaId(newAssign.getAreaID());
        oldAssign.setOutletAddress(newAssign.getAddress());

        if (oldAssign.getWorkStatus() == Assign.WorkStatus.FINISHED)
            oldAssign.setWorkStatus(Assign.WorkStatus.DOING);

        return AssignData.updateAllById(mContext, oldAssign.get_id(), oldAssign);
    }

}
