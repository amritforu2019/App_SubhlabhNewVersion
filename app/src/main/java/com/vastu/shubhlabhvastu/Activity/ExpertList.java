package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vastu.shubhlabhvastu.Adapter.AdapterAllExpert;
import com.vastu.shubhlabhvastu.Adapter.AdapterAllExpertFull;
import com.vastu.shubhlabhvastu.Adapter.AdapterAllExpertSearch;
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

public class ExpertList extends BaseActivity {
    private static final String TAG = "TOKEN_ERROR";
    private ImageView iv_Banner,iv_Goback;
    private TextView tv_Name,tv_title,tv_Summary,tv_GotoList;
    private Context context;
    private SharedPreferences pref;
    private ProgressBar loader;
    private  String exp_typ,title,sort_summary;
    private ArrayList<ModelAllExpert> modelAllExpert;
    List<ModelAllExpert> filtered = new ArrayList<ModelAllExpert>();
    RecyclerView rvList;
    private EditText et_head_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_list);
        initView();
        intentData();
        filldata();
        onclickAction();
    }
    @Override
    public void onBackPressed()
    {
        this.finish();
        //CommonTask.redirectFinishActivity(ExpertList.this, ExpertSummary.class);
    }
    private void initView()
    {
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        iv_Goback =  findViewById(R.id.iv_Goback);
        loader = findViewById(R.id.loader);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Summary = findViewById(R.id.tv_Summary);
        rvList = findViewById(R.id.rvList);
        et_head_search = findViewById(R.id.et_head_search);
    }
    private void intentData() {
        try {
            Bundle bundle = getIntent().getExtras();
            exp_typ = bundle.getString("exp_typ", "");
            sort_summary = bundle.getString("sort_summary", "");
            title = bundle.getString("title", "");
            tv_Name.setText(title);
            tv_Summary.setText(sort_summary);
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
    private void filldata() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.EXPERT+"?GET-TYPE=LIST&type="+exp_typ, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    modelAllExpert = new ArrayList<>();
                    //JSONObject  jsonRootObject = new JSONObject(response.getString("detail"));
                    JSONArray jsonArray = response.optJSONArray("detail");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        modelAllExpert.add(new ModelAllExpert(
                                jsonObject.getString("id"),
                                jsonObject.getString("t_name"),
                                jsonObject.getString("c_typ_name"),
                                jsonObject.getString("image"),
                                jsonObject.getString("edu_year"),
                                jsonObject.getString("rating")
                        ));
                    }
                    rvList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    rvList.setAdapter(new AdapterAllExpertFull(ExpertList.this, modelAllExpert));








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
    private void onclickAction()
    {
        iv_Goback.setOnClickListener(v-> this.finish());

        et_head_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals("")) {
                    filtered.clear();
                    rvList.setAdapter(new AdapterAllExpertSearch());
                    for (ModelAllExpert article : modelAllExpert) {
                        String title=article.getT_name().toLowerCase();
                        if (title.contains(charSequence.toString().toLowerCase()))
                            filtered.add(article);
                    }
                    rvList.setAdapter(new AdapterAllExpertSearch());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}