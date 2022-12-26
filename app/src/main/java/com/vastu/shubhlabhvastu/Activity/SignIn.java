package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

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

public class SignIn extends BaseActivity {
    private static final String TAG = "TOKEN_ERROR";
    private TextView tvForgotPassword, tvSignUp;
    private Button btnSignIn;
    private Context context;
    private SharedPreferences pref;
    private TextInputEditText etEmailId, etPassword;
    NestedProgress loader;
    private String DeviceToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);


        initView();

        isSessionCreated();

        viewClickListener();
    }
    private void isSessionCreated() {
        Bundle bundle = new Bundle();
        bundle.putString("from", "Login");
        //Toasts.makeText(context, "Inside Session", ToastType.SUCCESS);

        if (Session.getIsSession(pref) && Session.getMobileVerified(pref).equals("1"))
            CommonTask.redirectFinishActivity(SignIn.this, Home.class);
        else if (Session.getIsSession(pref) && Session.getMobileVerified(pref).equals("0"))
            sendOTP();
    }

    private void viewClickListener() {
        tvForgotPassword.setOnClickListener(v -> openFPasswordPage());
        tvSignUp.setOnClickListener(v -> openSignup());
        btnSignIn.setOnClickListener(v -> openFrontPage());
    }

    private void initView() {
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etEmailId = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        loader = findViewById(R.id.loader);
        getToken();
    }

    public void openFPasswordPage() {
        CommonTask.redirectActivity(this, ForgotPassword.class);
    }

    public void openSignup() {
        CommonTask.redirectActivity(this, SignUp.class);
    }

    public void openFrontPage() {
         if (!Validate.validateMobile(etEmailId)) {
            Toasts.makeText(context, "Invalid Mobile", ToastType.ERROR);
            etEmailId.setError("Invalid Mobile");
            etEmailId.requestFocus();
        } else if (!Validate.validateString(etPassword)) {
            Toasts.makeText(context, "Invalid Password", ToastType.ERROR);
            etPassword.setError("Invalid Password");
            etPassword.requestFocus();
        } else sendLoginRequest();
    }

    private void sendLoginRequest() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                  if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.ERROR);
                    Log.d("Response", responses);
                }

                if (response.getString("status").equals("1")) {
                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

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
                    isSessionCreated();
                } else {
                    Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
                }
            } catch (Exception e) {
                if(test_alert) {
                    Toasts.makeText(context, e.toString() + " " + e.getMessage(), ToastType.ERROR);
                    Log.d("_CatchListener", e.toString() + " " + e.getMessage());
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
                    Toasts.makeText(context, "Invalid Login Data !!! ", ToastType.ERROR);

                } else if (error instanceof ServerError) {
                //Indicates that the server responded with a error response

                } else if (error instanceof NetworkError) {
                //Indicates that there was network error while performing the request

                } else if (error instanceof ParseError) {
                // Indicates that the server response could not be parsed

                }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", Validate.returnString(etEmailId));
                map.put("password", Validate.returnString(etPassword));
                map.put("POST-TYPE", "LOGIN");
                map.put("token", DeviceToken);
                Log.d("param", map.toString());
                return map;
            }
        });
    }

    private void sendOTP() {
        Toasts.makeText(context, "OTP Requested", ToastType.SUCCESS);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    JSONObject data = new JSONObject(response.getString("data"));

                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

                    String OTP=data.getString("otp");
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "Login");
                    bundle.putString("OTP", OTP);
                    bundle.putString("token", DeviceToken);
                    CommonTask.redirectActivity(SignIn.this, Verify.class, bundle);

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
                map.put("cid", Session.getUserId(pref));

                map.put("POST-TYPE", "OTPSENT");

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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
        builder.setTitle("Warning...")
                .setMessage("Are You Sure want to Exit.?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}