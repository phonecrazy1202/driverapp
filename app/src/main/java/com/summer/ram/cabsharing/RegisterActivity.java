package com.summer.ram.cabsharing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class RegisterActivity extends AppCompatActivity {


    EditText name, password, re_password, contact_no;
    TextView txLogin;
    String server_ip, url, user_type;
    ProgressDialog dialog;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            server_ip = "https://10.0.2.2:8000";

            txLogin = (TextView) findViewById(R.id.b_login);
            name = (EditText) findViewById(R.id.et_name);
            password = (EditText) findViewById(R.id.et_password);
            re_password = (EditText) findViewById(R.id.et_re_password);
            contact_no = (EditText) findViewById(R.id.et_contact_no);
            register = (Button) findViewById(R.id.btn_signup);

            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = server_ip.concat("/register");
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Name is empty!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(contact_no.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Contact no is empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(re_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this,"Re-Password field is empty", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(re_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Passwords not matching!",Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage("Registering...");
                    dialog.show();
                    sendData();
                }


//                Intent register_new_user = new Intent("com.example.ayush.krishi_help.register");
//                startActivity(register_new_user);
            }
        });

        txLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        }
    public void sendData() {
        Log.d("Check", "Sending data opened");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://desolate-reef-33512.herokuapp.com/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_LONG).show();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("driver_number", contact_no.getText().toString().trim() );
                        editor.commit();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
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
                        Toast.makeText(RegisterActivity.this,"Connection error",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("number", contact_no.getText().toString().trim());
                params.put("password", password.getText().toString());
                params.put("name", name.getText().toString().trim());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

/*
        RequestParams params = new RequestParams();

        params.put("name", name.getText().toString());
        params.put("number", contact_no.getText().toString());
        params.put("password", password.getText().toString());
//        params.setUseJsonStreamer(true);

//        params.put("first_name", f_name.getText().toString());





        AsyncHttpClient client = new AsyncHttpClient();


        AsyncHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                String str = new String(bytes, "UTF-8");
                try {
                    if (response.getString("status").equals("fail")){
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration failed. The number exists", Toast.LENGTH_LONG).show();
                        Intent fIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(fIntent);
                    }
                    else if (response.getString("status").equals("success")) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Successfully register", Toast.LENGTH_SHORT).show();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("driver_number", contact_no.getText().toString().trim());
                        editor.commit();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                    }
                    Log.d("JSON", response.getString("status"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                response.getInt("status");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Error in registration!", Toast.LENGTH_SHORT).show();
            }
        };


        client.post(url, params, responseHandler);*/
    }

}
