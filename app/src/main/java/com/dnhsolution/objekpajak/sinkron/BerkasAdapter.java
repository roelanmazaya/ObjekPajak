package com.dnhsolution.objekpajak.sinkron;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dnhsolution.objekpajak.R;

import java.io.File;
import java.util.List;

/**
 * Created by sawrusdino on 09/04/2018.
 */

public class BerkasAdapter extends RecyclerView.Adapter<BerkasAdapter.MyViewHolder> {

    private List<Berkas> berkasList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public ImageView tvPeringatan, xIcon, ivBerkas;

        public MyViewHolder(View view){
            super(view);
            nama = (TextView) view.findViewById(R.id.tvBerkas);
            tvPeringatan = (ImageView) view.findViewById(R.id.tvPeringatan);
            xIcon = (ImageView)view.findViewById(R.id.xicon);
            ivBerkas = (ImageView)view.findViewById(R.id.ivBerkas);
        }
    }

    public BerkasAdapter(List<Berkas> berkasList){
        this.berkasList = berkasList;
    }

    @Override
    public BerkasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_berkas_silang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BerkasAdapter.MyViewHolder holder, int position) {
        final Berkas berkas = berkasList.get(position);
        holder.nama.setText(berkas.getFilename());

        if (berkas.getFilesize() > 20000){
            holder.tvPeringatan.setVisibility(View.VISIBLE);
        } else {
            holder.tvPeringatan.setVisibility(View.GONE);
        }
//        Bitmap bmp = BitmapFactory.decodeFile(berkas.getFilename());
//        holder.ivBerkas.setImageBitmap(bmp);

        Glide.with(holder.ivBerkas.getContext()).load(new File(berkas.getFilename()).toString())
                .centerCrop()
                .fitCenter()
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
                })
                .into(holder.ivBerkas);

        holder.xIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), berkas.getFilename(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return berkasList.size();
    }
}
