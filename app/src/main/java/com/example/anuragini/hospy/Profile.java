package com.example.anuragini.hospy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    TextView name,email,user,address,phone,gender;
    Bundle obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        user=(TextView)findViewById(R.id.user);
        address=(TextView)findViewById(R.id.address);
        phone=(TextView)findViewById(R.id.phone);
        gender=(TextView)findViewById(R.id.gender);
        obj=getIntent().getExtras();
        name.setText(obj.getString("name"));
        email.setText(obj.getString("email"));
        user.setText(obj.getString("user"));
        address.setText(obj.getString("address"));
        phone.setText(obj.getString("phone"));
        gender.setText(obj.getString("gender"));

    }
}
