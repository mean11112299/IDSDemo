package com.imark.nghia.idscore.network.webservices;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.windyroad.nghia.common.ConvertUtil;
import com.imark.nghia.idscore.network.webservices.models.GetAreaWSResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Nghia-PC on 9/10/2015.
 */
public class AreaWS {
    private static final String TAG = AreaWS.class.getName();

    public static List<GetAreaWSResult> getAll() throws IOException {

        URL url = new URL(WSConfig.PATH_GET_AREA);
        InputStream inputStream = url.openStream();  // đặt inputStream sẽ null???

        String strData = ConvertUtil.Stream2String(inputStream);
        Log.e(TAG, strData);

        return new Gson().fromJson(strData, new TypeToken<List<GetAreaWSResult>>(){}.getType());
    }
}
