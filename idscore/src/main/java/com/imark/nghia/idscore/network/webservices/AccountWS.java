package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.windyroad.nghia.common.ConvertUtil;
import com.windyroad.nghia.common.network.FormMIMEType;
import com.imark.nghia.idscore.network.webservices.models.BaseWSResult;
import com.imark.nghia.idscore.network.webservices.models.LoginWSResult;
import com.windyroad.nghia.common.network.UrlParam;
import com.windyroad.nghia.common.network.WebserviceUtil;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nghia-PC on 9/10/2015.
 * Tài khoản Web API
 */
public class AccountWS {

    private static final String TAG = AccountWS.class.getName();

    public static BaseWSResult changePassword(final long userId, final String oldPassword, final String newPassword) {
        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserTeamID", userId+""));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pOldPass", oldPassword));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pNewPass", newPassword));
        }};
        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_CHANGE_PASSWORD,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG + " changePassword", strData);

        return new Gson().fromJson(strData, BaseWSResult.class);
    }

    public static LoginWSResult login(final String username, final String password) {

        ArrayList<UrlParam> listParams = new ArrayList<UrlParam>() {{
            add(new UrlParam(UrlParam.ParamType.TEXT, "pAppCode", WSConfig.APP_CODE));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pUserName", username));
            add(new UrlParam(UrlParam.ParamType.TEXT, "pPassWord", password));
        }};
        InputStream inputStream = WebserviceUtil.sendPostForm(
                FormMIMEType.X_WWW_FORM_URLENDCODED,
                WSConfig.PATH_POST_LOGIN,
                listParams);

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG + " login", strData);

        return new Gson().fromJson(strData, LoginWSResult.class);
    }
}
