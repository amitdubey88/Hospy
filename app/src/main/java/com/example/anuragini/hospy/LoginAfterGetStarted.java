package com.example.anuragini.hospy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginAfterGetStarted extends AppCompatActivity {
    GridLayout mygrid;
    Animation fromBottom;
    TextView t1;
    Bundle obj;
    ProgressDialog loading;
    Intent i,j;
    String json_string;
    JSONArray jsonArray;
    SharedPreferences sharedPreferences;
    JSONArray jsonArray1,jsonArray2,jsonArray3,jsonArray4,jsonArray5,jsonArray6,jsonArray7;
    String user,pass,str="https://amitdubey99.000webhostapp.com/Android/Hospy/loginhospy.php";
    CardView search,rem,book,hospitals;
    BoomMenuButton bmb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_get_started);


        mygrid=(GridLayout)findViewById(R.id.mygrid);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        mygrid.startAnimation(fromBottom);
        t1=(TextView)findViewById(R.id.t1);
        t1.startAnimation(fromBottom);
        obj=getIntent().getExtras();
        String str[]=obj.getString("name").split(" ");
        t1.append(str[0]);
        search=(CardView)findViewById(R.id.search);
        rem=(CardView)findViewById(R.id.rem);
        book=(CardView)findViewById(R.id.book);
        hospitals=(CardView)findViewById(R.id.hospitalsNear);
        bmb=(BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),Search.class);
                startActivity(i);
            }
        });
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),Remainder.class);
                startActivity(i);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),BookDoctors.class);
                startActivity(i);
            }
        });
        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),MapHospitalActivity.class);
                startActivity(i);
            }
        });
        HamButton.Builder n[]=new HamButton.Builder[bmb.getButtonPlaceEnum().buttonNumber()];
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {
             if(i==0)
             n[i]=new HamButton.Builder().normalImageRes(R.drawable.profile).normalText("See profile");
            else if(i==1){
                 n[i]=new HamButton.Builder().normalImageRes(R.drawable.edit).normalText("Edit profile");
             }
            else if(i==2)
                 n[i]=new HamButton.Builder().normalImageRes(R.drawable.dustbin).normalText("Delete Your Account");
            else
                 n[i]=new HamButton.Builder().normalImageRes(R.drawable.logout).normalText("Sign out");
            bmb.addBuilder(n[i]);
            n[i].listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    if(index==0){
                        getdetails();
                    }
                    else if(index==1){
                        j=new Intent(getApplicationContext(),EditProfile.class);
                        j.putExtra("pass",obj.getString("pass"));
                        j.putExtra("user",obj.getString("user"));
                        startActivity(j);
                    }
                    else if(index==2){
                        j=new Intent(getApplicationContext(),DeleteAccount.class);
                        j.putExtra("user",obj.getString("user"));
                        j.putExtra("pass",obj.getString("pass"));
                        startActivity(j);
                    }
                    else{
                        finish();
                        j=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(j);
                    }
                }
            });
        }

    }
    private void getdetails()
    {

        loading = ProgressDialog.show(LoginAfterGetStarted.this, "Loading... Please Wait...", null, true, true);
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
                            Toast.makeText(getApplicationContext(), "Invalid username and password please try again...", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            i=new Intent(getApplicationContext(),Profile.class);
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
