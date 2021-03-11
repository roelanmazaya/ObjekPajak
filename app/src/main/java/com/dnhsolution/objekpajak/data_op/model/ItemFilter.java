package com.dnhsolution.objekpajak.data_op.model;

/**
 * Created by KF on 11/5/2018.
 */

public class ItemFilter {
    private String kd_filter;
    private String nama_filter;

    public ItemFilter(String kd_filter, String nama_filter) {
        this.kd_filter = kd_filter;
        this.nama_filter = nama_filter;
    }

    public String getKd_filter() {
        return kd_filter;
    }

    public void setKd_filter(String kd_filter) {
        this.kd_filter = kd_filter;
    }

    public String getNama_filter() {
        return nama_filter;
    }

    public void setNama_filter(String nama_filter) {
        this.nama_filter = nama_filter;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama_filter;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemFilter){
            ItemFilter c = (ItemFilter)obj;
            if(c.getNama_filter().equals(nama_filter) && c.getKd_filter().equals(kd_filter) ) return true;
        }

        return false;
    }


}
