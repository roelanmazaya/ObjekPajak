package com.dnhsolution.objekpajak.pendataan.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemKelurahan {
    private String kd_kel;
    private String nama_kel;

    public ItemKelurahan(String kd_kel, String nama_kel) {
        this.kd_kel = kd_kel;
        this.nama_kel = nama_kel;
    }

    public String getKd_kel() {
        return kd_kel;
    }

    public void setKd_kel(String kd_kel) {
        this.kd_kel = kd_kel;
    }

    public String getNama_kel() {
        return nama_kel;
    }

    public void setNama_kel(String nama_kel) {
        this.nama_kel = nama_kel;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_kel;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemKelurahan){
            ItemKelurahan c = (ItemKelurahan)obj;
            if(c.getNama_kel().equals(nama_kel) && c.getKd_kel().equals(kd_kel) ) return true;
        }

        return false;
    }


}
