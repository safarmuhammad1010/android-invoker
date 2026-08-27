package net.kogniter.dev;


import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;


public class AdvertiserWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    AdvertiserWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if ((mMainActivity.mUrlTargetAdvertiser != null) && (!url.startsWith(mMainActivity.mUrlTargetAdvertiser))) {
            mMainActivity.bukaUrlDiExternal(url);
            return true;
        }
        return false;
    }

    @JavascriptInterface
    public void buka(String url) {
        mMainActivity.bukaAdvertiser(url);
    }

    @JavascriptInterface
    public void tutup() {
        mMainActivity.tutupAdvertiser();
    }

}
