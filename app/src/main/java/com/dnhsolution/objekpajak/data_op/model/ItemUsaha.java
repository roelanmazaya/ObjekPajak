package com.dnhsolution.objekpajak.data_op.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemUsaha {
    private String kd_data;
    private String nama_usaha;

    public ItemUsaha(String kd_data, String nama_usaha) {
        this.kd_data = kd_data;
        this.nama_usaha = nama_usaha;
    }

    public String getKd_data() {
        return kd_data;
    }

    public void setKd_data(String kd_data) {
        this.kd_data = kd_data;
    }

    public String getNama_usaha() {
        return nama_usaha;
    }

    public void setNama_usaha(String nama_usaha) {
        this.nama_usaha = nama_usaha;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_usaha;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemUsaha){
            ItemUsaha c = (ItemUsaha)obj;
            if(c.getNama_usaha().equals(nama_usaha) && c.getKd_data().equals(kd_data) ) return true;
        }

        return false;
    }


}
