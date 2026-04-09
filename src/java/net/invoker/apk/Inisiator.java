package net.invoker.apk;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.net.URL;
import java.net.HttpURLConnection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;


class Inisiator {

    static final String NAMA_PREF = "inisiator";
    static final String URL_METADATA = "https://raw.githubusercontent.com/repoinvoker1/invoker/refs/heads/main/invoker.json";
    static final int BATAS_WAKTU_UNDUH = 1000 * 10;

    MainActivity mMainActivity;

    JSONObject mMetadataGlobal;

    boolean mApaDiUnduhDiLatarBelakang = false;
    int mPercobaanKe = 0;
    boolean mSedangMengunduh = false;
    boolean mApaSelesaiDiUnduh = false;
    boolean mSiap = false;
    Thread mThreadBatasWaktu;

    Inisiator(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mMainActivity.findViewById(R.id.tombol_coba_lagi).setOnClickListener((v) -> {
            android.util.Log.d("Invoker.Inisiator", "mencoba lagi...");
            mPercobaanKe += 1;
            sembunyikanKoneksiGagal();
            if (! mSedangMengunduh) {
                android.util.Log.d("Invoker.Inisiator", "tombol 'coba lagi' ditekan di saat sedang mengunduh, abaikan.");
                inisiasi();
            }
        });
    }

    void inisiasi() {
        android.util.Log.d("Invoker.Inisiator", "memulai inisiasi");

        new Thread(() -> {
            try {
                mSedangMengunduh = true;
                tungguBatasWaktu();
                unduhMetadata();
            } catch (Exception e) {
                android.util.Log.e("Invoker.Inisiator", e.getMessage());
                if (! mApaDiUnduhDiLatarBelakang) {
                    if (mPercobaanKe > 0) {
                        mSedangMengunduh = false;
                        try {
                            Thread.sleep(1500);
                            if (! mSedangMengunduh) {
                                tampilkanKoneksiGagal();
                            }
                        } catch (InterruptedException ie) {
                            android.util.Log.e("Invoker.Inisiator", ie.getMessage());
                        }
                    } else {
                        tampilkanKoneksiGagal();
                    }
                }
                mSedangMengunduh = false;
            }
        }).start();
    }

    private void tungguBatasWaktu() {
        if (mSiap) return;
        mThreadBatasWaktu = new Thread(() -> {
            try {
                Thread.sleep(BATAS_WAKTU_UNDUH);
                if (! (mApaSelesaiDiUnduh || mSiap)) {
                    tampilkanKoneksiGagal();
                }
            } catch (InterruptedException ie) {
                android.util.Log.e("Invoker.Inisiator", ie.getMessage());
            }
        });
        mThreadBatasWaktu.start();
    }

    private void unduhMetadata() throws Exception {
        if (mMetadataGlobal == null) {
            android.util.Log.d("Invoker.Inisiator", "mengunduh metadata di: " + URL_METADATA);
            HttpURLConnection httpConn = buatKoneksi(URL_METADATA);
            httpConn.connect();

            InputStream input = httpConn.getInputStream();
            String data = bacaInputKeString(input);

            mMetadataGlobal = new JSONObject(data);
        }
        cekVersi();
    }

    private void cekVersi() throws Exception {
        String versiGlobal = mMetadataGlobal.getString("versi");
        SharedPreferences pref = mMainActivity.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE);
        String versiLokal = pref.getString("versi", null);

        if (! ((versiLokal != null) && (!versiLokal.isEmpty()))) {
            android.util.Log.d("Invoker.Inisiator", "APK BARU");
            tampilkanTeksLoading();
            unduhRepo();
            bukaWeb(versiGlobal);
        } else {
            android.util.Log.d("Invoker.Inisiator", "repo lokal tersedia, versi lokal='" + versiLokal + "', versi global='" + versiGlobal + "'");

            if (versiLokal.equals(versiGlobal)) {
                android.util.Log.d("Invoker.Inisiator", "versi lokal dan global sama");
                bukaWeb(versiLokal);
                return;
            }
            android.util.Log.d("Invoker.Inisiator", "versi lokal dan global TIDAK sama");

            String versiLokalMayor = versiLokal.substring(0, versiLokal.indexOf('.'));
            String versiGlobalMayor = versiGlobal.substring(0, versiGlobal.indexOf('.'));
            android.util.Log.d("Invoker.Inisiator", "versiLokalMayor=" + versiLokalMayor + ", versiGlobalMayor=" + versiGlobalMayor);

            if (versiGlobalMayor.equals(versiLokalMayor)) {
                android.util.Log.d("Invoker.Inisiator", "versi MAYOR lokal dan global sama, unduh repo di latar belakang");
                mApaDiUnduhDiLatarBelakang = true;
                bukaWeb(versiLokal);
                unduhRepo();
            } else {
                android.util.Log.d("Invoker.Inisiator", "versi MAYOR lokal dan global TIDAK sama");
                tampilkanTeksLoading();
                unduhRepo();
                bukaWeb(versiGlobal);
            }
        }
    }

    private void unduhRepo() throws Exception {
        String versiGlobal = mMetadataGlobal.getString("versi");
        File folderRepo = new File(new File(mMainActivity.getFilesDir(), "repo"), versiGlobal);
        buatFolderRepo(folderRepo);
        android.util.Log.d("Invoker.Inisiator", "mengunduh repo " + versiGlobal + ", lalu simpan ke folder: " + folderRepo);

        JSONArray lisUrlKlien = mMetadataGlobal.getJSONArray("url_klien");
        for (int i = 0; i < lisUrlKlien.length(); i++) {
            String url = lisUrlKlien.getString(i);
            String[] ss = url.split("/refs/heads/main/");
            File berkas =  new File(folderRepo, ss[1]);
            unduhDanSimpan(url, berkas);
        }

        mApaSelesaiDiUnduh = true;
        perbaruiVersiLokal(versiGlobal);
    }

    private void buatFolderRepo(File folderRepo) throws Exception {
        Path path = Paths.get(folderRepo.toString());
        Files.createDirectories(path);
    }

    private void unduhDanSimpan(String url, File berkas) throws IOException {
        android.util.Log.d("Invoker.Inisiator", "mengunduh " + url + ", lalu simpan ke: " + berkas);

        HttpURLConnection httpConn = buatKoneksi(url);
        httpConn.connect();

        InputStream input = httpConn.getInputStream();
        FileOutputStream output = new FileOutputStream(berkas);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();
    }

    private void perbaruiVersiLokal(String versiLokalBaru) {
        SharedPreferences pref = mMainActivity.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE);
        String versiLokalSekarang = pref.getString("versi", null);
        android.util.Log.d("Invoker.Inisiator", "perbarui versiLokal: " + versiLokalSekarang + " ---> " + versiLokalBaru);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("versi", versiLokalBaru);
        editor.apply();
    }

    private void bukaWeb(String versi) {
        mSiap = true;

        String url = "https://app.local/invoker/berkas_lokal/repo/" + versi + "/index.html";
        android.util.Log.d("Invoker.Inisiator", "membuka web: " + url);

        mMainActivity.runOnUiThread(() -> {
            mMainActivity.mWebView.loadUrl(url);
        });
    }

    private HttpURLConnection buatKoneksi(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) httpUrl.openConnection();

        // Matikan cache:
        httpConn.setUseCaches(false);
        httpConn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
        httpConn.setRequestProperty("Pragma", "no-cache");
        httpConn.setRequestProperty("Expires", "0");

        return httpConn;
    }

    private String bacaInputKeString(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }

    private void tampilkanTeksLoading() {
        View teksLoading = mMainActivity.findViewById(R.id.teks_loading);
        mMainActivity.runOnUiThread(() -> {
            teksLoading.setVisibility(View.VISIBLE);
        });
    }

    private void tampilkanKoneksiGagal() {
        if (mSiap || mApaSelesaiDiUnduh) return;
        View koneksiGagal = mMainActivity.findViewById(R.id.koneksi_gagal);
        mMainActivity.runOnUiThread(() -> {
            koneksiGagal.setVisibility(View.VISIBLE);
        });
    }

    private void sembunyikanKoneksiGagal() {
        View koneksiGagal = mMainActivity.findViewById(R.id.koneksi_gagal);
        mMainActivity.runOnUiThread(() -> {
            koneksiGagal.setVisibility(View.GONE);
        });
    }

}
