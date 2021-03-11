package com.dnhsolution.objekpajak.pendataan;

/**
 * Created by sawrusdino on 09/04/2018.
 */

public class Berkas {

    private String filename;
    private int size;



    public Berkas(String filename, int size){
        this.filename = filename;
        this.size = size;
    }

    public String getFilename(){
        return filename;
    }

    public int getFilesize(){
        return size;
    }

    public void setFilename(){
        this.filename = filename;
    }
}
