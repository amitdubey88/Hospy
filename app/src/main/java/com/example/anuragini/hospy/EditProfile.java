package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class EditProfile extends AppCompatActivity {
    EditText name,email,address,phone;
    Button update;
    String json_string;
    ProgressDialog loading;
    JSONArray jsonArray;
    JSONArray jsonArray1,jsonArray4,jsonArray6,jsonArray7;
    String user,pass,str2="https://amitdubey99.000webhostapp.com/Android/Hospy/update.php",str="https://amitdubey99.000webhostapp.com/Android/Hospy/loginhospy.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Typeface t=Typeface.createFromAsset(getAssets(),"MeriendaOne-Regular.ttf");
        ((TextView)findViewById(R.id.txv_editprofile)).setTypeface(t);
        ((TextView)findViewById(R.id.updateinfo)).setTypeface(t);
        ((Button)findViewById(R.id.update)).setTypeface(t);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        address=(EditText)findViewById(R.id.address);
        phone=(EditText)findViewById(R.id.phone);
        update=(Button)findViewById(R.id.update);
        getdetails();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verify())
                    updatep();
                else
                    Toast.makeText(getApplicationContext(), "Invalid details filled please try again...", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void getdetails()
    {

        loading = ProgressDialog.show(EditProfile.this, "Loading... Please Wait...", null, true, true);
        Bundle obj=getIntent().getExtras();
        user = obj.getString("user");
        pass = obj.getString("pass");
        StringRequest sr = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        if (response.equals("NA"))
                        {
                            Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            try {
                                json_string=response;
                                jsonArray = new JSONArray(json_string);
                                JSONObject JO= jsonArray.getJSONObject(0);


                                jsonArray1 = JO.getJSONArray("email");
                                email.setText(jsonArray1.getString(0));
                                jsonArray6=JO.getJSONArray("phone");
                                phone.setText(jsonArray6.getString(0));
                                jsonArray4 = JO.getJSONArray("address");
                                address.setText(jsonArray4.getString(0));
                                jsonArray7 = JO.getJSONArray("name");
                                name.setText(jsonArray7.getString(0));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
    private  void updatep() {
        loading = ProgressDialog.show(EditProfile.this, "Updating... Please Wait", null, true, true);
        StringRequest sr = new StringRequest(Request.Method.POST, str2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            String flag = response;

                            if (flag.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception k) {
                            Toast.makeText(getApplicationContext(), "Failed"+k.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",name.getText().toString());
                params.put("address",address.getText().toString());
                params.put("email",email.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("user",user);
                params.put("pass",pass);
                return params;
            }
        };
        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);
    }
    private boolean verify(){
        if (!email.getText().toString().matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            email.requestFocus();
            email.setError("Failed!! invalid email");
            return false;
        }else if (phone.getText().toString().length() == 0) {
            phone.requestFocus();
            phone.setError("Failed!! Cannot be empty");
            return false;
        }else if (!phone.getText().toString().matches("^[+][0-9]{10,13}$")) {
            phone.requestFocus();
            phone.setError("Correct format +91xxxxxxxxxx");
            return false;
        }else if (email.getText().toString().length() == 0) {
            email.requestFocus();
            email.setError("Failed!! Cannot be empty");
            return false;
        }
        else
            return true;
    }
}





