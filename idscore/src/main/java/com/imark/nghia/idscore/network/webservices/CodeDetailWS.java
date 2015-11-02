package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.imark.nghia.idscore.network.webservices.models.GetCodeDetailsWSResult;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.WebserviceUtil;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/10/2015.
 */
public class CodeDetailWS {

    private static final String TAG = CodeDetailWS.class.getName();

    public static GetCodeDetailsWSResult getByGroupCode(final String groupCode) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pGroupCode", groupCode));
        }};
        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_GET_CODE_DETAIL,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, GetCodeDetailsWSResult.class);
    }
}
