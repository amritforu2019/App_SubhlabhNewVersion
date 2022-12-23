package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vastu.shubhlabhvastu.Adapter.AdapterAllExpert;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.Model.ModelAllExpert;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends BaseActivity implements Animation.AnimationListener  {

    private Context context;
    private ProgressBar loader;
    private Animation fadeout;
    private SharedPreferences pref;
    private String TAG = Home.class.getSimpleName();
    LinearLayout ll_astro,ll_vastu,ll_kundali,ll_palm,ll_tarot;
    TextView tv_astro,tv_vastu,tv_kundali,tv_palm,tv_tarot,tv_more_astro,tv_more_vastu,tv_more_vastukundali,tv_more_palmistry,tv_more_tarot;
    ScrollView svHome;

    private ArrayList<ModelAllExpert> modelAllExpert;
    private ArrayList<ModelAllExpert> modelAllExpertvastu;
    private ArrayList<ModelAllExpert> modelAllExpertvastukundali;
    private ArrayList<ModelAllExpert> modelAllExpertpalmistry;
    private ArrayList<ModelAllExpert> modelAllExperttarot;

    RecyclerView rvAstrology,rvVastu,rvVastuKundali,rvPalmistry,rvTarot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        //validate_login();

        initView();                                         // TODO : Init All View
        click_listeners();
        Get_Dahboard_data();
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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
    private void setView()
    {
        /*findViewById(R.id.menu_home).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        });*/
        //findViewById(R.id.menu_home).setBackgroundColor(getResources().getColor(R.color.gray_trans));
         drawerLayout.addView(LayoutInflater.from(this).inflate(R.layout.activity_home, null, false), 1);


    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void initView() {
        loader = findViewById(R.id.loader);
        ll_astro=findViewById(R.id.ll_astro);
        ll_vastu=findViewById(R.id.ll_vastu);
        ll_kundali=findViewById(R.id.ll_kundali);
        ll_palm=findViewById(R.id.ll_palm);
        ll_tarot=findViewById(R.id.ll_tarot);
        tv_astro=findViewById(R.id.tv_astro);
        tv_vastu=findViewById(R.id.tv_vastu);
        tv_kundali=findViewById(R.id.tv_kundali);
        tv_palm=findViewById(R.id.tv_palm);
        tv_tarot=findViewById(R.id.tv_tarot);
        svHome=findViewById(R.id.svHome);

        rvAstrology=findViewById(R.id.rvAstrology);
        tv_more_astro=findViewById(R.id.tv_more_astro);
        rvVastu=findViewById(R.id.rvVastu);
        tv_more_vastu=findViewById(R.id.tv_more_vastu);
        rvVastuKundali=findViewById(R.id.rvVastuKundali);
        tv_more_vastukundali=findViewById(R.id.tv_more_vastukundali);
        rvPalmistry=findViewById(R.id.rvPalmistry);
        tv_more_palmistry=findViewById(R.id.tv_more_palmistry);
        rvTarot=findViewById(R.id.rvTarot);
        tv_more_tarot=findViewById(R.id.tv_more_tarot);

        fadeout= AnimationUtils.loadAnimation(context,R.anim.blink);
        fadeout.setAnimationListener(this);

    }

    private void click_listeners()
    {
         ll_astro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_color();
                tv_astro.setTextColor(getResources().getColor(R.color.primary_text));
                //svHome.smoothScrollTo(0, rvAstrology.getTop());
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "astrology");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        ll_vastu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_color();
                tv_vastu.setTextColor(getResources().getColor(R.color.primary_text));
               // svHome.smoothScrollTo(0, rvVastu.getTop());
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "vastu");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        ll_kundali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_color();
                tv_kundali.setTextColor(getResources().getColor(R.color.primary_text));
                //svHome.smoothScrollTo(0, rvVastuKundali.getTop());
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "vastu_kundali");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        ll_palm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_color();
                tv_palm.setTextColor(getResources().getColor(R.color.primary_text));
                //svHome.smoothScrollTo(0, rvPalmistry.getTop());
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "palmistry");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        ll_tarot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_color();
                 tv_tarot.setTextColor(getResources().getColor(R.color.primary_text));
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "tarot");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        tv_more_astro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "astrology");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        tv_more_vastu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "vastu");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        tv_more_vastukundali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "vastu_kundali");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        tv_more_palmistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "palmistry");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });
        tv_more_tarot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exp_typ", "tarot");
                CommonTask.redirectActivity(Home.this, ExpertList.class, bundle);
            }
        });

    }
    
    private void reset_color()
    {
        tv_astro.setTextColor(getResources().getColor(R.color.light_black));
        tv_kundali.setTextColor(getResources().getColor(R.color.light_black));
        tv_palm.setTextColor(getResources().getColor(R.color.light_black));
        tv_tarot.setTextColor(getResources().getColor(R.color.light_black));
        tv_vastu.setTextColor(getResources().getColor(R.color.light_black));
    }

    private void Get_Dahboard_data() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.DASHBOARD, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    modelAllExpert = new ArrayList<>();
                    JSONObject  jsonRootObject = new JSONObject(response.getString("detail"));
                    JSONArray jsonArray = jsonRootObject.optJSONArray("astrology");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            modelAllExpert.add(new ModelAllExpert(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("t_name"),
                                    jsonObject.getString("c_typ"),
                                    jsonObject.getString("image"),
                                    jsonObject.getString("edu_year"),
                                    jsonObject.getString("rating")
                            ));
                    }
                    rvAstrology.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvAstrology.setAdapter(new AdapterAllExpert(Home.this, modelAllExpert));



                    modelAllExpertvastu = new ArrayList<>();
                    JSONArray jsonArrayvastu = jsonRootObject.optJSONArray("vastu");
                    for (int i = 0; i < jsonArrayvastu.length(); i++) {
                        JSONObject jsonObjectvastu = new JSONObject(jsonArrayvastu.getString(i));
                        modelAllExpertvastu.add(new ModelAllExpert(
                                jsonObjectvastu.getString("id"),
                                jsonObjectvastu.getString("t_name"),
                                jsonObjectvastu.getString("c_typ"),
                                jsonObjectvastu.getString("image"),
                                jsonObjectvastu.getString("edu_year"),
                                jsonObjectvastu.getString("rating")
                        ));
                    }
                    rvVastu.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvVastu.setAdapter(new AdapterAllExpert(Home.this, modelAllExpertvastu));


                    modelAllExpertvastukundali = new ArrayList<>();
                    JSONArray jsonArrayvastukundali = jsonRootObject.optJSONArray("vastu_kundali");
                    for (int i = 0; i < jsonArrayvastukundali.length(); i++) {
                        JSONObject jsonObjectvastukundali = new JSONObject(jsonArrayvastukundali.getString(i));
                        modelAllExpertvastukundali.add(new ModelAllExpert(
                                jsonObjectvastukundali.getString("id"),
                                jsonObjectvastukundali.getString("t_name"),
                                jsonObjectvastukundali.getString("c_typ"),
                                jsonObjectvastukundali.getString("image"),
                                jsonObjectvastukundali.getString("edu_year"),
                                jsonObjectvastukundali.getString("rating")
                        ));
                    }
                    rvVastuKundali.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvVastuKundali.setAdapter(new AdapterAllExpert(Home.this, modelAllExpertvastukundali));

                    modelAllExpertpalmistry = new ArrayList<>();
                    JSONArray jsonArraypalmistry = jsonRootObject.optJSONArray("palmistry");
                    for (int i = 0; i < jsonArraypalmistry.length(); i++) {
                        JSONObject jsonObjectpalmistry = new JSONObject(jsonArraypalmistry.getString(i));
                        modelAllExpertpalmistry.add(new ModelAllExpert(
                                jsonObjectpalmistry.getString("id"),
                                jsonObjectpalmistry.getString("t_name"),
                                jsonObjectpalmistry.getString("c_typ"),
                                jsonObjectpalmistry.getString("image"),
                                jsonObjectpalmistry.getString("edu_year"),
                                jsonObjectpalmistry.getString("rating")
                        ));
                    }
                    rvPalmistry.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvPalmistry.setAdapter(new AdapterAllExpert(Home.this, modelAllExpertpalmistry));


                    modelAllExperttarot = new ArrayList<>();
                    JSONArray jsonArraytarot = jsonRootObject.optJSONArray("tarot");
                    for (int i = 0; i < jsonArraytarot.length(); i++) {
                    JSONObject jsonObjecttarot = new JSONObject(jsonArraytarot.getString(i));
                    modelAllExperttarot.add(new ModelAllExpert(
                    jsonObjecttarot.getString("id"),
                    jsonObjecttarot.getString("t_name"),
                    jsonObjecttarot.getString("c_typ"),
                    jsonObjecttarot.getString("image"),
                    jsonObjecttarot.getString("edu_year"),
                    jsonObjecttarot.getString("rating")
                    ));
                    }
                    rvTarot.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvTarot.setAdapter(new AdapterAllExpert(Home.this, modelAllExperttarot));


                } else {

                }
            } catch (JSONException e) {
                Log.d("allCour_CatchListener", e.toString());
            } finally {
                loader.setVisibility(View.GONE);
            }
        }, error -> {
            loader.setVisibility(View.GONE);
            Log.d("allCour_ErrorListener", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(API.Authorization, Session.getToken(pref));
                map.put(API.Accept, API.ApplicationJSON);
                Log.d("param", map.toString());
                return map;
            }
        });
    }


}