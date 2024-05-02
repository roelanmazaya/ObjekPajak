package com.dnhsolution.objekpajak.pendataan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dnhsolution.objekpajak.R;

public class MainActivityPendatan extends AppCompatActivity {

    ImageView hotel, hiburan, restoran, mblb, ppj, parkir, air_tanah, reklame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pendatan);

        hotel = findViewById(R.id.hotel);
        hiburan = findViewById(R.id.hiburan);
        restoran = findViewById(R.id.restoran);
        mblb = findViewById(R.id.mblb);
        ppj = findViewById(R.id.ppj);
        parkir = findViewById(R.id.parkir);
        air_tanah = findViewById(R.id.air_tanah);
        reklame = findViewById(R.id.reklame);

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "01");
                startActivity(i);
            }
        });

        restoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "02");
                startActivity(i);
            }
        });

        hiburan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "03");
                startActivity(i);
            }
        });

        reklame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "04");
                startActivity(i);
            }
        });

        ppj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "05");
                startActivity(i);
            }
        });

        mblb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "06");
                startActivity(i);
            }
        });

        parkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "07");
                startActivity(i);
            }
        });

        air_tanah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PendataanActivity.class);
                i.putExtra("id_pajak", "08");
                startActivity(i);
            }
        });
    }
}