package com.rrja.carja.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chongge on 15/10/22.
 */
public class RrjaUtils {

    public static boolean getNetworkStatus(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

}
