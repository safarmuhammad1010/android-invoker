package net.kogniter.dev;


import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.net.Uri;


public class BrowserWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    BrowserWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if ((mMainActivity.mUrlTargetBrowser != null) && (!url.startsWith(mMainActivity.mUrlTargetBrowser))) {
            mMainActivity.bukaUrlDiExternal(url);
            return true;
        }
        return false;
    }

    @JavascriptInterface
    public void buka(String url) {
        mMainActivity.bukaBrowser(url);
    }

    @JavascriptInterface
    public void tutup() {
        mMainActivity.tutupBrowser();
    }

}
