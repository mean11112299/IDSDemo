package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.windyroad.nghia.common.ConvertUtil;
import com.imark.nghia.idscore.network.webservices.models.GetOutletsWSResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Nghia-PC on 9/10/2015.
 */
public class OutletWS {
    private static final String TAG = OutletWS.class.getName();
    private static GetOutletsWSResult all;

    public static GetOutletsWSResult getAll() throws IOException {

        String strUrl = WSConfig.PATH_GET_OUTLET + "?AppCode=" + WSConfig.APP_CODE;
        URL url = new URL(strUrl);
        InputStream inputStream = url.openStream();  // đặt inputStream sẽ null???

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, GetOutletsWSResult.class);
    }
}
