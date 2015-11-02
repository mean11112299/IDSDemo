package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imark.nghia.idscore.data.AssignImageData;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.RawHeaderType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.AssignImage;
import com.imark.nghia.idscore.network.webservices.models.PostAssignImgWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class AssignImgWS {

    private static final String TAG = AssignImgWS.class.getName();

    public static PostAssignImgWSResult post(final AssignImage assignImage, final long assignServerId, final long imageServerId){

        ArrayList<UrlParam> listParam = new ArrayList<UrlParam>(){{
          add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", assignImage.getSessionCode()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pImageID", imageServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pImageType", assignImage.getImageType()+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_ASSIGN_IMAGE,
                listParam
        );

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, PostAssignImgWSResult.class);
    }


    /**
     * Post và Update lại List Assign Image
     *
     * @param listAssignImg
     */
    public static BatchActionResult postAndUpdate(Context context, ArrayList<AssignImage> listAssignImg) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (AssignImage assignImage : listAssignImg) {
            boolean isSuccess = false;  // đánh dấu đã thành công, không thêm thất bại

            // kiểm tra Assign vs Image đã Upload
            Assign assign = assignImage.getAssign(context);
            Image image = assignImage.getImage(context);
            if (assign.getUploadStatus() == UploadStatus.UPLOADED
                    && image.getUploadStatus() == UploadStatus.UPLOADED) {

                PostAssignImgWSResult paiResult = AssignImgWS.post(assignImage, assign.getServerId(), image.getServerId());
                if (paiResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                        || paiResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                    AssignImageData.update_UploadStatus_ServerId_byId(
                            context, assignImage.get_id(), UploadStatus.UPLOADED, paiResult.getAssign_ImageID()
                    );

                    isSuccess = true;
                }
            }

            if (isSuccess) batchResult.addSuccess(1);
            else batchResult.addFail(1);
        }
        return batchResult;
    }
}
