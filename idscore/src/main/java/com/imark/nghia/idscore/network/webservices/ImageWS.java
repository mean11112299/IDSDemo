package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.imark.nghia.idscore.data.ImageData;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.network.webservices.models.PostImgWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/14/2015.
 */
public class ImageWS {

    private static final String TAG = ImageWS.class.getName();

    public static PostImgWSResult post(Image img){

        String strCreateAt = ConvertUtil.Calendar2String(img.getCreateAt(), WSConfig.FORMAT_POST_TIME, null);

        ArrayList<UrlParam> listParam = new ArrayList<UrlParam>();
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", img.getSessionCode()+""));
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", img.getCreateBy()+""));
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pDateTimeDevice", strCreateAt));

        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pLatGPS", img.getLatitude()));
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pLongGPS", img.getLongitude()));
        listParam.add(new UrlParam(UrlParam.ParamType.TEXT, "pFileName", img.getFileName()));
        listParam.add(new UrlParam(UrlParam.ParamType.FILE, "file", img.getFilePath()));

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.FORM_DATA,
                WSConfig.PATH_POST_IMAGE,
                listParam);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, PostImgWSResult.class);
    }


    /**
     * Post Update List Image
     * @param listImg
     */
    public static BatchActionResult postAndUpdate(Context context, ArrayList<Image> listImg) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (Image img : listImg) {

            PostImgWSResult piResult = ImageWS.post(img);
            if (piResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || piResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                ImageData.update_UploadStatus_ServerId_byId(
                        context, img.get_id(), UploadStatus.UPLOADED, piResult.getImageID()
                );

                batchResult.addSuccess(1);
            } else {
                batchResult.addFail(1);
            }
        }

        return batchResult;
    }
}
