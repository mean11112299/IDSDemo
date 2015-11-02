package com.imark.nghia.idscore.network.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.imark.nghia.idscore.data.ProductData;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.models.BatchActionResult;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.windyroad.nghia.common.network.UploadStatus;
import com.windyroad.nghia.common.network.WebserviceUtil;
import com.imark.nghia.idscore.data.models.Product;
import com.imark.nghia.idscore.network.webservices.models.GetProductsWSResult;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.QueryStringUtil;
import com.imark.nghia.idscore.network.webservices.models.PostProductWSResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghia-PC on 9/10/2015.
 */
public class ProductWS {
    private static String TAG = ProductWS.class.getName();

    public static GetProductsWSResult getAll() throws IOException {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "AppCode", WSConfig.APP_CODE));
        }};
        String strUrl = WSConfig.PATH_GET_PRODUCT + "?" + QueryStringUtil.parseQuery(listParams);
        URL url = new URL(strUrl);
        InputStream inputStream = url.openStream();  // đặt inputStream sẽ null???

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, GetProductsWSResult.class);
    }

    public static PostProductWSResult post(final Product product) {
        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pSessionCode", product.getSessionCode()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", product.getCreateBy()+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductName", product.getName()));
            //TODO: HỎI SERVER Về điều này ặc
            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductType", "1"));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductColor", product.getColor()));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pProductPrice", product.getPrice()+""));
        }};

        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_ADD_PRODUCT,
                listParams);

        String strValue = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strValue);

        return new Gson().fromJson(strValue, PostProductWSResult.class);
    }

    /**
     * Post, update list Product
     *
     * @param listProduct
     * @return all postAttendance success
     */
    public static BatchActionResult postAndUpdate(Context context, List<Product> listProduct) {
        BatchActionResult batchResult = new BatchActionResult(0, 0);  // tính thành công, thất bại

        for (Product product : listProduct) {

            PostProductWSResult ppResult = ProductWS.post(product);
            if (ppResult.getStatus() == BaseWSResult.STATUS_SUCCESS
                    || ppResult.getStatus() == BaseWSResult.STATUS_DUPLICATE) {

                ProductData.update_UploadStatus_ServerId_byId(
                        context, product.get_id(), UploadStatus.UPLOADED, ppResult.getProductID()
                );

                batchResult.addSuccess(1);
            } else {
                batchResult.addFail(1);
            }
        }

        return batchResult;
    }
}
