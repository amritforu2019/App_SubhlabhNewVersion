package com.vastu.shubhlabhvastu.Task;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Validate {
    public static String fillString(String data, EditText editText) {
        editText.setText(data.equals("null") || data.equals(null) || data.equals("") ? "" : data);
        return data;
    }

    public static String fillString(String data, TextView textView) {
        textView.setText(data.equals("null") || data.equals(null) || data.equals("") ? "" : data);
        return data;
    }

    public static String cleanString(String data){return data.replaceAll("[^a-zA-Z0-9 ]","");}

    public static String returnString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String returnString(Button editText) {
        return editText.getText().toString().trim();
    }

    public static String returnString(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static String viewVisibility(String data, EditText editText) {
        editText.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static String viewVisibility(String data, TextView textView) {
        textView.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static String viewVisibility(String data, Button button) {
        button.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static String viewVisibility(String data, LinearLayout linearLayout) {
        linearLayout.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static String viewVisibility(String data, CardView cardView) {
        cardView.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static String viewVisibility(String data, View view) {
        view.setVisibility(data.equals("null") || data.equals(null) || data.equals("") ? GONE : VISIBLE);
        return data;
    }

    public static boolean validateString(EditText editText) {
        if (editText.getText().toString().trim().equals(""))
            return false;
        return true;
    }

    public static boolean validateEmail(EditText editText) {
        if (Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches())
            return true;
        return false;
    }

    public static boolean validateMobile(EditText editText) {
        if (editText.getText().toString().trim().length() == 10)
            return true;
        return false;
    }

    public static boolean validateString(TextView textView) {
        if (textView.getText().toString().trim().equals(""))
            return false;
        return true;
    }

    public static boolean validateEmail(TextView textView) {
        if (Patterns.EMAIL_ADDRESS.matcher(textView.getText().toString().trim()).matches())
            return true;
        return false;
    }

    public static boolean validateMobile(TextView textView) {
        if (textView.getText().toString().trim().length() == 10)
            return true;
        return false;
    }
}
