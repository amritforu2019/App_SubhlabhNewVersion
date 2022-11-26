package com.vastu.shubhlabhvastu.Connection;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {
    private static InternetReceiver internetReciever = new InternetReceiver();

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void registerService(Activity activity) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(internetReciever, intentFilter);
    }

    public static void unregisterService(Activity activity) {
        activity.unregisterReceiver(internetReciever);
    }
}
