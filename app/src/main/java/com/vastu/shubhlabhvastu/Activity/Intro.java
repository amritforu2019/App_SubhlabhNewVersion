package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.vastu.shubhlabhvastu.Adapter.IntroViewPagerAdapter;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.Model.ScreenItem;
import com.vastu.shubhlabhvastu.R;
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

public class Intro extends BaseActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;
    private Context context;
    List<ScreenItem> mList = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.transition.button_animation);
        tvSkip = findViewById(R.id.tv_skip);
        context = getApplicationContext();


        // fill list screen
        getIntroData();


        // next button click Listner
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size()-1) { // when we rech to the last screen
                    // TODO : show the GETSTARTED Button and hide the indicator and the next button
                    loaddLastScreen();
                }
            }
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        // Get Started button click listener
        btnGetStarted.setOnClickListener(v -> CommonTask.redirectFinishActivity(this, Home.class));

        // skip button click listener
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        //tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }


    ///Get Intro Screen Data from API
    private void getIntroData() {
        mList = new ArrayList<>();
       /* modelInstructors = new ArrayList<>();
        binding.loader.setVisibility(View.VISIBLE);
        binding.rvMyAllInstructor.setAdapter(null);*/
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.COMMON_INTRO_DATA, responses -> {
            try {
                JSONObject response = new JSONObject(responses);

                if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.ERROR);
                    Log.d("Response", responses);
                }

                if (response.getString("status").equals("1")) {
                   JSONObject detail = new JSONObject(response.getString("detail"));
                   JSONArray data = new JSONArray(detail.getString("data"));
                    if(test_alert)
                        Log.d("Data", data.toString());


                    /*mList.add(new ScreenItem("Fresh Food","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.intro1));
                    mList.add(new ScreenItem("Fast Delivery","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.intro2));
                    mList.add(new ScreenItem("Easy Payment","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.intro1));
*/


                    for (int i = 0; i < data.length(); i++) {
                        JSONObject details = new JSONObject(data.getString(i));
                        mList.add(new ScreenItem(
                                details.getString("title"),
                                details.getString("summary"),
                                API.IMAGE_URL_INTRO+details.getString("image")
                        ));
                    }

                        // setup viewpager
                        screenPager =findViewById(R.id.screen_viewpager);
                        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
                        screenPager.setAdapter(introViewPagerAdapter);

                        // setup tablayout with viewpager

                        tabIndicator.setupWithViewPager(screenPager);

                    /*if(filterKey.equals("all")) {
                        fillCourse(detail.getString("courses"));
                    }

                    if (data.length() < 1) {
                        Toasts.makeText(context, "Not enough data to show", ToastType.SUCCESS);
                        finish();
                    }
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject details = new JSONObject(data.getString(i));
                        modelInstructors.add(new ModelInstructor(
                                details.getInt("id"),
                                details.getString("name"),
                                details.getString("photo"),
                                ""
                        ));

                        binding.rvMyAllInstructor.setLayoutManager(new GridLayoutManager(context, 2));
                        binding.rvMyAllInstructor.setAdapter(new AdapterInstructor(this, modelInstructors));
                    }*/
                } else {

                }
            } catch (JSONException e) {
                Log.d("_CatchListener", e.toString());
            } finally {
                //binding.loader.setVisibility(View.GONE);
            }
        }, error -> {
            //binding.loader.setVisibility(View.GONE);
            Log.d("_ErrorListener", error.toString());
        }) {/*
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put(API.Accept, API.ApplicationJSON);
                map.put(API.Authorization, Session.getToken(pref));
                return map;
            }*/
        });
    }
}