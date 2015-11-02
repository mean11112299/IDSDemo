package com.imark.nghia.idscore.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imark.nghia.idscore.helper.Constants;
import com.windyroad.nghia.common.ConvertUtil;
import com.imark.nghia.idscore.data.AreaData;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.BaseDataHandle;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idscore.data.models.Area;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.CodeDetail;
import com.imark.nghia.idscore.data.CodeDetailData;
import com.imark.nghia.idscore.data.models.Outlet;
import com.imark.nghia.idscore.data.OutletData;
import com.imark.nghia.idscore.data.models.Product;
import com.imark.nghia.idscore.network.webservices.models.GetAreaWSResult;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetCodeDetailsWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetNewAssignsWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetOutletsWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetProductsWSResult;
import com.imark.nghia.idscore.network.webservices.AreaWS;
import com.imark.nghia.idscore.network.webservices.AssignWS;
import com.imark.nghia.idscore.network.webservices.CodeDetailWS;
import com.imark.nghia.idscore.network.webservices.OutletWS;
import com.imark.nghia.idscore.network.webservices.ProductWS;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.imark.nghia.idscore.network.webservices.WSConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nghia-PC on 8/27/2015.
 * Lấy dữ liệu trên server lần đầu tiên chạy
 */
public class InitFirstDataWSTask extends AsyncTask<Void, Void, ActionResult> {

    private final Context mContext;
    private final ITaskResultListener mListener;
    private final long mUserId;

    public InitFirstDataWSTask(Context context, long userId, ITaskResultListener listener) {
        this.mContext = context;
        this.mUserId = userId;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ActionResult doInBackground(Void... params) {
        /*Flow
            Reset dữ liệu
            Get code Detail
            Get Area
            Get Outlet
            Get Product
            Get Assign
         */
        try {

            BaseDataHandle.resetAllTable(mContext);
            getCodeDetailData();
            getAreaData();
            getOutletData();
            getProductData();
            getAssignData();

            return new ActionResult(ActionResult.ResultStatus.SUCCESS, "");

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ActionResult(ActionResult.ResultStatus.FAIL, ex.getMessage());
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ActionResult result) {
        super.onPostExecute(result);

        mListener.onPostExecuteResult(result);
    }

    /**
     * Lấy dữ liệu Code Detail
     **/
    private void getCodeDetailData() throws IOException {

        // ----- Lấy Image Type -----
        GetCodeDetailsWSResult wsResult = CodeDetailWS.getByGroupCode(Constants.GROUP_CODE_IMAGE_TYPE);
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            // Thành công, thêm list dữ liệu CodeDetail

            for (GetCodeDetailsWSResult.SimpleCodeDetail simpleCodeDetail : wsResult.getListSimpleCodeDetail()) {
                // đổi thành CodeDetail, add Database
                CodeDetail codeDetail = new CodeDetail(
                        simpleCodeDetail.getGroupCode(),
                        simpleCodeDetail.getName(),
                        simpleCodeDetail.getValue(),
                        simpleCodeDetail.getOrdinal(),
                        ""
                );
                CodeDetailData.add(mContext, codeDetail);
            }
        }
    }

    /**
     * Lấy dữ liệu Outlet
     **/
    private void getOutletData() throws IOException {

        // ----- Lấy Outlet -----
        GetOutletsWSResult wsResult = OutletWS.getAll();
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            // Thành công, thêm list dữ liệu Outlet

            for (GetOutletsWSResult.SimpleOutlet simpleOutlet : wsResult.getListSimpleOutlet()) {
                // đổi thành Outlet, add Database

                Outlet Outlet = new Outlet(
                        Constants.DEFAULT_ID,
                        Integer.valueOf(simpleOutlet.getOutletID()),
                        simpleOutlet.getSessionCode(),
                        mUserId,
                        Calendar.getInstance(),
                        UploadStatus.UPLOADED,
                        simpleOutlet.getOutletName(),
                        simpleOutlet.getOutletDistrict(),
                        simpleOutlet.getOutletCity(),
                        simpleOutlet.getAddress(),
                        Constants.DEFAULT_LATITUDE,
                        Constants.DEFAULT_LONGITUDE
                );
                OutletData.add(mContext, Outlet);
            }
        }
    }

    /**
     * Lấy dữ liệu Product
     **/
    private void getProductData() throws IOException {

        // ----- Lấy Product -----
        GetProductsWSResult wsResult = ProductWS.getAll();
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {
            // Thành công, thêm list dữ liệu Product

            for (GetProductsWSResult.SimpleProduct simpleProduct : wsResult.getListSimpleProduct()) {
                // đổi thành Product, add Database

                Product product = new Product(
                        Constants.DEFAULT_ID,
                        simpleProduct.getID(),
                        Constants.DEFAULT_SESSION_CODE,
                        mUserId,
                        Calendar.getInstance(),
                        UploadStatus.UPLOADED,
                        simpleProduct.getProductName(),
                        simpleProduct.getColor(),
                        Long.valueOf(simpleProduct.getPrice())
                );
                ProductData.add(mContext, product);
            }
        }
    }

    /**
     * Lấy dữ liệu Vùng miền
     */
    private void getAreaData() throws IOException {

        // ----- Lấy -----
        List<GetAreaWSResult> listAreaWSGet = AreaWS.getAll();
        for (GetAreaWSResult areaWSGet : listAreaWSGet) {
            // đổi thành, add Database

            Area area = new Area(
                Constants.DEFAULT_ID,
                    areaWSGet.getID(),
                    Constants.DEFAULT_SESSION_CODE,
                    mUserId,
                    Calendar.getInstance(),
                    UploadStatus.UPLOADED,
                    areaWSGet.getName(),
                    areaWSGet.getParentID(),
                    areaWSGet.getAreaLevel()
            );
            AreaData.add(mContext, area);
        }
    }

    public void getAssignData() {
        GetNewAssignsWSResult wsResult = AssignWS.getNewAssigns(mUserId);
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE){
            // add data

            for (GetNewAssignsWSResult.SimpleOutlet newAssign : wsResult.getListSimpleOutlet()) {

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
                AssignData.add(mContext, assign);
            }
        }
    }
}

    