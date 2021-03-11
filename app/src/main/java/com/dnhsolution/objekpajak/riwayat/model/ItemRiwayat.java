package com.dnhsolution.objekpajak.riwayat.model;

public class ItemRiwayat {
    public String id_data;
    public int nomer;
    public String id_inc;
    public String nama_wp;
    public String nama_usaha;
    public String alamat_usaha;
    public String tgl_sinkron;

    public ItemRiwayat() {
    }

    public int getNomer() {
        return nomer;
    }

    public void setNomer(int nomer) {
        this.nomer = nomer;
    }

    public String getId_data() {
        return id_data;
    }

    public void setId_data(String id_data) {
        this.id_data = id_data;
    }

    public String getId_inc() {
        return id_inc;
    }

    public void setId_inc(String id_inc) {
        this.id_inc = id_inc;
    }

    public String getNama_wp() {
        return nama_wp;
    }

    public void setNama_wp(String nama_wp) {
        this.nama_wp = nama_wp;
    }

    public String getNama_usaha() {
        return nama_usaha;
    }

    public void setNama_usaha(String nama_usaha) {
        this.nama_usaha = nama_usaha;
    }

    public String getAlamat_usaha() {
        return alamat_usaha;
    }

    public void setAlamat_usaha(String alamat_usaha) {
        this.alamat_usaha = alamat_usaha;
    }

    public String getTgl_sinkron() {
        return tgl_sinkron;
    }

    public void setTgl_sinkron(String tgl_sinkron) {
        this.tgl_sinkron = tgl_sinkron;
    }
}
