package com.dnhsolution.objekpajak.coba;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.dnhsolution.objekpajak.R;

public class MainCoba extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    double lat, longit;

    private final float MIN_DISTANCE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coba);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, MIN_DISTANCE, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 5, this);

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        GPSTracker gpsTracker = new GPSTracker(this);

        if(gpsTracker.canGetLocation)
        {
            lat = gpsTracker.latitude;
            longit = gpsTracker.longitude;
            String alamat = gpsTracker.getAddressLine(MainCoba.this);
            System.out.println("latitude" +lat + "longitude" +longit+"// Alamat : "+alamat);

            Toast.makeText(getApplicationContext(), "latitude" +lat + "longitude" +longit+"// Alamat : "+alamat, Toast.LENGTH_LONG).show();

        }
        else
        {
            gpsTracker.showSettingsAlert();
        }
        try
        {
            //initializeMap();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        if(location.hasAccuracy())
        {
            return;
        }
        if(location.getAccuracy()>5)
        {
            return;

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
}
