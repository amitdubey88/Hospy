package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DeleteAccount extends AppCompatActivity {
    Button delete;
    EditText user,pass;
    String str="https://amitdubey99.000webhostapp.com/Android/Hospy/deleteProfile.php";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        delete = (Button) findViewById(R.id.delete);
        user=(EditText)findViewById(R.id.user);
        pass=(EditText) findViewById(R.id.pass);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((user.getText().toString()).equals(getIntent().getExtras().getString("user"))) && ((pass.getText().toString()).equals(getIntent().getExtras().getString("pass")))) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    del();
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    finish();
                                    startActivity(i);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure? You want to delete???").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else
                    Toast.makeText(DeleteAccount.this, "Invalid Username and Password!! Please try again...", Toast.LENGTH_LONG).show();
            }
        });
    }
    private  void del() {
        loading = ProgressDialog.show(DeleteAccount.this, "Deleting Account... Please Wait", null, true, true);
        StringRequest sr = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            String flag = response;

                            if (flag.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Deleted Successfully and Moved to Login Page.", Toast.LENGTH_LONG).show();
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
                params.put("user",user.getText().toString());
                params.put("pass",pass.getText().toString());
                return params;
            }
        };
        RequestQueue RQ= Volley.newRequestQueue(this);
        RQ.add(sr);
    }
}
