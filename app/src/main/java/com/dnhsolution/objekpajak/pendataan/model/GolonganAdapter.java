package com.dnhsolution.objekpajak.pendataan.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dnhsolution.objekpajak.R;
import com.dnhsolution.objekpajak.pendataan.PendataanActivity;
import com.dnhsolution.objekpajak.riwayat.model.ItemRiwayat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;


public class GolonganAdapter extends RecyclerView.Adapter<GolonganAdapter.MyViewHolder> implements Filterable{

    private List<ItemGol> golList;
    Context context;
    private List<ItemGol> golListFiltered;
    private GolonganAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, kode;

        public MyViewHolder(View view){
            super(view);
            nama = (TextView) view.findViewById(R.id.tvNamaGol);
            kode = (TextView) view.findViewById(R.id.tvKdGol);
        }
    }

    public GolonganAdapter(List<ItemGol> golonganList, Context context, GolonganAdapterListener listener){
        this.golList = golonganList;
        this.context = context;
        this.listener = listener;
        this.golListFiltered = golonganList;
    }

    @Override
    public GolonganAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_golongan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GolonganAdapter.MyViewHolder holder, int position) {
        final ItemGol itemGol = golList.get(position);
        holder.nama.setText(itemGol.getNama_gol());
        holder.kode.setText(itemGol.getKd_gol());

    }

    @Override
    public int getItemCount() {
        //return golList.size();
        return golListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    golListFiltered = golList;
                } else {
                    List<ItemGol> filteredList = new ArrayList<>();
                    for (ItemGol row : golList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama_gol().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    golListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = golListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                golListFiltered = (ArrayList<ItemGol>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface GolonganAdapterListener {
        void onGolonganSelected(ItemGol gol);
    }
}
