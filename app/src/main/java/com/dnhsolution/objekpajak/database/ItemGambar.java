package com.dnhsolution.objekpajak.database;

public class ItemGambar {
    int id_data;
    String id_inc;
    String path;

    public ItemGambar(int id_data, String id_inc, String path) {
        this.id_data = id_data;
        this.id_inc = id_inc;
        this.path = path;
    }

    public ItemGambar() {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
