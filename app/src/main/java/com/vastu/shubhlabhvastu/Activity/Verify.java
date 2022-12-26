package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputEditText;
import com.sn.lib.NestedProgress;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Verify extends BaseActivity {

    private static final String TAG = "TOKEN_ERROR";
    private TextInputEditText tv_otp;
    private Button btn_continueCode;
    private Context context;
    private SharedPreferences pref;
    NestedProgress loader;
    private  String from,OTP,Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        initView();
        intentData();
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
                        loader.setVisibility(View.VISIBLE);
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
    }

    private void initView()
    {
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        tv_otp =  findViewById(R.id.tv_otp);
        btn_continueCode =  findViewById(R.id.btn_continueCode);
        loader = findViewById(R.id.loader);
    }

    private void intentData() {
        try {
            Bundle bundle = getIntent().getExtras();
            from = bundle.getString("from", "");
            OTP = bundle.getString("OTP", "");
            Token = bundle.getString("token", "");
            Log.d("Bundle",bundle.toString());
        }
        catch (Exception ex)
        {
            Log.e("_Exception_bundle",ex.getMessage());
        }
    }

    private void verifyOTP() {

        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS,responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                   // JSONObject data = new JSONObject(response.getString("data"));

                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);


                    Bundle bundle = new Bundle();
                    bundle.putString("from", "Login");
                    Session.setMobileVerified(pref,"1");

                    CommonTask.redirectActivity(Verify.this, Home.class, bundle);

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
                map.put("cid", Session.getUserId(pref));
                map.put("otp", tv_otp.getText().toString());
                map.put("token", Token);
                map.put("POST-TYPE", "OTPVALIDATE");

                Log.d("param", map.toString());
                return map;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        CommonTask.redirectFinishActivity(Verify.this, SignIn.class);
    }
}