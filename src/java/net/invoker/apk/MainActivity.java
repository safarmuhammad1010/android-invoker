package net.invoker.apk;


import android.content.Intent;
import android.os.Build;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.graphics.Color;
import android.net.Uri;
import android.content.ContentResolver;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.widget.Toast;


public class MainActivity extends Activity {

    static final String URL = "https://kt3xm1nqsn.gt.tc";

    static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.util.Log.d("INVOKER", "onCreate()");

        setContentView(R.layout.activity_main);

        this.webView = findViewById(R.id.webview);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            webView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        this.webView.setWebViewClient(new WebKlien(this));
        this.webView.addJavascriptInterface(new JsInterface(this), "__android");

        WebView.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(MainActivity.USER_AGENT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        this.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebViewConsole", consoleMessage.message());
                return true;
            }
        });

        this.webView.loadUrl(MainActivity.URL);
    }

    @Override
    public void onBackPressed() {
        this.webView.evaluateJavascript("__mundur()", null);
    }

    private static final int PICK_FILE_REQUEST_CODE = 1;

    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                prosesBerkas(fileUri);
            }
        }
    }

    private void prosesBerkas(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();

            String mime = contentResolver.getType(uri);
            String ekstensi = MainActivity.mimeKeEkstensi(mime);
            String namaBerkasLokal = "foto_profil";

            InputStream input = contentResolver.openInputStream(uri);

            File file = new File(getFilesDir(), namaBerkasLokal);
            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
            input.close();

            long ms = System.currentTimeMillis();
            String namaFotoProfil = String.valueOf(ms);
            String urlFotoProfil = "https://app.local/invoker/berkas_lokal/foto_profil/" + namaFotoProfil + ekstensi;
            this.webView.evaluateJavascript("__data.simpan('profil.foto', '" + urlFotoProfil + "')", null);

            android.util.Log.d("INVOKER", "foto profil berhasil dipilih, dengan nama='" + namaFotoProfil + "', dan ukuran berkas=" + file.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class JsInterface {
        MainActivity mainActivity;

        JsInterface(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @JavascriptInterface
        public void pilihFotoProfil() {
            this.mainActivity.pilihGambar();
        }
    }

    static String mimeKeEkstensi(String mime) {
        if ("image/png".equals(mime)) return ".png";
        if ("image/jpeg".equals(mime)) return ".jpg";
        if ("image/gif".equals(mime)) return ".gif";
        if ("image/webp".equals(mime)) return ".webp";
        return ".png";
    }

}
