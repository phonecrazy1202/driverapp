/*
package com.summer.ram.cabsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


*/
/**
 * Created by ayush on 14/01/17.
 *//*

public class ActivityLogin extends AppCompatActivity {
    ImageView logo;
    EditText email,pass ;
    EditText f_name,l_name,password,re_password,contact_no,email_id;
    Button login,register ;  //creating button variables
    TextView tvRegister;
    ProgressDialog dialog;
    String server_ip,url,login_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        server_ip = "https://127.0.0.1:8000/";
        email = (EditText) findViewById(R.id.et_login_email);
        pass = (EditText) findViewById(R.id.et_login_pass);
        login = (Button) findViewById(R.id.btn_login) ;
        tvRegister = (TextView) findViewById(R.id.b_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLogin.this,ActivityRegister.class);
                startActivity(i);
            }
        });
        dialog= new ProgressDialog(ActivityLogin.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        url = server_ip.concat("/login");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(ActivityLogin.this,"No number entered", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(ActivityLogin.this,"No password entered", Toast.LENGTH_SHORT).show();
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
        RequestParams params = new RequestParams();
        params.put("email", email.getText().toString());
        params.put("pass", pass.getText().toString());

//        params.setUseJsonStreamer(true);

//        params.put("first_name", f_name.getText().toString());


        AsyncHttpClient client = new AsyncHttpClient();


        AsyncHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

//                String str = new String(bytes, "UTF-8");
                try {
                    String login_status;
                    login_status = response.getString("status");

                    if (login_status.equals("1")) {
//                        String user_type = response.getString("user_type");

                        dialog.dismiss();
                        Log.d("Login", "Success");
//                        Log.d("user_type", user_type);
                        login_email = email.getText().toString();
                        Log.d("email",login_email);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("login_email", login_email);
                        editor.commit();
//                        if (user_type.equals("buyer")) {
////                            startActivity(new Intent(ActivityLogin.this, ActivityBuyerLogin.class));
//
//                        }
//                        else if (user_type.equals("farmer")){
//                            startActivity(new Intent(ActivityLogin.this, MainPage.class));
//                        }

                        startActivity(new Intent(ActivityLogin.this, MainPage.class));


                        Toast.makeText(ActivityLogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    } else if (login_status.equals("noemail")) {
                        dialog.dismiss();
                        Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_wrongemail), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "No email");
                    } else if (login_status.equals("0")) {
                        dialog.dismiss();
                        Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_wrongpass), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "Fail");
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
                Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_senddatafail), Toast.LENGTH_SHORT).show();
            }
        };


        client.post(url, params, responseHandler);
    }

    public String get_email() {
        return login_email;
    }

}*/
