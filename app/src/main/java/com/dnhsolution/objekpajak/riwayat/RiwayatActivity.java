package com.dnhsolution.objekpajak.riwayat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.dnhsolution.objekpajak.BuildConfig;
import com.dnhsolution.objekpajak.MainActivity;
import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.config.Config;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.database.ItemGambar;
import com.dnhsolution.objekpajak.handler.HttpHandler;
import com.dnhsolution.objekpajak.riwayat.model.ItemRiwayat;
import com.dnhsolution.objekpajak.sinkron.SwipeController;
import com.dnhsolution.objekpajak.sinkron.SwipeControllerActions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {

    RecyclerView rvData;
    List<ItemRiwayat> dataRiwayat;
    LinearLayout linearLayout;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;

    DatabaseHandler databaseHandler;
    View ChildView;
    int RecyclerViewClickedItemPos;
    TextView tvKet;
    SwipeController swipeController = null;
    int jumlah_data = 0;
    ProgressDialog progressdialog;
    private String TAG = RiwayatActivity.class.getSimpleName();
    int status = 0;

    int jml = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Riwayat Objek Pajak");

        databaseHandler=new DatabaseHandler(RiwayatActivity.this);

        rvData = (RecyclerView)findViewById(R.id.rvData);
        tvKet = (TextView)findViewById(R.id.tvKet);
        dataRiwayat = new ArrayList<>();
        adapter = new RiwayatAdapter(dataRiwayat, RiwayatActivity.this);

        linearLayoutManager = new LinearLayoutManager(RiwayatActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), linearLayoutManager.getOrientation());

        rvData.setHasFixedSize(true);
        rvData.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        rvData.setAdapter(adapter);

        int jml_data = databaseHandler.CountDataRiwayatNew();

        if(jml_data==0){
            SharedPreferences sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            String ses_username = sharedPreferences.getString(Config.PREF_USERNAME, "");
            String ses_id_inc = sharedPreferences.getString(Config.PREF_ID_INC, "");

            if(ses_username.equalsIgnoreCase("")){
                dialogPeringatan("Sesi Login tidak tersimpan dengan benar. Silahkan Logout kemudian Login kembali !");
            }else if(ses_id_inc.equalsIgnoreCase("")){
                dialogPeringatan("Sesi Login tidak tersimpan dengan benar. Silahkan Logout kemudian Login kembali !");
            }else{
                cekData();
            }
        }else{
            getData();
        }

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
                    int id_data = Integer.parseInt(dataRiwayat.get(RecyclerViewClickedItemPos).getId_data());
                    Intent i = new Intent(RiwayatActivity.this, DetailActivity.class);
                    i.putExtra("id_data", id_data);
                    startActivity(i);
                    Log.d("hhhhuu", String.valueOf(id_data));
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

    private void cekData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.URL +"cekDataHistory";
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
                            final String jml_data = json.getString("jml_data");
                            AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatActivity.this);
                            builder.setMessage("Lanjutkan download "+jml_data+" data ?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            databaseHandler.delete_data_history();
                                            //databaseHandler.delete_data_gambar_history();
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
                String activity = "Riwayat-cekData";

                sendLog(username, sdk, model, info, activity);
                Toast.makeText(RiwayatActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

                String id_inc = sharedPreferences.getString(Config.PREF_ID_INC, "0");
                params.put("status", "200");
                params.put("id_inc_username", id_inc);

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

    private class getDataP extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressdialog = new ProgressDialog(RiwayatActivity.this);

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
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

            String id_inc = sharedPreferences.getString(Config.PREF_ID_INC, "0");
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Config.URL+"SinkronHistory?id_inc_username="+id_inc);

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

//                        databaseHandler.InsertHistory(new ItemData(
//                                0,json.getString("id_inc"), json.getString("nama_usaha"),
//                                json.getString("npwpd"),json.getString("alamat_usaha"),
//                                "", "",
//                                json.getString("namakec"), json.getString("namakel"),
//                                "", json.getString("jenis_pajak"), json.getString("namapajak"),
//                                json.getString("golongan"), json.getString("id_op"),"3",
//                                json.getString("tgl_insert"),json.getString("tgl_update"),json.getString("tgl_sinkron")
//                                , json.getString("lati"), json.getString("longi")
//                        ));

                        String lainnya = json.getString("panjang")+"/"+json.getString("lebar")+"/"+
                                json.getString("tinggi");

                        databaseHandler.InsertHistory(new ItemData(
                                0,json.getString("id_inc"), json.getString("nama_usaha"),
                                json.getString("npwpd"),json.getString("alamat_usaha"),
                                "", "",
                                json.getString("namakec"), json.getString("namakel"),
                                "", json.getString("jenis_pajak"), json.getString("namapajak"),
                                json.getString("golongan"), json.getString("id_op"), json.getString("lati"), json.getString("longi"),"3",
                                json.getString("tgl_insert"),json.getString("tgl_update"),json.getString("tgl_sinkron")
                                ,lainnya
                        ));

                        status = i+1;
                        publishProgress(status);
                        jml++;

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
                            String activity = "Riwayat-getDataP";

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
                        String activity = "Riwayat-getDataP";

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

            Snackbar.make(view, String.valueOf(jml)+" data berhasil direstore !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            getData();

            /**
             * Updating parsed JSON data into ListView
             * */
        }

    }

    private void getData() {
        dataRiwayat.clear();
        adapter.notifyDataSetChanged();
        final ProgressDialog progressDialog = new ProgressDialog(RiwayatActivity.this);
        progressDialog.setMessage("Mengambil data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        int jml_data = databaseHandler.CountDataRiwayatNew();

        if(jml_data==0){
            tvKet.setVisibility(View.VISIBLE);
        }else{
            tvKet.setVisibility(View.GONE);
        }

        try {
            List<ItemData> listDataFile=databaseHandler.getDataRiwayatNew();
            int nomer = 1;
            for(ItemData f:listDataFile){
                ItemRiwayat is = new ItemRiwayat();
                is.setNama_wp(f.getNamawp());
                is.setId_data(String.valueOf(f.getId_data()));
                is.setNama_usaha(f.getNama_usaha());
                is.setAlamat_usaha(f.getAlamat_usaha());
                is.setId_inc(f.getId_inc());
                is.setTgl_sinkron(f.getTgl_sync());
                is.setNomer(nomer);
                dataRiwayat.add(is);
                String gambar="";
//                List<ItemGambar> listFoto=databaseHandler.getDataFotoNew(String.valueOf(f.getId_data()));
//                for(ItemGambar g:listFoto){
//                    gambar+=g.getPath()+",";
//                }

                Log.d("OP_RIWAYAT", String.valueOf(f.getId_data())+"|"+f.getId_inc()+"|"+f.getNpwpd()
                        +"|"+f.getNama_usaha()+"###");
                nomer++;
            }
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        Log.d("OP_TERUPDATE", String.valueOf(dataRiwayat.size()));
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();

    }

    private void setupRecyclerView() {

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //Toast.makeText(RiwayatActivity.this, "Hapus id_data : "+dataRiwayat.get(position).getId_data(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatActivity.this);
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
//        List<ItemGambar> listFoto=databaseHandler.getDataFoto(dataRiwayat.get(position).getId_data());
//        for(ItemGambar g:listFoto){
//            File file = new File(g.getPath());
//            boolean deleted = file.delete();
//        }

        databaseHandler.delete_data_by_id_new(Integer.parseInt(dataRiwayat.get(position).getId_data()));
//        dataRiwayat.remove(position);
//        adapter.notifyItemRemoved(position);
//        adapter.notifyItemRangeChanged(position, dataRiwayat.size());
//        rvData.setAdapter(adapter);
        getData();
        View view = findViewById(R.id.content);
        Snackbar.make(view, "Data telah dihapus !", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(RiwayatActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.restore:
                // todo: goto back activity from here
                cekData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getCurrentDate() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = frmt.format(current);
        return dateString;
    }

    private void sendLog(String username, String sdk, String model, String info, String activity_class) {
        RequestQueue queue = Volley.newRequestQueue(RiwayatActivity.this);
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
                Toast.makeText(RiwayatActivity.this, "Respon bermasalah. Silahkan hubungi Admin !", Toast.LENGTH_LONG).show();
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
