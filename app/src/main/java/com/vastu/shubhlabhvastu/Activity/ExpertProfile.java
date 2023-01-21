package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;

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
import android.widget.ImageView;
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
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExpertProfile extends BaseActivity {

    private static final String TAG = "TOKEN_ERROR";
    private ImageView ivImage,iv_Goback;
    private TextView tv_exp_name,tvRating,tvExp,tvLang,tvExpIn,tvSummary;
    private Context context;
    private Button btn_GotoForm;
    private SharedPreferences pref;
    private ProgressBar loader;
    private  String exp_id,exp_typ_name,title,exp_typ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_profile);
        initView();
        intentData();
        get_page_data();
        click_action();
    }
    @Override
    public void onBackPressed()
    {
       this.finish();
    }
    private void initView()
    {
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        iv_Goback =  findViewById(R.id.iv_Goback);
        ivImage =  findViewById(R.id.ivImage);
        btn_GotoForm =  findViewById(R.id.btn_GotoForm);
        loader = findViewById(R.id.loader);
        tv_exp_name = findViewById(R.id.tv_exp_name);
        tvRating = findViewById(R.id.tvRating);
        tvExp = findViewById(R.id.tvExp);
        tvLang = findViewById(R.id.tvLang);
        tvExpIn = findViewById(R.id.tvExpIn);
        tvSummary = findViewById(R.id.tvSummary);
    }
    private void intentData() {
        try {
            Bundle bundle = getIntent().getExtras();
            exp_id = bundle.getString("exp_id", "");
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
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.EXPERT+"?GET-TYPE=PROFILE&exp_id="+exp_id, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.ERROR);
                    Log.d("Response", responses);
                }

                if (response.getString("status").equals("1")) {
                    if(test_alert) {
                        Toasts.makeText(context, response.getString("messege"), ToastType.SUCCESS);
                    }
                    JSONObject data = new JSONObject(response.getString("detail"));

                    tv_exp_name.setText(data.getString("t_name"));
                    title=(data.getString("t_name"));
                    tvRating.setText(data.getString("rating"));
                    tvRating.setText(data.getString("rating")+" Star Rating");
                    tvExp.setText(data.getString("edu_year")+" Yrs Exp");
                    tvLang.setText(data.getString("lang"));
                    tvExpIn.setText(data.getString("c_typ_name"));
                    exp_typ_name=data.getString("c_typ_name");
                    exp_typ=data.getString("c_typ");
                    tvSummary.setText(data.getString("c_subj"));


                    Glide.with(context).load(API.IMG_EXPERT+data.getString("image")).placeholder(R.drawable.card_style).error(R.drawable.no_image).into(ivImage);


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
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", exp_typ);
                Log.d("param", map.toString());
                return map;
            }*/
        });
    }
    private void click_action()
    {
        // Get Started button click listener
        btn_GotoForm.setOnClickListener(v -> gotoList());
        iv_Goback.setOnClickListener(v -> this.finish());
    }
    private void gotoList()
    {

        if(Session.getIsSession(pref)) {
            Bundle bundle = new Bundle();
            bundle.putString("exp_id", exp_id);
            bundle.putString("title", title);
            bundle.putString("exp_typ", exp_typ);
            bundle.putString("exp_typ_name", exp_typ_name);
            CommonTask.redirectFinishActivity(ExpertProfile.this, QueryForm.class, bundle);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExpertProfile.this);
            builder.setTitle("Alert...")
                    .setMessage("Kindly login first to book appointment \nContinue to login page........")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonTask.redirectFinishActivity(ExpertProfile.this,SignIn.class);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toasts.makeText(context,"Kindly login first to book appointment!!",ToastType.ERROR);
        }
    }
}