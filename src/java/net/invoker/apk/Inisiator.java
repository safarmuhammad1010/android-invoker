package net.invoker.apk;


import android.content.Context;
import android.content.SharedPreferences;

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
        String data = readStream(input);
        android.util.Log.d("Invoker.Inisiator", data);
        mMetadataGlobal = new JSONObject(data);
        cekVersiLokal();
    }

    private void unduhRepo() {

    }

    private void cekVersiLokal() {
        SharedPreferences pref = mMainActivity.getSharedPreferences(NAMA_PREF, Context.MODE_PRIVATE);
        String versiLokal = pref.getString("versi", null);
        if (! ((versiLokal != null) && (!versiLokal.isEmpty()))) {
            android.util.Log.d("Invoker.Inisiator", "repo lokal tersedia, cek versi");
        } else {
            android.util.Log.d("Invoker.Inisiator", "apk baru, unduh repo lalu tampilkan");
            unduhRepo();
        }
    }

    private String readStream(InputStream input) throws IOException {
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
