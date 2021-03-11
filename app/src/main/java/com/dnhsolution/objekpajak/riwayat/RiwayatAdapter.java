package com.dnhsolution.objekpajak.riwayat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.riwayat.model.ItemRiwayat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.MyViewHolder> {

    private List<ItemRiwayat> riwayatList;
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

    public RiwayatAdapter(List<ItemRiwayat> riwayatList, Context context){
        this.riwayatList = riwayatList;
        this.context = context;
    }

    @Override
    public RiwayatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data_sinkron, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RiwayatAdapter.MyViewHolder holder, int position) {
        final ItemRiwayat itemRiwayat = riwayatList.get(position);
        holder.nama.setText(itemRiwayat.getNama_usaha());
        holder.alamat.setText(itemRiwayat.getAlamat_usaha());
        holder.nomer.setText(String.valueOf(itemRiwayat.getNomer()));

        if(itemRiwayat.getTgl_sinkron().equalsIgnoreCase("null")){
            holder.waktu.setText("-");
        }else{
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date current = null;
            try {
                current = form.parse(itemRiwayat.getTgl_sinkron());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateString = frmt.format(current);
            holder.waktu.setText(dateString);
        }


    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }
}
