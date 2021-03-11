package com.dnhsolution.objekpajak.sinkron;

/**
 * Created by KHAN on 08/04/2018.
 */
import android.util.Log;

import com.dnhsolution.objekpajak.config.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UploadData {
    public static final String UPLOAD_URL= Config.URL+"TambahData";
    public static final String UPLOAD_URL_FILE= Config.URL+"TambahDataFile";

    private int serverResponseCode;

    public String uploadDataUmum(
            String nama_usaha, String alamat_usaha, String npwpd, String nm_kecamatan,
            String nm_kelurahan, String jenis_pajak, String id_op, String lokasi,
            String date_insert, String username, String id_inc, final ArrayList<String> foto, String kode_kec, String kode_kel) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String tgl_penelitian="";

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current = null;
        try {
            current = form.parse(date_insert);
            SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateString = frmt.format(current);
            tgl_penelitian = dateString;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }


        try {

            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nama_usaha\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nama_usaha);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"alamat_usaha\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(alamat_usaha);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"npwpd\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(npwpd);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nm_kecamatan\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nm_kecamatan);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nm_kelurahan\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nm_kelurahan);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"jenis_pajak\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(jenis_pajak);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id_op\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(id_op);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"lokasi\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(lokasi);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"date_insert\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(tgl_penelitian);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"username\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(username);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id_inc\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(id_inc);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"kd_kec\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(kode_kec);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"kd_kel\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(kode_kel);
            dos.writeBytes(lineEnd);

            for (int x = 0; x < foto.size(); x++) {
                String pat = foto.get(x);
                File sourceFile = new File(pat);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"foto[" + x + "]\";filename=\""
                        + pat + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                Log.i("Huzza", "Initial .available : " + bytesAvailable);

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                fileInputStream.close();
            }

            serverResponseCode = conn.getResponseCode();
            dos.flush();
            dos.close();
                //return conn.getResponseMessage();
//            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = ex.getMessage();
            String activity = "UploadData-WithImage1";
            sinkronActivity.sendLog(info, activity);
        } catch (Exception ec) {
            ec.printStackTrace();
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = ec.getMessage();
            String activity = "UploadData-WithImage2";
            sinkronActivity.sendLog(info, activity);
        }

        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ioex) {
                SinkronActivity sinkronActivity = new SinkronActivity();
                String info = ioex.getMessage();
                String activity = "UploadData-WithImage3";
                sinkronActivity.sendLog(info, activity);
            }
            return sb.toString();
        }else {
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = "Tidak dapat upload !";
            String activity = "UploadData-WithImage4";
            sinkronActivity.sendLog(info, activity);

            return "Could not upload";

        }
    }

    public String uploadDataUmum2(
            String nama_usaha, String alamat_usaha, String npwpd, String nm_kecamatan,
            String nm_kelurahan, String jenis_pajak, String id_op, String lokasi,
            String date_insert, String username, String id_inc, String kode_kec, String kode_kel) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String tgl_penelitian="";

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current = null;
        try {
            current = form.parse(date_insert);
            SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateString = frmt.format(current);
            tgl_penelitian = dateString;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }


        try {

            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nama_usaha\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nama_usaha);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"alamat_usaha\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(alamat_usaha);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"npwpd\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(npwpd);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nm_kecamatan\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nm_kecamatan);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"nm_kelurahan\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(nm_kelurahan);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"jenis_pajak\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(jenis_pajak);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id_op\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(id_op);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"lokasi\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(lokasi);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"date_insert\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(tgl_penelitian);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"username\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(username);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id_inc\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(id_inc);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"kd_kec\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(kode_kec);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"kd_kel\""
                    + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(kode_kel);
            dos.writeBytes(lineEnd);

            serverResponseCode = conn.getResponseCode();
            dos.flush();
            dos.close();
            //return conn.getResponseMessage();
//            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = ex.getMessage();
            String activity = "UploadData-WithoutImage1";
            sinkronActivity.sendLog(info, activity);
        } catch (Exception ec) {
            ec.printStackTrace();
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = ec.getMessage();
            String activity = "UploadData-WithoutImage2";
            sinkronActivity.sendLog(info, activity);
        }

        if (serverResponseCode == 200) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ioex) {
                SinkronActivity sinkronActivity = new SinkronActivity();
                String info = ioex.getMessage();
                String activity = "UploadData-WithoutImag3";
                sinkronActivity.sendLog(info, activity);
            }
            return sb.toString();
        }else {
            SinkronActivity sinkronActivity = new SinkronActivity();
            String info = "Tidak dapat upload !";
            String activity = "UploadData-WithoutImage4";
            sinkronActivity.sendLog(info, activity);
            return "Could not upload";
        }
    }

}
