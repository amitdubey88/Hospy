package com.example.anuragini.hospy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    ImageView mainimg;
    TextView welcomeText,descText;
    Button getStarted;
    Animation forimg,fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainimg=(ImageView)findViewById(R.id.mainimg);
        welcomeText=(TextView)findViewById(R.id.welcomeText);
        descText=(TextView)findViewById(R.id.descText);
        getStarted=(Button)findViewById(R.id.getStartedButton);

        forimg= AnimationUtils.loadAnimation(this,R.anim.forimg);
        fromBottom=AnimationUtils.loadAnimation(this,R.anim.frombottom);

        mainimg.startAnimation(forimg);
        welcomeText.startAnimation(fromBottom);
        descText.startAnimation(fromBottom);
        getStarted.startAnimation(fromBottom);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginAfterGetStarted.class);
                Bundle obj=getIntent().getExtras();
                i.putExtra("user",obj.getString("user"));
                i.putExtra("name",obj.getString("name"));
                i.putExtra("pass",obj.getString("pass"));
                i.putExtra("gender",obj.getString("gender"));
                startActivity(i);
                finish();
            }
        });
    }
}
