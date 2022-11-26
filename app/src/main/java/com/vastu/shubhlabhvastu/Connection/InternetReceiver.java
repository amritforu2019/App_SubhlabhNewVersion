package com.vastu.shubhlabhvastu.Connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.vastu.shubhlabhvastu.R;


public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!CheckConnection.isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.layout_connection, null);
            builder.setView(view);
            Button btn_retry = view.findViewById(R.id.btn_retry);
            Button btn_close = view.findViewById(R.id.btn_close);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Activity activity = (Activity) context;
            btn_retry.setOnClickListener(v -> {
                alertDialog.dismiss();
                onReceive(context, intent);
            });
            btn_close.setOnClickListener(v -> {
                activity.finishAffinity();
                System.exit(0);
            });
        } else {
            Log.v("------Connection------", "Connected with Internet");
        }
    }
}
