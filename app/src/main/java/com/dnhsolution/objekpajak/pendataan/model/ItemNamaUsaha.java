package com.dnhsolution.objekpajak.pendataan.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemNamaUsaha {
    private String id_inc;
    private String nama_tempat_usaha;
    private String nama_wp;
    private String alamat;
    private String kd_kecamatan;
    private String kecamatan;
    private String kd_kelurahan;
    private String kelurahan;
    private String namajp;
    private String jenispajak;
    private String golongan;
    private String id_op;
    private String id_data;
    private String status;

    public ItemNamaUsaha() {
    }

    public ItemNamaUsaha(String id_inc, String nama_tempat_usaha, String nama_wp, String alamat,
                         String kecamatan, String kelurahan, String namajp, String jenispajak,
                         String golongan, String id_op, String id_data, String kd_kecamatan, String kd_kelurahan) {
        this.id_inc = id_inc;
        this.nama_tempat_usaha = nama_tempat_usaha;
        this.nama_wp = nama_wp;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.namajp = namajp;
        this.jenispajak = jenispajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.id_data = id_data;
        this.kd_kecamatan = kd_kecamatan;
        this.kd_kelurahan = kd_kelurahan;
    }

    public ItemNamaUsaha(String id_inc, String nama_tempat_usaha, String nama_wp, String alamat,
                         String kecamatan, String kelurahan, String namajp, String jenispajak,
                         String golongan, String id_op, String id_data, String kd_kecamatan, String kd_kelurahan, String status) {
        this.id_inc = id_inc;
        this.nama_tempat_usaha = nama_tempat_usaha;
        this.nama_wp = nama_wp;
        this.alamat = alamat;
        this.kecamatan = kecamatan;
        this.kelurahan = kelurahan;
        this.namajp = namajp;
        this.jenispajak = jenispajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.id_data = id_data;
        this.kd_kecamatan = kd_kecamatan;
        this.kd_kelurahan = kd_kelurahan;
        this.status = status;
    }

    public String getId_inc() {
        return id_inc;
    }

    public void setId_inc(String id_inc) {
        this.id_inc = id_inc;
    }

    public String getNama_tempat_usaha() {
        return nama_tempat_usaha;
    }

    public void setNama_tempat_usaha(String nama_tempat_usaha) {
        this.nama_tempat_usaha = nama_tempat_usaha;
    }

    public String getNama_wp() {
        return nama_wp;
    }

    public void setNama_wp(String nama_wp) {
        this.nama_wp = nama_wp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getNamajp() {
        return namajp;
    }

    public void setNamajp(String namajp) {
        this.namajp = namajp;
    }

    public String getJenispajak() {
        return jenispajak;
    }

    public void setJenispajak(String jenispajak) {
        this.jenispajak = jenispajak;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getId_op() {
        return id_op;
    }

    public void setId_op(String id_op) {
        this.id_op = id_op;
    }

    public String getId_data() {
        return id_data;
    }

    public void setId_data(String id_data) {
        this.id_data = id_data;
    }

    public String getKd_kecamatan() {
        return kd_kecamatan;
    }

    public void setKd_kecamatan(String kd_kecamatan) {
        this.kd_kecamatan = kd_kecamatan;
    }

    public String getKd_kelurahan() {
        return kd_kelurahan;
    }

    public void setKd_kelurahan(String kd_kelurahan) {
        this.kd_kelurahan = kd_kelurahan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_tempat_usaha;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemNamaUsaha){
            ItemNamaUsaha c = (ItemNamaUsaha)obj;
            if(c.getNama_tempat_usaha().equals(nama_tempat_usaha) && c.getId_inc().equals(id_inc) ) return true;
        }

        return false;
    }


}
