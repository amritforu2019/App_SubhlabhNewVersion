package com.vastu.shubhlabhvastu.Activity;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vastu.shubhlabhvastu.BaseActivity;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Session.Session;
import com.vastu.shubhlabhvastu.Task.API;
import com.vastu.shubhlabhvastu.Task.ToastType;
import com.vastu.shubhlabhvastu.Task.Toasts;
import com.vastu.shubhlabhvastu.Task.Validate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryForm extends BaseActivity {
    TextInputEditText etDob,etName,etMobile,etEmail,etTrn_ref_no;
    TextInputLayout  IL_DOB;
    TextView etPackageSummary,txtMarquee,tv_qr;
    private static final String TAG = "TOKEN_ERROR";
    private ImageView iv_Goback,imageCode;
    private Context context;
    private SharedPreferences pref;
    private ProgressBar loader;
    AutoCompleteTextView etGenderDDL;
    Spinner spPackage;
    LinearLayout ILPackage;
    Button btn_GotoForm;
    private String exp_id,title,exp_typ,exp_typ_name,package_id,packagePrice,qr_ref_no,QrText;
    Uri payUrl;

    //

    WebView browser;

    ///Dialog Box/////
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    ///Dialog Box/////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_form);
        initView();
        intentData();
        //get_page_data();
        click_action();
        fill_State();
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
        loader = findViewById(R.id.loader);
        etDob=findViewById(R.id.etDob);
        IL_DOB=findViewById(R.id.IL_DOB);
        etName=findViewById(R.id.etName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etGenderDDL=findViewById(R.id.etGenderDDL);
        spPackage=findViewById(R.id.spPackage);
        etPackageSummary=findViewById(R.id.etPackageSummary);
        ILPackage=findViewById(R.id.ILPackage);
        btn_GotoForm=findViewById(R.id.btn_GotoForm);
        imageCode=findViewById(R.id.imageCode);
        etTrn_ref_no=findViewById(R.id.etTrn_ref_no);
        loader.setVisibility(View.GONE);
        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);

        browser = (WebView) findViewById(R.id.webview);
        tv_qr =   findViewById(R.id.tv_qr);


    }
    private void intentData()
    {
        try {
            Bundle bundle = getIntent().getExtras();
            exp_id = bundle.getString("exp_id", "");
            title = bundle.getString("title", "");
            exp_typ = bundle.getString("exp_typ", "");
            exp_typ_name = bundle.getString("exp_typ_name", "");
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


        /////Fill Gender ///////
        String[] type = new String[] {"MALE", "FEMALE"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        type);
        etGenderDDL.setAdapter(adapter);
        /////Fill Gender////////
        if (!Session.getUserName(pref).equals("")) etName.setText( Session.getUserName(pref).toUpperCase());
        if (!Session.getUserEmail(pref).equals("")) etEmail.setText(Session.getUserEmail(pref));
        if (!Session.getUserMobile(pref).equals("")) etMobile.setText(Session.getUserMobile(pref));



    }
    private void click_action()
    {
        etDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b) {
                    ////Default Date Select/////
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    ////Default Date Select/////

                    DatePickerDialog mDatePickerDialog = new DatePickerDialog(QueryForm.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            etDob.setText(day + "/" + (month + 1) + "/" + year);

                            try {
                                //String pattern = "yyyy-MM-dd HH:mm:ss";
                                String pattern = "dd/MM/yyyy";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                Date date = simpleDateFormat.parse(etDob.getText().toString());
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String format = formatter.format(date);
                                etDob.setText(format);
                                //get_pre_Schdule(provider_id,"1",format);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, year, month, dayOfMonth);
                    calendar.set(year, month, dayOfMonth);

                    /////Before date if needed//
					/*, year-15, month, dayOfMonth);
					calendar.set(year-15,month,dayOfMonth);*/

                    long value = calendar.getTimeInMillis();
                   // mDatePickerDialog.getDatePicker().setMinDate(value);
                 //mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 365*60*60*24*365 );
                 mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() );
                    mDatePickerDialog.show();
                }
            }
        });
       /* etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////Default Date Select/////
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                ////Default Date Select/////

                DatePickerDialog mDatePickerDialog = new DatePickerDialog(QueryForm.this,android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        etDob.setText(day + "/" + (month + 1) + "/" + year);

                        try {
                            //String pattern = "yyyy-MM-dd HH:mm:ss";
                            String pattern = "dd/MM/yyyy";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            Date date = simpleDateFormat.parse(etDob.getText().toString());
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String format = formatter.format(date);
                            etDob.setText(format);
                            //get_pre_Schdule(provider_id,"1",format);
                        }
                        catch (ParseException e){ e.printStackTrace();}

                    }
                }, year, month, dayOfMonth);
                calendar.set(year,month,dayOfMonth);

                /////Before date if needed//
					*//*, year-15, month, dayOfMonth);
					calendar.set(year-15,month,dayOfMonth);*//*

                long value=calendar.getTimeInMillis();
                mDatePickerDialog.getDatePicker().setMinDate(value);
                // mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 365*60*60*24*365 );
                mDatePickerDialog.show();


            }
        });*/

        spPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StateList st=(StateList) parent.getSelectedItem();
                package_id=getSelectedPackageId(st);
                packagePrice=getSelectedPackagePrice(st);
                if(!"0".equals(package_id))
                {
                    Calendar calendar=Calendar.getInstance();
                      qr_ref_no  = String.valueOf(calendar.getTimeInMillis());
                    //etPackageSummary.setText(Html.fromHtml(getSelectedPackage(st)));


                    browser.getSettings().setJavaScriptEnabled(true);
                    String htm="<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><head><meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=iso-8859-1\\\"><title>Lorem Ipsum</title></head><body>"+getSelectedPackage(st)+"</body></html>";
                    browser.loadData(htm, "text/html", "UTF-8");

                    ILPackage.setVisibility(View.VISIBLE);



                    //avsconsulting540@okaxis
                    QrText="upi://pay?pa=9695639782@okbizaxis&pn=Dev India IT Services&am="+packagePrice+"&tn="+qr_ref_no+"&cu=INR&mc=null&mcc=BCR2DN4TZTQY3TSH";

                    payUrl = new Uri.Builder()
                            .scheme("upi")
                            .authority("pay")
                            .appendQueryParameter("pa", "9695639782@okbizaxis")
                            .appendQueryParameter("pn", "Dev India IT Services")
                            .appendQueryParameter("mc", "null")
                            .appendQueryParameter("tid", qr_ref_no)
                            .appendQueryParameter("mcc", "BCR2DN4TZTQY3TSH")
                            .appendQueryParameter("tr", qr_ref_no)
                            .appendQueryParameter("tn", "Subhlabh")
                            .appendQueryParameter("am", packagePrice+".00")
                            .appendQueryParameter("cu", "INR")
                            /*.appendQueryParameter("url", "")*/
                            .build();

                    if(test_alert)
                    {
                        Toasts.makeText(context, payUrl.toString(), ToastType.SUCCESS);
                        Log.d("QrText", payUrl.toString());
                    }
                    Log.d("QrText", payUrl.toString());
                    //initializing MultiFormatWriter for QR code
                    MultiFormatWriter mWriter = new MultiFormatWriter();            try {
                    //BitMatrix class to encode entered text and set Width & Height
                    BitMatrix mMatrix = mWriter.encode(payUrl.toString(), BarcodeFormat.QR_CODE, 400,400);
                    BarcodeEncoder mEncoder = new BarcodeEncoder();
                    Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                    imageCode.setImageBitmap(mBitmap);//Setting generated QR code to imageView                // to hide the keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                   // manager.hideSoftInputFromWindow(etText.getApplicationWindowToken(), 0);
                    } catch (WriterException e) {
                    e.printStackTrace();
                }

                }
                else
                {
                    etPackageSummary.setText("");
                    ILPackage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_GotoForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        etTrn_ref_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code

                 btn_GotoForm.setVisibility(View.VISIBLE);



            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        iv_Goback.setOnClickListener(v-> this.finish());

        tv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //payUsingUpi("AVS consultancy services","avsconsulting540@okaxis",package_id,packagePrice,qr_ref_no);
                payUsingUpi();
            }
        });



    }

    final int UPI_PAYMENT = 0;
    void payUsingUpi(  ) {

        //QrText="upi://pay?pa="+upiId+"&pn="+name+"&am="+amount+"&tn="+tid+"&cu=INR&mc="+mc_code+"";




        if(test_alert)
        {
            Toasts.makeText(context, payUrl.toString().toString(), ToastType.SUCCESS);
            Log.d("QrText_byQuick", payUrl.toString().toString());
        }
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(payUrl);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(QueryForm.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(QueryForm.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(QueryForm.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(QueryForm.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(QueryForm.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(QueryForm.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /////////////////Spin State//////////////////////
    public void fill_State()
    {
        loader.setVisibility(View.VISIBLE);
        final String URL= API.PACKAGELIST+"&type="+exp_typ;
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(GET, URL, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.SUCCESS);
                    Log.d("Response", responses + URL);
                }

                if (response.getString("status").equals("1")) {
                    if(test_alert) {
                        Toasts.makeText(context, response.getString("messege"), ToastType.SUCCESS);
                    }
                    ////Ceate Listing
                    List<StateList> stateList =  new ArrayList<>();
                    StateList s1= new StateList("0",
                            "",
                            "--Select Package--",
                            "",
                            "0");
                    stateList.add(s1);


                    ///Add Json Value In List
                    JSONArray jsonArray = response.optJSONArray("detail");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                         StateList s2= new StateList(
                                 jsonObject.getString("id"),
                                 jsonObject.getString("package_typ"),
                                 jsonObject.getString("topic_name"),
                                 jsonObject.getString("topic_summary"),
                                 jsonObject.getString("price"));
                        stateList.add(s2);
                    }

                    /// Bound Array list with spinner
                    ArrayAdapter<StateList> adapterState = new ArrayAdapter<StateList>(context, R.layout.holder_spinner, stateList);
                    adapterState.setDropDownViewResource(R.layout.holder_spinner);
                    spPackage.setAdapter(adapterState);

                    ////Select Value If Avaliabel//
                                    /*if(!sess.get_state(prefs).equals(null))
                                    {
                                        glb.selectSpinnerValue(sp_State,sess.get_state(prefs));
                                    }*/

                    //glb.send(ToastType.SUCCESS,jsonObject1.getString("message"),context);
                    loader.setVisibility(View.GONE);

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

    public class StateList
    {
        private String id,package_typ,topic_name,topic_summary,price;
        String rate;

        public StateList(String id, String package_typ, String topic_name, String topic_summary, String price) {
            this.id = id;
            this.package_typ = package_typ;
            this.topic_name = topic_name;
            this.topic_summary = topic_summary;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPackage_typ() {
            return package_typ;
        }

        public void setPackage_typ(String package_typ) {
            this.package_typ = package_typ;
        }

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public String getTopic_summary() {
            return topic_summary;
        }

        public void setTopic_summary(String topic_summary) {
            this.topic_summary = topic_summary;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString()
        {
            if(price!="0")
                 rate=" Price : "+price;
            else rate="";

            return topic_name +  rate;
        }
    }

    public String getSelectedPackageId(StateList sl)
    {
        sl=(StateList)spPackage.getSelectedItem();
        return sl.getId();
    }
    public String getSelectedPackagePrice(StateList sl)
    {
        sl=(StateList)spPackage.getSelectedItem();
        return sl.getPrice();
    }
    public String getSelectedPackage(StateList sl)
    {
        sl=(StateList)spPackage.getSelectedItem();
        return sl.getTopic_summary();
    }
    /////////////////Spin State//////////////////////

    private void sendRequest() {
        loader.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory())).add(new StringRequest(POST, API.SAVEQUERY, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                if(test_alert)
                {
                    Toasts.makeText(context, response.toString(), ToastType.ERROR);
                    Log.d("Response", responses);
                }

                if (response.getString("status").equals("1")) {
                    //Toasts.makeText(context, response.getString("message"), ToastType.SUCCESS);

                    //JSONObject data = new JSONObject(response.getString("data"));
                    //Toasts.makeText(context, data.getString("id"), ToastType.SUCCESS);
                    //this.finish();
                    //showAlertDialog(R.layout.dialog_negative_layout,"Ohh Sorry !!!","Something not good retry....");

                    showAlertDialog(R.layout.dialog_positive_layout,"Congratulations","Your query submitted successfully we will contact you soon...");

                } else {
                    showAlertDialog(R.layout.dialog_negative_layout,"Ohh Sorry !!!","Something not good retry....");

                    //Toasts.makeText(context, response.getString("message"), ToastType.ERROR);
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
                map.put("email", Validate.returnString(etEmail));
                map.put("name", Validate.returnString(etName));
                map.put("mobile", Validate.returnString(etMobile));
                map.put("POST-TYPE", "QUERY");
                map.put("inq_id", exp_id);
                map.put("trn_typ", "QR_UPI");
                map.put("trn_amt", packagePrice);
                map.put("package_id", package_id);
                map.put("dob",Validate.returnString(etDob));
                map.put("gender",etGenderDDL.getText().toString());
                map.put("qr_ref_no",qr_ref_no);
                map.put("trn_ref_no",Validate.returnString(etTrn_ref_no));
                Log.d("param", map.toString());
                return map;
            }
        });
    }

    private void showAlertDialog(int layout,String title,String msg){
        dialogBuilder = new AlertDialog.Builder(QueryForm.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        TextView txt_top = layoutView.findViewById(R.id.textView);
        TextView txt_msg = layoutView.findViewById(R.id.textView2);
        txt_top.setText(title);
        txt_msg.setText(msg);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                closeme();

            }


        });
    }

    private void closeme()
    {
        this.finish();
    }


}