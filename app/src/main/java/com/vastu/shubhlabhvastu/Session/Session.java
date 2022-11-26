package com.vastu.shubhlabhvastu.Session;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {


    private static String IS_SESSION = "IS_SESSION";
    private static String IS_LOGGED = "IS_LOGGED";
    private static String USER_NAME = "USER_NAME";
    private static String USER_ID = "USER_ID";
    private static String USER_EMAIL = "USER_EMAIL";
    private static String USER_MOBILE = "USER_MOBILE";
    private static String ROLE_ID = "ROLE_ID";
    private static String TOKEN = "TOKEN";
    private static String IMAGE = "IMAGE";
    private static String USER_IDs = "USER_IDs";
    private static String STUDENT_ID = "STUDENT_ID";
    private static String MOBILE_VERIFIED = "MOBILE_VERIFIED";

    public static String savePreference(SharedPreferences preferences, String keys, String value) {
        Editor editor = preferences.edit();
        editor.putString(keys, value);
        editor.apply();
        return value;
    }

    public static int savePreference(SharedPreferences preferences, String keys, int value) {
        Editor editor = preferences.edit();
        editor.putInt(keys, value);
        editor.apply();
        return value;
    }

    public static boolean savePreference(SharedPreferences preferences, String keys, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(keys, value);
        editor.apply();
        return value;
    }

    public static float savePreference(SharedPreferences preferences, String keys, float value) {
        Editor editor = preferences.edit();
        editor.putFloat(keys, value);
        editor.apply();
        return value;
    }

    public static boolean destroySession(SharedPreferences preferences) {
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        return true;
    }

    public static boolean setIsSession(SharedPreferences preferences, boolean value) {
        return Session.savePreference(preferences, IS_SESSION, value);
    }

    public static boolean getIsSession(SharedPreferences preferences) {
        return preferences.getBoolean(IS_SESSION, false);
    }

    public static boolean setIsLogged(SharedPreferences preferences, boolean value) {
        return Session.savePreference(preferences, IS_LOGGED, value);
    }

    public static boolean getIsLogged(SharedPreferences preferences) {
        return preferences.getBoolean(IS_LOGGED, false);
    }

    public static String setUserName(SharedPreferences preferences, String user_name) {
        return savePreference(preferences, USER_NAME, user_name);
    }

    public static String getUserName(SharedPreferences preferences) {
        return preferences.getString(USER_NAME, "");
    }

    public static String setUserEmail(SharedPreferences preferences, String user_email) {
        return savePreference(preferences, USER_EMAIL, user_email);
    }

    public static String getUserEmail(SharedPreferences preferences) {
        return preferences.getString(USER_EMAIL, "");
    }

    public static String setToken(SharedPreferences preferences, String token) {
        return savePreference(preferences, TOKEN, "Bearer " + token);
    }

    public static String getToken(SharedPreferences preferences) {
        return preferences.getString(TOKEN, "");
    }

    public static String setUserMobile(SharedPreferences preferences, String user_mobile) {
        return savePreference(preferences, USER_MOBILE, user_mobile);
    }

    public static String getUserMobile(SharedPreferences preferences) {
        return preferences.getString(USER_MOBILE, "");
    }

    public static String setUserId(SharedPreferences preferences, String user_id) {
        return savePreference(preferences, USER_ID, user_id);
    }

    public static String getUserId(SharedPreferences preferences) {
        return preferences.getString(USER_ID, "");
    }

    public static String setRoleId(SharedPreferences preferences, String role_id) {
        return savePreference(preferences, ROLE_ID, role_id);
    }

    public static String getRoleId(SharedPreferences preferences) {
        return preferences.getString(ROLE_ID, "");
    }

    public static String setImage(SharedPreferences preferences, String user_name) {
        return savePreference(preferences, IMAGE, user_name);
    }

    public static String getImage(SharedPreferences preferences) {
        return preferences.getString(IMAGE, "");
    }

    public static String setUserIds(SharedPreferences preferences, String user_id) {
        return savePreference(preferences, USER_IDs, user_id);
    }

    public static String getUserIds(SharedPreferences preferences) {
        return preferences.getString(USER_IDs, "");
    }

    public static String getStudentId(SharedPreferences preferences) {
        return preferences.getString(STUDENT_ID, "");
    }

    public static String setStudentId(SharedPreferences preferences, String studentId) {
           return savePreference(preferences, STUDENT_ID, studentId);
    }

    public static String getMobileVerified(SharedPreferences preferences) {
        return preferences.getString(MOBILE_VERIFIED, "0");
    }

    public static String setMobileVerified(SharedPreferences preferences, String mobileVerified) {
        return savePreference(preferences, MOBILE_VERIFIED, mobileVerified);
    }
}
