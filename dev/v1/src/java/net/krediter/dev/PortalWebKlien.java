package net.krediter.dev;


import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.webkit.ValueCallback;
import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;

import net.krediter.dev.Utilitas;


public class PortalWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    private int konter_debug = 0;

    private int skor_misi = -1;
    private long waktu1 = 0;
    private long waktu2 = 0;
    private long waktu3 = 0;

    PortalWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        String path = url.getPath();
        if (path.endsWith("konfirmasi.php")) {
            android.util.Log.d("Krediter.WebViewPortal.Console", "KONFIRMASI skor_misi = " + skor_misi);
            android.util.Log.d("Krediter.WebViewPortal.Console", "KONFIRMASI waktu1 = " + waktu1);
            android.util.Log.d("Krediter.WebViewPortal.Console", "KONFIRMASI waktu2 = " + waktu2);
            android.util.Log.d("Krediter.WebViewPortal.Console", "KONFIRMASI waktu3 = " + waktu3);
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        String path = url.getPath();
        android.util.Log.d("Krediter.WebViewPortal.Console", "Memuat " + url);
        if (cekApakahUrlExternal(url)) {
            bukaUrlDiExternal(url.toString());
            return true;
        } else if (path.endsWith("iklan.php")) {
            waktu3 = System.currentTimeMillis();
            android.util.Log.d("Krediter.WebViewPortal.Console", "IKLAN " + waktu3);
        }
        return false;
    }

    private boolean cekApakahUrlExternal(Uri url) {
        if (mMainActivity.mUrlTargetPortal != null) {
            String host1 = getHostFromUrl(mMainActivity.mUrlTargetPortal);
            String host2 = url.getHost();
            if (host1 != null) {
                if (! host1.equals(host2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void bukaUrlDiExternal(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mMainActivity.startActivity(intent);
    }

    public static String getHostFromUrl(String urlString) {
        try {
            URI uri = new URI(urlString);
            return uri.getHost(); // Returns "www.example.com"
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @JavascriptInterface
    public void debug(String teks) {
        android.util.Log.d("Krediter.WebViewPortal.Console", "DEBUG: " + teks);
        konter_debug += 1;
        if (konter_debug == 1) {
            skor_misi = Integer.parseInt(teks);
        } else if (konter_debug == 2) {
            waktu1 = Long.parseLong(teks);
        } else if (konter_debug == 3) {
            // ...
        }else if (konter_debug == 4) {
            waktu2 = Long.parseLong(teks);
        }
    }

    @JavascriptInterface
    public String ll0(String plainJson) {
        String s = Utilitas.enAlp0(plainJson);
        android.util.Log.d("Krediter.WebViewPortal.Console", "enAlp0 => " + s);
        return s;
    }

    @JavascriptInterface
    public void buka(String url) {
        android.util.Log.d("Krediter.WebViewPortal.Console", "buka()");
        skor_misi = -1;
        waktu1 = 0;
        waktu2 = 0;
        waktu3 = 0;
        mMainActivity.bukaPortal(url);
    }

    @JavascriptInterface
    public void tutup() {
        android.util.Log.d("Krediter.WebViewPortal.Console", "tutup()");
        konter_debug = 0;
        mMainActivity.tutupPortal();
    }

    @JavascriptInterface
    public void misiBerhasil(String data) {
        android.util.Log.d("Krediter.WebViewPortal.Console", String.format("__portal.misiBerhasil('%s')", data));
        mMainActivity.runOnUiThread(() -> {
            mMainActivity.mIntegrator.mWebViewUtama.evaluateJavascript(
                String.format("__misi_berhasil('%s')", data),
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        mMainActivity.tutupPortal();
                    }
                }
            );
        });
    }

}
