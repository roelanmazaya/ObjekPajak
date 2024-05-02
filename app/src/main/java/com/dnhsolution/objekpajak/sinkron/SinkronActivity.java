package com.dnhsolution.objekpajak.sinkron;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dnhsolution.objekpajak.MainActivity;
import com.dnhsolution.objekpajak.config.Config;
import com.dnhsolution.objekpajak.config.SplashActivity;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.database.ItemGambar;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.dnhsolution.objekpajak.pendataan.VolleyMultipartRequest;
import com.dnhsolution.objekpajak.riwayat.RiwayatActivity;
import com.dnhsolution.objekpajak.sinkron.model.ItemSinkron;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dnhsolution.objekpajak.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dnhsolution.objekpajak.pendataan.VolleyMultipartRequest.DataPart;

public class SinkronActivity extends AppCompatActivity {

    RecyclerView rvData;
    List<ItemSinkron> dataSinkron;
    LinearLayout linearLayout;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    View ChildView;
    int RecyclerViewClickedItemPos;

    DatabaseHandler databaseHandler;
    int jml_data = 0;
    int datax = 0;

    int status = 0;
    ProgressDialog progressdialog;
    int jumlah_data = 0;
    private String TAG = MainActivity.class.getSimpleName();

    TextView tvKet;
    SwipeController swipeController = null;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkron);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Upload Data");

        databaseHandler = new DatabaseHandler(SinkronActivity.this);

        FloatingActionButton fab = findViewById(R.id.fab);
        rvData = (RecyclerView) findViewById(R.id.rvData);

        tvKet = (TextView)findViewById(R.id.tvKet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jml_data = databaseHandler.CountDataTerupdate();
                if (jml_data == 0) {
                    Toast.makeText(SinkronActivity.this, "Data tersimpan kosong ! Silahkan tambah data dahulu.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SinkronActivity.this);
                    builder1.setMessage("Lanjut upload " + String.valueOf(jml_data) + " data ke server ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ya",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    SharedPreferences sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                                    String ses_username = sharedPreferences.getString(Config.PREF_USERNAME, "");
                                    String ses_id_inc = sharedPreferences.getString(Config.PREF_ID_INC, "");

                                    if(ses_username.equalsIgnoreCase("")){
                                        dialogPeringatan("Sesi Login tidak tersimpan dengan benar. Silahkan Logout kemudian Login kembali !");
                                    }else if(ses_id_inc.equalsIgnoreCase("")){
                                        dialogPeringatan("Sesi Login tidak tersimpan dengan benar. Silahkan Logout kemudian Login kembali !");
                                    }else{
                                        SendData();
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "Tidak",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        dataSinkron = new ArrayList<>();
        adapter = new SinkronAdapter(dataSinkron, SinkronActivity.this);

        linearLayoutManager = new LinearLayoutManager(SinkronActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());

        rvData.setHasFixedSize(true);
        rvData.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        rvData.setAdapter(adapter);

        getData();

        setupRecyclerView();

        rvData.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                ChildView = rvData.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {
                    RecyclerViewClickedItemPos = rvData.getChildAdapterPosition(ChildView);
                    Log.d("hhhhuu", String.valueOf(RecyclerViewClickedItemPos));
                    int id_data = Integer.parseInt(dataSinkron.get(RecyclerViewClickedItemPos).getId_data());
                    Intent i = new Intent(SinkronActivity.this, DetailActivity.class);
                    i.putExtra("id_data", id_data);
                    startActivity(i);
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

    private void dialogPeringatan(final String pesan) {
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

    private void getData() {
        dataSinkron.clear();
        adapter.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(SinkronActivity.this);
        progressDialog.setMessage("Mengambil data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        int jml_data = databaseHandler.CountDataTerupdate();

        if(jml_data==0){
            tvKet.setVisibility(View.VISIBLE);
        }else{
            tvKet.setVisibility(View.GONE);
        }

        try {
            List<ItemData> listDataFile = databaseHandler.getDataTerupdate();
            int nomer = 1;
            for (ItemData f : listDataFile) {
                ItemSinkron is = new ItemSinkron();
                is.setNama_wp(f.getNamawp());
                is.setNomer(nomer);
                is.setId_data(String.valueOf(f.getId_data()));
                is.setNama_usaha(f.getNama_usaha());
                is.setAlamat_usaha(f.getAlamat_usaha());
                is.setId_inc(f.getId_inc());
                is.setTgl_update(f.getTgl_update());
                dataSinkron.add(is);
                String gambar = "";
                List<ItemGambar> listFoto = databaseHandler.getDataFoto(String.valueOf(f.getId_data()));
                for (ItemGambar g : listFoto) {
                    gambar += g.getPath() + ",";
                }

                Log.d("OP_TERUPDATE", String.valueOf(f.getId_data()) + "|" + f.getId_inc() + "|" + f.getNpwpd()
                        + "|" + f.getNama_usaha() + "|" + f.getNamawp() + "|" + f.getAlamat_usaha() + "|" + f.getNamakec()
                        + "|" + f.getNamakel() + "|" + f.getLati() + "|" + f.getLongi() + "|" + f.getTgl_update() + "|" + gambar+"|"+ f.getLainnya());
                nomer++;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        Log.d("OP_TERUPDATE", String.valueOf(dataSinkron.size()));
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    private void SendData() {
        class SendData extends AsyncTask<Void, Integer, String> {

            String today = getCurrentDate();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressdialog = new ProgressDialog(SinkronActivity.this);

                progressdialog.setIndeterminate(false);

                progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                progressdialog.setCancelable(false);

                progressdialog.setMessage("Upload data ke server ...");

                progressdialog.setMax(jml_data);

                progressdialog.show();

            }

            protected void onProgressUpdate(Integer... values)
            {
                progressdialog.setProgress(values[0]);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("HASIL", s);
                if(s.equalsIgnoreCase("sukses")){

                    if(datax==jml_data){
                        if (progressdialog.isShowing())
                            progressdialog.dismiss();
                        View view = findViewById(R.id.content);
                        Snackbar snackbar = Snackbar
                                .make(view, String.valueOf(datax)+" data berhasil dikirim !", Snackbar.LENGTH_LONG);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);

                        snackbar.show();
                        getData();
                    }else{

                    }

                }else if(s.equalsIgnoreCase("gagal")){
                    if (progressdialog.isShowing()) {
                        progressdialog.dismiss();
                        dialogNotifikasi(s);
                        String info = s;
                        String activity = "SinkronActivity-SendData";

                        sendLog(info, activity);
                    }
                }else{
                    if (progressdialog.isShowing()){
                        progressdialog.dismiss();
                        dialogNotifikasi(s);
                        String info = s;
                        String activity = "SinkronActivity-SendData";

                        sendLog(info, activity);
                    }

                }

            }

            @Override
            protected String doInBackground(Void... params) {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedPreferences.getString(Config.PREF_USERNAME, "");
                String id_inc = sharedPreferences.getString(Config.PREF_ID_INC, "");

                List<ItemData> listData = databaseHandler.getDataTerupdateAll();
                String msg = null;
                for (ItemData f : listData) {
                    try {
                        UploadData u = new UploadData();
                        String lokasi = f.getLati()+","+f.getLongi();
                        ArrayList<String> bksDis = new ArrayList<String>();
                        int jml_foto = 0;

                        //status data
                        String status_data = f.getStatus();
                        List<ItemGambar> listDataFile=databaseHandler.getDataFoto(String.valueOf(f.getId_data()));
                        for(ItemGambar d:listDataFile){

                            int max_id_history = databaseHandler.CountMaxHistory()+1;
                            String path_foto = d.getPath();
                           // databaseHandler.addDataFotoHistory(new ItemGambar(0, String.valueOf(max_id_history), path_foto));

                            Log.d("OP_FOTO", d.getPath()+" = "+f.getId_data());
                            //Toast.makeText(getContext(), id_sspd+" - "+f.get_foto(), Toast.LENGTH_SHORT).show();
                            bksDis.add(d.getPath());
                            jml_foto++;
                        }

                        if(jml_foto > 0){
                            msg = u.uploadDataUmum(f.getNama_usaha(), f.getAlamat_usaha(), f.getNpwpd(), f.getNamakec()
                                    ,f.getNamakel(), f.getJenis_pajak(), f.getId_op(),lokasi, f.getTgl_update(), username, id_inc, bksDis, f.getKodekec(), f.getKodekel(), f.getLainnya());
                        }else{
                            msg = u.uploadDataUmum2(f.getNama_usaha(), f.getAlamat_usaha(), f.getNpwpd(), f.getNamakec()
                                    ,f.getNamakel(), f.getJenis_pajak(), f.getId_op(),lokasi, f.getTgl_update(), username, id_inc, f.getKodekec(), f.getKodekel(), f.getLainnya());
                        }

                        status++;
                        publishProgress(status);

                        if(msg.equalsIgnoreCase("sukses")){
                            datax++;
                            if(status_data.equalsIgnoreCase("2")){
                                databaseHandler.updateDataMaster(new ItemData(
                                        f.getId_data(),
                                        "3",
                                        today

                                ));
//                                databaseHandler.InsertHistory(new ItemData(
//                                        0,f.getId_inc(), f.getNama_usaha(),f.getNpwpd(),f.getAlamat_usaha(),
//                                        f.getKodekec(), f.getKodekel(),f.getNamakec(), f.getNamakel(),
//                                        f.getNamawp(), f.getJenis_pajak(), f.getNamapajak(),
//                                        f.getGolongan(), f.getId_op(),"3", f.getTgl_insert(),f.getTgl_update(),
//                                        today, f.getLati(), f.getLongi()
//                                ));

                                databaseHandler.InsertHistory(new ItemData(0,
                                        f.getId_inc(), f.getNama_usaha(),
                                        f.getNpwpd(),f.getAlamat_usaha(),
                                        f.getKodekec(), f.getKodekel(),f.getNamakec(), f.getNamakel(), f.getNamawp(),
                                        f.getJenis_pajak(), f.getNamapajak(),
                                        f.getGolongan(), f.getId_op(), f.getLati(), f.getLongi(), "3",
                                        f.getTgl_insert(), f.getTgl_update(), today, f.getLainnya()
                                ));
                            }else{
                                databaseHandler.updateDataMaster(new ItemData(
                                        f.getId_data(),
                                        "01",
                                        today

                                ));
//                                databaseHandler.InsertHistory(new ItemData(
//                                        0,f.getId_inc(), f.getNama_usaha(),f.getNpwpd(),f.getAlamat_usaha(),
//                                        f.getKodekec(), f.getKodekel(),f.getNamakec(), f.getNamakel(),
//                                        f.getNamawp(), f.getJenis_pajak(), f.getNamapajak(),
//                                        f.getGolongan(), f.getId_op(),"01", f.getTgl_insert(),f.getTgl_update(),
//                                        today, f.getLati(), f.getLongi()
//                                ));

                                databaseHandler.InsertHistory(new ItemData(0,
                                        f.getId_inc(), f.getNama_usaha(),
                                        f.getNpwpd(),f.getAlamat_usaha(),
                                        f.getKodekec(), f.getKodekel(),f.getNamakec(), f.getNamakel(), f.getNamawp(),
                                        f.getJenis_pajak(), f.getNamapajak(),
                                        f.getGolongan(), f.getId_op(), f.getLati(), f.getLongi(), "01",
                                        f.getTgl_insert(), f.getTgl_update(), today, f.getLainnya()
                                ));
                            }

                        }else{
                            datax++;
                            databaseHandler.updateDataMaster(new ItemData(
                                    f.getId_data(),
                                    "4",
                                    today

                            ));
                        }
                    } catch (Exception e) {
                        cancel(true);
                        e.printStackTrace();
                        String info = e.getMessage();
                        String activity = "SinkronActivity-SendData";

                        sendLog(info, activity);
                    }

                }

                return msg;
            }
        }
        SendData uv = new SendData();
        uv.execute();
    }

    private String getCurrentDate() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = frmt.format(current);
        return dateString;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void setupRecyclerView() {

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //Toast.makeText(SinkronActivity.this, "Hapus id_data : "+dataSinkron.get(position).getId_data(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SinkronActivity.this);
                builder.setMessage("Yakin hapus data ini ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeData(position);
                                //getData(jml_data);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

//                adapter.players.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
            @Override
            public void onLeftClicked(int position) {
                //Toast.makeText(SinkronActivity.this, "Detail id_data : "+dataSinkron.get(position).getId_data(), Toast.LENGTH_SHORT).show();
//                adapter.players.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvData);

        rvData.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    public void removeData(final int position){
        List<ItemGambar> listFoto=databaseHandler.getDataFoto(dataSinkron.get(position).getId_data());
        for(ItemGambar g:listFoto){
            File file = new File(g.getPath());
            boolean deleted = file.delete();
        }
        databaseHandler.delete_data_by_id(Integer.parseInt(dataSinkron.get(position).getId_data()));
//        dataSinkron.remove(position);
//        adapter.notifyItemRemoved(position);
//        adapter.notifyItemRangeChanged(position, dataSinkron.size());
//        rvData.setAdapter(adapter);
        getData();

        View view = findViewById(R.id.content);
        Snackbar.make(view, "Data telah dihapus !", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(SinkronActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        ivBerhasil.setVisibility(View.GONE);
        ivGagal.setVisibility(View.VISIBLE);
        tvKeterangan.setText(pesan);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pesan.equalsIgnoreCase("0")){
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }



            }
        });
    }

    public void sendLog(String info, String activity_class) {
        RequestQueue queue = Volley.newRequestQueue(SinkronActivity.this);
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
                Toast.makeText(SinkronActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
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

//    private void uploadData(final int id_data) {
//
//        //getting the tag from the edittext
//        //final String tags = editTextTags.getText().toString().trim();
//
//        //our custom volley request
//        ProgressDialog progressDialog = new ProgressDialog(SinkronActivity.this);
//        progressDialog.setMessage("Mengirim data ke server...");
//        progressDialog.show();
//       // progressDialog.setCanceledOnTouchOutside(false);
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.URL+"TambahData",
//                new Response.Listener<NetworkResponse>() {
//                    @Override
//                    public void onResponse(NetworkResponse response) {
//                        //Log.d("RESPONSE", String.valueOf(response));
//                        try {
//                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                            Log.d("hasil", obj.getString("message"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//
//            /*
//             * If you want to add more parameters with the image
//             * you can do it here
//             * here we have only one parameter with the image
//             * which is tags
//             * */
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("tags", "ok");
//                return params;
//            }
//
//            /*
//             * Here we are passing image by renaming it with a unique name
//             * */
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//
//                long imagename = System.currentTimeMillis();
//                ArrayList<String> bksDis = new ArrayList<String>();
//                List<ItemGambar> listFoto = databaseHandler.getDataFoto(String.valueOf(id_data));
//                for (ItemGambar g : listFoto) {
//                    bksDis.add(g.getPath());
//                }
//                //Bitmap bmp = BitmapFactory.decodeFile(bksDis.get(0));
//                //params.put("foto", new DataPart(imagename + ".png", getFileDataFromDrawable(bmp)));
//                //Log.d("FileUpload", String.valueOf(getFileDataFromDrawable(bmp)));
//                for (int x = 0; x < bksDis.size(); x++) {
//                    Log.d("UPLOAD_IMAGE", bksDis.get(x));
//                    Bitmap bmp = BitmapFactory.decodeFile(bksDis.get(x));
//                    params.put("foto[]", new DataPart(imagename + ".png", getFileDataFromDrawable(bmp)));
//                }
//
//                return params;
//            }
//        };
//
//        //adding the request to volley
//        Volley.newRequestQueue(this).add(volleyMultipartRequest);
//    }


}
