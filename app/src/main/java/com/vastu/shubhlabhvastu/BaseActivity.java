package com.vastu.shubhlabhvastu;

import static androidx.core.view.GravityCompat.START;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.bumptech.glide.Glide;
import com.vastu.shubhlabhvastu.Activity.Home;
import com.vastu.shubhlabhvastu.Activity.MyAccount;
import com.vastu.shubhlabhvastu.Activity.SignIn;
import com.vastu.shubhlabhvastu.Connection.CheckConnection;
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private LinearLayout llProfileContainer;
    private SharedPreferences pref;
    private Context context;
    private TextView tvName, tvEmail,tvMobile,menu_app_version,txt_welcome,txt_name;
    //public CircleImageView imageView;
    public Boolean test_alert=false;
    LinearLayout contentView;

    static final float END_SCALE = 0.1f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = getApplicationContext();

        pref = PreferenceManager.getDefaultSharedPreferences(context);

        contentView = findViewById(R.id.content);




         txt_welcome = findViewById(R.id.txt_welcome);
         txt_name = findViewById(R.id.txt_name);
         tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
       // imageView = findViewById(R.id.imageView);
        llProfileContainer = findViewById(R.id.llProfileContainer);
         menu_app_version = findViewById(R.id.menu_app_version);
         menu_app_version.setText("App Version "+ BuildConfig.VERSION_NAME);

       /* if (!Session.getImage(pref).equals(""))
            Glide.with(context).load(Session.getImage(pref)).placeholder(R.drawable.compas).into(imageView);*/

        if (!Session.getUserName(pref).equals("")) txt_welcome.setText( "Welcome Back !");
        else txt_welcome.setText( "Welcome Guest !");
        if (!Session.getUserName(pref).equals("")) txt_name.setText( Session.getUserName(pref).toUpperCase());
        else txt_name.setText("Hi kindly login to view all pages..");
        if (!Session.getUserName(pref).equals("")) tvName.setText( Session.getUserName(pref).toUpperCase());
        if (!Session.getUserEmail(pref).equals("")) tvEmail.setText(Session.getUserEmail(pref));
        if (!Session.getUserMobile(pref).equals("")) tvMobile.setText("+91-"+Session.getUserMobile(pref));

         // llProfileContainer.setOnClickListener(v -> replaceActivity(StudentDashboard.class));

        initToolbar();

        drawerAction();

        addRippleEffect();

        menuClickListener();
    }

    private void menuClickListener() {

        if (!Session.getIsSession(pref)) {
            findViewById(R.id.menu_logout).setVisibility(View.GONE);
            findViewById(R.id.menu_login).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.menu_login).setOnClickListener(v -> {
              replaceActivity(SignIn.class);
        });

        findViewById(R.id.menu_home).setOnClickListener(v -> {
            if (Session.getStudentId(pref).equals(""))   { replaceActivity(SignIn.class); Toasts.makeText(context, "Kindly signup / login first", ToastType.ERROR); }
            else replaceActivity(MyAccount.class);
        });
        findViewById(R.id.menu_order).setOnClickListener(v -> {
            if (Session.getStudentId(pref).equals(""))   { replaceActivity(SignIn.class); Toasts.makeText(context, "Kindly signup / login first", ToastType.ERROR); }
            else replaceActivity(MyAccount.class);
        });
        findViewById(R.id.menu_rate).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
            startActivity(i);
        });
        findViewById(R.id.menu_refer).setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out "+ context.getString(R.string.app_name)+" app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
        findViewById(R.id.menu_support).setOnClickListener(v -> {
            if (Session.getStudentId(pref).equals(""))   { replaceActivity(SignIn.class); Toasts.makeText(context, "Kindly signup / login first", ToastType.ERROR); }
            else replaceActivity(MyAccount.class);
        });
       /* findViewById(R.id.menu_pricing).setOnClickListener(v -> {
            replaceActivity(Pricing.class);
        });
        //findViewById(R.id.menu_pricing).setVisibility(View.GONE);

        findViewById(R.id.menu_careers).setOnClickListener(v -> {
            replaceActivity(Careers.class);
        });
        //findViewById(R.id.menu_careers).setVisibility(View.GONE);

        findViewById(R.id.menu_terms_and_condition).setOnClickListener(v -> {
            replaceActivity(TermsCondition.class);
        });
        findViewById(R.id.menu_privacy_policy).setOnClickListener(v -> {
            replaceActivity(PrivacyPolicy.class);
        });
        findViewById(R.id.menu_legal).setOnClickListener(v -> {
            replaceActivity(Legal.class);
        });
        findViewById(R.id.menu_contact).setOnClickListener(v -> {
            replaceActivity(Contact.class);
        });
        findViewById(R.id.menu_student).setOnClickListener(v -> {
            replaceActivity(StudentDashboard.class);
        });
        findViewById(R.id.menu_teacher).setOnClickListener(v -> {
            replaceActivity(TeacherDashboard.class);
        });
        findViewById(R.id.menu_about).setOnClickListener(v -> {
            replaceActivity(About.class);
        });*/
      /*  findViewById(R.id.menu_share).setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out Anywhere Learning app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });*/
       /* findViewById(R.id.menu_developed_by).setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.friendzitsolutions.biz/"));
            startActivity(i);
        });*/
       findViewById(R.id.menu_logout).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle("Warning")
                    .setMessage("Are you sure to logout... Click yes for Logout")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logoutUser(dialog);
                        }
                    })
                    .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }

    private void replaceActivity(Class classs) {
        drawerLayout.closeDrawer(START);



        CommonTask.redirectFinishActivity(BaseActivity.this, classs);
    }

    private void addRippleEffect() {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {*/
           // findViewById(R.id.menu_home).setBackground(getDrawable(R.drawable.ripple));
         /*   findViewById(R.id.menu_my_batches).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_all_courses).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_all_classes).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_pricing).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_careers).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_terms_and_condition).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_privacy_policy).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_legal).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_contact).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_student).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_teacher).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_about).setBackground(getDrawable(R.drawable.ripple));
            findViewById(R.id.menu_logout).setBackground(getDrawable(R.drawable.ripple));*/
       // }
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!

        //To make it transparent use Color.Transparent in side setScrimColor();

        //drawerLayout.setScrimColor(Color.TRANSPARENT);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override

            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);

                final float offsetScale = 1 - diffScaledOffset;

                contentView.setScaleX(offsetScale);

                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width

                final float xOffset = drawerView.getWidth() * slideOffset;

                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;

                final float xTranslation = xOffset - xOffsetDiff;

                contentView.setTranslationX(xTranslation);

            }

        });

    }

    private void drawerAction() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        animateNavigationDrawer();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.front, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

       /* if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle("Warning")
                    .setMessage("Are you sure to logout... Click yes for Logout")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logoutUser(dialog);
                        }
                    })
                    .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser(DialogInterface dialogInterface) {
       /* Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.LOGOUT, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {
                    dialogInterface.dismiss();
                    Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);
                    Session.destroySession(pref);
                    CommonTask.redirectFinishActivity(BaseActivity.this, Choose.class);
                } else {
                    Toasts.makeText(context, responses, ToastType.ERROR);
                }
            } catch (JSONException e) {
                Log.d("logout_CatchListener", e.toString());
            } finally {

            }
        }, error -> {
            Log.d("logout_ErrorListener", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(API.Accept, API.ApplicationJSON);
                map.put(API.Authorization, Session.getToken(pref));
                return map;
            }
        });*/
        Session.destroySession(pref);
        dialogInterface.dismiss();
        Toasts.makeText(context, "Logout successfully !!!", ToastType.SUCCESS);
        Session.destroySession(pref);
        CommonTask.redirectFinishActivity(BaseActivity.this, SignIn.class);
    }

    @Override
    protected void onStart() {
        CheckConnection.registerService(BaseActivity.this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        CheckConnection.unregisterService(BaseActivity.this);
        super.onStop();
    }


    public void last_access(Context context) {

     /*   Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, API.STUDENT_LAST_ACCESS, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                Log.d("Response", responses);
                if (response.getString("status").equals("1")) {

                }
                else
                {

                }
            } catch (JSONException e) {
                Log.d("allCour_CatchListener", e.toString());
            } finally {
            }
        }, error -> {
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
        });*/
    }

    public SSLSocketFactory getSocketFactory() {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = getResources().openRawResource(R.raw.anywherelearning_in);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo("devindia.in") == 0; //The Hostname of your server.
                }
            };


            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            SSLContext context = null;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            SSLSocketFactory sf = context.getSocketFactory();
            return sf;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validate_login()
    {
        boolean ret=false;
        if (Session.getIsSession(pref))
            ret= true;
        if (!Session.getIsSession(pref))
            ret= false;
        return ret;
    }
 /*Toasts.makeText(context, "Session Expired !!! Kindly Login...", ToastType.ERROR);
            CommonTask.redirectFinishActivity(BaseActivity.this, SignIn.class);*/

}