package com.dnhsolution.objekpajak.pendataan.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemKecamatan {
    private String kd_kec;
    private String nama_kec;

    public ItemKecamatan(String kd_kec, String nama_kec) {
        this.kd_kec = kd_kec;
        this.nama_kec = nama_kec;
    }

    public String getKd_kec() {
        return kd_kec;
    }

    public void setKd_kec(String kd_kec) {
        this.kd_kec = kd_kec;
    }

    public String getNama_kec() {
        return nama_kec;
    }

    public void setNama_kec(String nama_kec) {
        this.nama_kec = nama_kec;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_kec;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemKecamatan){
            ItemKecamatan c = (ItemKecamatan)obj;
            if(c.getNama_kec().equals(nama_kec) && c.getKd_kec().equals(kd_kec) ) return true;
        }

        return false;
    }


}
