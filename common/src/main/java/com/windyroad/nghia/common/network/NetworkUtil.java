package com.windyroad.nghia.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Nghia-PC on 9/16/2015.
 */
public class NetworkUtil {
    /**
     * Lấy phương thức kết nối Mạng
     * @param context
     * @return
     */
    public static ConnectionType getConnectivityStatus(Context context){
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null){
            switch (networkInfo.getType()){
                case ConnectivityManager.TYPE_WIFI:
                    return ConnectionType.TYPE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return ConnectionType.TYPE_MOBILE_DATA;
            }
        }
        return ConnectionType.NOT_CONNECTED;
    }
}
