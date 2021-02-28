package com.laith.weatherforecast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laith.preference.LocationPreference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private LocationPreference pref;
    private Intent intent;
    private EditText editLocation;
    private Button btnGet;
    private Button btnCancel;
    private TextView myCity;
    Button cityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        intent = getIntent();
        pref = new LocationPreference(this);
        //here I can add
        //String city = pref.getCity();
        myCity = (TextView) findViewById(R.id.myCity);
        cityButton = (Button) findViewById(R.id.cityButton);


        editLocation = (EditText) findViewById(R.id.editLocation);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnCancel = (Button) findViewById(R.id.btnCancel);



       // editLocation.setText(city);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCity = editLocation.getText().toString();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1000);
                }else {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = getCurrentLocation(location.getLatitude(), location.getLongitude());
                        newCity = city;
                        myCity.setText(newCity);
                        //--------------------------
                        //String city = input.getText().toString();

                        setResult(1, intent);
                        pref.setCity(newCity);
                        finish();


                        //--------------------------
                    } catch (Exception e){
                        e.printStackTrace();
                        //  Toast.makeText(CurrentWeatherPage.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!newCity.isEmpty()) {
                    setResult(1, intent);
                    pref.setCity(newCity);
                    finish();
                } else {
                    Toast.makeText(LocationActivity.this, getString(R.string.error_empty), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1000:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //still works
                    try {
                        String city = getCurrentLocation(location.getLatitude(), location.getLongitude());
                        myCity.setText(city);
                        //--------------------------
                        setResult(1, intent);
                        pref.setCity(city);
                        finish();
                        //--------------------------
                    } catch (Exception e){
                        e.printStackTrace();
                       // Toast.makeText(CurrentWeatherPage.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Permission not granted" , Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public String getCurrentLocation(double lat, double lon){
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if(addresses.size() > 0){
                for(Address adr: addresses){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }
}
