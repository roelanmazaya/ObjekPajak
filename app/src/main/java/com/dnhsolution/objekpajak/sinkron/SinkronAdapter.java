package com.dnhsolution.objekpajak.sinkron;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.sinkron.model.ItemSinkron;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SinkronAdapter extends RecyclerView.Adapter<SinkronAdapter.MyViewHolder> {

    private List<ItemSinkron> sinkronList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, alamat, waktu, nomer;

        public MyViewHolder(View view){
            super(view);
            nama = (TextView) view.findViewById(R.id.tvNama);
            alamat = (TextView) view.findViewById(R.id.tvAlamat);
            waktu = (TextView)view.findViewById(R.id.tvWaktu);
            nomer = (TextView)view.findViewById(R.id.tvNomer);
        }
    }

    public SinkronAdapter(List<ItemSinkron> sinkronList, Context context){
        this.sinkronList = sinkronList;
        this.context = context;
    }

    @Override
    public SinkronAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data_sinkron, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SinkronAdapter.MyViewHolder holder, int position) {
        final ItemSinkron itemSinkron = sinkronList.get(position);
        holder.nama.setText(itemSinkron.getNama_usaha());
        holder.alamat.setText(itemSinkron.getAlamat_usaha());
        holder.nomer.setText(String.valueOf(itemSinkron.getNomer()));
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current = null;
        try {
            current = form.parse(itemSinkron.getTgl_update());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = frmt.format(current);
        holder.waktu.setText(dateString);

    }

    @Override
    public int getItemCount() {
        return sinkronList.size();
    }
}
