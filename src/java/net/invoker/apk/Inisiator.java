package net.invoker.apk;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONObject;


class Inisiator {

    static final String NAMA_PREF = "inisiator";
    static final String URL_METADATA = "https://raw.githubusercontent.com/repoinvoker1/invoker/refs/heads/main/invoker.json";

    MainActivity mMainActivity;

    JSONObject mMetadataGlobal;

    Inisiator(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    void inisiasi() {
        android.util.Log.d("Invoker.Inisiator", "memulai inisiasi");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    unduhMetadata();
                } catch (Exception e) {
                    android.util.Log.e("Invoker.Inisiator", e.getMessage());
                }
            }
        }).start();
    }

    private void unduhMetadata() throws Exception {
        android.util.Log.d("Invoker.Inisiator", "mengunduh metadata...");
        URL url = new URL(URL_METADATA);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream input = conn.getInputStream();
        String data = bacaInputKeString(input);
        android.util.Log.d("Invoker.Inisiator", data);
        mMetadataGlobal = new JSONObject(data);
        cekVersi();
    }

    private void cekVersi() throws Exception {
        String versiGlobal = mMetadataGlobal.getString("versi");
        SharedPreferences pref = mMainActivity.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE);
        String versiLokal = pref.getString("versi", null);
        if (! ((versiLokal != null) && (!versiLokal.isEmpty()))) {
            android.util.Log.d("Invoker.Inisiator", "APK baru");
            unduhRepo();
            bukaWeb(versiGlobal);
        } else {
            android.util.Log.d("Invoker.Inisiator", "repo lokal tersedia, versi lokal='" + versiLokal + "', versi global='" + versiGlobal + "'");
            if (versiLokal.equals(versiGlobal)) {
                android.util.Log.d("Invoker.Inisiator", "versi lokal dan global sama");
                bukaWeb(versiLokal);
                return;
            }
            // ...
        }
    }

    private void unduhRepo() throws Exception {
        String versiGlobal = mMetadataGlobal.getString("versi");
        File folderRepo = new File(mMainActivity.getFilesDir(), versiGlobal);
        android.util.Log.d("Invoker.Inisiator", "mengunduh repo " + versiGlobal + ", lalu simpan ke folder: " + folderRepo);

        perbaruiVersiLokal(versiGlobal);
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
        android.util.Log.d("Invoker.Inisiator", "membuka web " + versi);
        // ...
    }

    private void unduhDanSimpan(String url, String berkas) {
        // ...
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

}
