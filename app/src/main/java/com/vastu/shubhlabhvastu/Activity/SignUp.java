package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.POST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends BaseActivity {

    private TextView textView;
    private TextInputEditText etEmail, etMobile, etName, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private Context context;
    private SharedPreferences pref;
    private NestedProgress loader;
    private static final String TAG = "TOKEN_ERROR";
    private String DeviceToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        initView();
        viewClickListener();
    }

    private void viewClickListener() {
        textView.setOnClickListener(v -> openSignIn());
        btnSignUp.setOnClickListener(v -> openFrontPage());
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.signin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
       // etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        loader = findViewById(R.id.loader);
        getToken();
    }

    public void openSignIn() {
        CommonTask.redirectActivity(this, SignIn.class);
    }

    public void openFrontPage() {
        if (!Validate.validateString(etName)) {
            Toasts.makeText(context, "Invalid Name", ToastType.ERROR);
            etName.setError("Invalid Name");
            etName.requestFocus();
        } else if (!Validate.validateString(etMobile) || !Validate.validateMobile(etMobile)) {
            Toasts.makeText(context, "Invalid Mobile", ToastType.ERROR);
            etMobile.setError("Invalid Mobile");
            etMobile.requestFocus();
        } /*else if (!Validate.validateEmail(etEmail)) {
            Toasts.makeText(context, "Invalid Email", ToastType.ERROR);
            etEmail.setError("Invalid Email");
            etEmail.requestFocus();
        } */else if (!Validate.validateString(etPassword)) {
            Toasts.makeText(context, "Invalid Password", ToastType.ERROR);
            etPassword.setError("Invalid Password");
            etPassword.requestFocus();
        } else if (Validate.returnString(etPassword).length() < 6) {
            Toasts.makeText(context, "The password must be at least 6 characters", ToastType.ERROR);
            etPassword.setError("The password must be at least 6 characters");
            etPassword.requestFocus();
        } else if (!Validate.validateString(etConfirmPassword) || (!Validate.returnString(etPassword).equals(Validate.returnString(etConfirmPassword)))) {
            Toasts.makeText(context, "Confirm Password Not Matched", ToastType.ERROR);
            etConfirmPassword.setError("Confirm Password Not Matched");
            etConfirmPassword.requestFocus();
        } else {
            sendRegisterRequest();
        }
    }

    private void sendRegisterRequest() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.CUSTOMER_ACCESS,responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

                    JSONObject data = new JSONObject(response.getString("data"));
                    Session.setIsLogged(pref, true);
                    Session.setIsSession(pref, true);
                    Session.setUserId(pref, data.getString("id"));
                    //Session.setStudentId(pref, data.getString("uuid"));
                    //Session.setRoleId(pref, data.getString("role"));
                    Session.setUserName(pref, data.getString("c_name"));
                    //Session.setUserEmail(pref, data.getString("email"));
                    Session.setUserMobile(pref, data.getString("mobile"));
                    Session.setToken(pref, data.getString("token"));
                    Session.setImage(pref, data.getString("image"));
                    Session.setMobileVerified(pref, data.getString("mobile_verified"));
                    ///CommonTask.redirectFinishActivity(this, Home.class);
                    sendOTP();
                }
                else
                {
                    String t2=response.getString("data");
                    t2=t2.replaceAll("[^a-zA-Z0-9\\s+]", "");
                    Toasts.makeText(context, t2, ToastType.ERROR);
                }
            } catch (JSONException e) {
                Log.d("_CatchListener", e.toString());
            } finally {
                loader.setVisibility(View.GONE);
            }
        }, error -> {
            loader.setVisibility(View.GONE);
            Log.d("_ErrorListener", error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", Validate.returnString(etName));
                map.put("mobile", Validate.returnString(etMobile));
                //map.put("email", Validate.returnString(etEmail));
                map.put("password", Validate.returnString(etPassword));
                map.put("confirm_password", Validate.returnString(etConfirmPassword));
                //map.put("role", "3");
                map.put("device_token", DeviceToken);
                map.put("POST-TYPE", "REGISTER");
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
                    CommonTask.redirectActivity(SignUp.this, Verify.class, bundle);

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

    @Override
    public void onBackPressed()
    {
        CommonTask.redirectFinishActivity(SignUp.this, SignIn.class);
    }
}