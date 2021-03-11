package com.dnhsolution.objekpajak.data_op.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemNamaWP {
    private String kd_data;
    private String nama_wp;

    public ItemNamaWP(String kd_data, String nama_wp) {
        this.kd_data = kd_data;
        this.nama_wp = nama_wp;
    }

    public String getKd_data() {
        return kd_data;
    }

    public void setKd_data(String kd_data) {
        this.kd_data = kd_data;
    }

    public String getNama_wp() {
        return nama_wp;
    }

    public void setNama_wp(String nama_wp) {
        this.nama_wp = nama_wp;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_wp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemNamaWP){
            ItemNamaWP c = (ItemNamaWP)obj;
            if(c.getNama_wp().equals(nama_wp) && c.getKd_data().equals(kd_data) ) return true;
        }

        return false;
    }


}
