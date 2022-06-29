package com.example.anuragini.hospy;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class Search extends AppCompatActivity {
    String json_string;
    String JSON_STRING;
    String temp;
    ProgressDialog loading;
    String prev="Wait";
    String url= "https://amitdubey99.000webhostapp.com/Android/getdataforapp.php";

    ArrayList<String> items=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    protected Button btnShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Intent i = new Intent(this,details.class);


        initItems();

        {
            spinnerDialog = new SpinnerDialog(Search.this, items, "Select the Disease");
            spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    temp=item;
                    Toast.makeText(Search.this, "You selected "+item, Toast.LENGTH_LONG).show();
                    gatdata();

                }

            });

            btnShow = (Button) findViewById(R.id.btnShow);
            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    spinnerDialog.showSpinerDialog();
                }

            });
        }
    }


    private void initItems() {
        for(int i=0;i<100;i++)
        {
            items.add("Asthma");
            items.add("Allergies");
            items.add("Underactive thyroid");
            items.add("Yellow fever");
            items.add("Cold");
            items.add("Fever");
            items.add("Gallstones");
            items.add("Flu");
            items.add("Food Poisoning");
            items.add("Dysphagia");
            items.add("Dehydration");
            items.add("Dental abscess");
            items.add("Diarrhoea");
            items.add("Depression");
            items.add("Gallstones");
            items.add("Gum disease");
            items.add("High Cholesterol");
            items.add("Hepatitis B");
            items.add("Indigestion");
            items.add("Measles");
            items.add("Malaria");
            items.add("Insomnia");
            items.add("Pneumonia");
            items.add("Sunburn");
            items.add("Tonsillitis");
            items.add("Tuberculosis (TB)");
            items.add("Ringworm");
            items.add("Osteoarthritis");
            items.add("Iron Deficiency Anaemia");
            items.add("Labyrinthitis");
            break;
        }
    }
    void gatdata() {
        loading = ProgressDialog.show(Search.this, "Please Wait", null, true, true);
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.equals("FAILED:"))
                        {
                            Toast.makeText(getApplicationContext(), "INVALID", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Intent intent_name = new Intent(getApplicationContext(),details.class);
                            intent_name.putExtra("json_data",response);
                            startActivity(intent_name);
                            loading.dismiss();
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
                params.put("disease",temp);
                return params;
            }
        };
        RequestQueue RQ = Volley.newRequestQueue(this);
        RQ.add(sr);

    }
}
