package com.dnhsolution.objekpajak.database;

public class ItemData {
    int id_data;
    String id_inc;
    String nama_usaha;
    String npwpd;
    String alamat_usaha;
    String kodekec;
    String kodekel;
    String namakec;
    String namakel;
    String namawp;
    String jenis_pajak;
    String namapajak;
    String golongan;
    String id_op;
    String lati;
    String longi;
    String status;
    String tgl_insert;
    String tgl_update;
    String tgl_sync;
    String lainnya;

    public ItemData() {
    }

    public ItemData(int id_data, String id_inc, String nama_usaha, String npwpd, String alamat_usaha, String kodekec, String kodekel, String namakec, String namakel, String namawp, String jenis_pajak, String namapajak, String golongan, String id_op, String status, String tgl_insert) {
        this.id_data = id_data;
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.npwpd = npwpd;
        this.alamat_usaha = alamat_usaha;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
        this.namakec = namakec;
        this.namakel = namakel;
        this.namawp = namawp;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.status = status;
        this.tgl_insert = tgl_insert;
    }

    public ItemData(String id_inc, String nama_usaha, String alamat_usaha,
                    String npwpd, String namakec, String namakel,
                    String jenis_pajak, String namapajak, String golongan,
                    String id_op, String lati, String longi, String status,
                    String tgl_update, String kodekec, String kodekel) {
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.alamat_usaha = alamat_usaha;
        this.npwpd = npwpd;
        this.namakec = namakec;
        this.namakel = namakel;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_update = tgl_update;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
    }

    public ItemData(String id_inc, String nama_usaha, String alamat_usaha, String npwpd, String namakec, String namakel, String jenis_pajak, String namapajak, String golongan, String id_op, String lati, String longi, String status, String tgl_update) {
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.alamat_usaha = alamat_usaha;
        this.npwpd = npwpd;
        this.namakec = namakec;
        this.namakel = namakel;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_update = tgl_update;
    }

    public ItemData(String id_inc, String nama_usaha, String alamat_usaha, String namakec,
                    String namakel, String jenis_pajak, String namapajak, String golongan,
                    String id_op, String lati, String longi, String status, String tgl_update, String kodekec, String kodekel) {
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.alamat_usaha = alamat_usaha;
        this.namakec = namakec;
        this.namakel = namakel;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_update = tgl_update;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
    }

    public ItemData(String id_inc, String nama_usaha, String alamat_usaha, String namakec,
                    String namakel, String jenis_pajak, String namapajak, String golongan,
                    String id_op, String lati, String longi, String status, String tgl_update, String kodekec, String kodekel, String namawp, String npwpd) {
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.alamat_usaha = alamat_usaha;
        this.namakec = namakec;
        this.namakel = namakel;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_update = tgl_update;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
        this.namawp = namawp;
        this.npwpd = npwpd;
    }

    public ItemData(String id_inc, String nama_usaha, String alamat_usaha, String namakec, String namakel, String jenis_pajak, String namapajak, String golongan, String id_op, String lati, String longi, String status, String tgl_update) {
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.alamat_usaha = alamat_usaha;
        this.namakec = namakec;
        this.namakel = namakel;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_update = tgl_update;
    }

    public ItemData(int id_data, String status, String tgl_sync) {
        this.id_data = id_data;
        this.status = status;
        this.tgl_sync = tgl_sync;
    }

    public ItemData(int id_data, String id_inc, String nama_usaha, String npwpd, String alamat_usaha,
                    String kodekec, String kodekel, String namakec, String namakel,
                    String namawp, String jenis_pajak, String namapajak,
                    String golongan, String id_op, String status,
                    String tgl_insert, String tgl_update, String tgl_sync,
                    String lati, String longi) {
        this.id_data = id_data;
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.npwpd = npwpd;
        this.alamat_usaha = alamat_usaha;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
        this.namakec = namakec;
        this.namakel = namakel;
        this.namawp = namawp;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.status = status;
        this.tgl_insert = tgl_insert;
        this.tgl_update = tgl_update;
        this.tgl_sync = tgl_sync;
        this.lati = lati;
        this.longi = longi;

    }

    //perbaikan 12032021


    public ItemData(int id_data, String id_inc, String nama_usaha, String npwpd, String alamat_usaha, String kodekec, String kodekel, String namakec, String namakel, String namawp, String jenis_pajak, String namapajak, String golongan, String id_op, String lati, String longi, String status, String tgl_insert, String tgl_update, String tgl_sync, String lainnya) {
        this.id_data = id_data;
        this.id_inc = id_inc;
        this.nama_usaha = nama_usaha;
        this.npwpd = npwpd;
        this.alamat_usaha = alamat_usaha;
        this.kodekec = kodekec;
        this.kodekel = kodekel;
        this.namakec = namakec;
        this.namakel = namakel;
        this.namawp = namawp;
        this.jenis_pajak = jenis_pajak;
        this.namapajak = namapajak;
        this.golongan = golongan;
        this.id_op = id_op;
        this.lati = lati;
        this.longi = longi;
        this.status = status;
        this.tgl_insert = tgl_insert;
        this.tgl_update = tgl_update;
        this.tgl_sync = tgl_sync;
        this.lainnya = lainnya;
    }

    public int getId_data() {
        return id_data;
    }

    public void setId_data(int id_data) {
        this.id_data = id_data;
    }

    public String getId_inc() {
        return id_inc;
    }

    public void setId_inc(String id_inc) {
        this.id_inc = id_inc;
    }

    public String getNama_usaha() {
        return nama_usaha;
    }

    public void setNama_usaha(String nama_usaha) {
        this.nama_usaha = nama_usaha;
    }

    public String getNpwpd() {
        return npwpd;
    }

    public void setNpwpd(String npwpd) {
        this.npwpd = npwpd;
    }

    public String getAlamat_usaha() {
        return alamat_usaha;
    }

    public void setAlamat_usaha(String alamat_usaha) {
        this.alamat_usaha = alamat_usaha;
    }

    public String getKodekec() {
        return kodekec;
    }

    public void setKodekec(String kodekec) {
        this.kodekec = kodekec;
    }

    public String getKodekel() {
        return kodekel;
    }

    public void setKodekel(String kodekel) {
        this.kodekel = kodekel;
    }

    public String getJenis_pajak() {
        return jenis_pajak;
    }

    public void setJenis_pajak(String jenis_pajak) {
        this.jenis_pajak = jenis_pajak;
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

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_insert() {
        return tgl_insert;
    }

    public void setTgl_insert(String tgl_insert) {
        this.tgl_insert = tgl_insert;
    }

    public String getTgl_update() {
        return tgl_update;
    }

    public void setTgl_update(String tgl_update) {
        this.tgl_update = tgl_update;
    }

    public String getTgl_sync() {
        return tgl_sync;
    }

    public void setTgl_sync(String tgl_sync) {
        this.tgl_sync = tgl_sync;
    }

    public String getNamakec() {
        return namakec;
    }

    public void setNamakec(String namakec) {
        this.namakec = namakec;
    }

    public String getNamakel() {
        return namakel;
    }

    public void setNamakel(String namakel) {
        this.namakel = namakel;
    }

    public String getNamawp() {
        return namawp;
    }

    public void setNamawp(String namawp) {
        this.namawp = namawp;
    }

    public String getNamapajak() {
        return namapajak;
    }

    public void setNamapajak(String namapajak) {
        this.namapajak = namapajak;
    }

    public String getLainnya() {
        return lainnya;
    }

    public void setLainnya(String lainnya) {
        this.lainnya = lainnya;
    }
}
