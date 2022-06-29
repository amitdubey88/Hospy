package com.example.anuragini.hospy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    Intent i;
    Animation f,fB;
    TextView tv;
    ImageView d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        f= AnimationUtils.loadAnimation(this,R.anim.splash);
        fB=AnimationUtils.loadAnimation(this,R.anim.splashtextanim);
        d=(ImageView)findViewById(R.id.d);
        tv=(TextView)findViewById(R.id.tv);
        launcher obj=new launcher();
        obj.start();
    }
    private class launcher extends Thread{
        public void run(){
            try{
                d.startAnimation(f);
                tv.startAnimation(fB);
                sleep(3500);
            }catch(Exception e){
                e.printStackTrace();
            }
            SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.PREFS_NAME,0);
            boolean hasLoggedIn=sharedPreferences.getBoolean("hasLoggedIn",false);
            if(hasLoggedIn){
                Intent in=new Intent(Splash.this,LoginAfterGetStarted.class);
                startActivity(in);
                finish();
            }
            else {
                i = new Intent(Splash.this, HospitalsNear.class);
                startActivity(i);
                Splash.this.finish();
            }
        }
    }
}
