package com.dnhsolution.objekpajak.data_op;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import android.view.WindowManager;
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
import com.dnhsolution.objekpajak.config.Config;
import com.dnhsolution.objekpajak.data_op.model.ItemFilter;
import com.dnhsolution.objekpajak.data_op.model.ItemNamaWP;
import com.dnhsolution.objekpajak.data_op.model.ItemUsaha;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.dnhsolution.objekpajak.pendataan.model.ItemGolongan;
import com.dnhsolution.objekpajak.pendataan.model.ItemNamaUsaha;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataObjekPajak extends AppCompatActivity {

    EditText etAlamat, etKecamatan, etKelurahan, etNamaWP, etNamaTempatUsaha,
            etJenisPajak, etGolongan, etNPWPD;
    EditText et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11, et12,etNamaWPC, etNamaTempatUsahaC;
    ImageView ivNamaWPC, ivNamaUsahaC;

    LinearLayout Lnamawp, Lnpwpd, Lnamausaha;
    String spFilter ="", spNamaWP ="", spNamaUsaha ="", spNUsaha="";
    TextView tvUsaha;
    View vUsaha;

    ArrayAdapter<ItemFilter> adapterFilter;
    ArrayAdapter<ItemNamaWP> adapterNamaWP;
    ArrayAdapter<ItemUsaha> adapterNamaUsaha;
    ArrayAdapter<ItemNamaUsaha> adapterNUsaha;

    ArrayList<ItemFilter> filterArrayList = new ArrayList<>();
    ArrayList<ItemNamaWP> namaWPArrayList = new ArrayList<>();
    ArrayList<ItemUsaha> namaUsahaArrayList = new ArrayList<>();
    ArrayList<ItemNamaUsaha> nUsahaArrayList = new ArrayList<>();

    Spinner spinFilter, spinNamaWP, spinNamaUsaha, spinNUsaha;
    String npwpd="";

    DatabaseHandler databaseHandler;
    CardView cvDetail;


    EditText etPanjang, etLebar, etLokasiPasang;
    EditText  etSisi, etTeks;
    CheckBox cbKetinggian;
    CheckBox cbRokok;
    LinearLayout LReklame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_objek_pajak);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Objek Pajak");

        init();

        filterByNpwpd();

        ivNamaWPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNamaWPC.getText().toString().trim().equalsIgnoreCase("")){
                    Snackbar.make(view, "Data tidak ditemukan !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{

                    getDataByFilter(spFilter);
                    spinNamaUsaha.setVisibility(View.GONE);
                    spinNUsaha.setVisibility(View.GONE);
                    tvUsaha.setVisibility(View.GONE);
                    vUsaha.setVisibility(View.GONE);
                }
            }
        });

        ivNamaUsahaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNamaTempatUsahaC.getText().toString().trim().equalsIgnoreCase("")){
                    Snackbar.make(view, "Data tidak ditemukan !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    getDataByFilter(spFilter);
                    spinNamaWP.setVisibility(View.GONE);
                }
            }
        });

        getDataFilter();

        spinAction();

    }

    private void spinAction() {

        spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemFilter itemFilter = (ItemFilter) parent.getSelectedItem();

                spFilter = itemFilter.getKd_filter();

                etNamaWP.setText("");
                //alias dari id_op
                etNPWPD.setText("");
                etNamaTempatUsaha.setText("");
                etAlamat.setText("");
                etKecamatan.setText("");
                etKelurahan.setText("");
                etJenisPajak.setText("");
                etGolongan.setText("");
                spinNUsaha.setVisibility(View.GONE);
                tvUsaha.setVisibility(View.GONE);
                vUsaha.setVisibility(View.GONE);

                if(spFilter.equalsIgnoreCase("0")){
                    Lnamawp.setVisibility(View.GONE);
                    Lnamausaha.setVisibility(View.GONE);
                    Lnpwpd.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                    spinNamaWP.setVisibility(View.GONE);
                }else if (spFilter.equalsIgnoreCase("1")){
                    Lnamawp.setVisibility(View.VISIBLE);
                    Lnamausaha.setVisibility(View.GONE);
                    Lnpwpd.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                }else if (spFilter.equalsIgnoreCase("2")){
                    Lnamawp.setVisibility(View.GONE);
                    Lnamausaha.setVisibility(View.VISIBLE);
                    Lnpwpd.setVisibility(View.GONE);
                    spinNamaWP.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                }else if (spFilter.equalsIgnoreCase("3")){
                    Lnamawp.setVisibility(View.GONE);
                    Lnamausaha.setVisibility(View.GONE);
                    Lnpwpd.setVisibility(View.VISIBLE);
                    spinNamaWP.setVisibility(View.GONE);
                    spinNamaUsaha.setVisibility(View.GONE);
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinNamaWP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemNamaWP itemNamaWP = (ItemNamaWP) parent.getSelectedItem();

                spNamaWP = itemNamaWP.getKd_data();

                if(spNamaWP.equalsIgnoreCase("0")){
                    etNamaWP.setText("");
                    //alias dari id_op
                    etNPWPD.setText("");
                    etNamaTempatUsaha.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                }else{
                    getDetailData(spNamaWP, spFilter);
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

                ItemUsaha itemUsaha = (ItemUsaha) parent.getSelectedItem();

                spNamaUsaha = itemUsaha.getKd_data();

                if(spNamaUsaha.equalsIgnoreCase("0")){
                    etNamaWP.setText("");
                    //alias dari id_op
                    etNPWPD.setText("");
                    etNamaTempatUsaha.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                }else{
//                    getDetailData(spNamaUsaha, spFilter);
                    getDetailData2(spNamaUsaha);
//                    Toast.makeText(DataObjekPajak.this, "spinNamaUsaha", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinNUsaha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ItemNamaUsaha itemNamaUsaha = (ItemNamaUsaha) parent.getSelectedItem();

                spNUsaha = itemNamaUsaha.getId_data();

                if(spNUsaha.equalsIgnoreCase("0")){
                    etNamaWP.setText("");
                    //alias dari id_op
                    etNPWPD.setText("");
                    etNamaTempatUsaha.setText("");
                    etAlamat.setText("");
                    etKecamatan.setText("");
                    etKelurahan.setText("");
                    etJenisPajak.setText("");
                    etGolongan.setText("");
                }else{
                    etAlamat.setText(itemNamaUsaha.getAlamat());
                    etKecamatan.setText(itemNamaUsaha.getKecamatan());
                    etKelurahan.setText(itemNamaUsaha.getKelurahan());
                    etJenisPajak.setText(itemNamaUsaha.getJenispajak());
                    etGolongan.setText(itemNamaUsaha.getGolongan());
                }

                //Toast.makeText(getApplicationContext(), "ID VERLAP"+id_data+",  NOP : "+country.getNop(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterByNpwpd() {
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
                }else{
                    et4.requestFocus();
                }

            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et4.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et4.getText().toString().length()==0){
                    et3.requestFocus();
                }else{
                    et5.requestFocus();
                }

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et5.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et5.getText().toString().length()==0){
                    et4.requestFocus();
                }else{
                    et6.requestFocus();
                }

            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et6.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et6.getText().toString().length()==0){
                    et5.requestFocus();
                }else {
                    et7.requestFocus();
                }

            }
        });
        et7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et7.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et7.getText().toString().length()==0){
                    et6.requestFocus();
                }else{
                    et8.requestFocus();
                }

            }
        });
        et8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et8.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et8.getText().toString().length()==0){
                    et7.requestFocus();
                }else{
                    et9.requestFocus();
                }

            }
        });
        et9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et9.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et9.getText().toString().length()==0){
                    et8.requestFocus();
                }else{
                    et10.requestFocus();
                }

            }
        });
        et10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et10.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et10.getText().toString().length()==0){
                    et9.requestFocus();
                }else{
                    et11.requestFocus();
                }

            }
        });
        et11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et11.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et11.getText().toString().length()==0){
                    et10.requestFocus();
                }else{
                    et12.requestFocus();
                }

            }
        });
        et12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et12.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et12.getText().toString().length()==0){
                    et11.requestFocus();
                }else{

                    npwpd = et1.getText().toString()+et2.getText().toString()+et3.getText().toString()
                            +et4.getText().toString()+et5.getText().toString()+et6.getText().toString()
                            +et7.getText().toString()+et8.getText().toString()+et9.getText().toString()
                            +et10.getText().toString()+et11.getText().toString()+et12.getText().toString();

                    getDataByFilter(spFilter);
                    spinNamaWP.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init() {
        databaseHandler=new DatabaseHandler(DataObjekPajak.this);
        etAlamat = (EditText)findViewById(R.id.etAlamatUsaha);
        etNPWPD = (EditText)findViewById(R.id.etNPWPD);
        etKecamatan = (EditText)findViewById(R.id.etKec);
        etKelurahan = (EditText)findViewById(R.id.etKel);
        etNamaWP = (EditText)findViewById(R.id.etNamaWP);
        etNamaWPC = (EditText)findViewById(R.id.etNamaWPC);
        etNamaTempatUsaha = (EditText)findViewById(R.id.etNamaTempatUsaha);
        etNamaTempatUsahaC = (EditText)findViewById(R.id.etNamaTempatUsahaC);
        etJenisPajak = (EditText)findViewById(R.id.etJenisPajak);
        etGolongan = (EditText)findViewById(R.id.etGol);
        spinFilter = (Spinner)findViewById(R.id.spFilter);
        spinNamaWP = (Spinner)findViewById(R.id.spNamaWP);
        spinNamaUsaha = (Spinner)findViewById(R.id.spNamaUsaha);
        spinNUsaha = (Spinner)findViewById(R.id.spNUsaha);
        tvUsaha = (TextView)findViewById(R.id.tvNamaUsaha);
        vUsaha = (View)findViewById(R.id.vNamaUsaha);
        cvDetail = (CardView)findViewById(R.id.cvDetail);

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

        Lnamawp = (LinearLayout)findViewById(R.id.Lnamawp);
        Lnpwpd = (LinearLayout)findViewById(R.id.Lnpwpd);
        Lnamausaha = (LinearLayout)findViewById(R.id.Lnamausaha);

        ivNamaUsahaC = (ImageView)findViewById(R.id.Ivnamausahac);
        ivNamaWPC = (ImageView)findViewById(R.id.Ivnamawpc);

        //reklame
        etPanjang = (EditText)findViewById(R.id.etPanjang);
        etLebar = (EditText)findViewById(R.id.etLebar);
        etSisi = (EditText)findViewById(R.id.etSisi);
        etTeks = (EditText)findViewById(R.id.etTeks);
        etLokasiPasang = (EditText)findViewById(R.id.etLokasiPasang);
        LReklame = (LinearLayout)findViewById(R.id.LReklame);

        cbKetinggian = (CheckBox)findViewById(R.id.cbKetinggian);
        cbRokok = (CheckBox)findViewById(R.id.cbRokok);

        LReklame.setVisibility(View.GONE);
    }

    private void getDetailData2(String id_inc) {
        final ProgressDialog progressDialog = new ProgressDialog(DataObjekPajak.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(DataObjekPajak.this);
        String url = Config.URL +"getDetailFilter";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject json = jsonArray.getJSONObject(0);

                    String npwpd = "-";
                    String nama_wp = "-";
                    String golongan = "-";
                    if(!json.getString("NAMA_WP").equalsIgnoreCase("null")){
                        nama_wp = json.getString("NAMA_WP");
                    }

                    if(!json.getString("NPWPD").equalsIgnoreCase("null")){
                        npwpd = json.getString("NPWPD");
                    }

                    if(!json.getString("NAMA_GOLONGAN").equalsIgnoreCase("null")){
                        golongan = json.getString("NAMA_GOLONGAN");
                    }
                    etNamaWP.setText(nama_wp);
                    //alias dari id_op
                    etNPWPD.setText(npwpd);
                    etNamaTempatUsaha.setText(json.getString("NAMA_USAHA"));
                    etAlamat.setText(json.getString("ALAMAT_USAHA"));
                    etKecamatan.setText(json.getString("KECAMATAN"));
                    etKelurahan.setText(json.getString("KELURAHAN"));
                    etJenisPajak.setText(json.getString("NAMA_PAJAK"));
                    etGolongan.setText(golongan);

                    if(json.getString("JENIS_PAJAK").equalsIgnoreCase("04")){
                        LReklame.setVisibility(View.VISIBLE);
                        String panjang = "-", lebar = "-", sisi = "-", lokasi = "-", rokok = "0", ketinggian = "0", teks = "-" ;

                        if(!json.getString("PANJANG").equalsIgnoreCase("null")){
                            panjang = json.getString("PANJANG");
                        }

                        if(!json.getString("LEBAR").equalsIgnoreCase("null")){
                            lebar = json.getString("LEBAR");
                        }

                        if(!json.getString("TINGGI").equalsIgnoreCase("null")){
                            ketinggian = json.getString("TINGGI");
                        }

                        if(!json.getString("LOKASI_PASANG").equalsIgnoreCase("null")){
                            lokasi = json.getString("LOKASI_PASANG");
                        }

                        if(!json.getString("SISI").equalsIgnoreCase("null")){
                            sisi = json.getString("SISI");
                        }
                        if(!json.getString("TEKS").equalsIgnoreCase("null")){
                            teks = json.getString("TEKS");
                        }
                        if(!json.getString("ROKOK").equalsIgnoreCase("null")){
                            rokok = json.getString("ROKOK");
                        }
                        etPanjang.setText(panjang);
                        etLebar.setText(lebar);
                        etSisi.setText(sisi);
                        if(ketinggian.equalsIgnoreCase("0")){
                            cbKetinggian.setChecked(false);
                        }else {
                            cbKetinggian.setChecked(true);
                        }
                        if(rokok.equalsIgnoreCase("0")){
                            cbRokok.setChecked(false);
                        }else {
                            cbRokok.setChecked(true);
                        }
                        etTeks.setText(teks);
                        etLokasiPasang.setText(lokasi);
                    }else{
                        LReklame.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getGolongan";
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getGolongan";

                Toast.makeText(DataObjekPajak.this, "Respon bermasalah !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ID_INC", id_inc);
//                params.put("value", cari);
//                System.out.println(spFilter+"//"+cari);

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

    private void getDetailData(String id_data, String filter) {
        final ProgressDialog progressDialog = new ProgressDialog(DataObjekPajak.this);
        progressDialog.setMessage("Mengambil data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        String npwpd="";
        try {
            List<ItemData> listDataFile=databaseHandler.getDetailData(Integer.parseInt(id_data));
            for(ItemData f:listDataFile){

                etNPWPD.setText(f.getNpwpd());
                etNamaWP.setText(f.getNamawp());

                npwpd=f.getNpwpd();
                int jml_data = databaseHandler.count_data(npwpd);
                if(filter.equalsIgnoreCase("1")){
                    if(jml_data==1){
                        etNamaTempatUsaha.setText(f.getNama_usaha());
                        etAlamat.setText(f.getAlamat_usaha());
                        etKecamatan.setText(f.getNamakec());
                        etKelurahan.setText(f.getNamakel());
                        etJenisPajak.setText(f.getNamapajak());
                        etGolongan.setText(f.getGolongan());
                        spinNUsaha.setVisibility(View.GONE);
                        tvUsaha.setVisibility(View.GONE);
                        vUsaha.setVisibility(View.GONE);
                        etNamaTempatUsaha.setVisibility(View.VISIBLE);
                    }else{
                        etNamaTempatUsaha.setVisibility(View.GONE);
                        spinNUsaha.setVisibility(View.VISIBLE);
                        tvUsaha.setVisibility(View.VISIBLE);
                        vUsaha.setVisibility(View.VISIBLE);

                        nUsahaArrayList.clear();
                        nUsahaArrayList.add(new ItemNamaUsaha("0","Pilih Nama Tempat Usaha",
                                "","","","","","",
                                "","","", "", ""));

                        List<ItemNamaUsaha> lisUsaha=databaseHandler.getDataByNPWPD(npwpd);
                        for(ItemNamaUsaha c:lisUsaha){
                            try {
                                nUsahaArrayList.add(new ItemNamaUsaha(c.getId_inc(),c.getNama_tempat_usaha(),
                                        c.getNama_wp(),c.getAlamat(),c.getKecamatan(),c.getKelurahan(),c.getNamajp(),c.getJenispajak(),
                                        c.getGolongan(),c.getId_op(),c.getId_data(), c.getKd_kecamatan(), c.getKd_kelurahan()));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        adapterNUsaha = new ArrayAdapter<ItemNamaUsaha>(this, R.layout.spinner_item, nUsahaArrayList);
                        spinNUsaha.setAdapter(adapterNUsaha);

                    }
                }else{
                    etNamaTempatUsaha.setText(f.getNama_usaha());
                    etAlamat.setText(f.getAlamat_usaha());
                    etKecamatan.setText(f.getNamakec());
                    etKelurahan.setText(f.getNamakel());
                    etJenisPajak.setText(f.getNamapajak());
                    etGolongan.setText(f.getGolongan());
                    spinNUsaha.setVisibility(View.GONE);
                    tvUsaha.setVisibility(View.GONE);
                    vUsaha.setVisibility(View.GONE);
                    etNamaTempatUsaha.setVisibility(View.VISIBLE);
                }


            }


        }catch (SQLiteException e){
            e.printStackTrace();
        }

        progressDialog.dismiss();
    }

    private void getDataByFilter(String spFilter) {

        if(spFilter.equalsIgnoreCase("1")){
            int i = 0;
            namaWPArrayList.clear();
            namaWPArrayList.add(new ItemNamaWP("0", "Pilih Nama WP"));
            List<ItemNamaUsaha> listData=databaseHandler.getDataByNamaWP(etNamaWPC.getText().toString());
            for(ItemNamaUsaha f:listData){
                try {
                    namaWPArrayList.add(new ItemNamaWP(f.getId_data(), f.getNama_wp()));
                    i++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            adapterNamaWP = new ArrayAdapter<ItemNamaWP>(DataObjekPajak.this, R.layout.spinner_item, namaWPArrayList);
            spinNamaWP.setAdapter(adapterNamaWP);
            spinNamaWP.setVisibility(View.VISIBLE);

            if(i==0){
                etNamaWP.setText("");
                //alias dari id_op
                etNPWPD.setText("");
                etNamaTempatUsaha.setText("");
                etAlamat.setText("");
                etKecamatan.setText("");
                etKelurahan.setText("");
                etJenisPajak.setText("");
                etGolongan.setText("");
            }
        }else if(spFilter.equalsIgnoreCase("2")){
//            int i = 0;
//            namaUsahaArrayList.clear();
//            namaUsahaArrayList.add(new ItemUsaha("0", "Pilih Nama Usaha"));
//            List<ItemNamaUsaha> listData=databaseHandler.getDataByNamaUsaha(etNamaTempatUsahaC.getText().toString());
//            for(ItemNamaUsaha f:listData){
//                try {
//                    namaUsahaArrayList.add(new ItemUsaha(f.getId_data(), f.getNama_tempat_usaha()));
//                    i++;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            adapterNamaUsaha = new ArrayAdapter<ItemUsaha>(DataObjekPajak.this, R.layout.spinner_item, namaUsahaArrayList);
//            spinNamaUsaha.setAdapter(adapterNamaUsaha);
//            spinNamaUsaha.setVisibility(View.VISIBLE);
//
//            if(i==0){
//                etNamaWP.setText("");
//                //alias dari id_op
//                etNPWPD.setText("");
//                etNamaTempatUsaha.setText("");
//                etAlamat.setText("");
//                etKecamatan.setText("");
//                etKelurahan.setText("");
//                etJenisPajak.setText("");
//                etGolongan.setText("");
//            }

//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            getDataServer(spFilter, etNamaTempatUsahaC.getText().toString());
        }else if(spFilter.equalsIgnoreCase("3")){
//            int i =0;
//            namaUsahaArrayList.clear();
//            namaUsahaArrayList.add(new ItemUsaha("0", "Pilih Nama Usaha"));
//            List<ItemNamaUsaha> listData=databaseHandler.getDataByNPWPD2(npwpd);
//            for(ItemNamaUsaha f:listData){
//                try {
//                    namaUsahaArrayList.add(new ItemUsaha(f.getId_data(), f.getNama_tempat_usaha()));
//                    i++;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            adapterNamaUsaha = new ArrayAdapter<ItemUsaha>(DataObjekPajak.this, R.layout.spinner_item, namaUsahaArrayList);
//            spinNamaUsaha.setAdapter(adapterNamaUsaha);
//            spinNamaUsaha.setVisibility(View.VISIBLE);
//
//            if(i==0){
//                etNamaWP.setText("");
//                //alias dari id_op
//                etNPWPD.setText("");
//                etNamaTempatUsaha.setText("");
//                etAlamat.setText("");
//                etKecamatan.setText("");
//                etKelurahan.setText("");
//                etJenisPajak.setText("");
//                etGolongan.setText("");
//            }


            getDataServer(spFilter, npwpd);
        }

        cvDetail.setVisibility(View.VISIBLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getDataServer(String spFilter, String cari) {
        final ProgressDialog progressDialog = new ProgressDialog(DataObjekPajak.this);
        progressDialog.setMessage("Mengambil data server...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(DataObjekPajak.this);
        String url = Config.URL +"getDataOPFilter";
        Log.d("URL_FILER", "getDataServer: "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    int i;
                    //jika spFilter = 2 => Nama Usaha
                    if(spFilter.equalsIgnoreCase("2")){
                        namaUsahaArrayList.clear();
                        namaUsahaArrayList.add(new ItemUsaha("0", "Pilih Nama Usaha"));
                        for (i=0;i<jsonArray.length();i++){
                            try {

                                JSONObject json = jsonArray.getJSONObject(i);
                                namaUsahaArrayList.add(new ItemUsaha(json.getString("ID_INC"),json.getString("NAMA_USAHA")));

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        adapterNamaUsaha = new ArrayAdapter<ItemUsaha>(DataObjekPajak.this, R.layout.spinner_item, namaUsahaArrayList);

                        if(i==0){
                            etNamaWP.setText("");
                            //alias dari id_op
                            etNPWPD.setText("");
                            etNamaTempatUsaha.setText("");
                            etAlamat.setText("");
                            etKecamatan.setText("");
                            etKelurahan.setText("");
                            etJenisPajak.setText("");
                            etGolongan.setText("");
                        }

                    }else{
                        namaUsahaArrayList.clear();
                        namaUsahaArrayList.add(new ItemUsaha("0", "Pilih Nama Usaha"));
                        for (i=0;i<jsonArray.length();i++){
                            try {

                                JSONObject json = jsonArray.getJSONObject(i);
                                namaUsahaArrayList.add(new ItemUsaha(json.getString("ID_INC"),json.getString("NAMA_USAHA")));

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        adapterNamaUsaha = new ArrayAdapter<ItemUsaha>(DataObjekPajak.this, R.layout.spinner_item, namaUsahaArrayList);

                        if(i==0){
                            etNamaWP.setText("");
                            //alias dari id_op
                            etNPWPD.setText("");
                            etNamaTempatUsaha.setText("");
                            etAlamat.setText("");
                            etKecamatan.setText("");
                            etKelurahan.setText("");
                            etJenisPajak.setText("");
                            etGolongan.setText("");
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    String info = e.getMessage();
                    String activity = "PendataanActivity-getGolongan";
                }
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                if(spFilter.equalsIgnoreCase("2")){
                    spinNamaUsaha.setAdapter(adapterNamaUsaha);
                    spinNamaUsaha.setVisibility(View.VISIBLE);
                }else{
                    spinNamaUsaha.setAdapter(adapterNamaUsaha);
                    spinNamaUsaha.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String info = error.getMessage();
                String activity = "PendataanActivity-getGolongan";

                Toast.makeText(DataObjekPajak.this, "Respon bermasalah !", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filter", spFilter);
                params.put("value", cari);
                System.out.println(spFilter+"//"+cari);

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

    private void getDataFilter() {
        filterArrayList.add(new ItemFilter("0", "Pilih"));
//        filterArrayList.add(new ItemFilter("1", "Nama WP"));
        filterArrayList.add(new ItemFilter("2", "Nama Tempat Usaha"));
        filterArrayList.add(new ItemFilter("3", "NPWPD"));
        adapterFilter = new ArrayAdapter<ItemFilter>(DataObjekPajak.this, R.layout.spinner_item, filterArrayList);
        spinFilter.setAdapter(adapterFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(DataObjekPajak.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
