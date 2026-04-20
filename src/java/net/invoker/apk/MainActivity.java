package net.invoker.apk;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.view.View;
import android.widget.Toast;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends Activity {

    static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 11; SAMSUNG SM-G973U) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/14.2 Chrome/87.0.4280.141 Mobile Safari/537.36";

    static MainActivity instance;

    WebView mWebView;
    Loading mLoading;
    boolean mSudahSiap = false;

    Inisiator mInisiator;
    Integrator mIntegrator;

    WebView mBrowser;
    View mLayoutBrowser;
    View mTombolTutupBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        android.util.Log.d("Invoker.MainActivity", "onCreate()");

        setContentView(R.layout.activity_main);

        mBrowser = (WebView) findViewById(R.id.browser);
        mLayoutBrowser = findViewById(R.id.layout_browser);
        mTombolTutupBrowser = findViewById(R.id.tombol_tutup_browser);
        mWebView = findViewById(R.id.webview);

        mLoading = new Loading(this);
        mLoading.mulai();

        mIntegrator = new Integrator(this, mWebView);
        mInisiator = new Inisiator(this, mIntegrator);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            mWebView.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        mWebView.setWebViewClient(new WebKlien(this));
        mWebView.addJavascriptInterface(new JsInterface(this), "__apk");
        mWebView.addJavascriptInterface(mIntegrator.mJsInterfaceWeb, "__servis");

        WebView.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setUserAgentString(MainActivity.USER_AGENT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                /*
                if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING)
                    android.util.Log.w("Invoker.WebViewUtama.Console", consoleMessage.message());
                else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR)
                    android.util.Log.e("Invoker.WebViewUtama.Console", consoleMessage.message());
                */
                android.util.Log.d("Invoker.WebViewUtama.Console", consoleMessage.message());
                return true;
            }
        });


        initEfekSuara();

        initBrowser();

        mInisiator.mulai();
    }

    private void tutupBrowser() {
        runOnUiThread(() -> {
            mLayoutBrowser.setVisibility(View.GONE);
        });
    }

    static class BrowserJsInterface {
        MainActivity mMainActivity;

        BrowserJsInterface(MainActivity mainActivity) {
            mMainActivity = mainActivity;
        }

        @JavascriptInterface
        public void tutupBrowser() {
            mMainActivity.tutupBrowser();
        }
    }

    private void initBrowser() {
        mBrowser.setWebViewClient(new BrowserWebKlien(this));
        mBrowser.addJavascriptInterface(new BrowserJsInterface(this), "__android");

        WebView.setWebContentsDebuggingEnabled(true);

        mBrowser.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("Invoker.WebViewBrowser.Console", consoleMessage.message());
                return true;
            }
        });

        WebSettings webSettings = mBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        mTombolTutupBrowser.setOnClickListener((v) -> {
            tutupBrowser();
        });
    }

    String mUrlTargetBrowser = null;

    void bukaDiBrowser(String url) {
        mUrlTargetBrowser = url;
        runOnUiThread(() -> {
            if (! url.equals(mWebView.getUrl())) {
                mBrowser.loadUrl("about:blank");
                mBrowser.loadUrl(url);
            }
            mLayoutBrowser.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onBackPressed() {
        if (mLayoutBrowser.getVisibility() == View.VISIBLE) return;
        mWebView.evaluateJavascript("__mundur()", null);
    }

    SoundPool mSoundPool;
    int mSuaraTransisi;
    int mSuaraInsentif;

    private void initEfekSuara() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
            mSoundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();
        } else {
            mSoundPool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        }
        mSuaraTransisi = mSoundPool.load(this, R.raw.transisi, 1);
        mSuaraInsentif = mSoundPool.load(this, R.raw.insentif, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundPool != null) {
            mSoundPool.release();
        }
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
        // Proses Berkas Foto Profil:
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                prosesBerkasFotoProfil(fileUri);
            }
        }
    }

    private void prosesBerkasFotoProfil(Uri uri) {
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
            mWebView.evaluateJavascript("__data.simpan('profil.foto', '" + urlFotoProfil + "')", null);

            android.util.Log.d("Invoker.MainActivity", "foto profil berhasil dipilih, dengan nama='" + namaFotoProfil + "', dan ukuran berkas=" + file.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getClipboardText() {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = null;
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                ClipData.Item item = primaryClip.getItemAt(0);
                CharSequence text = item.getText();
                if (text != null) {
                    pasteData = text.toString();
                } else {
                    android.util.Log.d("Invoker.MainActivity", "Clipboard bukan teks");
                }
            }
        }
        return pasteData;
    }

    public void tos(String pesan) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }

    static class JsInterface {

        static final int DURASI_GETAR = 150;

        MainActivity mMainActivity;
        Vibrator mVibrator;

        JsInterface(MainActivity mainActivity) {
            mMainActivity = mainActivity;
            mVibrator = (Vibrator) mMainActivity.getSystemService(Context.VIBRATOR_SERVICE);
        }

        @JavascriptInterface
        public void siap() {
            mMainActivity.mLoading.sembunyikan();
            mMainActivity.mSudahSiap = true;
            android.util.Log.d("Invoker.MainActivity", "sudah siap");
        }

        @JavascriptInterface
        public String dUrlServer() {
            String urlServer = null;
            try {
                urlServer = mMainActivity.mInisiator.mMetadataGlobal.getString("url_server");
            } catch (Exception e) {
                android.util.Log.e("Invoker.MainActivity", e.getMessage());
            }
            return urlServer;
        }

        @JavascriptInterface
        public void pilihFotoProfil() {
            mMainActivity.pilihGambar();
        }

        @JavascriptInterface
        public String ambilPapanKlip() {
            return mMainActivity.getClipboardText();
        }

        @JavascriptInterface
        public void getar() {
            if (mVibrator != null && mVibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    mVibrator.vibrate(VibrationEffect.createOneShot(DURASI_GETAR, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    mVibrator.vibrate(DURASI_GETAR);
                }
            }
        }

        @JavascriptInterface
        public void suara1() {
            float volume = ambilVolumeHp();
            mMainActivity.mSoundPool.play(mMainActivity.mSuaraTransisi, volume, volume, 1, 0, 1.0f);
        }

        @JavascriptInterface
        public void suara2() {
            // ...
        }

        @JavascriptInterface
        public void suara3() {
            // ...
        }

        @JavascriptInterface
        public void suara4() {
            float volume = ambilVolumeHp();
            mMainActivity.mSoundPool.play(mMainActivity.mSuaraInsentif, volume, volume, 1, 0, 1.0f);
        }

        private float ambilVolumeHp() {
            float volume = 1.0f;
            AudioManager am = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                float current = (float) am.getStreamVolume(AudioManager.STREAM_MUSIC);
                float max = (float) am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                if (max > 0) {
                    volume = current / max;
                }
            }
            return volume;
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
