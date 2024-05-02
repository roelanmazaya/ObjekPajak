package com.dnhsolution.objekpajak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dnhsolution.objekpajak.coba.Main3Activity;
import com.dnhsolution.objekpajak.coba.MainCoba;
import com.dnhsolution.objekpajak.config.Config;
import com.dnhsolution.objekpajak.config.SplashActivity;
import com.dnhsolution.objekpajak.data_op.DataObjekPajak;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.handler.HttpHandler;
import com.dnhsolution.objekpajak.pendataan.MainActivityPendatan;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.dnhsolution.objekpajak.riwayat.RiwayatActivity;
import com.dnhsolution.objekpajak.sinkron.SinkronActivity;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.android.widget.AnimatedSvgView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView bgapp,bgapp_slide, clover, ivMore;
    LinearLayout textsplash, texthome, menus, login;
    Animation frombottom;
    AnimatedSvgView svgView;
    EditText etUsername, etPassword;
    Button btnLogin;
    Animation fade_in, fade_out;

    SharedPreferences sharedPreferences;
    String ses_status, ses_status_anim;
    DatabaseHandler databaseHandler;
    CardView cvmPendataan, cvmSinkron, cvmRiwayat, cvmOP;

    Handler handler = new Handler();
    int status = 0;
    ProgressDialog progressdialog;
    int jumlah_data = 0;
    private String TAG = MainActivity.class.getSimpleName();
    String versionName="";
    int show_notif = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler=new DatabaseHandler(MainActivity.this);

        Log.d("MAX_ID", String.valueOf(databaseHandler.CountMaxDataMaster()));
        Log.d("MAX_DATA", String.valueOf(databaseHandler.CountAllDataMaster()));
        Log.d("COUNT_GAMBAR", String.valueOf(databaseHandler.count_data_gambar()));

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        ses_status_anim = sharedPreferences.getString(Config.PREF_STATUS_ANIM, "0");

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        bgapp_slide = (ImageView) findViewById(R.id.bgappslide);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        login = (LinearLayout) findViewById(R.id.login);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        cvmPendataan = (CardView)findViewById(R.id.menuTambahObjek);
        cvmSinkron = (CardView)findViewById(R.id.menuSinkron);
        cvmRiwayat = (CardView)findViewById(R.id.menuRiwayat);
        cvmOP = (CardView)findViewById(R.id.menuOP);

        svgView = (AnimatedSvgView) findViewById(R.id.ivLogoDaerah);

        login.setVisibility(View.GONE);
        textsplash.setVisibility(View.GONE);


        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            //binding.tvVersionCode.setText("v" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        cekDataPembaruan();

        cvmPendataan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int jml_data = databaseHandler.CountAllDataMaster();
                if(jml_data==0){
                    dialogPeringatan();
                }else{
                    if(show_notif==1){
                        dialogNotif("Pembaruan aplikasi tersedia. Silahkan install aplikasi terbaru !");
                    }else{
//                        startActivity(new Intent(MainActivity.this, PendataanActivity.class));
                        startActivity(new Intent(MainActivity.this, MainActivityPendatan.class));
                    }
                }

                Log.d("STATUS_ANIM", "onClick: "+ses_status_anim);

            }
        });

        cvmSinkron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int jml_data = databaseHandler.CountDataTerupdate();
                if(jml_data == 0 && show_notif == 1){
                    dialogNotif("Pembaruan aplikasi tersedia. Silahkan install aplikasi terbaru !");
                }else{
                    startActivity(new Intent(MainActivity.this, SinkronActivity.class));
                }
            }
        });

        cvmRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_notif==1){
                    dialogNotif("Pembaruan aplikasi tersedia. Silahkan install aplikasi terbaru !");
                }else{
                    startActivity(new Intent(MainActivity.this, RiwayatActivity.class));
                }
            }
        });

        cvmOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_notif==1){
                    dialogNotif("Pembaruan aplikasi tersedia. Silahkan install aplikasi terbaru !");
                }else{
                    startActivity(new Intent(MainActivity.this, DataObjekPajak.class));
                }
            }
        });

        fade_in = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out);

//        if(ses_status_anim.equalsIgnoreCase("0")){
//            svgView.postDelayed(new Runnable() {
//
//                @Override public void run() {
//                    svgView.start();
//                }
//
//            }, 500);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
//                    ses_status = sharedPreferences.getString(Config.PREF_STATUS, "0");
//
//                    if(ses_status.equalsIgnoreCase("0")) {
//                        textsplash.animate().translationY(170).alpha(0).setDuration(800).setStartDelay(300);
//                        login.setVisibility(View.VISIBLE);
//                        login.startAnimation(frombottom);
//                        //Toast.makeText(MainActivity.this, sharedPreferences.getString(Config.PREF_STATUS, "0"), Toast.LENGTH_SHORT).show();
//                    }else{
//                        //Toast.makeText(MainActivity.this, sharedPreferences.getString(Config.PREF_STATUS, "0"), Toast.LENGTH_SHORT).show();
//                        textsplash.animate().translationY(170).alpha(0).setDuration(500).setStartDelay(300);
//                        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
//                        //bgapp_slide.animate().translationY(-1900).setDuration(800).setStartDelay(300);
//                        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
//                        login.animate().translationY(170).alpha(0).setDuration(600).setStartDelay(300);
//
//                        menus.setVisibility(View.VISIBLE);
//                        texthome.setVisibility(View.VISIBLE);
//                        texthome.startAnimation(frombottom);
//                        menus.startAnimation(frombottom);
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                login.setVisibility(View.GONE);
//                                textsplash.setVisibility(View.GONE);
//
//                                bgapp_slide.setVisibility(View.VISIBLE);
//                                bgapp_slide.setAnimation(fade_in);
//                                fade_in.start();
//
//                            }
//                        },1000);
//                    }
//                }
//            },3000);
//        }else{
//
//
//        }

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        login.animate().translationY(170).alpha(0).setDuration(600).setStartDelay(300);

        menus.setVisibility(View.VISIBLE);
        texthome.setVisibility(View.VISIBLE);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                bgapp_slide.setVisibility(View.VISIBLE);
                bgapp_slide.setAnimation(fade_in);
                fade_in.start();

                requestMultiplePermissions();
                if(show_notif==1){
                    dialogNotif("Pembaruan aplikasi tersedia. Silahkan install aplikasi terbaru !");
                }
            }

        },1000);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(etUsername.getText().toString().trim().equalsIgnoreCase("")){
//                    etUsername.setError("Silahkan isi form ini !");
//                    etUsername.requestFocus();
//                }else if(etPassword.getText().toString().trim().equalsIgnoreCase("")){
//                    etPassword.setError("Silahkan isi form ini !");
//                    etPassword.requestFocus();
//                }else{
//                    sendData();
//                }
//            }
//        });

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenuLain();
            }
        });

        getInformationDevice();

    }

    public void getInformationDevice(){
        System.out.println("Informasi Perangkat");
        System.out.println("==================================");
        System.out.println(System.getProperty("os.version")); // OS version
        System.out.println(android.os.Build.VERSION.SDK);     // API Level
        System.out.println(Build.DEVICE);// Device
        System.out.println(android.os.Build.MODEL);             // Model
        System.out.println(android.os.Build.PRODUCT );          // Product
        System.out.println("==================================");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

//    private void sendData() {
//        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        String url = Config.URL +"Auth";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("result");
//                    JSONObject json = jsonArray.getJSONObject(0);
//                    String pesan = json.getString("pesan");
//                    if(pesan.equalsIgnoreCase("0")){
//                        dialogNotifikasi(pesan);
//                        //Toast.makeText(MainActivity.this, "Gagal Login. Username atau Password salah !", Toast.LENGTH_SHORT).show();
//                    }else if(pesan.equalsIgnoreCase("1")){
//                        //String username = json.getString("username");
//                        //String password = json.getString("password");
//                        String id_inc = json.getString("id_inc");
//                        String nip = json.getString("nip");
//                        String nama = json.getString("nama");
//                        String unit_upt_id = json.getString("unit_upt_id");
//                        String ms_role_id = json.getString("ms_role_id");
//
//                        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
//
//                        //membuat editor untuk menyimpan data ke shared preferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        //menambah data ke editor
//                        editor.putString(Config.PREF_USERNAME, etUsername.getText().toString());
//                        editor.putString(Config.PREF_PASSWORD, etPassword.getText().toString());
//                        editor.putString(Config.PREF_ID_INC, id_inc);
//                        editor.putString(Config.PREF_NIP, nip);
//                        editor.putString(Config.PREF_NAMA, nama);
//                        editor.putString(Config.PREF_UNIT_UPT_ID, unit_upt_id);
//                        editor.putString(Config.PREF_MS_ROLE_ID, ms_role_id);
//                        editor.putString(Config.PREF_STATUS, "1");
//                        editor.putString(Config.PREF_STATUS_ANIM, "1");
//
//                        //menyimpan data ke editor
//                        editor.apply();
//                        dialogNotifikasi(pesan);
//                    }else{
//                        Toast.makeText(MainActivity.this, "Jaringan masih sibuk !", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("username", etUsername.getText().toString());
//                params.put("password", etPassword.getText().toString());
//
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//
//        queue.add(stringRequest);
//    }

//    private void dialogNotifikasi(final String pesan) {
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        View mView = inflater.inflate(R.layout.dialog_notifikasi, null);
//
//        final Button btnOk;
//        final ImageView ivBerhasil,ivGagal;
//        final TextView tvKeterangan;
//
//        btnOk = (Button) mView.findViewById(R.id.btnOk);
//        ivBerhasil = (ImageView) mView.findViewById(R.id.ivBerhasil);
//        ivGagal = (ImageView) mView.findViewById(R.id.ivGagal);
//        tvKeterangan = (TextView) mView.findViewById(R.id.tvKeterangan);
//
//        mBuilder.setView(mView);
//        final AlertDialog dialog = mBuilder.create();
//        dialog.show();
//        dialog.setCanceledOnTouchOutside(false);
//
//        if(pesan.equalsIgnoreCase("0")){
//            ivBerhasil.setVisibility(View.GONE);
//            ivGagal.setVisibility(View.VISIBLE);
//            tvKeterangan.setText("Gagal masuk ! Silahkan cek username atau password.");
//        }else{
//            ivBerhasil.setVisibility(View.VISIBLE);
//            ivGagal.setVisibility(View.GONE);
//            //ivKeterangan.setBackgroundResource(R.mipmap.ic_berhasil);
//            tvKeterangan.setText("Selamat Berhasil Masuk !");
//        }
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(pesan.equalsIgnoreCase("0")){
//                    dialog.dismiss();
//                }else{
//                    dialog.dismiss();
//                    bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
//                    //bgapp_slide.animate().translationY(-1900).setDuration(800).setStartDelay(300);
//                    clover.animate().alpha(0).setDuration(800).setStartDelay(600);
//                    login.animate().translationY(170).alpha(0).setDuration(600).setStartDelay(300);
//
//                    menus.setVisibility(View.VISIBLE);
//                    texthome.setVisibility(View.VISIBLE);
//                    texthome.startAnimation(frombottom);
//                    menus.startAnimation(frombottom);
//
//                    login.setVisibility(View.GONE);
//                    textsplash.setVisibility(View.GONE);
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            bgapp_slide.setVisibility(View.VISIBLE);
//                            bgapp_slide.setAnimation(fade_in);
//                            fade_in.start();
//
//                        }
//                    },1000);
//                }
//
//
//
//            }
//        });
//    }
    //beres
    private void dialogMenuLain() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_menu, null);

        final LinearLayout lSinkron, lExit;

        lSinkron = (LinearLayout) mView.findViewById(R.id.lSinkron);
        lExit = (LinearLayout) mView.findViewById(R.id.lExit);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        lSinkron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekData();
                dialog.dismiss();
            }
        });

        lExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                //menambah data ke editor
                editor.putString(Config.PREF_STATUS, "0");

                //menyimpan data ke editor
                editor.apply();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });

    }

    //beres
    private void dialogPeringatan() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_notifikasi, null);

        final Button btnOk;
        final ImageView ivBerhasil,ivGagal;
        final TextView tvKeterangan;

        btnOk = (Button) mView.findViewById(R.id.btnOk);
        ivBerhasil = (ImageView) mView.findViewById(R.id.ivBerhasil);
        ivGagal = (ImageView) mView.findViewById(R.id.ivGagal);
        tvKeterangan = (TextView) mView.findViewById(R.id.tvKeterangan);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        ivBerhasil.setVisibility(View.GONE);
        ivGagal.setVisibility(View.GONE);
        tvKeterangan.setText("Silahkan download data terlebih dahulu untuk dapat mengakses menu ini.");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogMenuLain();
            }
        });
    }

    private void dialogNotif(final String pesan) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_notifikasi, null);

        final Button btnOk;
        final ImageView ivBerhasil,ivGagal, ivDetail;
        final TextView tvKeterangan;

        btnOk = (Button) mView.findViewById(R.id.btnOk);
        ivBerhasil = (ImageView) mView.findViewById(R.id.ivBerhasil);
        ivGagal = (ImageView) mView.findViewById(R.id.ivGagal);
        ivDetail = (ImageView) mView.findViewById(R.id.ivDetail);
        tvKeterangan = (TextView) mView.findViewById(R.id.tvKeterangan);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        ivBerhasil.setVisibility(View.GONE);
        ivGagal.setVisibility(View.GONE);
        ivDetail.setVisibility(View.VISIBLE);

        tvKeterangan.setText(pesan);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void cekData() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Config.URL +"cekData";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    for (i=0;i<jsonArray.length();i++){
                        try {
                            String today = getCurrentDate();
                            JSONObject json = jsonArray.getJSONObject(i);
                            final String jml_data = json.getString("jml_data");
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Lanjutkan download "+jml_data+" data ?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            databaseHandler.delete_data_master();
                                            databaseHandler.delete_data_gambar();
                                            jumlah_data = Integer.parseInt(jml_data);
                                            new getDataP().execute();
                                            //getData(jml_data);
                                        }
                                    })
                                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");
                String info = error.toString();
                String activity = "MainActivity-cekData";

                sendLog(username, sdk, model, info, activity);
                Toast.makeText(MainActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "200");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 70000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 70000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(stringRequest);
    }

    private void cekDataPembaruan() {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Config.URL +"cekDataPembaruan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    for (i=0;i<jsonArray.length();i++){
                        try {

                            JSONObject json = jsonArray.getJSONObject(i);
                            final String version_apk = json.getString("version");

                            if(!version_apk.equalsIgnoreCase(versionName)){
                                show_notif = 1;

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "200");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 70000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 70000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(stringRequest);
    }

    private String getCurrentDate() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = frmt.format(current);
        return dateString;
    }

    private class getDataP extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressdialog = new ProgressDialog(MainActivity.this);

            progressdialog.setIndeterminate(false);

            progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            progressdialog.setCancelable(false);

            progressdialog.setMessage("Download data dari server ...");

            progressdialog.setMax(jumlah_data);

            progressdialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config.URL+"SinkronPemutakhiran");

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json = data.getJSONObject(i);
                        String today = getCurrentDate();
                        databaseHandler.add_data_sinkron(new ItemData(
                                0,json.getString("id_inc"),json.getString("nama_usaha"),
                                json.getString("npwpd"), json.getString("alamat_usaha"),
                                json.getString("kodekec"),json.getString("kodekel"),
                                json.getString("namakec"),json.getString("namakel"),json.getString("namawp"),
                                json.getString("jenis_pajak"),json.getString("namapajak"),
                                json.getString("golongan"),json.getString("id_op"),"0",today
                        ));

                        status = i+1;
                        publishProgress(status);

                        System.out.println("No. "+String.valueOf(i)+", Nama Usaha : "+json.getString("nama_usaha"));
                    }
                } catch (final JSONException e) {

                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences;
                            sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                            String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                            String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                            String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");
                            String info = e.getMessage();
                            String activity = "MainActivity-getDataP";

                            sendLog(username, sdk, model, info, activity);
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences;
                        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                        String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                        String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                        String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");
                        String info = jsonStr;
                        String activity = "MainActivity-getDataP";

                        sendLog(username, sdk, model, info, activity);
                    }
                });

            }

            return null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            progressdialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressdialog.isShowing())
                progressdialog.dismiss();
            View view = findViewById(R.id.content);
            Log.d("DATA_SERVER",String.valueOf(status)+"|"+String.valueOf(databaseHandler.CountAllDataMaster()));
            Snackbar.make(view, String.valueOf(databaseHandler.CountAllDataMaster())+" data berhasil disinkron !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            /**
             * Updating parsed JSON data into ListView
             * */
        }

    }

    private void sendLog(String username, String sdk, String model, String info, String activity_class) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Config.URL +"SaveLogError";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("sukses")){
                    Log.d("SendLog", "Berhasil kirim log");
                }else{
                    Log.d("SendLog", "Gagal kirim log");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error : ", error.toString());
                Toast.makeText(MainActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("sdk", sdk);
                params.put("model", model);
                params.put("info", info);
                params.put("activity_class", activity_class);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(stringRequest);
    }


}
