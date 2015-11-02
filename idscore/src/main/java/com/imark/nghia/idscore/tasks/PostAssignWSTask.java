package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.AssignWS;
import com.imark.nghia.idscore.network.webservices.ProductWS;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idscore.data.models.Product;

import java.util.List;

/**
 * Created by Nghia-PC on 9/17/2015.
 */
public class PostAssignWSTask extends AsyncTask<Void, Void, ActionResult> {

    private final ITaskResultListener mResultListener;
    private final long mUserId;
    private Context mContext;
    private long mAssignId;

    public PostAssignWSTask(Context context, long assigId, long userId, ITaskResultListener resultListener) {
        this.mContext = context;
        this.mUserId = userId;
        this.mAssignId = assigId;
        this.mResultListener = resultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ActionResult doInBackground(Void... voids) {
        /* Flow:
        Post Product
        Post Assign
        Post Comment
        Post img = Assign Img
        Post Product Shown
            -> img = Product Shown Img
            
        Thành công tất cả => ASSIGN => Hoàn thành
        NOTE: Post finish => Đổi Post_STATUS, SERVER_ID nếu trả về 
        // Dữ liệu đã tồn tại, server tự cập nhật */

        ActionResult resultFinal = new ActionResult(ActionResult.ResultStatus.FAIL, "");

        try {

            // ---- Post Only Product, vì không có bảng cha
            List<Product> listProduct;
            listProduct = ProductData.getByUploadStatus(mContext, UploadStatus.WAITING_UPLOAD);  // lấy hết Product
            ProductWS.postAndUpdate(mContext, listProduct);

            resultFinal = AssignWS.postAllData(mContext, mAssignId);

        } catch (Exception ex) {
            ex.printStackTrace();
            resultFinal = new ActionResult(ActionResult.ResultStatus.FAIL, ex.getMessage());
        }
        return resultFinal;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(ActionResult actionResult) {
        super.onPostExecute(actionResult);

        mResultListener.onPostExecuteResult(actionResult);
    }
}
