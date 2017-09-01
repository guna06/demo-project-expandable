package com.sqindiaadmin.com.sqmobileadmin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 04-03-2017.
 */

public class DataService {

    public static String URL_ADMIN= "http://104.197.80.225:3010/sq/admin/";
    public static String VERSION= "http://104.197.80.225:3010/sq/";






    public static boolean isNetworkAvailable(Context c1) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

