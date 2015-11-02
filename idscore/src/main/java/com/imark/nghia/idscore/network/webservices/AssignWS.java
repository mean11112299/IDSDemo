package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.data.AssignImageData;
import com.imark.nghia.idscore.data.CommentData;
import com.imark.nghia.idscore.data.ImageData;
import com.imark.nghia.idscore.data.OutletData;
import com.imark.nghia.idscore.data.ProductShownData;
import com.imark.nghia.idscore.data.ProductShownImgData;
import com.imark.nghia.idscore.data.models.AssignImage;
import com.imark.nghia.idscore.data.models.Comment;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.data.models.Outlet;
import com.imark.nghia.idscore.data.models.ProductShown;
import com.imark.nghia.idscore.data.models.ProductShownImg;
import com.imark.nghia.idscore.helper.Constants;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.ActionResult;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.GetNewAssignsWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostAssignWSResult;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nghia-PC on 9/15/2015.
 */
public class AssignWS {

    private static final String TAG = AssignWS.class.getName();

    public static GetNewAssignsWSResult getNewAssigns(final long userId) {
        List<UrlParam> listParam = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", userId + ""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_GET_ASSIGN_OUTLET,
                listParam);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, GetNewAssignsWSResult.class);
    }

    public static PostAssignWSResult post(final Assign assign) {

        final String createAt = ConvertUtil.Calendar2String(assign.getCreateAt(), WSConfig.FORMAT_POST_TIME, null);

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", assign.getSessionCode()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", assign.getCreateBy()+""));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pOutletName", assign.getOutletName()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pDistrict", assign.getOutletDistrict()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pCity", assign.getOutletCity()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAddress", assign.getOutletAddress()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAreaID", assign.getOutletAreaId()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pDateDevice", createAt));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pLatGPS", assign.getOutletLatitude()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pLongGPS", assign.getOutletLongitude()+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_ASSIGN_OUTLET,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostAssignWSResult.class);
    }

    public static BaseWSResult postComplete(final long assignServerId) {
        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {
            {
                add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
                add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
            }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ASSIGN_COMPLETE,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, BaseWSResult.class);
    }

    /**
     * Post và Update lại Assign
     * @param assign
     * @return true thành công
     */
    public static ActionResult postAndUpdate(Context mContext, Assign assign) {
        PostAssignWSResult paResult = AssignWS.post(assign);
        if (paResult.getStatus() == BaseWSResult.STATUS_SUCCESS ||
                paResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

            AssignData.update_UploadStatus_ServerId_byId(mContext, assign.get_id(), UploadStatus.UPLOADED, paResult.getAssignID());

            OutletData.add(mContext, new Outlet(
                    Constants.DEFAULT_ID,
                    paResult.getOutletID(),
                    Constants.DEFAULT_SESSION_CODE,
                    assign.getCreateBy(),
                    Calendar.getInstance(),
                    UploadStatus.UPLOADED,
                    assign.getOutletName(),
                    assign.getOutletDistrict(),
                    assign.getOutletCity(),
                    assign.getOutletAddress(),
                    Constants.DEFAULT_LATITUDE,
                    Constants.DEFAULT_LONGITUDE
            ));  // add Outlet để sử dụng lại

            return new ActionResult(ActionResult.ResultStatus.SUCCESS, "");
        }

        return new ActionResult(ActionResult.ResultStatus.FAIL, paResult.getDescription());
    }

    /**
     * Post Complete và update lại
     * @param context
     * @param assign
     * @return
     */
    public static ActionResult postStatusCompleteAndUpdate(Context context, Assign assign) {
        BaseWSResult wsResult = AssignWS.postComplete(assign.getServerId());
        if (wsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                || wsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE){

            AssignData.changeStatus(context, assign.get_id(), Assign.WorkStatus.FINISHED);
            return  new ActionResult(ActionResult.ResultStatus.SUCCESS, "");
        }
        return new ActionResult(ActionResult.ResultStatus.FAIL, wsResult.getDescription());
    }

    /** Post Toàn bộ dữ liệu liên quan, hàm update chính */
    public static ActionResult postAllData(Context context, long assignId){
        /*
        Post Assign
        Post Comment
        Post img = Assign Img
        Post Product Shown
            -> img = Product Shown Img

        Thành công tất cả => ASSIGN => Hoàn thành
        NOTE: Post finish => Đổi Post_STATUS, SERVER_ID nếu trả về
        // Dữ liệu đã tồn tại, server tự cập nhật
         */

        ActionResult resultFinal = new ActionResult(ActionResult.ResultStatus.FAIL, "");

        //--- Post theo Tuần tự, bảng cha trước đến bảng con -------
        //region lấy dữ liệu liên quan
        ArrayList<Image> listImg = new ArrayList<>();
        Assign assign;
        ArrayList<Comment> listComment;
        ArrayList<AssignImage> listAssignImg;
        ArrayList<ProductShown> listProductShown;
        ArrayList<ProductShownImg> listProductShownImg = new ArrayList<>();

        //--- Lấy giá trị cần Post từ db ------
        assign = AssignData.getById(context, assignId);  // get Assign hiện tại
        listComment = CommentData.getBy_AssignId_UploadStatus(context, assign.get_id(), UploadStatus.WAITING_UPLOAD);
        listAssignImg = AssignImageData.getBy_AssignId_UploadStatus(context, assign.get_id(), UploadStatus.WAITING_UPLOAD);
        listProductShown = ProductShownData.getBy_AssignId_UploadStatus(context, assign.get_id(), UploadStatus.WAITING_UPLOAD);

        for (AssignImage ai : listAssignImg) {
            Image img = ImageData.getById(context, ai.getImageId());
            listImg.add(img);  // thêm ảnh
        }

        for (ProductShown ps : listProductShown) {

            ArrayList<ProductShownImg> listPSI = ProductShownImgData.getBy_AssignId_UploadStatus(
                    context, ps.get_id(), UploadStatus.WAITING_UPLOAD
            );
            listProductShownImg.addAll(listPSI);

            for (ProductShownImg psi : listPSI) {
                Image img1 = ImageData.getById(context, psi.getImageId());
                listImg.add(img1);  // thêm ảnh
            }
        }
        //endregion

        //--- Post giá trị lấy được lên WS ---
        // biến kiểm tra thành công, thất bại
        ActionResult actionAssign = new ActionResult(ActionResult.ResultStatus.SUCCESS, "");
        BatchActionResult actionComments, actionAssignImgs, actionProductShowns, actionProductShowImgs;
        actionComments = actionAssignImgs = actionProductShowns = actionProductShowImgs = null;

        //Image
        ImageWS.postAndUpdate(context, listImg);

        //Assign
        if (assign.getUploadStatus() == UploadStatus.WAITING_UPLOAD) {
            actionAssign = AssignWS.postAndUpdate(context, assign);
            // load lại Assign Để sử dụng
            if (actionAssign.getResult() == ActionResult.ResultStatus.SUCCESS){
                assign = AssignData.getById(context, assign.get_id());
            }
        }

        // tách ra vì Có thể Assign Đã uploaded
        if (actionAssign.getResult() == ActionResult.ResultStatus.SUCCESS) {

            //Comment
            actionComments = CommentWS.postAndUpdate(context, listComment);

            //Assign Image
            actionAssignImgs = AssignImgWS.postAndUpdate(context, listAssignImg);

            //Product Shown
            actionProductShowns = ProductShownWS.postAndUpdate(context, listProductShown);

            //Product Shown Image
            actionProductShowImgs = ProductShownImgWS.postAndUpdate(context, listProductShownImg);

            // --- Lấy kết quả ---
            int totalFail = actionComments.getTotalFail() + actionAssignImgs.getTotalFail()
                    + actionProductShowns.getTotalFail() + actionProductShowImgs.getTotalFail();
            if (totalFail <= 0){
                // Thành công hoàn toàn, kết quả cuối cùng => Post complete

                resultFinal = AssignWS.postStatusCompleteAndUpdate(context, assign);
            }

        } else {
            resultFinal = new ActionResult(ActionResult.ResultStatus.FAIL, actionAssign.getMessage());
        }
        return resultFinal;
    }
}
