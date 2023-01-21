package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ExpertSummary extends BaseActivity {
    private static final String TAG = "TOKEN_ERROR";
    private ImageView iv_Banner,iv_Goback;
    private TextView tv_Name,tv_title,tv_Summary,tv_GotoList;
    private Context context;
    private SharedPreferences pref;
    private ProgressBar loader;
    private  String exp_typ,sort_summary,title;

    LinearLayout ll_astro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_summary);
        initView();
        intentData();
        get_page_data();
        click_action();
    }
    @Override
    public void onBackPressed()
    {
        CommonTask.redirectFinishActivity(ExpertSummary.this, Home.class);
    }
    private void initView()
    {
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        iv_Goback =  findViewById(R.id.iv_Goback);
        iv_Banner =  findViewById(R.id.iv_Banner);
        tv_GotoList =  findViewById(R.id.tv_GotoList);
        loader = findViewById(R.id.loader);
        tv_Name = findViewById(R.id.tv_Name);
        tv_title = findViewById(R.id.tv_title);
        tv_Summary = findViewById(R.id.tv_Summary);
    }
    private void intentData() {
        try {
            Bundle bundle = getIntent().getExtras();
            exp_typ = bundle.getString("exp_typ", "");

            if(test_alert)
            {
                Toasts.makeText(context, "BUNDEL : "+ bundle.toString(), ToastType.ERROR);
                Log.d("Bundle",bundle.toString());
            }
        }
        catch (Exception ex)
        {
            Log.e("_Exception_bundle",ex.getMessage());
        }
    }
    private void get_page_data() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.PAGE+"?type="+exp_typ, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.ERROR);
                    Log.d("Response", responses);
                }

                if (response.getString("status").equals("1")) {
                    JSONObject data = new JSONObject(response.getString("detail"));

                    if(test_alert) {
                        Toasts.makeText(context, response.getString("messege"), ToastType.SUCCESS);
                        Toasts.makeText(context,API.IMG_URL_PAGE+data.getString("image"), ToastType.SUCCESS);
                    }

                    tv_Name.setText(data.getString("t_name"));
                    tv_GotoList.setText("View Our "+data.getString("t_name"));
                    tv_title.setText(data.getString("title"));
                    title=data.getString("t_name");
                    tv_Summary.setText(data.getString("summary"));
                    sort_summary=data.getString("sort_summary");
                    Glide.with(context).load(API.IMG_URL_PAGE+data.getString("image")).placeholder(R.drawable.card_style).error(R.drawable.no_image).into(iv_Banner);


                } else {
                    Toasts.makeText(context, response.getString("messege"), ToastType.ERROR);
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
                map.put("type", exp_typ);
                Log.d("param", map.toString());
                return map;
            }
        });
    }
    private void click_action()
    {
        // Get Started button click listener
        tv_GotoList.setOnClickListener(v -> gotoList());
        iv_Goback.setOnClickListener(v -> CommonTask.redirectFinishActivity(ExpertSummary.this, Home.class));
     }
     private void gotoList()
     {
         Bundle bundle = new Bundle();
         bundle.putString("exp_typ", exp_typ);
         bundle.putString("title", title);
         bundle.putString("sort_summary", sort_summary);
         CommonTask.redirectActivity(ExpertSummary.this, ExpertList.class,bundle);
     }
}