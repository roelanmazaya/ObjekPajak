package com.dnhsolution.objekpajak.pendataan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dnhsolution.objekpajak.BuildConfig;
import com.dnhsolution.objekpajak.MainActivity;
import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.coba.Main2Activity;
import com.dnhsolution.objekpajak.coba.MainCoba;
import com.dnhsolution.objekpajak.config.Config;
import com.dnhsolution.objekpajak.config.SplashActivity;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.database.ItemGambar;
import com.dnhsolution.objekpajak.pendataan.helpers.GPSTracker;
import com.dnhsolution.objekpajak.pendataan.model.GolonganAdapter;
import com.dnhsolution.objekpajak.pendataan.model.ItemGol;
import com.dnhsolution.objekpajak.pendataan.model.ItemGolongan;
import com.dnhsolution.objekpajak.pendataan.model.ItemJenisPajak;
import com.dnhsolution.objekpajak.pendataan.model.ItemKecamatan;
import com.dnhsolution.objekpajak.pendataan.model.ItemKelurahan;
import com.dnhsolution.objekpajak.pendataan.model.ItemNamaUsaha;
import com.dnhsolution.objekpajak.riwayat.DetailActivity;
import com.dnhsolution.objekpajak.riwayat.RiwayatActivity;
import com.dnhsolution.objekpajak.riwayat.RiwayatAdapter;
import com.dnhsolution.objekpajak.riwayat.model.ItemRiwayat;
import com.dnhsolution.objekpajak.sinkron.SinkronActivity;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PendataanActivity extends AppCompatActivity implements LocationListener, GolonganAdapter.GolonganAdapterListener{

    ProgressDialog dialog = null;
    LinearLayout frmLok;
    LocationManager locationManager;
    static final int REQUEST_LOCATION = 1;
    private Button btnSimpan, btnEdit, btLokasi;
    EditText etLat, etLong, etAlamat, etKecamatan, etKelurahan,
    et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11, et12,
    etNamaWP, etNamaTempatUsaha, etJenisPajak, etGolongan, etIdInc;

    //khusus reklame
    EditText etPanjang, etLebar, etTinggi;
    LinearLayout LReklame;

    CountDownTimer myTimer = null;
    DatabaseHandler databaseHandler;
//    MyLocationListener myLL = new MyLocationListener();

    //Fitur Kamera
    RecyclerView rvBerkas;
    private BerkasAdapter bAdapter;
    private List<Berkas> berkasList = new ArrayList<>();
    ImageView ivFoto;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String tempNameFile = "ObjekPajak.jpg";
    private static final int FILE_SELECT_CODE = 5;
    private Uri filePath;
    private static final String IMAGE_DIRECTORY = "/ObjekPajak";
    private File destFile;
    private SimpleDateFormat dateFormatter;
    File wallpaperDirectory;
    int RecyclerViewClickedItemPos;
    View ChildView, vNamaUsaha;
    TextView tvNamaUsaha;

    Spinner spinJenisPajak, spinGolongan, spinNamaUsaha;
    String spJP="", spG="0", spNU="";
    String namaJP="", namaG="", namaNU="";
    ArrayAdapter<ItemJenisPajak> adapterJP;
    ArrayAdapter<ItemGolongan> adapterGol;
    ArrayAdapter<ItemNamaUsaha> adapterNU;
    ArrayList<ItemJenisPajak> pajakArrayList = new ArrayList<>();
    ArrayList<ItemGolongan> golonganArrayList = new ArrayList<>();
    ArrayList<ItemNamaUsaha> namausahaArrayList = new ArrayList<>();
    String id_op="";
    int id_data;

    GPSTracker gpsTracker;

    int status_gbr = 0;

    //kebutuhan select option golongan
    TextView tvGol;
    LinearLayout LGol;

    RecyclerView rvData;
    List<ItemGol> dataGolongan;
    List<ItemGol> dataGolonganOri;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    private GolonganAdapter adapter;
    View ChildViewGol;
    int RecyclerViewClickedItemPosGol;
    TextView tvKet;

    //keb form kecamatan-kelurahan baru
    Spinner spinKec, spinKel;
    String spKec="", spKel="";
    String namaKec="", namaKel="";
    String kode_kel = "";
    ArrayAdapter<ItemKecamatan> adapterKec;
    ArrayAdapter<ItemKelurahan> adapterKel;
    ArrayList<ItemKecamatan> kecArrayList = new ArrayList<>();
    ArrayList<ItemKelurahan> kelArrayList = new ArrayList<>();
    String status_data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendataan);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pendataan dan Pemutakhiran");

        init();

        requestMultiplePermissions(); // check permission

        //function mencari data dengan NOP
        searchNop();

        //Function mencari lokasi
        searchLokasi();

        //FITUR KAMERA

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_gbr = 1;
                //Toast.makeText(PendataanActivity.this, String.valueOf(status_gbr), Toast.LENGTH_SHORT).show();
                int bks = rvBerkas.getAdapter().getItemCount();

                if(bks>9){
                    Snackbar snackbar = Snackbar
                            .make(view, "Foto maksimum 10 !", Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                    builder.setMessage("Pilihan Tambah Foto")
                            .setPositiveButton("Galeri", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    MY_CAMERA_PERMISSION_CODE);
                                            //showFileChooser();
                                        } else {
                                            showFileChooser();
                                        }
                                    } else {
                                        showFileChooser();
                                    }
                                }
                            })
                            .setNegativeButton("Kamera", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    MY_CAMERA_PERMISSION_CODE);
                                            //showFileChooser();
                                        } else {
                                            try {
                                                Calendar cal = Calendar.getInstance();
                                                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss", Locale.getDefault());
                                                tempNameFile = "KOP_"+sdf.format(cal.getTime())+".jpg";
                                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                File f = new File(wallpaperDirectory, tempNameFile);
                                                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                                        BuildConfig.APPLICATION_ID + ".provider",
                                                        f);
                                                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                            }catch (Exception e){
                                                String info = e.getMessage();
                                                String activity = "PendataanActivity-ivFoto";

                                                sendLog(info, activity);
                                            }

                                        }
                                    } else {
                                        try {
                                            Calendar cal = Calendar.getInstance();
                                            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss", Locale.getDefault());
                                            tempNameFile = "KOP_"+sdf.format(cal.getTime())+".jpg";
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            File f = new File(wallpaperDirectory, tempNameFile);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                            startActivityForResult(intent, CAMERA_REQUEST);
                                        }catch (Exception e){

                                            String info = e.getMessage();
                                            String activity = "PendataanActivity-ivFoto2";
                                            sendLog(info, activity);
                                        }

                                    }

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        rvBerkas.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                ChildView = rvBerkas.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {
                    RecyclerViewClickedItemPos = rvBerkas.getChildAdapterPosition(ChildView);
                    Log.d("hhhhuu", String.valueOf(RecyclerViewClickedItemPos));
                    dialogDetailGambar(RecyclerViewClickedItemPos);
                    //removeberkasnAt(RecyclerViewClickedItemPos);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //ACTION BUTTON SIMPAN
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spJP.equalsIgnoreCase("06")){
                    spG="-";
                    namaG="-";
                }

                if(etAlamat.getText().toString().trim().equalsIgnoreCase("")){
                    etAlamat.requestFocus();
                    etAlamat.setError("Silahkan isi form ini !");
                }else if(etKecamatan.getText().toString().trim().equalsIgnoreCase("")){
                    etKecamatan.requestFocus();
                    etKecamatan.setError("Silahkan isi form ini !");
                }else if(etKelurahan.getText().toString().trim().equalsIgnoreCase("")){
                    etKelurahan.requestFocus();
                    etKelurahan.setError("Silahkan isi form ini !");
                }else if(spKec.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Silahkan Pilih Kecamatan !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else if(spKel.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Silahkan Pilih Kelurahan !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else if(spJP.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Silahkan Pilih Jenis Pajak !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else if(spG.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                                .make(view, "Silahkan Pilih Golongan !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();

                }
                else{

                    if(etIdInc.getText().toString().equalsIgnoreCase("")){
                        if(etNamaTempatUsaha.getText().toString().trim().equalsIgnoreCase("")){
                            etNamaTempatUsaha.requestFocus();
                            etNamaTempatUsaha.setError("Silahkan isi form ini !");
                        }else{
                            if(etLat.getText().toString().equalsIgnoreCase("") || etLong.getText().toString().equalsIgnoreCase("")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                                final Dialog alertDialog;

                                    builder.setTitle("Koordinat Lat-Long masih kosong !");
                                    builder.setMessage("Anda ingin menyimpan atau mencari koordinat Lat-Long lagi ?");
                                    builder.setPositiveButton("Cari Lokasi", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Show location settings when the user acknowledges the alert dialog
                                            //getLoc(1);
                                            CariLokasi();
                                        }
                                    });
                                    builder.setNegativeButton("Simpan Data", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            InsertDataBaru();
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.setCanceledOnTouchOutside(true);
                                    alertDialog.show();
                            }else{
                                //Toast.makeText(PendataanActivity.this, "Insert Baru", Toast.LENGTH_SHORT).show();
                                InsertDataBaru();
                            }
                        }
                    }else{
                        if(etLat.getText().toString().equalsIgnoreCase("") || etLong.getText().toString().equalsIgnoreCase("")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                            final Dialog alertDialog;

                            builder.setTitle("Koordinat Lat-Long masih kosong !");
                            builder.setMessage("Anda ingin menyimpan atau mencari koordinat Lat-Long lagi ?");
                            builder.setPositiveButton("Cari Lokasi", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Show location settings when the user acknowledges the alert dialog
                                    //getLoc(1);
                                    CariLokasi();
                                }
                            });
                            builder.setNegativeButton("Simpan Data", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UpdatePemutakhiran();
                                    //Toast.makeText(PendataanActivity.this, "Update", Toast.LENGTH_SHORT).show();
                                }
                            });

                            alertDialog = builder.create();
                            alertDialog.setCanceledOnTouchOutside(true);
                            alertDialog.show();
                        }else{
                            //Toast.makeText(PendataanActivity.this, "Update", Toast.LENGTH_SHORT).show();
                            UpdatePemutakhiran();
                        }
                    }

                }
            }
        });

        spinJenisPajak.setEnabled(false);
        spinGolongan.setEnabled(false);
        spinKec.setEnabled(false);
        spinKel.setEnabled(false);

        getKecamatan();

        spinKec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemKecamatan itemKecamatan = (ItemKecamatan) parent.getSelectedItem();

                spKec = itemKecamatan.getKd_kec();
                namaKec = itemKecamatan.getNama_kec();
                if(spKec.equalsIgnoreCase("0")){

                }else{
                    getKelurahan(spKec);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinKel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemKelurahan itemKelurahan = (ItemKelurahan) parent.getSelectedItem();

                spKel = itemKelurahan.getKd_kel();
                namaKel = itemKelurahan.getNama_kel();

                if(spKel.equalsIgnoreCase("0")){
                    // ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#e22051"));

//                    Snackbar.make(view, "Silahkan Pilih Golongan !", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    //Toast.makeText(getApplicationContext(), "Silahkan Pilih Nop", Toast.LENGTH_SHORT).show();
                }else{
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinJenisPajak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemJenisPajak itemJenisPajak = (ItemJenisPajak) parent.getSelectedItem();

                spJP = itemJenisPajak.getKd_jenis_pajak();
                namaJP = itemJenisPajak.getNama_pajak();
                tvGol.setText("Pilih Golongan");
                spG = "0";
                namaG = "";
                if(spJP.equalsIgnoreCase("0")){

                }else{
                   //getGolongan(spJP);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinGolongan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemGolongan itemGolongan = (ItemGolongan) parent.getSelectedItem();

                spG = itemGolongan.getKd_gol();
                namaG = itemGolongan.getNama_gol();

                if(spG.equalsIgnoreCase("0")){
                    // ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#e22051"));

//                    Snackbar.make(view, "Silahkan Pilih Golongan !", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    //Toast.makeText(getApplicationContext(), "Silahkan Pilih Nop", Toast.LENGTH_SHORT).show();
                }else{
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinNamaUsaha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemNamaUsaha inu = (ItemNamaUsaha) parent.getSelectedItem();

                spNU = inu.getId_inc();
                String []splitArray=inu.getNama_tempat_usaha().split("-");
                String new_text=splitArray[0];
                namaNU = new_text;

                //Toast.makeText(PendataanActivity.this, namaNU, Toast.LENGTH_SHORT).show();

                if(spNU.equalsIgnoreCase("0")){
                    //golonganArrayList.clear();
                    // ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#e22051"));

//                    Snackbar.make(view, "Silahkan Pilih Jenis Pajak !", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    //Toast.makeText(getApplicationContext(), "Silahkan Pilih Nop", Toast.LENGTH_SHORT).show();
                }else{
                    etNamaWP.setText(inu.getNama_wp());
                    //etNamaTempatUsaha.setText(inu.getNama_tempat_usaha());
                    etNamaTempatUsaha.setVisibility(View.GONE);
                    etAlamat.setText(inu.getAlamat());
                    etKecamatan.setText(inu.getKecamatan());
                    etKelurahan.setText(inu.getKelurahan());
                    etJenisPajak.setText(inu.getJenispajak());
                    etGolongan.setText(inu.getGolongan());
                    etIdInc.setText(inu.getId_inc());

                    String kode_kec = inu.getKd_kecamatan();
                    kode_kel = inu.getKd_kelurahan();
                    String jp = inu.getJenispajak();
                    id_op = inu.getId_op();
                    id_data = Integer.parseInt(inu.getId_data());
                    status_data = inu.getStatus();

                    //Toast.makeText(PendataanActivity.this, status_data, Toast.LENGTH_SHORT).show();

                    int i;
                    for(i=0;i<pajakArrayList.size();i++){
                        String p = pajakArrayList.get(i).getKd_jenis_pajak();
                        String nm_p = pajakArrayList.get(i).getNama_pajak();
                        if(p.equalsIgnoreCase(jp)){
                            spinJenisPajak.setSelection(i);
                            spJP=p;
                            namaJP=nm_p;
                            getGolongan(spJP);
                        }
                    }

                    int x;
                    for(x=0;x<kecArrayList.size();x++){
                        String kd_kec = kecArrayList.get(x).getKd_kec();
                        String nm_kec = kecArrayList.get(x).getNama_kec();
                        if(kd_kec.equalsIgnoreCase(kode_kec)){
                            spinKec.setSelection(x);
                            spKec=kd_kec;
                            namaKec=nm_kec;
                            getKelurahan(spKec);
                        }
                    }
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void searchLokasi() {
        frmLok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etAlamat.getText().toString().trim().equalsIgnoreCase("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                    builder.setTitle("Lokasi sudah ada !");
                    builder.setMessage("Yakin untuk memperbarui lokasi ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            CariLokasi();
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }else{
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        // Build the alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
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
                        CariLokasi();
                    }
                }


            }
        });

        btLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etAlamat.getText().toString().trim().equalsIgnoreCase("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                    builder.setTitle("Lokasi sudah ada !");
                    builder.setMessage("Yakin untuk memperbarui lokasi ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Show location settings when the user acknowledges the alert dialog
                            CariLokasi();
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    Dialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }else{
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        // Build the alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
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
                    }else{
                        CariLokasi();
                    }
                }
            }
        });
    }

    private void searchNop() {
        et1.requestFocus();
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et1.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et1.getText().toString().length()==0){
                    et1.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et2.requestFocus();
                }

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et2.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et2.getText().toString().length()==0){
                    et1.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et3.requestFocus();
                }

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et3.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et3.getText().toString().length()==0){
                    et2.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et4.requestFocus();
                }

            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et4.getText().toString().length()==0){
//                    et5.requestFocus();
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                }
                et4.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et4.getText().toString().length()==0){
                    et3.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et5.requestFocus();
                }

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et5.getText().toString().length()==0){
//                    et6.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et5.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et5.getText().toString().length()==0){
                    et4.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et6.requestFocus();
                }

            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et6.getText().toString().length()==0){
//                    et7.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et6.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et6.getText().toString().length()==0){
                    et5.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else {
                    et7.requestFocus();
                }

            }
        });
        et7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et7.getText().toString().length()==0){
//                    et8.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et7.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et7.getText().toString().length()==0){
                    et6.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et8.requestFocus();
                }

            }
        });
        et8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et8.getText().toString().length()==0){
//                    et9.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et8.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et8.getText().toString().length()==0){
                    et7.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et9.requestFocus();
                }

            }
        });
        et9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et9.getText().toString().length()==0){
//                    et10.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et9.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et9.getText().toString().length()==0){
                    et8.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et10.requestFocus();
                }

            }
        });
        et10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et10.getText().toString().length()==0){
//                    et11.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                    //etNamaWP.setVisibility(View.VISIBLE);
//                }
                et10.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et10.getText().toString().length()==0){
                    et9.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et11.requestFocus();
                }

            }
        });
        et11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et11.getText().toString().length()==0){
//                    et12.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
//                   // etNamaWP.setVisibility(View.VISIBLE);
//                }
                et11.requestFocus();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et11.getText().toString().length()==0){
                    et10.requestFocus();
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }else{
                    et12.requestFocus();
                }

            }
        });
        et12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(et12.getText().toString().length()==1){
//                    et12.requestFocus();
//                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
////                    etNamaWP.setVisibility(View.VISIBLE);
//                }
                et12.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et12.getText().toString().length()==1){
                    if(et1.getText().toString().equalsIgnoreCase("")){
                        et1.requestFocus();
                        et1.setError("Harap isi bidang ini !");
                    }else if(et2.getText().toString().equalsIgnoreCase("")){
                        et2.requestFocus();
                        et2.setError("Harap isi bidang ini !");
                    }else if(et3.getText().toString().equalsIgnoreCase("")){
                        et3.requestFocus();
                        et3.setError("Harap isi bidang ini !");
                    }else if(et4.getText().toString().equalsIgnoreCase("")){
                        et4.requestFocus();
                        et4.setError("Harap isi bidang ini !");
                    }else if(et5.getText().toString().equalsIgnoreCase("")){
                        et5.requestFocus();
                        et5.setError("Harap isi bidang ini !");
                    }else if(et6.getText().toString().equalsIgnoreCase("")){
                        et6.requestFocus();
                        et6.setError("Harap isi bidang ini !");
                    }else if(et7.getText().toString().equalsIgnoreCase("")){
                        et7.requestFocus();
                        et7.setError("Harap isi bidang ini !");
                    }else if(et8.getText().toString().equalsIgnoreCase("")){
                        et8.requestFocus();
                        et8.setError("Harap isi bidang ini !");
                    }else if(et9.getText().toString().equalsIgnoreCase("")){
                        et9.requestFocus();
                        et9.setError("Harap isi bidang ini !");
                    }else if(et10.getText().toString().equalsIgnoreCase("")){
                        et10.requestFocus();
                        et10.setError("Harap isi bidang ini !");
                    }else if(et11.getText().toString().equalsIgnoreCase("")){
                        et11.requestFocus();
                        et11.setError("Harap isi bidang ini !");
                    }else if(et12.getText().toString().equalsIgnoreCase("")){
                        et12.requestFocus();
                        et12.setError("Harap isi bidang ini !");
                    }else{

                        etAlamat.setText("");
                        etKecamatan.setText("");
                        etKelurahan.setText("");
                        etJenisPajak.setText("");
                        etGolongan.setText("");
                        etNamaWP.setText("");
                        etNamaTempatUsaha.setText("");
                        //etLat.setText("");
                        //etLong.setText("");
                        spinKec.setSelection(0);
                        spinKel.setSelection(0);
                        spinJenisPajak.setSelection(0);
                        spinGolongan.setSelection(0);
                        status_data = "";


                        try{
                            String npwpd = et1.getText().toString()+et2.getText().toString()+et3.getText().toString()
                                    +et4.getText().toString()+et5.getText().toString()+et6.getText().toString()
                                    +et7.getText().toString()+et8.getText().toString()+et9.getText().toString()
                                    +et10.getText().toString()+et11.getText().toString()+et12.getText().toString();
//                            Cursor cursor = db.query(databaseHandler.TABLE_MASTER,
//                                    new String[]{databaseHandler.col_namawp, databaseHandler.col_nama_usaha,
//                                            databaseHandler.col_alamat_usaha,databaseHandler.col_namakec,
//                                            databaseHandler.col_namakel, databaseHandler.col_namapajak,
//                                            databaseHandler.col_golongan, databaseHandler.col_id_inc, databaseHandler.col_jenis_pajak, databaseHandler.col_id_op,
//                                            databaseHandler.col_lati, databaseHandler.col_longi, databaseHandler.col_id_data},
//                                    databaseHandler.col_npwpd+ "=?",
//                                    new String[]{npwpd}, null, null, null, null);
//
//                            if (cursor != null)
//                                cursor.moveToFirst();

                            // prepare note object
//                            int cek = databaseHandler.count_data_master(npwpd);
                            int cek = 0;
                            SQLiteDatabase db = databaseHandler.getReadableDatabase();
                            Cursor c = db.rawQuery("SELECT jenis_pajak, status FROM ms_data WHERE npwpd="+npwpd, null);
                            if (c.moveToFirst()){
                                do {
                                    // Passing values
                                    String jenisPajak = c.getString(0);
                                    String sts = c.getString(1);
                                    if(jenisPajak.equalsIgnoreCase("04")){
                                        //status 00 = data lebih dari 1
                                        if(sts.equalsIgnoreCase("00")){
                                        }else if(sts.equalsIgnoreCase("01")){
                                        }else{
                                            cek++;
                                            Log.d("jp_status", jenisPajak+"//"+sts);
                                        }
                                    } else if(jenisPajak.equalsIgnoreCase("06")){
                                        //status 00 = data lebih dari 1
                                        if(sts!="00" || sts!="01"){
                                            cek++;
                                        }
                                    }
                                    else{
                                        if(sts.equalsIgnoreCase("0")){
                                            cek++;
                                        }
                                    }
                                    // Do something Here with values
                                } while(c.moveToNext());
                            }
                            c.close();
                            db.close();

                            //Toast.makeText(PendataanActivity.this, String.valueOf(cek), Toast.LENGTH_SHORT).show();

                            if(cek!=0){
                                etNamaTempatUsaha.setVisibility(View.GONE);
                                tvNamaUsaha.setVisibility(View.VISIBLE);
                                vNamaUsaha.setVisibility(View.VISIBLE);
                                spinNamaUsaha.setVisibility(View.VISIBLE);
                                etNamaWP.setVisibility(View.VISIBLE);
                                getDataByNPWPD(npwpd);
//                                etNamaWP.setText(cursor.getString(0));
//                                etNamaTempatUsaha.setText(cursor.getString(1));
//                                etAlamat.setText(cursor.getString(2));
//                                etKecamatan.setText(cursor.getString(3));
//                                etKelurahan.setText(cursor.getString(4));
//                                etJenisPajak.setText(cursor.getString(5));
//                                etGolongan.setText(cursor.getString(6));
//                                etIdInc.setText(cursor.getString(7));
//                                String jp = cursor.getString(8);
//                                id_op = cursor.getString(9);
//                                id_data = Integer.parseInt(cursor.getString(12));
//                                //etLat.setText(cursor.getString(10));
//                                //etLong.setText(cursor.getString(11));
//
//                                int i;
//                                for(i=0;i<pajakArrayList.size();i++){
//                                    String p = pajakArrayList.get(i).getKd_jenis_pajak();
//                                    String nm_p = pajakArrayList.get(i).getNama_pajak();
//                                    if(p.equalsIgnoreCase(jp)){
//                                        spinJenisPajak.setSelection(i);
//                                        spJP=p;
//                                        namaJP=nm_p;
//                                    }
//                                }
                            }else{
                                View view = findViewById(R.id.content);
                                Snackbar.make(view, "Data tidak ditemukan !", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                            //cursor.close();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    et11.requestFocus();
                    etIdInc.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                    etNamaWP.setText("");
                    etNamaTempatUsaha.setText("");
                    spinKec.setSelection(0);
                    spinKel.setSelection(0);
                    spinJenisPajak.setSelection(0);
                    spinGolongan.setSelection(0);
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    etNamaWP.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void init(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        frmLok = (LinearLayout) findViewById(R.id.frmLokasi);
        databaseHandler=new DatabaseHandler(PendataanActivity.this);
        gpsTracker = new GPSTracker(this);

        try{
            wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
                wallpaperDirectory.mkdirs();
            }
        }catch (Exception e){
            String info = e.getMessage();
            String activity = "PendataanActivity-init";

            sendLog(info, activity);
        }

        etLat = (EditText)findViewById(R.id.etLat);
        etLong = (EditText)findViewById(R.id.etLong);
        etAlamat = (EditText)findViewById(R.id.etAlamatUsaha);
        etKecamatan = (EditText)findViewById(R.id.etKec);
        etKelurahan = (EditText)findViewById(R.id.etKel);
        etNamaWP = (EditText)findViewById(R.id.etNamaWP);
        etNamaTempatUsaha = (EditText)findViewById(R.id.etNamaTempatUsaha);
        etJenisPajak = (EditText)findViewById(R.id.etJenisPajak);
        etGolongan = (EditText)findViewById(R.id.etGol);
        etIdInc = (EditText)findViewById(R.id.etIdInc);
        btLokasi = (Button)findViewById(R.id.btnLokasi);
        btnSimpan = (Button)findViewById(R.id.btnSimpan);
        spinGolongan = (Spinner)findViewById(R.id.spGolongan);
        spinJenisPajak = (Spinner)findViewById(R.id.spJenisPajak);
        spinNamaUsaha = (Spinner)findViewById(R.id.spNamaUsaha);
        spinKec = (Spinner)findViewById(R.id.spKec);
        spinKel = (Spinner)findViewById(R.id.spKel);

        tvGol = (TextView)findViewById(R.id.tvGol);
        LGol = (LinearLayout)findViewById(R.id.LGol);

        //camera
        ivFoto = (ImageView)findViewById(R.id.ivFoto);
        rvBerkas = (RecyclerView)findViewById(R.id.rvBerkas);
        bAdapter= new BerkasAdapter(berkasList);
        RecyclerView.LayoutManager mLayoutManagerss = new LinearLayoutManager(getApplicationContext());
        //rvBerkas.setLayoutManager(mLayoutManagerss);
        rvBerkas.setLayoutManager(new GridLayoutManager(this, 5));
        rvBerkas.setItemAnimator(new DefaultItemAnimator());
        rvBerkas.setAdapter(bAdapter);

        vNamaUsaha = (View)findViewById(R.id.vNamaUsaha);
        tvNamaUsaha = (TextView)findViewById(R.id.tvNamaUsaha);
        etNamaWP.setVisibility(View.GONE);

        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        et4 = (EditText)findViewById(R.id.et4);
        et5 = (EditText)findViewById(R.id.et5);
        et6 = (EditText)findViewById(R.id.et6);
        et7 = (EditText)findViewById(R.id.et7);
        et8 = (EditText)findViewById(R.id.et8);
        et9 = (EditText)findViewById(R.id.et9);
        et10 = (EditText)findViewById(R.id.et10);
        et11 = (EditText)findViewById(R.id.et11);
        et12 = (EditText)findViewById(R.id.et12);

        //reklame
        etPanjang = (EditText)findViewById(R.id.etPanjang);
        etLebar = (EditText)findViewById(R.id.etLebar);
        etTinggi = (EditText)findViewById(R.id.etTinggi);
        LReklame = (LinearLayout)findViewById(R.id.LReklame);

        LReklame.setVisibility(View.GONE);


        tvGol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spJP.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Silahkan Pilih Jenis Pajak !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else{
                    dialogPeringatan(spJP);
                }
            }
        });

        LGol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spJP.equalsIgnoreCase("0")){
                    Snackbar snackbar = Snackbar
                            .make(view, "Silahkan Pilih Jenis Pajak !", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                }else{
                    dialogPeringatan(spJP);
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //clear - try catch
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == this.RESULT_CANCELED){
            return;
        }
        if(requestCode == FILE_SELECT_CODE){
            filePath = data.getData();
            String a = RealPathUtil.getRealPath(getApplicationContext(), filePath);

            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                Log.d("Foto", "File Uri: " + uri.toString());
                // Get the path
                String path = a;

                Log.d("Foto", "File Path: " + path);
                File sourceLocation = new File (path);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss", Locale.getDefault());
                String filename = "GOP_"+sdf.format(cal.getTime())+".jpg";
                File targetLocation = new File (wallpaperDirectory.toString(), filename);

                if(sourceLocation.exists()){

                    Log.v("Pesan", "Proses Pindah");
                    try {
                        InputStream in = new FileInputStream(sourceLocation);
                        OutputStream out = new FileOutputStream(targetLocation);
                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        Log.v("Pesan", "Copy file successful.");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        String info = e.getMessage();
                        String activity = "PendataanActivity-onActivityResultFile1";

                        sendLog(info, activity);
                    } catch (IOException e) {
                        e.printStackTrace();
                        String info = e.getMessage();
                        String activity = "PendataanActivity-onActivityResultFile2";

                        sendLog(info, activity);
                    }
                }else{
                    Log.v("Pesan", "Copy file failed. Source file missing.");
                    String info = "Copy file failed. Source file missing.";
                    String activity = "PendataanActivity-onActivityResultFile3";

                    sendLog(info, activity);
                }

                File file = new File(wallpaperDirectory.toString(),filename);
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));

                Log.d("PirangMB", String.valueOf(file_size));

                Berkas berkas = new Berkas(file.getAbsolutePath(), file_size);
                berkasList.add(berkas);
                bAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == CAMERA_REQUEST){
            if (resultCode == RESULT_OK) {
                System.out.println("CAMERA_REQUEST1");
//                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    File f = new File(wallpaperDirectory.toString());
                    Log.d("File", String.valueOf(f));
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals(tempNameFile)) {
                            f = temp;
                            File filePhoto = new File(wallpaperDirectory.toString(), tempNameFile);
                            //pic = photo;

                            int file_size = Integer.parseInt(String.valueOf(filePhoto.length() / 1024));

                            Log.d("PirangMB", String.valueOf(file_size));
                            //tvFileName.setVisibility(View.VISIBLE);
                            // ivBerkas.setVisibility(View.VISIBLE);

                            Berkas berkas = new Berkas(f.getAbsolutePath(), file_size);
                            berkasList.add(berkas);
                            //gridberkasList.add(berkas);

                            bAdapter.notifyDataSetChanged();
                            //gbAdapter.notifyDataSetChanged();
                            break;
                        }

                    }
                }catch (Exception e){
                    String info = e.getMessage();
                    String activity = "PendataanActivity-onActivityResultCamera";

                    sendLog(info, activity);
                }


            }
        }
    }

    public void removeberkasnAt(int position){
        File file = new File(berkasList.get(position).getFilename());
        boolean deleted = file.delete();
        berkasList.remove(position);
        bAdapter.notifyItemRemoved(position);
        bAdapter.notifyItemRangeChanged(position, berkasList.size());
        rvBerkas.setAdapter(bAdapter);
    }

    //clear
    public void UpdatePemutakhiran(){

        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Menyimpan Data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        int bks = rvBerkas.getAdapter().getItemCount();

        final DatabaseHandler databaseHandler=new DatabaseHandler(this);
        String today = getCurrentDate();

        if(bks!=0){
            int x;
            for ( x = 0; x < bks; x++){
                String file = berkasList.get(x).getFilename();
                int size = berkasList.get(x).getFilesize();
                String path_file = "";

                if(size > 1000){
                    File f = new File(file);
                    String result = decodeFile(f, x);
                    if(result != "gagal"){
                        File path = new File(berkasList.get(x).getFilename());
                        boolean deleted = path.delete();
                        path_file = result;
                    }
                }else{
                    path_file = file;
                }

                if(!status_data.equalsIgnoreCase("0")){
                    id_data = databaseHandler.CountMaxDataMaster()+1;
                }

                databaseHandler.addDataFoto(new ItemGambar(0, String.valueOf(id_data), path_file));

            }

            Log.d("JUMLAH", "x : "+String.valueOf(x)+", bks : "+String.valueOf(bks));

            if(x == bks){
                try {
                    String lati = "-";
                    String longi = "-";

                    if(!etLat.getText().toString().equalsIgnoreCase("")){
                        lati = etLat.getText().toString();
                    }

                    if(!etLong.getText().toString().equalsIgnoreCase("")){
                        longi = etLong.getText().toString();
                    }

//                    databaseHandler.updateDataPemutakhiran(new ItemData(
//                            etIdInc.getText().toString(), namaNU,
//                            etAlamat.getText().toString(), etKecamatan.getText().toString(),
//                            etKelurahan.getText().toString(), spJP, namaJP, namaG, spG,
//                            lati, longi,"2", today
//                    ));

                    SQLiteDatabase db = databaseHandler.getReadableDatabase();
                    String id_inc = etIdInc.getText().toString();
                    String npwpd = "", namawp = "";
                    Cursor c = db.rawQuery("SELECT npwpd, namawp FROM ms_data WHERE id_inc="+id_inc, null);

                    if (c.moveToFirst()){
                        do {
                            // Passing values
                            npwpd = c.getString(0);
                            namawp = c.getString(1);
                            // Do something Here with values
                        } while(c.moveToNext());
                    }
                    c.close();
                    db.close();

                    if(!status_data.equalsIgnoreCase("0")){
                        Log.i("UpdatePemutakhiran","Double, status"+status_data);
                        databaseHandler.InsertDataBaru2(new ItemData(
                                etIdInc.getText().toString(), namaNU,
                                etAlamat.getText().toString(), namaKec,
                                namaKel, spJP, namaJP, namaG, spG,
                                lati, longi,"00", today, spKec, spKel, namawp, npwpd
                        ));
                    }else{
                        Log.i("UpdatePemutakhiran","Single, status"+status_data);
                        databaseHandler.updateDataPemutakhiran(new ItemData(
                                etIdInc.getText().toString(), namaNU,
                                etAlamat.getText().toString(), namaKec,
                                namaKel, spJP, namaJP, namaG, spG,
                                lati, longi,"2", today, spKec, spKel
                        ));
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

                if(databaseHandler.equals("")){
                    Toast.makeText(getApplicationContext(),"Gagal Menyimpan ! ", Toast.LENGTH_LONG).show();
                } else{
                    etIdInc.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                    etNamaWP.setText("");
                    etNamaTempatUsaha.setText("");
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    tvNamaUsaha.setVisibility(View.GONE);
                    vNamaUsaha.setVisibility(View.GONE);
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                    et5.setText("");
                    et6.setText("");
                    et7.setText("");
                    et8.setText("");
                    et9.setText("");
                    et10.setText("");
                    et11.setText("");
                    et12.setText("");
                    etLat.setText("");
                    etLong.setText("");
                    et1.requestFocus();
                    spinKec.setSelection(0);
                    spinKel.setSelection(0);
                    spinJenisPajak.setSelection(0);
                    spinGolongan.setSelection(0);
                    spinNamaUsaha.setSelection(0);
                    berkasList.clear();
                    bAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Berhasil Menyimpan Data ! ", Toast.LENGTH_LONG).show();

                }
            }
        }else{
            try {
                String lati = "-";
                String longi = "-";

                if(!etLat.getText().toString().equalsIgnoreCase("")){
                    lati = etLat.getText().toString();
                }

                if(!etLong.getText().toString().equalsIgnoreCase("")){
                    longi = etLong.getText().toString();
                }

//                databaseHandler.updateDataPemutakhiran(new ItemData(
//                        etIdInc.getText().toString(), namaNU,
//                        etAlamat.getText().toString(), etKecamatan.getText().toString(),
//                        etKelurahan.getText().toString(), spJP, namaJP, namaG, spG,
//                        lati, longi,"2", today
//                ));

                SQLiteDatabase db = databaseHandler.getReadableDatabase();
                String id_inc = etIdInc.getText().toString();
                String npwpd = "", namawp = "";
                Cursor c = db.rawQuery("SELECT npwpd, namawp FROM ms_data WHERE id_inc="+id_inc, null);

                if (c.moveToFirst()){
                    do {
                        // Passing values
                        npwpd = c.getString(0);
                        namawp = c.getString(1);
                        // Do something Here with values
                    } while(c.moveToNext());
                }
                c.close();
                db.close();

                if(!status_data.equalsIgnoreCase("0")){
                    Log.i("UpdatePemutakhiran","Double, status"+status_data);
                    databaseHandler.InsertDataBaru2(new ItemData(
                            etIdInc.getText().toString(), namaNU,
                            etAlamat.getText().toString(), namaKec,
                            namaKel, spJP, namaJP, namaG, spG,
                            lati, longi,"00", today, spKec, spKel, namawp, npwpd
                    ));
                }else{
                    Log.i("UpdatePemutakhiran","Single, status"+status_data);
                    databaseHandler.updateDataPemutakhiran(new ItemData(
                            etIdInc.getText().toString(), namaNU,
                            etAlamat.getText().toString(), namaKec,
                            namaKel, spJP, namaJP, namaG, spG,
                            lati, longi,"2", today, spKec, spKel
                    ));
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            if(databaseHandler.equals("")){
                Toast.makeText(getApplicationContext(),"Gagal Menyimpan ! ", Toast.LENGTH_LONG).show();
            } else{
                etIdInc.setText("");
                etAlamat.setText("");
                etKecamatan.setText("");
                etKelurahan.setText("");
                etJenisPajak.setText("");
                etGolongan.setText("");
                etNamaWP.setText("");
                etNamaTempatUsaha.setText("");
                etNamaTempatUsaha.setVisibility(View.VISIBLE);
                spinNamaUsaha.setVisibility(View.GONE);
                tvNamaUsaha.setVisibility(View.GONE);
                vNamaUsaha.setVisibility(View.GONE);
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                et7.setText("");
                et8.setText("");
                et9.setText("");
                et10.setText("");
                et11.setText("");
                et12.setText("");
                etLat.setText("");
                etLong.setText("");
                et1.requestFocus();
                spinKec.setSelection(0);
                spinKel.setSelection(0);
                spinJenisPajak.setSelection(0);
                spinGolongan.setSelection(0);
                spinNamaUsaha.setSelection(0);
                berkasList.clear();
                bAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Berhasil Menyimpan Data ! ", Toast.LENGTH_LONG).show();

            }
        }

        progressDialog.dismiss();

    }

    //clear
    public void InsertDataBaru(){
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Menyimpan Data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        int bks = rvBerkas.getAdapter().getItemCount();

        final DatabaseHandler databaseHandler=new DatabaseHandler(this);
        id_data = databaseHandler.CountMaxDataMaster()+1;
        String today = getCurrentDate();

        if(bks!=0){
            int x;
            for ( x = 0; x < bks; x++){
                String file = berkasList.get(x).getFilename();
                int size = berkasList.get(x).getFilesize();
                String path_file = "";

                if(size > 1000){
                    File f = new File(file);
                    String result = decodeFile(f, x);
                    if(result != "gagal"){
                        File path = new File(berkasList.get(x).getFilename());
                        boolean deleted = path.delete();
                        path_file = result;
                    }
                }else{
                    path_file = file;
                }

                databaseHandler.addDataFoto(new ItemGambar(0, String.valueOf(id_data), path_file));

            }
            Log.d("JUMLAH", "x : "+String.valueOf(x)+", bks : "+String.valueOf(bks));

            if(x == bks) {
                try {
                    String lati = "-";
                    String longi = "-";

                    if(!etLat.getText().toString().equalsIgnoreCase("")){
                        lati = etLat.getText().toString();
                    }

                    if(!etLong.getText().toString().equalsIgnoreCase("")){
                        longi = etLong.getText().toString();
                    }

//                    databaseHandler.InsertDataBaru(new ItemData(
//                            "-", etNamaTempatUsaha.getText().toString(),
//                            etAlamat.getText().toString(), "-", etKecamatan.getText().toString(),
//                            etKelurahan.getText().toString(), spJP, namaJP, namaG, spG,
//                            lati, longi, "2", today
//                    ));

                    databaseHandler.InsertDataBaru(new ItemData(
                            "-", etNamaTempatUsaha.getText().toString(),
                            etAlamat.getText().toString(), "-", namaKec,
                            namaKel, spJP, namaJP, namaG, spG,
                            lati, longi, "2", today, spKec, spKel
                    ));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(databaseHandler.equals("")){
                    Toast.makeText(getApplicationContext(),"Gagal Menyimpan ! ", Toast.LENGTH_LONG).show();
                } else {
                    etIdInc.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etLat.setText("");
                    etLong.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                    etNamaWP.setText("");
                    etNamaTempatUsaha.setText("");
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                    et5.setText("");
                    et6.setText("");
                    et7.setText("");
                    et8.setText("");
                    et9.setText("");
                    et10.setText("");
                    et11.setText("");
                    et12.setText("");
                    et1.requestFocus();
                    spinKec.setSelection(0);
                    spinKel.setSelection(0);
                    spinJenisPajak.setSelection(0);
                    spinGolongan.setSelection(0);
                    berkasList.clear();
                    bAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Berhasil Menyimpan Data Baru ! ", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            try {
                String lati = "-";
                String longi = "-";

                if(!etLat.getText().toString().equalsIgnoreCase("")){
                    lati = etLat.getText().toString();
                }

                if(!etLong.getText().toString().equalsIgnoreCase("")){
                    longi = etLong.getText().toString();
                }
//                databaseHandler.InsertDataBaru(new ItemData(
////                        "-", etNamaTempatUsaha.getText().toString(),
////                        etAlamat.getText().toString(), "-", etKecamatan.getText().toString(),
////                        etKelurahan.getText().toString(), spJP, namaJP, namaG, spG,
////                        lati, longi, "2", today
////                ));

                databaseHandler.InsertDataBaru(new ItemData(
                        "-", etNamaTempatUsaha.getText().toString(),
                        etAlamat.getText().toString(), "-", namaKec,
                        namaKel, spJP, namaJP, namaG, spG,
                        lati, longi, "2", today, spKec, spKel
                ));

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(databaseHandler.equals("")){
                Toast.makeText(getApplicationContext(),"Gagal Menyimpan ! ", Toast.LENGTH_LONG).show();
            } else {
                etIdInc.setText("");
                etAlamat.setText("");
                etKecamatan.setText("");
                etKelurahan.setText("");
                etLat.setText("");
                etLong.setText("");
                etJenisPajak.setText("");
                etGolongan.setText("");
                etNamaWP.setText("");
                etNamaTempatUsaha.setText("");
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                et7.setText("");
                et8.setText("");
                et9.setText("");
                et10.setText("");
                et11.setText("");
                et12.setText("");
                et1.requestFocus();
                spinKec.setSelection(0);
                spinKel.setSelection(0);
                spinJenisPajak.setSelection(0);
                spinGolongan.setSelection(0);
                berkasList.clear();
                bAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Berhasil Menyimpan Data Baru ! ", Toast.LENGTH_LONG).show();
            }
        }

        progressDialog.dismiss();

    }

    private String getCurrentDate() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = frmt.format(current);
        return dateString;
    }

    //clear - try catch
    private void getKecamatan() {
        kecArrayList.clear();
        // adapterJP.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
        String url = Config.URL +"getKecamatan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    kecArrayList.add(new ItemKecamatan("0","Pilih Kecamatan"));
                    for (i=0;i<jsonArray.length();i++){
                        try {

                            JSONObject json = jsonArray.getJSONObject(i);
                            kecArrayList.add(new ItemKecamatan(json.getString("kd_kecamatan"),json.getString("nama_kecamatan")));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }


                    adapterKec = new ArrayAdapter<ItemKecamatan>(PendataanActivity.this, R.layout.spinner_item, kecArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getDataKec";

                    sendLog(info, activity);
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                spinKec.setAdapter(adapterKec);
                spinKec.setEnabled(true);
                progressDialog.dismiss();

                //mengambil data jenis pajak
                getDataJP();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getDataJP";

                sendLog(info, activity);
                Toast.makeText(PendataanActivity.this, "Respon bermasalah !", Toast.LENGTH_LONG).show();
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

    //clear - try catch
    private void getKelurahan(String kode_kecamatan) {
        kelArrayList.clear();
        // adapterJP.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
        String url = Config.URL +"getKelurahan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    kelArrayList.add(new ItemKelurahan("0","Pilih Kelurahan"));
                    for (i=0;i<jsonArray.length();i++){
                        try {

                            JSONObject json = jsonArray.getJSONObject(i);
                            kelArrayList.add(new ItemKelurahan(json.getString("kd_kelurahan"),json.getString("nama_kelurahan")));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }


                    adapterKel = new ArrayAdapter<ItemKelurahan>(PendataanActivity.this, R.layout.spinner_item, kelArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getKelurahan";

                    sendLog(info, activity);
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                spinKel.setAdapter(adapterKel);
                spinKel.setEnabled(true);
                progressDialog.dismiss();

                if(!kode_kel.equalsIgnoreCase("")){
                    int i;
                    for (i=0;i<kelArrayList.size();i++){
                        String kd_kel = kelArrayList.get(i).getKd_kel();
                        String nm_kel = kelArrayList.get(i).getNama_kel();
                        if(kd_kel.equalsIgnoreCase(kode_kel)){
                            spinKel.setSelection(i);
                            spKel=kd_kel;
                            namaKel=nm_kel;
                        }
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getDataJP";

                sendLog(info, activity);
                Toast.makeText(PendataanActivity.this, "Respon bermasalah !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "200");
                params.put("kode_kec", kode_kecamatan);

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

    //clear - try catch
    private void getDataJP() {
        pajakArrayList.clear();
       // adapterJP.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
        String url = Config.URL +"getJenisPajak";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    pajakArrayList.add(new ItemJenisPajak("0","Pilih Jenis Pajak"));
                    for (i=0;i<jsonArray.length();i++){
                        try {

                            JSONObject json = jsonArray.getJSONObject(i);
                            pajakArrayList.add(new ItemJenisPajak(json.getString("kd_jenis_pajak"),json.getString("nama_pajak")));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }


                    adapterJP = new ArrayAdapter<ItemJenisPajak>(PendataanActivity.this, R.layout.spinner_item, pajakArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getDataJP";

                    sendLog(info, activity);
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                spinJenisPajak.setAdapter(adapterJP);
                spinJenisPajak.setEnabled(true);
                progressDialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getDataJP";

                sendLog(info, activity);
                Toast.makeText(PendataanActivity.this, "Respon bermasalah !", Toast.LENGTH_LONG).show();
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

    private void getDataByNPWPD(String npwpd) {
        namausahaArrayList.clear();
        namausahaArrayList.add(new ItemNamaUsaha("0","Pilih Nama Tempat Usaha",
                "","","","","","",
                "","","", "", "", ""));

        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_inc, nama_usaha, namawp, alamat_usaha," +
                "namakec, namakel, namapajak, jenis_pajak, golongan, id_op, id_data," +
                "kodekec, kodekel, status FROM ms_data WHERE npwpd="+npwpd, null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String col1 = c.getString(0); //id_inc
                String col2 = c.getString(1); //nama_usaha
                String col3 = c.getString(2); //namawp
                String col4 = c.getString(3); //alamat_usaha
                String col5 = c.getString(4); //kec
                String col6 = c.getString(5); //kel
                String col7 = c.getString(6); //nama_pajak
                String col8 = c.getString(7); //jenis_pajak
                String col9 = c.getString(8); //golongan
                String col10 = c.getString(9); //id_op
                String col11 = c.getString(10); //id_data
                String col12 = c.getString(11); //kode_kec
                String col13 = c.getString(12); //kode_kel
                String col14 = c.getString(13); //status

                if(col8.equalsIgnoreCase("04")){
                    if(col14.equalsIgnoreCase("00")){
                    }else if( col14.equalsIgnoreCase("01")){
                    }else{
                        namausahaArrayList.add(new ItemNamaUsaha(col1,col2.trim()+" - "+ col7,
                                col3, col4, col5, col6, col7, col8,
                                col9, col10, col11, col12, col13, col14));
                    }
                } else if(col8.equalsIgnoreCase("06")){
                    if(col14.equalsIgnoreCase("00")){
                    }else if( col14.equalsIgnoreCase("01")){
                    }else{
                        namausahaArrayList.add(new ItemNamaUsaha(col1,col2.trim()+" - "+ col7,
                                col3, col4, col5, col6, col7, col8,
                                col9, col10, col11, col12, col13, col14));
                    }
                }
                else{
                    if(col14.equalsIgnoreCase("0")){
                        namausahaArrayList.add(new ItemNamaUsaha(col1,col2.trim()+" - "+ col7,
                                col3, col4, col5, col6, col7, col8,
                                col9, col10, col11, col12, col13, col14));
                    }
                }
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();

//        List<ItemNamaUsaha> listData=databaseHandler.getDataByNPWPD(npwpd);
//        for(ItemNamaUsaha f:listData){
//            try {
//                namausahaArrayList.add(new ItemNamaUsaha(f.getId_inc(),f.getNama_tempat_usaha()+" - "+ f.getNamajp(),
//                        f.getNama_wp(),f.getAlamat(),f.getKecamatan(),f.getKelurahan(),f.getNamajp(),f.getJenispajak(),
//                        f.getGolongan(),f.getId_op(),f.getId_data(), f.getKd_kecamatan(), f.getKd_kelurahan()));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }

        adapterNU = new ArrayAdapter<ItemNamaUsaha>(PendataanActivity.this, R.layout.spinner_item, namausahaArrayList);
        spinNamaUsaha.setAdapter(adapterNU);
    }

    //clear - try catch
    private void getGolongan(final String id_inc) {
        golonganArrayList.clear();
       // adapterGol.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
        String url = Config.URL +"getGolongan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    golonganArrayList.add(new ItemGolongan("0","Pilih Golongan"));
                    for (i=0;i<jsonArray.length();i++){
                        try {

                            JSONObject json = jsonArray.getJSONObject(i);
                            golonganArrayList.add(new ItemGolongan(json.getString("id_op"),json.getString("deskripsi")));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }


                    adapterGol = new ArrayAdapter<ItemGolongan>(PendataanActivity.this, R.layout.spinner_item, golonganArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getGolongan";

                    sendLog(info, activity);
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                spinGolongan.setAdapter(adapterGol);
                progressDialog.dismiss();

                if(!id_op.equalsIgnoreCase("")){
                    int i;
                    for (i=0;i<golonganArrayList.size();i++){
                        String g = golonganArrayList.get(i).getKd_gol();
                        String nm_g = golonganArrayList.get(i).getNama_gol();
                        if(g.equalsIgnoreCase(id_op)){
                            spinGolongan.setSelection(i);
                            spG=g;
                            namaG=nm_g;
                            tvGol.setText(nm_g);
                        }
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getGolongan";

                sendLog(info, activity);
                Toast.makeText(PendataanActivity.this, "Respon bermasalah !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_inc", id_inc);
                System.out.println(id_inc);

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
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
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

    //clear - try catch
    private String decodeFile(File f, int x) {
        Bitmap b = null;
        String status = "gagal";
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            BitmapFactory.decodeFile(f.getAbsolutePath(),o);
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFile";

            sendLog(info, activity);
        } catch (IOException e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFile2";

            sendLog(info, activity);
        }

        int imageHeight = o.outHeight;
        int imageWidth = o.outWidth;


        Log.d("SIZE_FILE", "Width_O : "+ imageWidth + " Height_O : "+ imageHeight);

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2.28, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFIle3";

            sendLog(info, activity);
        } catch (IOException e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFile4";

            sendLog(info, activity);
        }

        Log.d("SIZE_FILE", "Width : "+ b.getWidth() + " Height : "+ b.getHeight());
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss", Locale.getDefault());

        destFile = new File(wallpaperDirectory.toString(), "Compress_"
                + sdf.format(cal.getTime())+String.valueOf(x)+ ".jpg");

        Matrix matrix = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(f.getAbsolutePath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
        } catch (IOException e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFile5";

            sendLog(info, activity);
        }

        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);

        Log.d("SIZE_FILE", "Width_R : "+ b.getWidth() + " Height_R : "+ b.getHeight());

        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            status = destFile.toString();

        } catch (Exception e) {
            e.printStackTrace();
            String info = e.getMessage();
            String activity = "PendataanActivity-decodeFile6";

            sendLog(info, activity);
        }
        return status;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo goto back activity from here
                System.out.println("Back Top");
                gpsTracker.stopUsingGPS();
                Intent intent = new Intent(PendataanActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dialogDetailGambar(final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_detail_gambar, null);

        final LinearLayout lOk, lHapus;
        final ImageView ivGambar;

        lOk = (LinearLayout) mView.findViewById(R.id.lOk);
        lHapus = (LinearLayout) mView.findViewById(R.id.lHapus);
        ivGambar = (ImageView) mView.findViewById(R.id.ivGambar);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        Glide.with(this).load(new File(berkasList.get(position).getFilename()).toString())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("xmx1","Error "+e.toString());
                        String info = e.getMessage();
                        String activity = "PendataanActivity-Glide";

                        sendLog(info, activity);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("xmx1","no Error ");
                        return false;
                    }
                }).into(ivGambar);

//        Bitmap bmp = BitmapFactory.decodeFile(berkasList.get(position).getFilename());
//        ivGambar.setImageBitmap(bmp);

        lOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        lHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                builder.setMessage("Lanjutkan hapus gambar ini ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeberkasnAt(position);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int bks = rvBerkas.getAdapter().getItemCount();

        if(!etAlamat.getText().toString().trim().equalsIgnoreCase("")){
            if(bks==0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
                builder.setTitle("Lokasi sudah ada !");
                builder.setMessage("Yakin untuk memperbarui lokasi ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Show location settings when the user acknowledges the alert dialog
                        CariLokasi();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                Dialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }else{

                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    // Build the alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
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
                    CariLokasi();
                }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gpsTracker.stopUsingGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();

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

    public void CariLokasi(){

        View view = findViewById(R.id.content);
        Snackbar snackbar = Snackbar
                .make(view, "Sedang mencari lokasi !", Snackbar.LENGTH_LONG);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();

        try {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5, this);
            }

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }


        if(gpsTracker.canGetLocation())
        {

            String alamat = gpsTracker.getAddressLine(PendataanActivity.this);
            String kec = gpsTracker.getLocality(PendataanActivity.this);
            String kel = gpsTracker.getSubLocality(PendataanActivity.this);
            ((EditText)findViewById(R.id.etKec)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            ((EditText)findViewById(R.id.etKel)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            ((EditText)findViewById(R.id.etLat)).setText(String.valueOf(gpsTracker.getLatitude()));
            ((EditText)findViewById(R.id.etLong)).setText(String.valueOf(gpsTracker.getLongitude()));
            ((EditText)findViewById(R.id.etAlamatUsaha)).setText(alamat);
            ((EditText)findViewById(R.id.etKec)).setText(kec);
            ((EditText)findViewById(R.id.etKel)).setText(kel);

            System.out.println(alamat);
            //Toast.makeText(PendataanActivity.this, alamat, Toast.LENGTH_SHORT).show();

        }
        else
        {
            gpsTracker.showSettingsAlert();
        }
    }

    public void sendLog(String info, String activity_class) {
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
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
                Toast.makeText(PendataanActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                String username = sharedPreferences.getString(Config.PREF_USERNAME, "username");
                String sdk = sharedPreferences.getString(Config.PREF_INFO_SDK, "sdk");
                String model = sharedPreferences.getString(Config.PREF_INFO_MODEL, "model");

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

    private void dialogPeringatan(final String id_inc) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_golongan, null);


        final EditText etCari;

        final Button btnBatal;

        rvData = (RecyclerView) mView.findViewById(R.id.rvData);
        tvKet = (TextView) mView.findViewById(R.id.tvKet);
        etCari = (EditText) mView.findViewById(R.id.etCari);
        btnBatal = (Button) mView.findViewById(R.id.btnBatal);

        dataGolongan = new ArrayList<>();
        dataGolonganOri = new ArrayList<>();
        adapter = new GolonganAdapter(dataGolongan, this, this);

        linearLayoutManager = new LinearLayoutManager(PendataanActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());

        rvData.setHasFixedSize(true);
        rvData.setLayoutManager(linearLayoutManager);
        rvData.addItemDecoration(dividerItemDecoration);
        rvData.setAdapter(adapter);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        getGol(id_inc);

        etCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                //adapter.getFilter().filter(editable.toString());
                filter(editable.toString());
                System.out.println(editable.toString());
            }
        });

        rvData.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                ChildViewGol = rvData.findChildViewUnder(e.getX(), e.getY());

                if(ChildViewGol != null && gestureDetector.onTouchEvent(e)) {
                    RecyclerViewClickedItemPosGol = rvData.getChildAdapterPosition(ChildViewGol);
                    spG = dataGolongan.get(RecyclerViewClickedItemPosGol).getKd_gol();
                    namaG = dataGolongan.get(RecyclerViewClickedItemPosGol).getNama_gol();
                    tvGol.setText(dataGolongan.get(RecyclerViewClickedItemPosGol).getNama_gol());
                    closeKeyBoard();
                    dialog.dismiss();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public void getGol(String id_inc){
        dataGolongan.clear();
        dataGolonganOri.clear();
        adapter.notifyDataSetChanged();
        // adapterGol.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(PendataanActivity.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(PendataanActivity.this);
        String url = Config.URL +"getGolongan";
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
                            ItemGol is = new ItemGol();
                            is.setKd_gol(json.getString("id_op"));
                            is.setNama_gol(json.getString("deskripsi"));
                            dataGolongan.add(is);
                            dataGolonganOri.add(is);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    if(i==0){
                        tvKet.setVisibility(View.VISIBLE);
                    }else{
                        tvKet.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getGolongan";

                    sendLog(info, activity);
                }

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getGolongan";

                sendLog(info, activity);
                Toast.makeText(PendataanActivity.this, "Respon bermasalah !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_inc", id_inc);
                System.out.println(id_inc);

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

    @Override
    public void onGolonganSelected(ItemGol gol) {
        Toast.makeText(getApplicationContext(), "Selected: " + gol.getNama_gol() + ", " + gol.getKd_gol(), Toast.LENGTH_LONG).show();
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        dataGolongan.clear();

        if(text.isEmpty()){

            for (ItemGol row : dataGolonganOri) {
                //if the existing elements contains the search input
                ItemGol is = new ItemGol();
                is.setKd_gol(row.getKd_gol());
                is.setNama_gol(row.getNama_gol());
                dataGolongan.add(is);
                System.out.println("ori : "+row.getNama_gol());
            }

            tvKet.setVisibility(View.GONE);
        }else{
            int jml_filter = 0;
            for (ItemGol row : dataGolonganOri) {
                //if the existing elements contains the search input
                if (row.getNama_gol().toLowerCase().contains(text.toLowerCase())) {
                    ItemGol is = new ItemGol();
                    //adding the element to filtered list
                    is.setKd_gol(row.getKd_gol());
                    is.setNama_gol(row.getNama_gol());
                    dataGolongan.add(is);
                    System.out.println("filter : "+row.getNama_gol());
                    jml_filter++;
                }
            }

            if(jml_filter==0){
                tvKet.setVisibility(View.VISIBLE);
            }else{
                tvKet.setVisibility(View.GONE);
            }
        }

        adapter.notifyDataSetChanged();
    }


    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //MENCARI LOKASI
//    public void getLoc(final int iStatus){
//
//
//        myTimer = new CountDownTimer(15000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
//                //Toast.makeText(TambahDataObjekActivity.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
//                //here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//                //Toast.makeText(TambahDataObjekActivity.this, "Selesai", Toast.LENGTH_SHORT).show();
//               // dialog.dismiss();
//                AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
//                final Dialog alertDialog;
//
//                if(iStatus!=2){
//                    builder.setTitle("Lokasi tidak ditemukan");
//                    builder.setMessage("Apakah anda ingin melanjutkan pencarian ?");
//                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // Show location settings when the user acknowledges the alert dialog
//                            getLoc(1);
//                        }
//                    });
//                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // Show location settings when the user acknowledges the alert dialog
//                            // alertDialog.dismiss();
//                        }
//                    });
//
//                    alertDialog = builder.create();
//                    alertDialog.setCanceledOnTouchOutside(false);
//                    alertDialog.show();
//                }
//
//
//            }
//
//        };
//
//        if(iStatus==1){
////            dialog = ProgressDialog.show(PendataanActivity.this, "",
////                    "Mencari Lokasi...", true);
//            getLocation2();
//            myTimer.start();
//        }else if(iStatus==2){
//            myTimer.cancel();
//        }
//    }
//
//    public void getLoc2(final int iStatus){
//
//
//        myTimer = new CountDownTimer(15000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
//                //Toast.makeText(TambahDataObjekActivity.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
//                //here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//                //Toast.makeText(TambahDataObjekActivity.this, "Selesai", Toast.LENGTH_SHORT).show();
//                //dialog.dismiss();
//
//
//                if(!etLat.getText().toString().trim().equalsIgnoreCase("") && !etLong.getText().toString().trim().equalsIgnoreCase("")){
//
//                }else{
//                    if(iStatus!=2){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
//                        final Dialog alertDialog;
//                        builder.setTitle("Lokasi tidak ditemukan");
//                        builder.setMessage("Apakah anda ingin melanjutkan pencarian ?");
//                        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Show location settings when the user acknowledges the alert dialog
//                                getLoc(1);
//                            }
//                        });
//                        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Show location settings when the user acknowledges the alert dialog
//                                // alertDialog.dismiss();
//                            }
//                        });
//
//                        alertDialog = builder.create();
//                        alertDialog.setCanceledOnTouchOutside(false);
//                        alertDialog.show();
//                    }
//                }
//
//
//
//            }
//
//        };
//
//        if(iStatus==1){
////            dialog = ProgressDialog.show(PendataanActivity.this, "",
////                    "Mencari Lokasi...", true);
//            getLocation22();
//            myTimer.start();
//        }else if(iStatus==2){
//            myTimer.cancel();
//        }
//    }

//    void getLocation2() {
//        try {
//
//            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//            } else {
//                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if(location != null){
//
//
//                    double lati = location.getLatitude();
//                    double longi = location.getLongitude();
//
//                    //((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
//                    //((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));
//
//                    System.out.println("Lat atas: "+lati);
//                    System.out.println("Long atas : "+longi);
//                }else{
//                    System.out.println("Lat atas : null");
//                    System.out.println("Long atas : null");
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10, myLL
//                );
//            }
//
//
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    void getLocation22() {
//        try {
//
//            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//            } else {
//                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if(location != null){
//
//
//                    double lati = location.getLatitude();
//                    double longi = location.getLongitude();
//
//                    //((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
//                    //((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));
//
//                    System.out.println("Lat atas: "+lati);
//                    System.out.println("Long atas : "+longi);
//                }else{
//                    System.out.println("Lat atas : null");
//                    System.out.println("Long atas : null");
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10, myLL
//                );
//            }
//
//
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }

//    private class MyLocationListener implements LocationListener {
//        public void onLocationChanged(Location location) {
//
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    location.getLongitude(), location.getLatitude()
//            );
//
//            //Geocoder geocoder = new Geocoder(TambahDataObjekActivity.this, Locale.getDefault());
//
//            double lati = location.getLatitude();
//            double longi = location.getLongitude();
//
//            Geocoder geocoder;
//            List<Address> addresses = null;
//            geocoder = new Geocoder(PendataanActivity.this, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(lati, longi, 1);
//
//
//                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//
//            System.out.println("COy : "+addresses);
//
//            if(addresses!=null) {
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String Subcity = addresses.get(0).getSubLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//
//                System.out.println(address+"\n"+city+"\n"+Subcity+"\n"+state+"\n"+country+"\n"+postalCode+"\n"+knownName);
//                ((EditText)findViewById(R.id.etKec)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//                ((EditText)findViewById(R.id.etKel)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//
//                if(etAlamat.getText().toString().equalsIgnoreCase(address)){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PendataanActivity.this);
//                    final Dialog alertDialog;
//                    builder.setTitle("Lokasi telah diperbarui");
//                    builder.setMessage("Apakah anda ingin mengganti lokasi baru ?");
//                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // Show location settings when the user acknowledges the alert dialog
//                            ((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
//                            ((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));
//                            ((EditText)findViewById(R.id.etAlamatUsaha)).setText(String.valueOf(address));
//                            ((EditText)findViewById(R.id.etKec)).setText(String.valueOf(city));
//                            ((EditText)findViewById(R.id.etKel)).setText(String.valueOf(Subcity));
//                        }
//                    });
//                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            // Show location settings when the user acknowledges the alert dialog
//                            // alertDialog.dismiss();
//                        }
//                    });
//
//                    alertDialog = builder.create();
//                    alertDialog.setCanceledOnTouchOutside(false);
//                    alertDialog.show();
//                }else{
//
//                    ((EditText)findViewById(R.id.etLat)).setText(String.valueOf(lati));
//                    ((EditText)findViewById(R.id.etLong)).setText(String.valueOf(longi));
//                    ((EditText)findViewById(R.id.etAlamatUsaha)).setText(String.valueOf(address));
//                    ((EditText)findViewById(R.id.etKec)).setText(String.valueOf(city));
//                    ((EditText)findViewById(R.id.etKel)).setText(String.valueOf(Subcity));
//                }
//
//
//                //getLoc(2);
//                myTimer.cancel();
//                //System.out.println("getLoc(2)");
//                //dialog.dismiss();
//            }else{
//                System.out.println("Data kosong");
//            }
//            System.out.println(lati);
//            System.out.println(longi);
//
//            //mengecek apakah berhasil ambil data atau tidak
//
//
//            //Toast.makeText(TambahDataObjekActivity.this, message, Toast.LENGTH_LONG).show();
//        }
//        public void onStatusChanged(String s, int i, Bundle b) {
//            //Toast.makeText(TambahDataObjekActivity.this, "GPS Status Dirubah",
//
//            // Toast.LENGTH_LONG).show();
//
//            System.out.println("GPS Status Dirubah");
//        }
//        public void onProviderDisabled(String s) {
//            /*Toast.makeText(TambahDataObjekActivity.this,
//                    "Provider disabled by the user. GPS turned off",
//                    Toast.LENGTH_LONG).show();*/
//            View view = findViewById(R.id.content);
//            Snackbar snackbar = Snackbar
//                    .make(view, "GPS Tidak Menyala !", Snackbar.LENGTH_LONG);
//
//            // Changing action button text color
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//            textView.setTextColor(Color.YELLOW);
//
//            snackbar.show();
//        }
//        public void onProviderEnabled(String s) {
//
//            View view = findViewById(R.id.content);
//            Snackbar snackbar = Snackbar
//                    .make(view, "GPS Menyala !", Snackbar.LENGTH_LONG);
//
//
//            // Changing action button text color
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//            textView.setTextColor(Color.WHITE);
//
//            snackbar.show();
//        }
//    }



}
