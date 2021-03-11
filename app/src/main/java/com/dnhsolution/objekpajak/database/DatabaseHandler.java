package com.dnhsolution.objekpajak.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dnhsolution.objekpajak.pendataan.model.ItemNamaUsaha;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pdrd";

    // Contacts table name
    public final String TABLE_MASTER = "ms_data";
    public final String TABLE_HISTORY = "tb_history";
    public final String TABLE_GAMBAR = "tb_gambar";
    public final String TABLE_GAMBAR_HISTORY = "tb_gambar_history";

    //kolom ms_data dan tb_history
    public final String col_id_data="id_data";
    public final String col_id_inc="id_inc";
    public final String col_nama_usaha="nama_usaha";
    public final String col_alamat_usaha="alamat_usaha";
    public final String col_npwpd="npwpd";
    public final String col_kodekec="kodekec";
    public final String col_kodekel="kodekel";
    public final String col_jenis_pajak="jenis_pajak";
    public final String col_namapajak="namapajak";
    public final String col_golongan="golongan";
    public final String col_id_op="id_op";
    public final String col_lati="lati";
    public final String col_longi="longi";
    public final String col_status="status";
    public final String col_tgl_insert="tgl_insert";
    public final String col_tgl_update="tgl_update";
    public final String col_tgl_sync="tgl_sync";
    public final String col_namakec="namakec";
    public final String col_namakel="namakel";
    public final String col_namawp="namawp";

    //kolom tb_gambar
    public final String col_id_data_gambar = "id_data_gambar";
    public final String col_id_inc_gambar = "id_inc_gambar";
    public final String col_path = "path";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Buat Tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABEL_MASTER = "CREATE TABLE " + TABLE_MASTER + "("
                + col_id_data + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_id_inc + " TEXT," + col_npwpd + " TEXT,"
                + col_nama_usaha + " TEXT," + col_alamat_usaha + " TEXT," + col_kodekec + " TEXT," + col_kodekel + " TEXT," + col_jenis_pajak + " TEXT,"
                + col_namakec + " TEXT," + col_namakel + " TEXT," + col_namawp + " TEXT," + col_namapajak + " TEXT,"
                + col_golongan + " TEXT," + col_id_op + " TEXT," + col_lati + " TEXT," + col_longi + " TEXT," + col_status + " TEXT,"
                + col_tgl_insert + " TEXT," + col_tgl_update + " TEXT," + col_tgl_sync + " TEXT" +")";

        String CREATE_TABEL_HISTORY = "CREATE TABLE " + TABLE_HISTORY + "("
                + col_id_data + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_id_inc + " TEXT," + col_npwpd + " TEXT,"
                + col_nama_usaha + " TEXT," + col_alamat_usaha + " TEXT," + col_kodekec + " TEXT," + col_kodekel + " TEXT," + col_jenis_pajak + " TEXT,"
                + col_namakec + " TEXT," + col_namakel + " TEXT," + col_namawp + " TEXT," + col_namapajak + " TEXT,"
                + col_golongan + " TEXT," + col_id_op + " TEXT," + col_lati + " TEXT," + col_longi + " TEXT," + col_status + " TEXT,"
                + col_tgl_insert + " TEXT," + col_tgl_update + " TEXT," + col_tgl_sync + " TEXT" +")";

        String CREATE_TABEL_GAMBAR = "CREATE TABLE " + TABLE_GAMBAR + "("
                + col_id_data_gambar + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_id_inc_gambar + " TEXT," + col_path + " TEXT"+")";

        String CREATE_TABEL_GAMBAR_HISTORY = "CREATE TABLE " + TABLE_GAMBAR_HISTORY + "("
                + col_id_data_gambar + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_id_inc_gambar + " TEXT," + col_path + " TEXT"+")";


        db.execSQL(CREATE_TABEL_MASTER);
        db.execSQL(CREATE_TABEL_HISTORY);
        db.execSQL(CREATE_TABEL_GAMBAR);
        db.execSQL(CREATE_TABEL_GAMBAR_HISTORY);
    }

    // Perbarui database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tabel lama jika sudah ada
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMBAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMBAR_HISTORY);

        // Create tables again
        onCreate(db);
    }

    //ADD DATA
    public void add_data_sinkron(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();
        values.put(col_id_inc, id.getId_inc());
        values.put(col_nama_usaha, id.getNama_usaha());
        values.put(col_npwpd, id.getNpwpd());
        values.put(col_alamat_usaha, id.getAlamat_usaha());
        values.put(col_kodekec, id.getKodekec());
        values.put(col_kodekel, id.getKodekel());
        values.put(col_jenis_pajak, id.getJenis_pajak());
        values.put(col_id_op, id.getId_op());
        values.put(col_golongan, id.getGolongan());
        values.put(col_namakec, id.getNamakec());
        values.put(col_namakel, id.getNamakel());
        values.put(col_namawp, id.getNamawp());
        values.put(col_namapajak, id.getNamapajak());
        values.put(col_status, id.getStatus());
        values.put(col_tgl_insert, id.getTgl_insert());

        // memasukkan data
        db.insert(TABLE_MASTER, null, values);
        db.close(); // Menutup koneksi database

    }

    public void addDataFoto(ItemGambar iv) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();
        values.put(col_id_inc_gambar, iv.getId_inc());
        values.put(col_path, iv.getPath());

        // memasukkan data
        db.insert(TABLE_GAMBAR, null, values);
        db.close(); // Menutup koneksi database
    }

    public void InsertDataBaru(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();

        values.put(col_nama_usaha, id.getNama_usaha());
        values.put(col_alamat_usaha, id.getAlamat_usaha());
        values.put(col_npwpd, id.getNpwpd());
        values.put(col_namakec, id.getNamakec());
        values.put(col_namakel, id.getNamakel());
        values.put(col_jenis_pajak, id.getJenis_pajak());
        values.put(col_namapajak, id.getNamapajak());
        values.put(col_golongan, id.getGolongan());
        values.put(col_id_op, id.getId_op());
        values.put(col_lati, id.getLati());
        values.put(col_longi, id.getLongi());
        values.put(col_status, id.getStatus());
        values.put(col_tgl_update, id.getTgl_update());
        values.put(col_tgl_insert, id.getTgl_update());
        values.put(col_namawp, "-");
        values.put(col_kodekec, id.getKodekec());
        values.put(col_kodekel, id.getKodekel());

        db.insert(TABLE_MASTER, null,values);
        db.close();
    }

    public void InsertDataBaru2(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();

        values.put(col_nama_usaha, id.getNama_usaha());
        values.put(col_alamat_usaha, id.getAlamat_usaha());
        values.put(col_npwpd, id.getNpwpd());
        values.put(col_namakec, id.getNamakec());
        values.put(col_namakel, id.getNamakel());
        values.put(col_jenis_pajak, id.getJenis_pajak());
        values.put(col_namapajak, id.getNamapajak());
        values.put(col_golongan, id.getGolongan());
        values.put(col_id_op, id.getId_op());
        values.put(col_lati, id.getLati());
        values.put(col_longi, id.getLongi());
        values.put(col_status, id.getStatus());
        values.put(col_tgl_update, id.getTgl_update());
        values.put(col_tgl_insert, id.getTgl_update());
        values.put(col_namawp, id.getNamawp());
        values.put(col_kodekec, id.getKodekec());
        values.put(col_kodekel, id.getKodekel());
        values.put(col_id_inc, id.getId_inc());

        db.insert(TABLE_MASTER, null,values);
        db.close();
    }

    //DELETE DATA
    public void delete_data_master(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MASTER);
        db.close();
    }

    public void delete_data_history(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HISTORY);
        db.close();
    }

    public void delete_data_gambar(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GAMBAR);
        db.close();
    }

    public void delete_data_gambar_history(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GAMBAR_HISTORY);
        db.close();
    }

    public void delete_data_by_id(int id_data){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GAMBAR + " WHERE " + col_id_inc_gambar +"=="+id_data);
        db.execSQL("DELETE FROM " + TABLE_MASTER + " WHERE " + col_id_data +"=="+id_data);
        db.close();
    }

    //READ DATA
//    public int count_data_master(String npwpd) {
//        String countQuery = "SELECT  * FROM "+ TABLE_MASTER +" WHERE "+col_npwpd+"=="+npwpd+" AND "+col_status+"==0";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
//    }

    public int count_data_master(String npwpd) {
        String countQuery = "SELECT  * FROM "+ TABLE_MASTER +" WHERE "+col_npwpd+"=="+npwpd+" AND "+col_status+"==0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int CountAllDataMaster() {
        String countQuery = "SELECT  * FROM "+ TABLE_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int CountDataTerupdate() {
        String countQuery = "SELECT  * FROM "+ TABLE_MASTER +" WHERE "+col_status+" IN ('2', '00')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int CountDataRiwayat() {
        String countQuery = "SELECT  * FROM "+ TABLE_MASTER +" WHERE "+col_status+"==3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int CountMaxDataMaster(){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT MAX("+col_id_data+") FROM "+TABLE_MASTER, null);
        int maxid = (cursor.moveToFirst() ? cursor.getInt(0) : 0);
        return maxid;
    }

    public int count_data_gambar() {
        String countQuery = "SELECT  * FROM "+ TABLE_GAMBAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int count_data(String npwpd) {
        String countQuery = "SELECT  * FROM "+ TABLE_MASTER +" WHERE "+col_npwpd+"=="+npwpd;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList getDataGambar(String id_inc) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_GAMBAR +" WHERE "+ col_id_inc_gambar + "=="+id_inc;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemGambar svf = new ItemGambar();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setPath(cursor.getString(2));
                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataTerupdate() {
        ArrayList list = new ArrayList();


        String selectQuery = "SELECT "+col_id_data+","+col_id_inc+","+col_npwpd+
                ","+col_namawp+","+col_nama_usaha+","+col_alamat_usaha+
                ","+col_namakec+","+col_namakel+","+col_lati+","+col_longi+
                ","+col_tgl_update+","+col_jenis_pajak+","+col_id_op+
                " FROM " + TABLE_MASTER +" WHERE "+ col_status + " IN ('2', '00') ORDER BY "+col_tgl_update+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setNpwpd(cursor.getString(2));
                svf.setNamawp(cursor.getString(3));
                svf.setNama_usaha(cursor.getString(4));
                svf.setAlamat_usaha(cursor.getString(5));
                svf.setNamakec(cursor.getString(6));
                svf.setNamakel(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setTgl_update(cursor.getString(10));
                svf.setJenis_pajak(cursor.getString(11));
                svf.setId_op(cursor.getString(12));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

//    public ArrayList getDataByNPWPD(String npwpd) {
//        ArrayList list = new ArrayList();
//
//        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
//                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
//                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_id_op+
//                ","+col_id_data+" FROM " + TABLE_MASTER +" WHERE "+ col_npwpd + "=="+npwpd +" AND "+
//                col_status+"==0";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // Perulangan semua data untuk dimasukkan kedalam list
//        if (cursor.moveToFirst()) {
//            do {
//                ItemNamaUsaha svf = new ItemNamaUsaha();
//                //svf.set_id(Integer.valueOf(cursor.getString(0)));
//                svf.setId_inc(cursor.getString(0));
//                svf.setNama_tempat_usaha(cursor.getString(1));
//                svf.setNama_wp(cursor.getString(2));
//                svf.setAlamat(cursor.getString(3));
//                svf.setKecamatan(cursor.getString(4));
//                svf.setKelurahan(cursor.getString(5));
//                svf.setNamajp(cursor.getString(6));
//                svf.setJenispajak(cursor.getString(7));
//                svf.setGolongan(cursor.getString(8));
//                svf.setId_op(cursor.getString(9));
//                svf.setId_data(cursor.getString(10));
//
//                // Menambahkan data ke dalam list
//                list.add(svf);
//            } while (cursor.moveToNext());
//        }
//
//        // return list
//        return list;
//    }

    public ArrayList getDataByNPWPD(String npwpd) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_id_op+
                ","+col_id_data+","+col_kodekec+","+col_kodekel+" FROM " + TABLE_MASTER +" WHERE "+ col_npwpd + "=="+npwpd +" AND "+
                col_status+"==0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemNamaUsaha svf = new ItemNamaUsaha();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_tempat_usaha(cursor.getString(1));
                svf.setNama_wp(cursor.getString(2));
                svf.setAlamat(cursor.getString(3));
                svf.setKecamatan(cursor.getString(4));
                svf.setKelurahan(cursor.getString(5));
                svf.setNamajp(cursor.getString(6));
                svf.setJenispajak(cursor.getString(7));
                svf.setGolongan(cursor.getString(8));
                svf.setId_op(cursor.getString(9));
                svf.setId_data(cursor.getString(10));
                svf.setKd_kecamatan(cursor.getString(11));
                svf.setKd_kelurahan(cursor.getString(12));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataByNPWPD2(String npwpd) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_id_op+
                ","+col_id_data+" FROM " + TABLE_MASTER +" WHERE "+ col_npwpd + "=='"+npwpd +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemNamaUsaha svf = new ItemNamaUsaha();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_tempat_usaha(cursor.getString(1));
                svf.setNama_wp(cursor.getString(2));
                svf.setAlamat(cursor.getString(3));
                svf.setKecamatan(cursor.getString(4));
                svf.setKelurahan(cursor.getString(5));
                svf.setNamajp(cursor.getString(6));
                svf.setJenispajak(cursor.getString(7));
                svf.setGolongan(cursor.getString(8));
                svf.setId_op(cursor.getString(9));
                svf.setId_data(cursor.getString(10));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataByNamaWP(String nama_wp) {
        ArrayList list = new ArrayList();

//        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
//                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
//                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_npwpd+
//                ","+col_id_data+" FROM " + TABLE_MASTER +" WHERE "+ col_namawp + "=='"+nama_wp +"'";

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_npwpd+
                ","+col_id_data+" FROM " + TABLE_MASTER +" WHERE "+ col_namawp + " LIKE '%"+nama_wp +"%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemNamaUsaha svf = new ItemNamaUsaha();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_tempat_usaha(cursor.getString(1));
                svf.setNama_wp(cursor.getString(2));
                svf.setAlamat(cursor.getString(3));
                svf.setKecamatan(cursor.getString(4));
                svf.setKelurahan(cursor.getString(5));
                svf.setNamajp(cursor.getString(6));
                svf.setJenispajak(cursor.getString(7));
                svf.setGolongan(cursor.getString(8));
                svf.setId_op(cursor.getString(9));
                svf.setId_data(cursor.getString(10));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataByNamaUsaha(String nama_usaha) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_jenis_pajak+","+col_golongan+","+col_npwpd+
                ","+col_id_data+" FROM " + TABLE_MASTER +" WHERE "+ col_nama_usaha + " LIKE '%"+nama_usaha +"%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemNamaUsaha svf = new ItemNamaUsaha();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_tempat_usaha(cursor.getString(1));
                svf.setNama_wp(cursor.getString(2));
                svf.setAlamat(cursor.getString(3));
                svf.setKecamatan(cursor.getString(4));
                svf.setKelurahan(cursor.getString(5));
                svf.setNamajp(cursor.getString(6));
                svf.setJenispajak(cursor.getString(7));
                svf.setGolongan(cursor.getString(8));
                svf.setId_op(cursor.getString(9));
                svf.setId_data(cursor.getString(10));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDetailData(int id_data) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_golongan+","+col_lati+","+col_longi+
                ","+col_npwpd+" FROM " + TABLE_MASTER +" WHERE "+ col_id_data + "=="+id_data;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_usaha(cursor.getString(1));
                svf.setNamawp(cursor.getString(2));
                svf.setAlamat_usaha(cursor.getString(3));
                svf.setNamakec(cursor.getString(4));
                svf.setNamakel(cursor.getString(5));
                svf.setNamapajak(cursor.getString(6));
                svf.setGolongan(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setNpwpd(cursor.getString(10));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataRiwayat() {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_data+","+col_id_inc+","+col_npwpd+
                ","+col_namawp+","+col_nama_usaha+","+col_alamat_usaha+
                ","+col_namakec+","+col_namakel+","+col_lati+","+col_longi+
                ","+col_tgl_sync+","+col_jenis_pajak+","+col_id_op+
                " FROM " + TABLE_MASTER +" WHERE "+ col_status + "==3 ORDER BY "+col_tgl_sync+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setNpwpd(cursor.getString(2));
                svf.setNamawp(cursor.getString(3));
                svf.setNama_usaha(cursor.getString(4));
                svf.setAlamat_usaha(cursor.getString(5));
                svf.setNamakec(cursor.getString(6));
                svf.setNamakel(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setTgl_sync(cursor.getString(10));
                svf.setJenis_pajak(cursor.getString(11));
                svf.setId_op(cursor.getString(12));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataFoto(String id_data) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_GAMBAR +" WHERE "+col_id_inc_gambar +"=="+id_data;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemGambar svf = new ItemGambar();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setPath(cursor.getString(2));
                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    //UPDATE DATA
    public int updateDataPemutakhiran(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();


        values.put(col_nama_usaha, id.getNama_usaha());
        values.put(col_alamat_usaha, id.getAlamat_usaha());
        values.put(col_namakec, id.getNamakec());
        values.put(col_namakel, id.getNamakel());
        values.put(col_jenis_pajak, id.getJenis_pajak());
        values.put(col_namapajak, id.getNamapajak());
        values.put(col_golongan, id.getGolongan());
        values.put(col_id_op, id.getId_op());
        values.put(col_lati, id.getLati());
        values.put(col_longi, id.getLongi());
        values.put(col_status, id.getStatus());
        values.put(col_tgl_update, id.getTgl_update());
        values.put(col_kodekec, id.getKodekec());
        values.put(col_kodekel, id.getKodekel());

        return db.update(TABLE_MASTER, values, col_id_inc + " = ?",
                new String[] { String.valueOf(id.getId_inc()) });
    }

    public int updateDataMaster(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();


        values.put(col_status, id.getStatus());
        values.put(col_tgl_sync, id.getTgl_sync());

        return db.update(TABLE_MASTER, values, col_id_data + " = ?",
                new String[] { String.valueOf(id.getId_data()) });
    }


    //update 29-01-20

    public ArrayList getDataTerupdateAll() {
        ArrayList list = new ArrayList();


        String selectQuery = "SELECT "+col_id_data+","+col_id_inc+","+col_npwpd+
                ","+col_namawp+","+col_nama_usaha+","+col_alamat_usaha+
                ","+col_namakec+","+col_namakel+","+col_lati+","+col_longi+
                ","+col_tgl_update+","+col_jenis_pajak+","+col_id_op+
                ","+col_namapajak+","+col_golongan+
                ","+col_kodekec+","+col_kodekel+
                ","+col_tgl_insert+","+col_status+
                " FROM " + TABLE_MASTER +" WHERE "+ col_status + " IN ('2', '00')  ORDER BY "+col_tgl_update+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setNpwpd(cursor.getString(2));
                svf.setNamawp(cursor.getString(3));
                svf.setNama_usaha(cursor.getString(4));
                svf.setAlamat_usaha(cursor.getString(5));
                svf.setNamakec(cursor.getString(6));
                svf.setNamakel(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setTgl_update(cursor.getString(10));
                svf.setJenis_pajak(cursor.getString(11));
                svf.setId_op(cursor.getString(12));
                svf.setNamapajak(cursor.getString(13));
                svf.setGolongan(cursor.getString(14));
                svf.setKodekec(cursor.getString(15));
                svf.setKodekel(cursor.getString(16));
                svf.setTgl_insert(cursor.getString(17));
                svf.setStatus(cursor.getString(18));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public void InsertHistory(ItemData id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();

        values.put(col_nama_usaha, id.getNama_usaha());
        values.put(col_alamat_usaha, id.getAlamat_usaha());
        values.put(col_npwpd, id.getNpwpd());
        values.put(col_namakec, id.getNamakec());
        values.put(col_namakel, id.getNamakel());
        values.put(col_jenis_pajak, id.getJenis_pajak());
        values.put(col_namapajak, id.getNamapajak());
        values.put(col_golongan, id.getGolongan());
        values.put(col_id_op, id.getId_op());
        values.put(col_lati, id.getLati());
        values.put(col_longi, id.getLongi());
        values.put(col_status, id.getStatus());
        values.put(col_tgl_update, id.getTgl_update());
        values.put(col_tgl_insert, id.getTgl_insert());
        values.put(col_tgl_sync, id.getTgl_sync());
        values.put(col_namawp, id.getNamawp());
        values.put(col_kodekec, id.getKodekec());
        values.put(col_kodekel, id.getKodekel());
        values.put(col_id_inc, id.getId_inc());

        db.insert(TABLE_HISTORY, null,values);
        db.close();
    }

    public int CountMaxHistory(){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT MAX("+col_id_data+") FROM "+TABLE_HISTORY, null);
        int maxid = (cursor.moveToFirst() ? cursor.getInt(0) : 0);
        return maxid;
    }

    public void addDataFotoHistory(ItemGambar iv) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String today = getCurrentDate();

        ContentValues values = new ContentValues();
        values.put(col_id_inc_gambar, iv.getId_inc());
        values.put(col_path, iv.getPath());

        // memasukkan data
        db.insert(TABLE_GAMBAR_HISTORY, null, values);
        db.close(); // Menutup koneksi database
    }

    public ArrayList getDataRiwayatNew() {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_data+","+col_id_inc+","+col_npwpd+
                ","+col_namawp+","+col_nama_usaha+","+col_alamat_usaha+
                ","+col_namakec+","+col_namakel+","+col_lati+","+col_longi+
                ","+col_tgl_sync+","+col_jenis_pajak+","+col_id_op+
                " FROM " + TABLE_HISTORY +" WHERE "+ col_status + " IN ('3', '01') ORDER BY "+col_tgl_sync+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setNpwpd(cursor.getString(2));
                svf.setNamawp(cursor.getString(3));
                svf.setNama_usaha(cursor.getString(4));
                svf.setAlamat_usaha(cursor.getString(5));
                svf.setNamakec(cursor.getString(6));
                svf.setNamakel(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setTgl_sync(cursor.getString(10));
                svf.setJenis_pajak(cursor.getString(11));
                svf.setId_op(cursor.getString(12));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public ArrayList getDataFotoNew(String id_data) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_GAMBAR_HISTORY +" WHERE "+col_id_inc_gambar +"=="+id_data;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemGambar svf = new ItemGambar();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_data(Integer.parseInt(cursor.getString(0)));
                svf.setId_inc(cursor.getString(1));
                svf.setPath(cursor.getString(2));
                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public int CountDataRiwayatNew() {
        String countQuery = "SELECT  * FROM "+ TABLE_HISTORY +" WHERE "+col_status+"==3";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList getDetailDataNew(int id_data) {
        ArrayList list = new ArrayList();

        String selectQuery = "SELECT "+col_id_inc+","+col_nama_usaha+","+col_namawp+
                ","+col_alamat_usaha+","+col_namakec+","+col_namakel+
                ","+col_namapajak+","+col_golongan+","+col_lati+","+col_longi+
                ","+col_npwpd+" FROM " + TABLE_HISTORY +" WHERE "+ col_id_data + "=="+id_data;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Perulangan semua data untuk dimasukkan kedalam list
        if (cursor.moveToFirst()) {
            do {
                ItemData svf = new ItemData();
                //svf.set_id(Integer.valueOf(cursor.getString(0)));
                svf.setId_inc(cursor.getString(0));
                svf.setNama_usaha(cursor.getString(1));
                svf.setNamawp(cursor.getString(2));
                svf.setAlamat_usaha(cursor.getString(3));
                svf.setNamakec(cursor.getString(4));
                svf.setNamakel(cursor.getString(5));
                svf.setNamapajak(cursor.getString(6));
                svf.setGolongan(cursor.getString(7));
                svf.setLati(cursor.getString(8));
                svf.setLongi(cursor.getString(9));
                svf.setNpwpd(cursor.getString(10));

                // Menambahkan data ke dalam list
                list.add(svf);
            } while (cursor.moveToNext());
        }

        // return list
        return list;
    }

    public void delete_data_by_id_new(int id_data){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HISTORY + " WHERE " + col_id_data +"=="+id_data);
        db.close();
    }


}
