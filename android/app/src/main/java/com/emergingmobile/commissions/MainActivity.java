package com.emergingmobile.commissions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.emergingmobile.commissions.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String userId;
    private String formattedDate;
    private String baseUrl = "http://ec2-3-82-196-11.compute-1.amazonaws.com:5000";

    private float accessoryValue = 0;
    private int humValue, humxValue, jetpacksValue, tabletsValue, tmpValue, tmpMdValue,
            vzProtectValue, vzProtectMdValue, nativeDialerValue = 0;

    float accessoryRevPayout = 0.35f;
    int tmpPayout = 60;
    int tmpMdPayout = 180;
    int vzProtectPayout = 70;
    int vzProtectMdPayout = 210;
    int tabletPayout = 200;
    int jetpackPayout = 200;
    int humPayout = 50;
    int humxPayout = 200;
    int nativeDialerPayout = 200;

    DatePickerDialog picker;
    EditText eText;

    EditText accessoryEditText;
    TextView accessoryBucketTextView;

    EditText tmpEditText;
    TextView tmpBucketTextView;

    EditText tmpMdEditText;
    TextView tmpMdBucketTextView;

    EditText vzProtectEditText;
    TextView vzProtectBucketTextView;

    // VZ Protect MD
    EditText vzProtectMdEditText;
    TextView vzProtectMdBucketTextView;
    // Tablets
    EditText tabletsEditText;
    TextView tabletsBucketTextView;
    // Jetpacks
    EditText jetpacksEditText;
    TextView jetpacksBucketTextView;
    // Hums
    EditText humsEditText;
    TextView humsBucketTextView;
    // Hum X
    EditText humxEditText;
    TextView humxBucketTextView;
    // Native Dialer
    EditText nativeDialerEditText;
    TextView nativeDialerBucketTextView;

    Button btnGet;
    //TextView tvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tvw=(TextView)findViewById(R.id.textView1);

        setupListeners();

        userId = AWSMobileClient.getInstance().getUsername();

        Date date = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = dateFormat.format(date);

        eText=(EditText) findViewById(R.id.editDateText);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setText(formattedDate);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText((monthOfYear + 1)  + "/" + dayOfMonth + "/" + year);
                                String formattedMonth = String.format("%02d", monthOfYear + 1);
                                String formattedDay = String.format("%02d", dayOfMonth);
                                formattedDate = formattedMonth + "/" + formattedDay + "/" + year;
                                loadDataFromFlask(formattedDate);
                            }
                        }, year, month, day);
                picker.show();

            }
        });
        /*
        btnGet=(Button)findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ eText.getText());
            }
        });

         */
    }

    private void setupListeners() {
    // When focus changes, submit changes to the dynamodb via our flask app.

        // Accessory Bucket

        accessoryEditText = (EditText) findViewById(R.id.edit_accessories);
        accessoryBucketTextView = (TextView) findViewById(R.id.accessory_bucket);

        accessoryEditText.addTextChangedListener(new TextChangedListener<EditText>(accessoryEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                accessoryBucketTextView.setText("$" + accessoryRevPayout * Float.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        accessoryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // TMP Bucket

        tmpEditText = (EditText) findViewById(R.id.edit_tmp);
        tmpBucketTextView = (TextView) findViewById(R.id.tmp_bucket);

        tmpEditText.addTextChangedListener(new TextChangedListener<EditText>(tmpEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                tmpBucketTextView.setText("$" + tmpPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        tmpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // TMP MD Bucket

        tmpMdEditText = (EditText) findViewById(R.id.edit_tmp_md);
        tmpMdBucketTextView = (TextView) findViewById(R.id.tmp_md_bucket);

        tmpMdEditText.addTextChangedListener(new TextChangedListener<EditText>(tmpEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                tmpMdBucketTextView.setText("$" + tmpMdPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        tmpMdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // VZ Protect

        vzProtectEditText = (EditText) findViewById(R.id.edit_vzp);
        vzProtectBucketTextView = (TextView) findViewById(R.id.vzp_bucket);

        vzProtectEditText.addTextChangedListener(new TextChangedListener<EditText>(vzProtectEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                vzProtectBucketTextView.setText("$" + vzProtectPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        vzProtectEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // VZ Protect MD

        vzProtectMdEditText = (EditText) findViewById(R.id.edit_vzpmd);
        vzProtectMdBucketTextView = (TextView) findViewById(R.id.vzpmd_bucket);

        vzProtectMdEditText.addTextChangedListener(new TextChangedListener<EditText>(vzProtectMdEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                vzProtectMdBucketTextView.setText("$" + vzProtectMdPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        vzProtectMdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // Tablets
        tabletsEditText = (EditText) findViewById(R.id.edit_tablets);
        tabletsBucketTextView = (TextView) findViewById(R.id.tablets_bucket);

        tabletsEditText.addTextChangedListener(new TextChangedListener<EditText>(tabletsEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                tabletsBucketTextView.setText("$" + tabletPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        tabletsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // Jetpacks
        jetpacksEditText = (EditText) findViewById(R.id.edit_jetpacks);
        jetpacksBucketTextView = (TextView) findViewById(R.id.jetpacks_bucket);

        jetpacksEditText.addTextChangedListener(new TextChangedListener<EditText>(jetpacksEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                jetpacksBucketTextView.setText("$" + jetpackPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        jetpacksEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // Hums
        humsEditText = (EditText) findViewById(R.id.edit_hums);
        humsBucketTextView = (TextView) findViewById(R.id.hums_bucket);

        humsEditText.addTextChangedListener(new TextChangedListener<EditText>(humsEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                humsBucketTextView.setText("$" + humPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        humsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // Hum X
        humxEditText = (EditText) findViewById(R.id.edit_humx);
        humxBucketTextView = (TextView) findViewById(R.id.humx_bucket);

        humxEditText.addTextChangedListener(new TextChangedListener<EditText>(humxEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                humxBucketTextView.setText("$" + humxPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        humxEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });

        // Native Dialer
        nativeDialerEditText = (EditText) findViewById(R.id.edit_native_dialer);
        nativeDialerBucketTextView = (TextView) findViewById(R.id.native_dialer_bucket);

        nativeDialerEditText.addTextChangedListener(new TextChangedListener<EditText>(nativeDialerEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                nativeDialerBucketTextView.setText("$" + nativeDialerPayout * Integer.valueOf(s.toString()));
                updateTotalBucket();
            }
        });

        nativeDialerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) { postUpdatesToFlask(); }
            }
        });
    }

    private void updateTotalBucket() {

    }

    /*
    Method to submit updates to Flask app.
     */
    private void postUpdatesToFlask() {
        OkHttpClient client = new OkHttpClient();

        JSONObject postData = new JSONObject();
        JSONObject postCommissionData = new JSONObject();

        try {
            if (!accessoryEditText.getText().toString().equals("")) { postCommissionData.put("accessory_rev", Float.valueOf(accessoryEditText.getText().toString())); }
            if (!humsEditText.getText().toString().equals("")) { postCommissionData.put("hum", Integer.valueOf(humsEditText.getText().toString())); }
            if (!humxEditText.getText().toString().equals("")) { postCommissionData.put("humx", Integer.valueOf(humxEditText.getText().toString())); }
            if (!jetpacksEditText.getText().toString().equals("")) { postCommissionData.put("jetpacks", Integer.valueOf(jetpacksEditText.getText().toString())); }
            if (!tabletsEditText.getText().toString().equals("")) { postCommissionData.put("tablets", Integer.valueOf(tabletsEditText.getText().toString())); }
            if (!tmpEditText.getText().toString().equals("")) { postCommissionData.put("tmp", Integer.valueOf(tmpEditText.getText().toString())); }
            if (!tmpMdEditText.getText().toString().equals("")) { postCommissionData.put("tmp_md", Integer.valueOf(tmpMdEditText.getText().toString())); }
            if (!vzProtectEditText.getText().toString().equals("")) { postCommissionData.put("vz_protect", Integer.valueOf(vzProtectEditText.getText().toString())); }
            if (!vzProtectMdEditText.getText().toString().equals("")) { postCommissionData.put("vz_protect_md", Integer.valueOf(vzProtectMdEditText.getText().toString())); }
            if (!nativeDialerEditText.getText().toString().equals("")) { postCommissionData.put("native_dialer", Integer.valueOf(nativeDialerEditText.getText().toString())); }

            postData.put("id", userId);
            postData.put("date", formattedDate);
            postData.put("comm", postCommissionData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, postData.toString());

        Request request = new Request.Builder()
                .url(baseUrl + "/write")
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response)
                throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexprected code " + response);
                } else {

                    //Toast.makeText(getApplicationContext(),"FLASK!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void loadDataFromFlask(final String loadDate) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/load").newBuilder();
        urlBuilder.addQueryParameter("id", userId);
        urlBuilder.addQueryParameter("date", loadDate);
        String loadUrl = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(loadUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {

                final String loadResponse = response.body().string();

                Log.e("GOTIT", loadResponse);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(loadResponse);
                            try {
                                accessoryEditText.setText(json.getJSONObject("comm").getString("accessory_rev"));
                            } catch (JSONException e) {
                                accessoryEditText.setText("0");
                            }

                            try {
                                humsEditText.setText(json.getJSONObject("comm").getString("hum"));
                            } catch (JSONException e) {
                                humsEditText.setText(getString(R.string.def_value));
                            }

                            try {
                                humxEditText.setText(json.getJSONObject("comm").getString("humx"));
                            } catch (JSONException e) {
                                humxEditText.getText().clear();
                            }

                            try {
                                jetpacksEditText.setText(json.getJSONObject("comm").getString("jetpacks"));
                            } catch (JSONException e) {
                                jetpacksEditText.getText().clear();
                            }

                            try {
                                tabletsEditText.setText(json.getJSONObject("comm").getString("tablets"));
                            } catch (JSONException e) {
                                tabletsEditText.getText().clear();
                            }

                            try {
                                tmpEditText.setText(json.getJSONObject("comm").getString("tmp"));
                            } catch (JSONException e) {
                                tmpEditText.getText().clear();
                            }

                            try {
                                tmpMdEditText.setText(json.getJSONObject("comm").getString("tmp_md"));
                            } catch (JSONException e) {
                                tmpMdEditText.getText().clear();
                            }

                            try {
                                vzProtectEditText.setText(json.getJSONObject("comm").getString("vz_protect"));
                            } catch (JSONException e) {
                                vzProtectEditText.getText().clear();
                            }

                            try {
                                vzProtectMdEditText.setText(json.getJSONObject("comm").getString("vz_protect_md"));
                            } catch (JSONException e) {
                                vzProtectMdEditText.getText().clear();
                            }

                            try {
                                nativeDialerEditText.setText(json.getJSONObject("comm").getString("native_dialer"));
                            } catch (JSONException e) {
                                nativeDialerEditText.getText().clear();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /*
    This method was posted by zMan on stackoverflow. Provides an easy way
    to pickup touches outside of our editText.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}