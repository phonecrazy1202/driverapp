package com.summer.ram.cabsharing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText etLogin, etPassword;
    Button bLogin;
    TextView tvRegister;
    ProgressDialog dialog;
    String server_ip, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin = (EditText) findViewById(R.id.et_login_number);
        etPassword = (EditText) findViewById(R.id.et_login_pass);
        bLogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.b_register);
        server_ip = "http://10.0.2.2:8000";
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        url = server_ip.concat("/login");

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBU","Clicked");
                if (TextUtils.isEmpty(etLogin.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "No number entered", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "No password entered", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage("Logging in");
                    dialog.show();
                    sendData();
                }
            }
        });


    }

    public void sendData() {
        Log.d("Check", "Sending data opened");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://desolate-reef-33512.herokuapp.com/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Get response",response);
                        dialog.dismiss();
                        //Toast.makeText(LoginActivity.this,response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String login_status = jsonObject.getString("status");
                            Log.d("DEBUG","Status is "+login_status);
                            login_status=login_status.trim();
                            if (login_status.equals("correct"))
                            {
                                Toast.makeText(LoginActivity.this,"Authenticated",Toast.LENGTH_SHORT);
                                Log.d("I am awesome", "Success");
                                String number = etLogin.getText().toString().trim();
                                Log.d("number",number);
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("driver_number", number);
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else if (login_status.equals("fail")) {
                                dialog.dismiss();
                                String reason = jsonObject.getString("reason");
                                Toast.makeText(LoginActivity.this, reason , Toast.LENGTH_SHORT).show();
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
                        dialog.dismiss();;
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {
                            try {

                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.d("here123",res.toString());
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
                        //Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this,"Connection error! Please check your internet connection",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("number", etLogin.getText().toString().trim());
                params.put("password", etPassword.getText().toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}