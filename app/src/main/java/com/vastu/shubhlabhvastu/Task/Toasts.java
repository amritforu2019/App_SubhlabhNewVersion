package com.vastu.shubhlabhvastu.Task;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vastu.shubhlabhvastu.R;


public class Toasts {
    public static void makeText(Context context, String message, ToastType toastType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView image = (ImageView) layout.findViewById(R.id.imageView);
        TextView text = (TextView) layout.findViewById(R.id.textView);
        switch (toastType) {
            case ERROR:
                image.setImageResource(R.drawable.ic_info);
                break;
            case SUCCESS:
                image.setImageResource(R.drawable.ic_true);
                break;
            case PROCESSING:
                image.setImageResource(R.drawable.ic_processing);
                break;
        }
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
