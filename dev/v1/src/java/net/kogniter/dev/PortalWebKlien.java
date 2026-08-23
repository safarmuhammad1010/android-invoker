package net.kogniter.dev;


import android.graphics.Bitmap;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.webkit.ValueCallback;
import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import net.kogniter.dev.Utilitas;


public class PortalWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    private boolean vi1 = false;

    private boolean isShowingError = false;
    private String lastGoodUrl = null;

    private int konter_debug = 0;

    private String has_misi_aktif = "NIHIL";
    private int skor_misi = -1;
    private long waktu1 = 0;
    private long waktu2 = 0;
    private long waktu3 = 0;

    PortalWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    private String kkkkk() {
        long t = System.currentTimeMillis();
        String teks_data = Long.toString(t);

        teks_data = Utilitas.enAlp0(teks_data);

        try {
            teks_data = java.net.URLEncoder.encode(teks_data, "UTF-8");
        } catch (Exception e) {
            android.util.Log.e("Kogniter.WebViewPortal.Console", "ERROR: " + e.getMessage());
            return "";
        }

        return teks_data;
    }

    private boolean qqqqq(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        android.util.Log.d("Kogniter.WebViewPortal.Console", "KONFIRMASI skor_misi = " + skor_misi);
        android.util.Log.d("Kogniter.WebViewPortal.Console", "KONFIRMASI waktu1 = " + waktu1);
        android.util.Log.d("Kogniter.WebViewPortal.Console", "KONFIRMASI waktu2 = " + waktu2);
        android.util.Log.d("Kogniter.WebViewPortal.Console", "KONFIRMASI waktu3 = " + waktu3);
        String teks_data;
        try {
            JSONObject obj = new JSONObject();
            obj.put("waktu1", waktu1);
            obj.put("waktu2", waktu2);
            obj.put("waktu3", waktu3);
            obj.put("skor_misi", skor_misi);
            String teks_json = obj.toString();
            android.util.Log.d("Kogniter.WebViewPortal.Console", "plainJson = " + teks_json);
            teks_data = Utilitas.enAlp0(teks_json);
        } catch (JSONException e) {
            android.util.Log.e("Kogniter.WebViewPortal.Console", "ERROR: " + e.getMessage());
            return false;
        }

        try {
            teks_data = java.net.URLEncoder.encode(teks_data, "UTF-8");
        } catch (Exception e) {
            android.util.Log.e("Kogniter.WebViewPortal.Console", "ERROR: " + e.getMessage());
            return false;
        }

        Uri urlBaru = url.buildUpon()
                    .appendQueryParameter("analytic_kogniter", teks_data)
                    .build();
        view.loadUrl(urlBaru.toString());

        android.util.Log.d("Kogniter.WebViewPortal.Console", "URL IKLAN BARU => " + urlBaru.toString());

        return true;
    }

    private boolean iiiii(WebView view, WebResourceRequest request) {
        if (! vi1) {
            android.util.Log.d("Kogniter.WebViewPortal.Console", "VI1 INVALID");
            return false;
        }

        Uri url = request.getUrl();
        long t = System.currentTimeMillis();

        String teks_data = Long.toString(t);
        teks_data = Utilitas.enAlp0(teks_data);
        try {
            teks_data = java.net.URLEncoder.encode(teks_data, "UTF-8");
        } catch (Exception e) {
            android.util.Log.e("Kogniter.WebViewPortal.Console", "ERROR: " + e.getMessage());
            return false;
        }

        Uri urlBaru = url.buildUpon()
                    .appendQueryParameter("i1", teks_data)
                    .build();
        view.loadUrl(urlBaru.toString());

        android.util.Log.d("Kogniter.WebViewPortal.Console", "URL INDEX BARU => " + urlBaru.toString());

        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        String path = url.getPath();
        android.util.Log.d("Kogniter.WebViewPortal.Console", "Memuat " + url);
        if (cekApakahUrlExternal(url)) {
            bukaUrlDiExternal(url.toString());
            return true;
        } else if (path.endsWith("iklan.php")) {
            waktu3 = System.currentTimeMillis();
            android.util.Log.d("Kogniter.WebViewPortal.Console", "IKLAN " + waktu3);
            return qqqqq(view, request);
        } else if (path.endsWith("index.php")) {
            android.util.Log.d("Kogniter.WebViewPortal.Console", "INDEX: " + url);
            return iiiii(view, request);
        }
        return false;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) {
            showErrorPage("network", error.getErrorCode(), error.getDescription().toString());
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (request.isForMainFrame()) {
            String reason = errorResponse.getReasonPhrase();
            showErrorPage("http", errorResponse.getStatusCode(), reason != null ? reason : "");
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        android.util.Log.d("Kogniter.WebViewPortal.Console", "halaman dimulai: " + url);
        isShowingError = url.startsWith("file:///android_asset/gagal_memuat.html");
        if (!isShowingError) {
            lastGoodUrl = url;
        }
    }

    private void showErrorPage(String type, int code, String message) {
        String url = "file:///android_asset/gagal_memuat.html?type=" + type
                + "&code=" + code
                + "&msg=" + Uri.encode(message);
        mMainActivity.mPortal.loadUrl(url);
    }

    private boolean cekApakahUrlExternal(Uri url) {
        if (url.toString().startsWith("file:///android_asset/")) {
            return false;
        }
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
        android.util.Log.d("Kogniter.WebViewPortal.Console", "DEBUG[" + konter_debug + "] " + teks);
        konter_debug += 1;
        if (konter_debug == 1) {
            waktu1 = Long.parseLong(teks);
        } else if (konter_debug == 2) {
            String s = teks.split(": ", 2)[1];
            skor_misi = Integer.parseInt(s);
        } else if (konter_debug == 3) {
            // ...
        } else if (konter_debug == 4) {
            waktu2 = Long.parseLong(teks);
        } else if (konter_debug == 5) {
            String s = teks.split(": ", 2)[1];
            has_misi_aktif = s;
        }
    }

    @JavascriptInterface
    public String ll0(String plainJson) {
        String s = Utilitas.enAlp0(plainJson);
        android.util.Log.d("Kogniter.WebViewPortal.Console", "enAlp0 => " + s);
        return s;
    }

    public void i1() {
        android.util.Log.d("Kogniter.WebViewPortal.Console", "i1 VALID");
        vi1 = true;
    }

    @JavascriptInterface
    public void i2() {
        String k = kkkkk();
        android.util.Log.d("Kogniter.WebViewPortal.Console", "i2 VALID: " + k);
        mMainActivity.runOnUiThread(() -> {
            mMainActivity.mPortal.evaluateJavascript(
                String.format("window.URL_KONFIRMASI += '&i2_kogniter=%s'", k),
            null);
        });
    }

    @JavascriptInterface
    public void i3() {
        String k = kkkkk();
        android.util.Log.d("Kogniter.WebViewPortal.Console", "i3 VALID: " + k);
        mMainActivity.runOnUiThread(() -> {
            mMainActivity.mPortal.evaluateJavascript(
                String.format("window.URL_KONFIRMASI += '&i3_kogniter=%s'", k),
            null);
        });
    }

    @JavascriptInterface
    public void i4() {
        String k = kkkkk();
        android.util.Log.d("Kogniter.WebViewPortal.Console", "i4 VALID: " + k);
        mMainActivity.runOnUiThread(() -> {
            mMainActivity.mPortal.evaluateJavascript(
                String.format("window.URL_KONFIRMASI += '&i4_kogniter=%s'", k),
            null);
        });
    }

    @JavascriptInterface
    public void buka(String url) {
        android.util.Log.d("Kogniter.WebViewPortal.Console", "buka()");
        konter_debug = 0;
        skor_misi = -1;
        waktu1 = 0;
        waktu2 = 0;
        waktu3 = 0;
        lastGoodUrl = url;
        mMainActivity.bukaPortal(url);
    }

    @JavascriptInterface
    public void tutup() {
        android.util.Log.d("Kogniter.WebViewPortal.Console", "tutup()");
        mMainActivity.tutupPortal();
    }

    @JavascriptInterface
    public void muatUlang() {
        android.util.Log.d("Kogniter.WebViewPortal.Console", "muatUlang()");
        if (lastGoodUrl != null) {
            android.util.Log.d("Kogniter.WebViewPortal.Console", "memuat ulang " + lastGoodUrl);
            mMainActivity.runOnUiThread(() -> {
                mMainActivity.mPortal.loadUrl(lastGoodUrl);
            });
        }
    }

    @JavascriptInterface
    public void misiBerhasil(String data) {
        android.util.Log.d("Kogniter.WebViewPortal.Console", String.format("__portal.misiBerhasil('%s')", data));
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
