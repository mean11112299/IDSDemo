package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.ProductShownImgData;
import com.imark.nghia.idscore.data.models.Image;
import com.imark.nghia.idscore.data.models.ProductShown;
import com.imark.nghia.idscore.data.models.ProductShownImg;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.PostProductShownImgWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class ProductShownImgWS {

    private static final String TAG = ProductShownImgWS.class.getName();

    public static PostProductShownImgWSResult post(final ProductShownImg productShownImg, final long productShownServerId,
                                                final long imageServerId) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", productShownImg.getSessionCode()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", productShownImg.getCreateBy()+""));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductShownID", productShownServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pImageID", imageServerId+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_PRODUCT_SHOWN_IMG,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostProductShownImgWSResult.class);
    }


    /**
     * Post và Update lại Product Shown Image
     * @param listProductShownImg
     * @return
     */
    public static BatchActionResult postAndUpdate(Context context, ArrayList<ProductShownImg> listProductShownImg) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (ProductShownImg psi : listProductShownImg) {

            boolean isSuccess = false;  // đánh dấu đã thành công, không thêm thất bại

            // kiểm tra Product Shown vs Image đã Upload
            ProductShown productShown = psi.getProductShow(context);
            Image image = psi.getImage(context);
            if (productShown.getUploadStatus() == UploadStatus.UPLOADED
                    && image.getUploadStatus() == UploadStatus.UPLOADED) {

                PostProductShownImgWSResult ppsiResult = ProductShownImgWS
                        .post(psi, productShown.getServerId(), image.getServerId());
                if (ppsiResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                        || ppsiResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                    ProductShownImgData.update_UploadStatus_ServerId_byId(
                            context, psi.get_id(), UploadStatus.UPLOADED, ppsiResult.getProductShown_ImageID()
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
