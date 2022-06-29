package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    Button send;
    EditText email;
    TextView msgTxt;
    ProgressDialog loading;
    String str="https://amitdubey99.000webhostapp.com/Android/Hospy/emailsender.php",emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        send=(Button) findViewById(R.id.send);
        email=(EditText) findViewById(R.id.email);
        msgTxt=(TextView) findViewById(R.id.msgtxt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgTxt.setText("");
                sendE();
            }
        });
    }
    private void sendE()
    {
        loading = ProgressDialog.show(ForgotPassword.this, "Working on.... Please wait..", null, true, true);
        emailText = email.getText().toString().trim();

        StringRequest sr = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if ((response.trim()).equals("NA"))
                        {
                            loading.dismiss();
                            msgTxt.setText("Email not registered...Please try again..");
                        }
                        if((response.trim()).equals("failed"))
                        {
                            loading.dismiss();
                            msgTxt.setText("Something went wrong..");
                        }
                        if((response.trim()).equals("success")) {
                            loading.dismiss();
                            msgTxt.setText("Email Sent with Username and Password.. Please check and Login.");
                        }
                    }
                },

                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "ERROR" , Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email",emailText);

                return params;

            }
        };

        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);

    }

}


