package com.dnhsolution.objekpajak.pendataan.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemJenisPajak {
    private String kd_jenis_pajak;
    private String nama_pajak;

    public ItemJenisPajak(String kd_jenis_pajak, String nama_pajak) {
        this.kd_jenis_pajak = kd_jenis_pajak;
        this.nama_pajak = nama_pajak;
    }

    public String getKd_jenis_pajak() {
        return kd_jenis_pajak;
    }

    public void setKd_jenis_pajak(String kd_jenis_pajak) {
        this.kd_jenis_pajak = kd_jenis_pajak;
    }

    public String getNama_pajak() {
        return nama_pajak;
    }

    public void setNama_pajak(String nama_pajak) {
        this.nama_pajak = nama_pajak;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_pajak;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemJenisPajak){
            ItemJenisPajak c = (ItemJenisPajak )obj;
            if(c.getNama_pajak().equals(nama_pajak) && c.getKd_jenis_pajak().equals(kd_jenis_pajak) ) return true;
        }

        return false;
    }


}
