package com.vastu.shubhlabhvastu.Task;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static void hideKeyboard(Activity act) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getDeviceId(Context context) {
        String id = "";
        try {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static void selectSpinnerItemByValue(Spinner spnr, long value) {
        ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItemId(position) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    public String convertNumber_bystring(String num_string) {
        try {
            float val = Float.parseFloat(num_string);
            NumberFormat cf1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            String amt = cf1.format(val);
            String[] a = amt.split("\\.");
            return String.valueOf(a[0]);
        } catch (Exception es) {
            return "0.00";
        }
    }

    public String date_modify(String dt) {
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(dt);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String format = formatter.format(date);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;

    }

    public String get_err_msg(String txt) {
        String f_txt = "";
        try {

            String[] errorary = txt.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", "").split(",");
            for (String str : errorary) {
                String[] errtxt = str.split(":");
                f_txt = f_txt + "\n" + errtxt[1];
            }
            Log.e("ERRMSG", f_txt);

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
            f_txt = txt;
        }
        return f_txt;
    }
}
