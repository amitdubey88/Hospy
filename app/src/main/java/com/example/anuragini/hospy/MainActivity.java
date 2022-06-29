package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button b;
    Button b1,forgot;
    EditText t1,t2;
    ProgressDialog loading;
    Intent i,j;
    public static String PREFS_NAME="MyPrefsFile";
    String json_string;
    JSONArray jsonArray;
    JSONArray jsonArray1,jsonArray2,jsonArray3,jsonArray4,jsonArray5,jsonArray6,jsonArray7;
    String str="https://amitdubey99.000webhostapp.com/Android/Hospy/loginhospy.php",user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button)findViewById(R.id.b1);
        t1=(EditText)findViewById(R.id.t1);
        t2=(EditText)findViewById(R.id.t2);
        b=(Button)findViewById(R.id.b);
        forgot=(Button) findViewById(R.id.forgot);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.PREFS_NAME,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("hasLooggedIn",true);
                editor.commit();
                login();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j=new Intent(MainActivity.this,Reg.class);
                startActivity(j);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    private void login()
    {
        loading = ProgressDialog.show(MainActivity.this, "Logging In... Please Wait...", null, true, true);
        user = t1.getText().toString().trim();
        pass = t2.getText().toString().trim();
        StringRequest sr = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if ((response.trim()).equals("NA"))
                        {
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(),"Invalid username and password please try again...", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            loading.dismiss();
                            Intent i = new Intent(getApplicationContext(),Login.class);
                            try {
                                json_string=response;
                                jsonArray = new JSONArray(json_string);
                                JSONObject JO= jsonArray.getJSONObject(0);


                                jsonArray1 = JO.getJSONArray("email");
                                i.putExtra("email",jsonArray1.getString(0));
                                jsonArray2 = JO.getJSONArray("user");
                                i.putExtra("user",jsonArray2.getString(0));
                                jsonArray3=JO.getJSONArray("pass");
                                i.putExtra("pass",jsonArray3.getString(0));
                                jsonArray5=JO.getJSONArray("gender");
                                i.putExtra("gender",jsonArray5.getString(0));
                                jsonArray6=JO.getJSONArray("phone");
                                i.putExtra("phone",jsonArray6.getString(0));
                                jsonArray4 = JO.getJSONArray("address");
                                i.putExtra("address",jsonArray4.getString(0));
                                jsonArray7 = JO.getJSONArray("name");
                                i.putExtra("name",jsonArray7.getString(0));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(i);
                            finish();
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
                params.put("user",user);
                params.put("pass",pass);
                return params;

            }
        };

        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);

    }

}
