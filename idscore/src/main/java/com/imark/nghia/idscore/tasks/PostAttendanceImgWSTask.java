package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.AttendanceImgData;
import com.imark.nghia.idscore.data.ImageData;
import com.imark.nghia.idscore.data.models.AttendanceImg;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAttendanceImgWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostImgWSResult;
import com.imark.nghia.idscore.network.webservices.AttendanceImgWS;
import com.imark.nghia.idscore.network.webservices.ImageWS;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class PostAttendanceImgWSTask extends AsyncTask<Void, Void, PostAttendanceImgWSResult> {


    private final Context mContext;
    private final IResultListener mListener;
    private final long mAttendanceImgId;

    public PostAttendanceImgWSTask(Context context, long attendanceImgId, IResultListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mAttendanceImgId = attendanceImgId;
    }

    @Override
    protected PostAttendanceImgWSResult doInBackground(Void... voids) {
        // Flow:
        // Lấy attendanceServerId, lấy Image
        // Upload Image, Cập nhật trạng thái, ServerId Image
        // Upload ATI, Cập nhật trạng thái, ServerId
        // Trả về kết quả

        // Ghi chú: Không kiểm tra update Thành công, vì gởi lại, server tự kiểm tra =))

        PostAttendanceImgWSResult resultValue = new PostAttendanceImgWSResult(BaseWSResult.STATUS_UNKNOWN, "", 0);

        try{
            AttendanceImg ati = AttendanceImgData.getById(mContext, mAttendanceImgId);
            Image img = ati.getImage(mContext);

            //--- Upload Image----
            PostImgWSResult wsResult = ImageWS.post(img);
            if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE){

                long imgServerId = wsResult.getImageID();
                ImageData.update_UploadStatus_ServerId_byId(mContext, img.get_id(),
                        UploadStatus.UPLOADED, imgServerId);

                //---- Upload ATI----
                resultValue = AttendanceImgWS
                        .postAttendanceImg(ati.getSessionCode(), ati.getAttendance(mContext).getServerId(), imgServerId);
                if (resultValue.getStatus() == BaseWSResult.STATUS_SUCCESS
                        || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE){

                    AttendanceImgData.update_UploadStatus_ServerId_byId(mContext, ati.get_id(),
                            UploadStatus.UPLOADED, resultValue.getAttendance_ImageID());
                }
            } else {

                resultValue.setDescription(wsResult.getDescription());
            }

        } catch (Exception ex){
            ex.printStackTrace();

            resultValue.setDescription(ex.getMessage());
        }
        return resultValue;
    }

    @Override
    protected void onPostExecute(PostAttendanceImgWSResult result) {
        super.onPostExecute(result);

        mListener.onPostExecute(result);
    }

    public interface IResultListener{
        public void onPostExecute(PostAttendanceImgWSResult result);
    }
}
