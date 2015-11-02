package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.imark.nghia.idscore.data.ProductShownData;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.models.Product;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.ProductShown;
import com.imark.nghia.idscore.network.webservices.models.PostProductShownWSResult;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/18/2015.
 */
public class ProductShownWS {
    private static final String TAG = ProductShownWS.class.getName();

    public static PostProductShownWSResult post(final ProductShown productShown, final long assignServerId,
                                                final long productServerId) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", productShown.getSessionCode()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", productShown.getCreateBy()+""));

            add(new UrlParam(UrlParam.ParamType.TEXT, "pAssignID", assignServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductID", productServerId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pNumber", productShown.getNumber()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pPrice", productShown.getRetailPrice()+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_PRODUCT_SHOWN,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostProductShownWSResult.class);
    }

    /**
     * Post và Update lại Product Shown
     * @param listProductShown
     * @return
     */
    public static BatchActionResult postAndUpdate(Context context, ArrayList<ProductShown> listProductShown) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (ProductShown productShown : listProductShown) {

            boolean isSuccess = false;  // đánh dấu đã thành công, không thêm thất bại

            // kiểm tra Assign vs Product đã Upload
            Assign assign = productShown.getAssign(context);
            Product product = productShown.getProduct(context);
            if (assign.getUploadStatus() == UploadStatus.UPLOADED
                    && product.getUploadStatus() == UploadStatus.UPLOADED) {

                PostProductShownWSResult ppsResult = ProductShownWS.post(productShown, assign.getServerId(), product.getServerId());
                if (ppsResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                        || ppsResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                    ProductShownData.update_UploadStatus_ServerId_byId(
                            context, productShown.get_id(), UploadStatus.UPLOADED, ppsResult.getProductShownID()
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
