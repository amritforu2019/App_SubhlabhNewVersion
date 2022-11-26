package com.vastu.shubhlabhvastu;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;
import com.vastu.shubhlabhvastu.Activity.Intro;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    Animation animation,animation1;
    ImageView ll_logo;
    Button tv_Appname;

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private SharedPreferences pref;
    private Context context;
    AlertDialog.Builder builder;
    TextView AppVer;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.transition.rotate_zoom_inout);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.transition.side_up);
        ll_logo=   findViewById(R.id.ll_logo);
        tv_Appname=   findViewById(R.id.tv_Appname);
        ll_logo.startAnimation(animation);
        tv_Appname.startAnimation(animation1);

        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        builder = new AlertDialog.Builder(this);
        AppVer=findViewById(R.id.AppVer);
        AppVer.setText("App Version : "+ BuildConfig.VERSION_NAME);

        validate_app_version();

        tv_Appname.setOnClickListener(v -> CommonTask.redirectFinishActivity(this, Intro.class));

    }

    private void validate_app_version()
    {
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.COMMON_APP_VERSION, responses ->
        {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if(test_alert)
                Toasts.makeText(context, response.toString(), ToastType.ERROR);

                if (response.getString("status").equals("1"))
                {
                    JSONObject response_1= new JSONObject(response.getString("detail"));
                    String Ver=response_1.getString("version");
                    String Ver_TYP=response_1.getString("type");
                    if(Ver.equals(BuildConfig.VERSION_NAME))
                    {
                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            CommonTask.redirectFinishActivity(MainActivity.this, intro.class);
                            }
                        }, SPLASH_DISPLAY_LENGTH);*/

                        if(test_alert)
                        Toasts.makeText(context, "Version Is validate Successfully", ToastType.SUCCESS);

                    }
                    else
                    {
                        if(Ver_TYP.equals("Y"))
                        {
                            builder.setMessage("New version : " + Ver + " is available in Google Play Store kindly update app")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else
                        {
                            Toasts.makeText(context, "New version : " + Ver + " is available in Google Play Store kindly update app", ToastType.PROCESSING);
                           /* new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Session.getIsSession(pref) && Session.getMobileVerified(pref).equals("1"))
                                        CommonTask.redirectFinishActivity(Splash.this, MainActivity.class);
                                    else  if (Session.getIsSession(pref) && Session.getMobileVerified(pref).equals("0"))
                                        CommonTask.redirectFinishActivity(Splash.this, Login.class);
                                    //CommonTask.redirectFinishActivity(Splash.this, MainActivity.class);
                                }
                            }, SPLASH_DISPLAY_LENGTH);*/
                        }
                    }
                }
                else
                {
                    Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
                }
            } catch (JSONException e) {
                Log.d("allCour_CatchListener", e.toString());
            } finally {
            }
        }, error -> {
            Log.d("allCour_ErrorListener", error.toString());
        }) {
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(API.Authorization, Session.getToken(pref));
                map.put(API.Accept, API.ApplicationJSON);
                Log.d("param", map.toString());
                return map;
            }*/
        });
    }

}