package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.network.webservices.AssignWS;
import com.imark.nghia.idscore.network.webservices.ProductWS;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.Product;

import java.util.List;

/**
 * Created by Nghia-PC on 9/21/2015.
 */
public class PostListAssignWSTask extends AsyncTask<Void, ActionResult, ActionResult> {


    private final Context mContext;
    private final List<Assign> mListAssign;
    private final TaskListener mListener;
    private final BatchActionResult mProgressResult;

    public PostListAssignWSTask(Context context, List<Assign> listAssign, TaskListener listener) {
        this.mContext = context;
        this.mListAssign = listAssign;
        this.mListener = listener;
        mProgressResult = new BatchActionResult(mListAssign.size());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ActionResult doInBackground(Void... voids) {

        // ---- Post Only Product, vì không có bảng cha
        List<Product> listProduct;
        listProduct = ProductData.getByUploadStatus(mContext, UploadStatus.WAITING_UPLOAD);  // lấy hết Product
        //Product
        ProductWS.postAndUpdate(mContext, listProduct);

        // Upload từng Assign
        for (Assign assign : mListAssign){
            ActionResult paResult = AssignWS.postAllData(mContext, assign.get_id());
            publishProgress(paResult);
        }

        return new ActionResult(ActionResult.ResultStatus.SUCCESS, "");
    }

    @Override
    protected void onProgressUpdate(ActionResult... values) {
        super.onProgressUpdate(values);

        // Tính giá trị trả về, thêm thành công, thất bại
        if (values[0].getResult() == ActionResult.ResultStatus.SUCCESS){
            mProgressResult.addSuccess(1);
        } else {
            mProgressResult.addFail(1);
        }

        // trả về Listener
        mListener.onProgressUpdate(mProgressResult);
    }

    @Override
    protected void onPostExecute(ActionResult result) {
        super.onPostExecute(result);

        mListener.onPostExecute(result);
    }

    /** Lắng nghe hành động trả về */
    public interface TaskListener {
        /** Update giá trị */
        public void onProgressUpdate(BatchActionResult batchResult);
        /** lấy kết quả hành đổng */
        public void onPostExecute(ActionResult result);
    }
}
