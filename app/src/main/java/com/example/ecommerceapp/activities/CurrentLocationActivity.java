package com.example.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CurrentLocationActivity extends AppCompatActivity {


    FusedLocationProviderClient fusedLocationProviderClient;
    TextView city,country,address,latitude,longitude;
    Toolbar toolbar;
    Button getLocation;
    private final static  int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);


        toolbar = findViewById(R.id.location_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        city=findViewById(R.id.city_label);
        country=findViewById(R.id.country_label);
        address=findViewById(R.id.address_label);
        latitude=findViewById(R.id.latitude_label);
        longitude=findViewById(R.id.longitude_label);

        getLocation = findViewById(R.id.get_location_btn);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CurrentLocationActivity.this);


        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    private void getLocation() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){


           fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null){
                                Geocoder geocoder = new Geocoder(CurrentLocationActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    latitude.setText("Latitude :"+addresses.get(0).getLatitude());
                                    longitude.setText("Longitude :"+addresses.get(0).getLongitude());
                                    address.setText("Address :"+addresses.get(0).getAddressLine(0));
                                    city.setText("City :"+addresses.get(0).getLocality());
                                    country.setText("Country :"+addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }else {
                askPermission();
              }
    }

    private void askPermission() {

        ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode ==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
             getLocation();
            }else{
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}