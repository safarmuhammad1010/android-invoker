package net.invoker.apk;


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


public class PortalWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    PortalWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        if (cekApakahUrlExternal(url)) {
            bukaUrlDiExternal(url.toString());
            return true;
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
    public void buka(String url) {
        mMainActivity.bukaPortal(url);
    }

    @JavascriptInterface
    public void tutup() {
        mMainActivity.tutupPortal();
    }

    @JavascriptInterface
    public void misiBerhasil(String data) {
        android.util.Log.d("Invoker.WebViewPortal.Console", String.format("__portal.misiBerhasil('%s')", data));
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
