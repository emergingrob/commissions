package com.emergingmobile.commissions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import com.emergingmobile.commissions.R;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
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

    Button btnGet;
    //TextView tvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tvw=(TextView)findViewById(R.id.textView1);

        setupListeners();

        eText=(EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText((monthOfYear + 1)  + "/" + dayOfMonth + "/" + year);
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

        accessoryEditText = (EditText) findViewById(R.id.edit_accessories);
        accessoryBucketTextView = (TextView) findViewById(R.id.accessory_bucket);

        accessoryEditText.addTextChangedListener(new TextChangedListener<EditText>(accessoryEditText) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                accessoryBucketTextView.setText("$" + 0.35f * Float.valueOf(s.toString()));
            }
        });

        accessoryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // When focus changes, submit changes to the dynamodb via our flask app.
                if (!hasFocus) {
                    //
                    postUpdatesToFlask();
                }
            }
        });
    }

    /*
    Method to submit updates to Flask app.
     */
    private void postUpdatesToFlask() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("accessory_rev", "125")
                .add("tel", "90301171XX")
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-54-145-89-95.compute-1.amazonaws.com")
                .post(formBody)
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

    /*
    This method was posted on zMan on stackoverflow. Provides an easy way
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