package com.zju.biomedit.copdmanager.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by wangzheyu on 2017/4/17.
 */

public class WifiDetect {
    public static int getCurrentNetType(Context context) {
        int type = 0;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = 0;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = 1;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            type = 2;
        }
        return type;
    }
}
