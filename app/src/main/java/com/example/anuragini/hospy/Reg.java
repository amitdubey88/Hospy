package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class Reg extends AppCompatActivity {
    Button b1;
    boolean regE,regP,regU;
    EditText t1,t2,t3,t4,t5,t6,t7;
    ProgressDialog loading;
    ProgressBar progressBarPhone,progressBarEmail,progressBarUser;
    RadioGroup rg;
    String str="https://amitdubey99.000webhostapp.com/Android/Hospy/signuphospy.php",name,user,pass,address,email,phone,gender,confirm;

    String strESite="https://amitdubey99.000webhostapp.com/Android/Hospy/ifregemail.php";
    String strPSite="https://amitdubey99.000webhostapp.com/Android/Hospy/checkphone.php";
    String strUSite="https://amitdubey99.000webhostapp.com/Android/Hospy/checkuser.php";

    RadioButton selected;
    int registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        try {
            rg = (RadioGroup) findViewById(R.id.rg);
            b1 = (Button) findViewById(R.id.b1);
            t1 = (EditText) findViewById(R.id.t1);
            t2 = (EditText) findViewById(R.id.t2);
            t3 = (EditText) findViewById(R.id.t3);
            t4 = (EditText) findViewById(R.id.t4);
            t6 = (EditText) findViewById(R.id.t6);
            t7 = (EditText) findViewById(R.id.t7);
            t5 = (EditText) findViewById(R.id.t5);
            progressBarUser=(ProgressBar) findViewById(R.id.progressBarUser);
            progressBarEmail=(ProgressBar) findViewById(R.id.progressBarEmail);
            progressBarPhone=(ProgressBar) findViewById(R.id.progressBarPhone);
            t7.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    CheckP p=new CheckP();
                    p.run();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            t2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    CheckE e=new CheckE();
                    e.run();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            t3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    CheckU u=new CheckU();
                    u.run();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    selected = (RadioButton) rg.findViewById(rg.getCheckedRadioButtonId());
                    if(selected!=null)
                    gender=selected.getText().toString();
                    name = t1.getText().toString(); email = t2.getText().toString(); user = t3.getText().toString();
                    pass = t4.getText().toString(); address = t6.getText().toString(); phone = t7.getText().toString(); confirm = t5.getText().toString();

                    boolean check = validateInfo(name, email, user, pass, phone, confirm, selected);
                    if (check) {
                        reg();
                    }
                         else {
                        Toast.makeText(getApplicationContext(), "Sorry try again with valid information", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    boolean isRegPhone(){
        if(regP) {
            t7.requestFocus();
            t7.setError("Phone No already registered");
            return true;
        }
        else
            return false;
    }
    boolean isRegEmail(){
        if(regE) {
            t2.requestFocus();
            t2.setError("Email already registered");
            return true;
        }
        else
            return false;
    }
    boolean isRegUser(){
        if(regU) {
            t3.requestFocus();
            t3.setError("Username already registered");
            return true;
        }
        else
            return false;
    }
    private boolean validateInfo(String name,String email,String username,String password,String phone,String confirm,RadioButton selected) {
        if (name.length() == 0) {
            t1.requestFocus();
            t1.setError("Failed!! Cannot be empty");
            return false;
        } else if (!name.matches("[a-zA-z ]+")) {
            t1.requestFocus();
            t1.setError("Enter only alphabetical characters");
            return false;
        } else if (email.length() == 0) {
            t2.requestFocus();
            t2.setError("Failed!! Cannot be empty");
            return false;
        } else if (!email.matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            t2.requestFocus();
            t2.setError("Failed!! invalid email");
            return false;
        } else if (username.length() == 0) {
            t3.requestFocus();
            t3.setError("Failed!! Cannot be empty");
            return false;
        } else if (!username.matches("[a-zA-z0-9._]+")) {
            t3.requestFocus();
            t3.setError("Failed!! invalid username");
            return false;
        } else if (password.length() <= 7) {
            t4.requestFocus();
            t4.setError("Failed!! must be of 8 digits or characters");
            return false;
        } else if (!phone.matches("^[+][0-9]{10,13}$")) {
            t7.requestFocus();
            t7.setError("Correct format +91xxxxxxxxxx");
            return false;
        } else if (phone.length() == 0) {
            t7.requestFocus();
            t7.setError("Failed!! Cannot be empty");
            return false;
        } else if (!confirm.equals(password)) {
            t5.requestFocus();
            t5.setError("Failed!! Password doesn't matched");
            return false;
        } else if(selected==null){
            Toast.makeText(this, "Gender not selected", Toast.LENGTH_LONG).show();
            return false;
        } else if(isRegPhone()){
            return false;
        } else if(isRegEmail()){
            return false;
        } else if(isRegUser()){
            return false;
        }
        else
            return true;


    }
    private  void reg() {
        loading = ProgressDialog.show(Reg.this, "Register In Progress", null, true, true);
        StringRequest sr = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            String flag = response;

                            if (flag.equals("success")) {
                                t1.setText("");
                                t2.setText("");
                                t3.setText("");
                                t4.setText("");
                                t5.setText("");
                                t6.setText("");
                                t7.setText("");
                                rg.clearCheck();
                                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();

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
                params.put("name",name);
                params.put("user",user);
                params.put("address",address);
                params.put("email",email);
                params.put("pass",pass);
                params.put("gender",gender);
                params.put("phone",phone);
                return params;
            }
        };
        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);
    }
    private void checkEmail()
    {
        progressBarEmail.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, strESite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarEmail.setVisibility(View.INVISIBLE);
                        if (response.equals("NA"))
                        {
                            regE=false;
                        }
                        else
                        {
                            regE=true;
                        }
                        isRegEmail();
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
                params.put("email",email);
                return params;

            }
        };

        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);

    }


    private void checkphone()
    {
        progressBarPhone.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, strPSite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarPhone.setVisibility(View.INVISIBLE);
                        if (response.equals("NA"))
                        {
                            regP=false;
                        }
                        else
                        {
                            regP=true;
                        }
                        isRegPhone();
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
                params.put("phone",phone);
                return params;

            }
        };

        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);

    }
    private void checkuser()
    {
        progressBarUser.setVisibility(View.VISIBLE);
        StringRequest sr = new StringRequest(Request.Method.POST, strUSite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarUser.setVisibility(View.INVISIBLE);
                        if (response.equals("NA"))
                        {
                            regU=false;
                        }
                        else
                        {
                            regU=true;
                        }
                        isRegUser();
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
                return params;

            }
        };
        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);
    }
    class CheckE extends Thread{
        public void run(){
            email=t2.getText().toString();
            checkEmail();
        }
    }
    class CheckP extends Thread{
        public void run(){
            phone=t7.getText().toString();
            checkphone();
        }
    }class CheckU extends Thread{
        public void run(){
            user=t3.getText().toString();
            checkuser();
        }
    }
}



