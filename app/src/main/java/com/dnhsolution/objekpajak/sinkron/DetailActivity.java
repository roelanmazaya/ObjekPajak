package com.dnhsolution.objekpajak.sinkron;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dnhsolution.objekpajak.MainActivity;
import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.database.DatabaseHandler;
import com.dnhsolution.objekpajak.database.ItemData;
import com.dnhsolution.objekpajak.database.ItemGambar;
import com.dnhsolution.objekpajak.pendataan.Berkas;
import com.dnhsolution.objekpajak.pendataan.BerkasAdapter;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.dnhsolution.objekpajak.riwayat.RiwayatActivity;
import com.dnhsolution.objekpajak.riwayat.model.ItemRiwayat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    Button btnKembali;
    EditText etLat, etLong, etAlamat, etKecamatan, etKelurahan, etNpwpd,
            etNamaWP, etNamaTempatUsaha, etJenisPajak, etGolongan, etIdInc;


    DatabaseHandler databaseHandler;
    RecyclerView rvBerkas;
    private BerkasAdapter bAdapter;
    private List<Berkas> berkasList = new ArrayList<>();
    int RecyclerViewClickedItemPos;
    View ChildView;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Objek Pajak");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        databaseHandler=new DatabaseHandler(DetailActivity.this);

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
        etNpwpd = (EditText)findViewById(R.id.etNpwpd);
        btnKembali = (Button)findViewById(R.id.btnKembali);

        //camera
        rvBerkas = (RecyclerView)findViewById(R.id.rvBerkas);
        bAdapter= new BerkasAdapter(berkasList);
        RecyclerView.LayoutManager mLayoutManagerss = new LinearLayoutManager(getApplicationContext());
//        rvBerkas.setLayoutManager(mLayoutManagerss);
        rvBerkas.setLayoutManager(new GridLayoutManager(this, 5));
        rvBerkas.setItemAnimator(new DefaultItemAnimator());
        rvBerkas.setAdapter(bAdapter);

        int id_data = getIntent().getIntExtra("id_data", 0);
        //Toast.makeText(this, String.valueOf(id_data), Toast.LENGTH_SHORT).show();
        getData(id_data);

        btnKembali.setVisibility(View.GONE);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, SinkronActivity.class));
                DetailActivity.this.finish();
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
    }

    private void getData(int id_data) {
        final ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this);
        progressDialog.setMessage("Mengambil data ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            List<ItemData> listDataFile=databaseHandler.getDetailData(id_data);
            for(ItemData f:listDataFile){

                etNpwpd.setText(f.getNpwpd());
                etNamaTempatUsaha.setText(f.getNama_usaha());
                etNamaWP.setText(f.getNamawp());
                etAlamat.setText(f.getAlamat_usaha());
                etKecamatan.setText(f.getNamakec());
                etKelurahan.setText(f.getNamakel());
                etJenisPajak.setText(f.getNamapajak());
                etGolongan.setText(f.getGolongan());
                etLat.setText(f.getLati());
                etLong.setText(f.getLongi());

            }


        }catch (SQLiteException e){
            e.printStackTrace();
        }

        List<ItemGambar> listFoto=databaseHandler.getDataFoto(String.valueOf(id_data));
        for(ItemGambar g:listFoto){
            File filePhoto = new File(g.getPath());
            int file_size = Integer.parseInt(String.valueOf(filePhoto.length() / 1024));
            Log.d("DETAIL", g.getPath()+", "+file_size);
            Berkas berkas = new Berkas(g.getPath(), file_size);
            berkasList.add(berkas);
            bAdapter.notifyDataSetChanged();
        }

       // Toast.makeText(this, String.valueOf(berkasList.size()), Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(DetailActivity.this, SinkronActivity.class);
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
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("xmx1","no Error ");
                        return false;
                    }
                }).into(ivGambar);


        lOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        lHapus.setVisibility(View.GONE);

        lHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
