package com.dnhsolution.objekpajak.config;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.dnhsolution.objekpajak.MainActivity;
import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.jaredrummler.android.widget.AnimatedSvgView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        ses_status_anim = sharedPreferences.getString(Config.PREF_STATUS_ANIM, "0");
        ses_status = sharedPreferences.getString(Config.PREF_STATUS, "0");

        init();

        svgView.postDelayed(new Runnable() {

            @Override
            public void run() {
                svgView.start();
            }

        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(ses_status.equalsIgnoreCase("0")) {
                    textsplash.animate().translationY(170).alpha(0).setDuration(800).setStartDelay(300);
                    login.setVisibility(View.VISIBLE);
                    login.startAnimation(frombottom);
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
//                    Toast.makeText(SplashActivity.this, "Login", Toast.LENGTH_SHORT).show();
                }
            }
        },3000);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsername.getText().toString().trim().equalsIgnoreCase("")){
                    etUsername.setError("Silahkan isi form ini !");
                    etUsername.requestFocus();
                }else if(etPassword.getText().toString().trim().equalsIgnoreCase("")){
                    etPassword.setError("Silahkan isi form ini !");
                    etPassword.requestFocus();
                }else{
                    sendData();
                }
            }
        });

    }

    private void init() {

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

        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

    }

    private void sendData() {
        final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
        String url = Config.URL +"Auth";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject json = jsonArray.getJSONObject(0);
                    String pesan = json.getString("pesan");
                    if(pesan.equalsIgnoreCase("0")){
                        dialogNotifikasi(pesan);
                        //Toast.makeText(MainActivity.this, "Gagal Login. Username atau Password salah !", Toast.LENGTH_SHORT).show();
                    }else if(pesan.equalsIgnoreCase("1")){
                        //String username = json.getString("username");
                        //String password = json.getString("password");
                        String id_inc = json.getString("id_inc");
                        String nip = json.getString("nip");
                        String nama = json.getString("nama");
                        String unit_upt_id = json.getString("unit_upt_id");
                        String ms_role_id = json.getString("ms_role_id");

                        String info_sdk = android.os.Build.VERSION.SDK;
                        String info_model = android.os.Build.MODEL;

                        //membuat editor untuk menyimpan data ke shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //menambah data ke editor
                        editor.putString(Config.PREF_USERNAME, etUsername.getText().toString());
                        editor.putString(Config.PREF_PASSWORD, etPassword.getText().toString());
                        editor.putString(Config.PREF_ID_INC, id_inc);
                        editor.putString(Config.PREF_NIP, nip);
                        editor.putString(Config.PREF_NAMA, nama);
                        editor.putString(Config.PREF_UNIT_UPT_ID, unit_upt_id);
                        editor.putString(Config.PREF_MS_ROLE_ID, ms_role_id);
                        editor.putString(Config.PREF_STATUS, "1");
                        editor.putString(Config.PREF_INFO_SDK, info_sdk);
                        editor.putString(Config.PREF_INFO_MODEL, info_model);

                        //menyimpan data ke editor
                        editor.apply();
                        dialogNotifikasi(pesan);
                    }else{
                        SharedPreferences sharedPreferences;
                        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                        String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                        String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                        String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");
                        String info = response;
                        String activity = "SplashActivity-sendData";

                        sendLog(username, sdk, model, info, activity);
                        Toast.makeText(SplashActivity.this, "Respon bermasalah !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SharedPreferences sharedPreferences;
                    sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                    String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                    String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                    String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");
                    String info = e.getMessage();
                    String activity = "SplashActivity-sendData";

                    sendLog(username, sdk, model, info, activity);
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
                String activity = "SplashActivity-sendData";

                sendLog(username, sdk, model, info, activity);
                Toast.makeText(SplashActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", etUsername.getText().toString());
                params.put("password", etPassword.getText().toString());

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

    private void dialogNotifikasi(final String pesan) {
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

        if(pesan.equalsIgnoreCase("0")){
            ivBerhasil.setVisibility(View.GONE);
            ivGagal.setVisibility(View.VISIBLE);
            tvKeterangan.setText("Gagal masuk ! Silahkan cek username atau password.");
        }else{
            ivBerhasil.setVisibility(View.VISIBLE);
            ivGagal.setVisibility(View.GONE);
            //ivKeterangan.setBackgroundResource(R.mipmap.ic_berhasil);
            tvKeterangan.setText("Selamat Berhasil Masuk !");
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pesan.equalsIgnoreCase("0")){
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }



            }
        });
    }

    private void sendLog(String username, String sdk, String model, String info, String activity_class) {
        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
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
                Toast.makeText(SplashActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
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
