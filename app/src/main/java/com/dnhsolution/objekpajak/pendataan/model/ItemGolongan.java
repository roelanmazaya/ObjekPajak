package com.dnhsolution.objekpajak.pendataan.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemGolongan {
    private String kd_gol;
    private String nama_gol;

    public ItemGolongan(String kd_gol, String nama_gol) {
        this.kd_gol = kd_gol;
        this.nama_gol = nama_gol;
    }

    public String getKd_gol() {
        return kd_gol;
    }

    public void setKd_gol(String kd_gol) {
        this.kd_gol = kd_gol;
    }

    public String getNama_gol() {
        return nama_gol;
    }

    public void setNama_gol(String nama_gol) {
        this.nama_gol = nama_gol;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_gol;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemGolongan){
            ItemGolongan c = (ItemGolongan)obj;
            if(c.getNama_gol().equals(nama_gol) && c.getKd_gol()==kd_gol ) return true;
        }

        return false;
    }


}
