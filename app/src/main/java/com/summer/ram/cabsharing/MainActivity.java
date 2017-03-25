package com.summer.ram.cabsharing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String status;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if(isNetworkAvailable()==false)
        {
            //no valid internet
            Toast.makeText(MainActivity.this,"No internet!",Toast.LENGTH_SHORT);
            onPause();
        }

        RadioButton availableButton = (RadioButton)findViewById(R.id.radio_available);
        RadioButton notavailableButton = (RadioButton)findViewById(R.id.radio_notavailable);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioid);
        Button logout = (Button)findViewById(R.id.buttonLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_available)
                {
                    //give a POST request to API to change status to "Free"
                    status = "Free";
                    Log.d("DEBUUG","Available clicked");
                    sendData(status);

                }
                else
                {
                    //give a POST request to API to change status to "Busy"
                    status = "Not available";
                    Log.d("DEBUUG","Not available clicked");
                    sendData(status);
                }
            }
        });


    }
    public void sendData(final String status) {
        Log.d("Check", "Sending data opened");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://desolate-reef-33512.herokuapp.com/update_status",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Get response",response);
                       // Toast.makeText(LoginActivity.this,response, Toast.LENGTH_LONG).show();
                        String number="None";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String login_status = jsonObject.getString("status");
                            Log.d("DEBUG","Status is "+login_status);
                            login_status=login_status.trim();
                            if (login_status.equals("success"))
                            {
                                Log.d("I am awesome", "Success");
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                number = settings.getString("driver_number","None");
                                if(number.equals("None"))
                                    return;

                                //String number = etLogin.getText().toString().trim();
                                Log.d("number",number);
                                //startActivity(new Intent(LoginActivity.this, MainActivity.class));


                                //Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else if (login_status.equals("fail")) {
                               // dialog.dismiss();
                                String reason = "Failed";
                                Toast.makeText(MainActivity.this, reason , Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Log.d("DEBUUG","abc123"+login_status+"mnmn");
                            }
                        }
                        catch(Exception e)
                        {
                            Log.d("DEBUG","Not JSON");
                        }
                        /**/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dialog.dismiss();;
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {
                            try {

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.d("here",res.toString());
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                        Toast.makeText(MainActivity.this,"Connection error",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String number = settings.getString("driver_number","None");
                params.put("number", number);
                String change_status = status;
                params.put("status",status);
                Log.d("Put status","Put status "+change_status);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
