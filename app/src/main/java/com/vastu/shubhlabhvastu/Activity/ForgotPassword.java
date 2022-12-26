package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sn.lib.NestedProgress;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;
import com.vastu.shubhlabhvastu.Task.Validate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends BaseActivity {

    private static final String TAG = "TOKEN_ERROR";
    private TextInputEditText tv_mobile,tv_otp,etPassword,etcPassword;
    private Button btn_sent_otp,btn_continueCode,btn_update_pass;
    private Context context;
    private SharedPreferences pref;
    NestedProgress loader;
    private  String user_id,OTP;
    private TextInputLayout text_Password,text_CPassword,text_otp,text_mobile;
    String DeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        onclick();
        getToken();
    }

    private void initView()
    {
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        tv_mobile =  findViewById(R.id.tv_mobile);
        text_mobile =  findViewById(R.id.text_mobile);
        btn_sent_otp =  findViewById(R.id.btn_sent_otp);

        tv_otp =  findViewById(R.id.tv_otp);
        tv_otp .setVisibility(View.GONE);
        text_otp =  findViewById(R.id.text_otp);
        text_otp .setVisibility(View.GONE);
        btn_continueCode =  findViewById(R.id.btn_continueCode);
        btn_continueCode .setVisibility(View.GONE);

        etcPassword =  findViewById(R.id.etcPassword);
        etcPassword .setVisibility(View.GONE);
        etPassword =  findViewById(R.id.etPassword);
        etPassword .setVisibility(View.GONE);
        btn_update_pass =  findViewById(R.id.btn_update_pass);
        btn_update_pass .setVisibility(View.GONE);

        text_Password =  findViewById(R.id.text_Password);
        text_Password .setVisibility(View.GONE);
        text_CPassword =  findViewById(R.id.text_CPassword);
        text_CPassword .setVisibility(View.GONE);

        loader = findViewById(R.id.loader);


    }

    private void onclick()
    {
        btn_sent_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tv_mobile.getText().toString().equals(""))
                {
                    if(tv_mobile.getText().toString().length()<10)
                    {
                        tv_mobile.setError("Enter 10 Digit Mobile No");
                        tv_mobile.requestFocus();
                    }
                    else
                    {
                        tv_mobile.setError(null);
                        sendOTP();
                    }
                }
                else {
                    tv_mobile.setError("Enter Mobile No");
                    tv_mobile.requestFocus();
                }
                //openFrontPage();
            }
        });
        btn_continueCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tv_otp.getText().toString().equals(""))
                {
                    if(tv_otp.getText().toString().length()<4)
                    {
                        tv_otp.setError("Enter 4 Digit OTP");
                        tv_otp.requestFocus();
                    }
                    else
                    {
                        tv_otp.setError(null);
                        verifyOTP();
                    }
                }
                else {
                    tv_otp.setError("Enter OTP");
                    tv_otp.requestFocus();
                }
                //openFrontPage();
            }
        });
        btn_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validate.validateString(etPassword)) {
                    Toasts.makeText(context, "Invalid Password", ToastType.ERROR);
                    etPassword.setError("Invalid Password");
                    etPassword.requestFocus();
                } else if (Validate.returnString(etPassword).length() < 6) {
                    Toasts.makeText(context, "The password must be at least 6 characters", ToastType.ERROR);
                    etPassword.setError("The password must be at least 6 characters");
                    etPassword.requestFocus();
                } else if (!Validate.validateString(etcPassword) || (!Validate.returnString(etPassword).equals(Validate.returnString(etcPassword)))) {
                    Toasts.makeText(context, "Confirm Password Not Matched", ToastType.ERROR);
                    etcPassword.setError("Confirm Password Not Matched");
                    etcPassword.requestFocus();
                } else {
                    update_password();
                }
            }
        });
    }

    private void sendOTP() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS,responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                loader.setVisibility(View.GONE);
                if (response.getString("status").equals("1")) {
                    JSONObject data = new JSONObject(response.getString("data"));

                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

                    OTP=data.getString("otp");
                    user_id=data.getString("user_id");
                    text_mobile.setVisibility(View.GONE);
                    tv_mobile.setVisibility(View.GONE);
                    btn_sent_otp.setVisibility(View.GONE);
                    text_otp .setVisibility(View.VISIBLE);
                    tv_otp .setVisibility(View.VISIBLE);
                    btn_continueCode .setVisibility(View.VISIBLE);
                /*    Bundle bundle = new Bundle();
                    bundle.putString("from", "Login");
                    bundle.putString("OTP", OTP);
                    CommonTask.redirectActivity(this, Verify.class, bundle);*/

                } else {
                    Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
                }
            } catch (Exception e) {

                Log.d("_CatchListener", e.toString() + " " + e.getMessage());
            } finally {
                loader.setVisibility(View.GONE);
            }
        }, error -> {
            loader.setVisibility(View.GONE);
            Log.d("_ErrorListener", error.toString());
        })

        {
            /* @Override
             public Map<String, String> getHeaders() {
             Map<String, String> map = new HashMap<>();
             map.put(API.Accept, API.ApplicationJSON);
             map.put(API.Authorization, Session.getToken(pref));
             return map;
         }*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", tv_mobile.getText().toString());

                map.put("POST-TYPE", "RECOVER");

                Log.d("param", map.toString());
                return map;
            }
        });
    }

    private void verifyOTP() {

        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS,responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    // JSONObject data = new JSONObject(response.getString("data"));

                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);


                    text_otp .setVisibility(View.GONE);
                    tv_otp .setVisibility(View.GONE);
                    btn_continueCode .setVisibility(View.GONE);

                    etcPassword .setVisibility(View.VISIBLE);
                    etPassword .setVisibility(View.VISIBLE);
                    btn_update_pass .setVisibility(View.VISIBLE);
                    text_CPassword .setVisibility(View.VISIBLE);
                    text_Password .setVisibility(View.VISIBLE);

                } else {
                    Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
                }
            } catch (Exception e) {

                if(test_alert) {
                    Toasts.makeText(context, e.toString() + " " + e.getMessage(), ToastType.ERROR);
                    Log.d("_ErrorListener", e.toString() + " " + e.getMessage());
                }
            } finally {
                loader.setVisibility(View.GONE);
            }
        }, error -> {
            loader.setVisibility(View.GONE);

            if(test_alert) {
                Toasts.makeText(context, error.toString() + " " + error.getMessage(), ToastType.ERROR);
                Log.d("_ErrorListener", error.toString() + " " + error.getMessage());
            }
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                //This indicates that the request has either time out or there is no connection

            } else if (error instanceof AuthFailureError) {
                // Error indicating that there was an Authentication Failure while performing the request
                Toasts.makeText(context, "Invalid OTP Code !!! ", ToastType.ERROR);

            } else if (error instanceof ServerError) {
                //Indicates that the server responded with a error response

            } else if (error instanceof NetworkError) {
                //Indicates that there was network error while performing the request

            } else if (error instanceof ParseError) {
                // Indicates that the server response could not be parsed

            }
        })

        {
            /* @Override
             public Map<String, String> getHeaders() {
             Map<String, String> map = new HashMap<>();
             map.put(API.Accept, API.ApplicationJSON);
             map.put(API.Authorization, Session.getToken(pref));
             return map;
         }*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cid", user_id);
                map.put("otp", tv_otp.getText().toString());
                map.put("token", DeviceToken);
                map.put("POST-TYPE", "OTPVALIDATE");

                Log.d("param", map.toString());
                return map;
            }
        });
    }

    private void update_password() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    //JSONObject data = new JSONObject(response.getString("detail"));

                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

                    text_Password .setVisibility(View.GONE);
                    text_CPassword .setVisibility(View.GONE);
                    etcPassword .setVisibility(View.GONE);
                    etPassword .setVisibility(View.GONE);
                    btn_update_pass .setVisibility(View.GONE);

                    JSONObject data = new JSONObject(response.getString("data"));
                    Session.setIsLogged(pref, true);
                    Session.setIsSession(pref, true);
                    Session.setUserId(pref, data.getString("id"));
                    //Session.setStudentId(pref, data.getString("uuid"));
                    //Session.setRoleId(pref, data.getString("role"));
                    Session.setUserName(pref, data.getString("c_name"));
                    Session.setUserEmail(pref, data.getString("email"));
                    Session.setUserMobile(pref, data.getString("mobile"));
                    Session.setToken(pref, data.getString("token"));
                    Session.setImage(pref, data.getString("image"));
                    Session.setMobileVerified(pref, data.getString("mobile_verified"));
                    CommonTask.redirectFinishActivity(ForgotPassword.this, Home.class);

                } else {
                    Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
                }
            } catch (Exception e) {

                Log.d("_CatchListener", e.toString() + " " + e.getMessage());
            } finally {
                loader.setVisibility(View.GONE);
            }
        }, error -> {
            loader.setVisibility(View.GONE);
            Log.d("_ErrorListener", error.toString());
        })

        {
            /* @Override
             public Map<String, String> getHeaders() {
             Map<String, String> map = new HashMap<>();
             map.put(API.Accept, API.ApplicationJSON);
             map.put(API.Authorization, Session.getToken(pref));
             return map;
         }*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id);
                map.put("password", etcPassword.getText().toString());
                map.put("POST-TYPE","RESETPASSWORD");
                //map.put("confirm_pass", etPassword.getText().toString());

                Log.d("param", map.toString());
                return map;
            }
        });
    }

    private void getToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        DeviceToken = task.getResult();

                        // Log and toast
                        //Toasts.makeText(context, DeviceToken, ToastType.PROCESSING);


                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        CommonTask.redirectFinishActivity(ForgotPassword.this, SignIn.class);
    }
}