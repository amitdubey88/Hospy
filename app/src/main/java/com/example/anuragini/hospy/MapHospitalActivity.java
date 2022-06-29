package com.example.anuragini.hospy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class MapHospitalActivity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationProviderClient;
    Location mLastKnownLocation;
    LocationCallback locationCallback;
    Button btnFind;
    WebView mapWeb;
    String url;
    boolean a = false;
    Toolbar mToolbar;
    ProgressBar webProgressBar;
    double latitude, longitude;
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_hospital);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btnFind = (Button) findViewById(R.id.btn_find);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapHospitalActivity.this);
        mapWeb = (WebView) findViewById(R.id.map);
        webProgressBar = (ProgressBar) findViewById(R.id.progress);
        setSupportActionBar(mToolbar);
        webProgressBar.setVisibility(View.VISIBLE);
        WebSettings webSettings = mapWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mapWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });


        getSupportActionBar().setTitle("Search nearby hospitals");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //Check if GPS is on
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapHospitalActivity.this);
        url = "google.com/maps/@" + 23.6102 + "," + 85.2799 + ",7z";
        mapWeb.setWebViewClient(new MWebViewClient());
        mapWeb.loadUrl(url);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapHospitalActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(MapHospitalActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapHospitalActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {


                        e1.printStackTrace();
                    }
                }
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webProgressBar.setVisibility(View.VISIBLE);
                url = "google.com/maps/search/hospitals/@" + latitude + "," + longitude + ",15z/data=!3m1!4b1";
                mapWeb.loadUrl(url);

                WebSettings webSettings = mapWeb.getSettings();
                webSettings.setJavaScriptEnabled(true);


                Toast.makeText(MapHospitalActivity.this, "Showing nearby hospitals", Toast.LENGTH_LONG).show();
                btnFind.setVisibility(View.INVISIBLE);
            }
        });


    }


    void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastKnownLocation = task.getResult();
                    if (mLastKnownLocation != null) {
                        latitude=mLastKnownLocation.getLatitude();
                        longitude=mLastKnownLocation.getLongitude();
                    } else {
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult == null) {
                                    return;
                                }
                                mLastKnownLocation = locationResult.getLastLocation();
                                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(MapHospitalActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapHospitalActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Unable to get last location", Toast.LENGTH_LONG).show();
                }
            }
        });


    }





    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(mapWeb.canGoBack()){
                        mapWeb.goBack();
                    }
                    else{
                        finish();
                    }
                    return true;

            }
        }
        return super.onKeyDown(keyCode,event);
    }
    class MWebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(url.startsWith("tel")){
                Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse(url));
                startActivity(intent);
                mapWeb.goBack();
                Toast.makeText(MapHospitalActivity.this, "Dialing the number.", Toast.LENGTH_LONG).show();
                return true;
            }
            if(url.startsWith("intent")){
//                view.loadUrl(url);
//                if(url.startsWith("intent")){
//                    mapWeb.stopLoading();
//                    mapWeb.goBack();
//                    Toast.makeText(MapHospitalActivity.this, "Can not open", Toast.LENGTH_LONG).show();
//                    return false;
//                }
                String currUrl=mapWeb.getUrl();
                if(currUrl.contains("google.com/maps/@23.6102,85.2799,7z")){
                    if(btnFind.getVisibility()!=View.VISIBLE){
                        btnFind.setVisibility(View.VISIBLE);
                    }
                }
               try{
                   view.loadUrl(url);
               if(url.startsWith("intent")) {
                   mapWeb.goBack();
                   Toast.makeText(MapHospitalActivity.this, "Opening maps", Toast.LENGTH_LONG).show();
               }
                   Intent intent=Intent.parseUri(url,Intent.URI_INTENT_SCHEME);
                   startActivity(intent);
               }catch(URISyntaxException e){
                   Toast.makeText(MapHospitalActivity.this, "Something wennt wrong"+ e.toString(), Toast.LENGTH_LONG).show();
               }
//                try{
//                    Intent mapIntent=new Intent(Intent.ACTION_SEND);
//                    mapIntent.setType("text/plain");
//                    mapIntent.setPackage("com.google.android.gms");
//                    mapIntent.putExtra(Intent.EXTRA_TEXT,mapWeb.getUrl());
//                    startActivity(mapIntent);
//                }catch(android.content.ActivityNotFoundException e){
//                    Toast.makeText(MapHospitalActivity.this, "Maps not installed", Toast.LENGTH_LONG).show();
//                }
            }
            else {
                return true;
            }
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            webProgressBar.setVisibility(View.GONE);
        }

    }
}

