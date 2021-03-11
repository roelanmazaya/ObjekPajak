package com.dnhsolution.objekpajak.coba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {
    LinearLayout frmLok;
    EditText etLat, etLong, etAlamat, etKecamatan, etKelurahan;
    CountDownTimer myTimer = null;
    ProgressDialog dialog = null;
    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        frmLok = (LinearLayout) findViewById(R.id.frmLokasi);
        etLat = (EditText)findViewById(R.id.etLat);
        etLong = (EditText)findViewById(R.id.etLong);
        etAlamat = (EditText)findViewById(R.id.etAlamatUsaha);
        etKecamatan = (EditText)findViewById(R.id.etKec);
        etKelurahan = (EditText)findViewById(R.id.etKel);

        requestMultiplePermissions();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        frmLok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // Build the alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
                    builder.setTitle("Layanan Lokasi tidak Aktif");
                    builder.setMessage("Hidupkan Layanan Lokasi dan GPS");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    // getLoc(1);
                }else{
                    getLoc(1);
                }

            }
        });
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
        builder.setTitle("Perizian dibutuhkan !");
        builder.setMessage("Aplikasi ini membutuhkan perizinan untuk akses beberapa feature. Anda dapat mengatur di Pengaturan Aplikasi.");
        builder.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void getLoc(final int iStatus){


        myTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
                //Toast.makeText(TambahDataObjekActivity.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                //Toast.makeText(TambahDataObjekActivity.this, "Selesai", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
                final Dialog alertDialog;

                if(iStatus!=2){
                    builder.setTitle("Lokasi tidak ditemukan");
                    builder.setMessage("Apakah anda ingin melanjutkan pencarian ?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            getLoc(1);
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            // alertDialog.dismiss();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }


            }

        };

        if(iStatus==1){
            dialog = ProgressDialog.show(Main3Activity.this, "",
                    "Mencari Lokasi...", true);
            getLocation2();
            myTimer.start();
        }else if(iStatus==2){
            myTimer.cancel();
        }
    }

    void getLocation2() {
        try {

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){


                    double lati = location.getLatitude();
                    double longi = location.getLongitude();

                    ((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
                    ((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));

                    System.out.println("Lat atas: "+lati);
                    System.out.println("Long atas : "+longi);
                }else{
                    System.out.println("Lat atas : null");
                    System.out.println("Long atas : null");
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, new MyLocationListener()
                );
            }

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );

            //Geocoder geocoder = new Geocoder(TambahDataObjekActivity.this, Locale.getDefault());

            double lati = location.getLatitude();
            double longi = location.getLongitude();

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(Main3Activity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(lati, longi, 1);


                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();

            }

            System.out.println("COy : "+addresses);

            if(addresses!=null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String Subcity = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                System.out.println(address+"\n"+city+"\n"+Subcity+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+knownName);
                ((EditText)findViewById(R.id.etKec)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                ((EditText)findViewById(R.id.etKel)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});

                ((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
                ((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));
                ((EditText)findViewById(R.id.etAlamatUsaha)).setText(String.valueOf(address));
                ((EditText)findViewById(R.id.etKec)).setText(String.valueOf(city));
                ((EditText)findViewById(R.id.etKel)).setText(String.valueOf(Subcity));
                //getLoc(2);
                myTimer.cancel();
                //System.out.println("getLoc(2)");
                dialog.dismiss();
            }else{
                System.out.println("Data kosong");
            }
            System.out.println("LOKASI : ");
            System.out.println(lati);
            System.out.println(longi);

            //mengecek apakah berhasil ambil data atau tidak


            //Toast.makeText(TambahDataObjekActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String s, int i, Bundle b) {
            //Toast.makeText(TambahDataObjekActivity.this, "GPS Status Dirubah",

            // Toast.LENGTH_LONG).show();

            System.out.println("GPS Status Dirubah");
        }
        public void onProviderDisabled(String s) {
            /*Toast.makeText(TambahDataObjekActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();*/
            View view = findViewById(R.id.content);
            Snackbar snackbar = Snackbar
                    .make(view, "GPS Tidak Menyala !", Snackbar.LENGTH_LONG);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);

            snackbar.show();
        }
        public void onProviderEnabled(String s) {

            View view = findViewById(R.id.content);
            Snackbar snackbar = Snackbar
                    .make(view, "GPS Menyala !", Snackbar.LENGTH_LONG);


            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        }
    }


}
