package com.example.user.whereareyou;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    TextView latT, lngT, bearingT, speedT, accuracyT, altitudeT, addressT;

    @Override
    public void onLocationChanged(Location location) {

        Double lati = location.getLatitude();
        Double lon = location.getLongitude();
        Double alt = location.getAltitude();
        Float brearing = location.getBearing();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            List<Address> listAddresses = geocoder.getFromLocation(lati, lon, 1);

            if (listAddresses != null && listAddresses.size() > 0) {

                Log.i("PlaceInfo: ", listAddresses.get(0).toString());

                String address = "";


                for(int i=0; i<=listAddresses.get(0).getMaxAddressLineIndex(); i++){

                    address += listAddresses.get(0).getAddressLine(i);

                }

                addressT.setText("Address: " + address);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        latT.setText("Latitude: " + lati.toString());
        lngT.setText("Longitude: " + lon.toString());
        speedT.setText("Speed: " + speed.toString());
        bearingT.setText("Bearing: " + brearing.toString());
        accuracyT.setText("Accuracy: " + accuracy.toString());
        latT.setText("Latitude: " + lati.toString());

        Log.i("Latitute: ", lati.toString());
        Log.i("Longitude: ", lon.toString());
        Log.i("alt: ", alt.toString());
        Log.i("breaing: ", brearing.toString());
        Log.i("speed: ", speed.toString());
        Log.i("accuracy: ", accuracy.toString());

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);


    }

    @Override
    protected void onResume() {

        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        latT = (TextView) findViewById(R.id.latitude);
        lngT = (TextView) findViewById(R.id.longitude);
        accuracyT = (TextView) findViewById(R.id.accuracy);
        speedT = (TextView) findViewById(R.id.Speed);
        bearingT = (TextView) findViewById(R.id.bearing);
        altitudeT = (TextView) findViewById(R.id.altitude);
        addressT = (TextView) findViewById(R.id.address);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);

    }
}
