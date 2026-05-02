package net.invoker.apk;


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

    private void bukaUrlDiExternal(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mMainActivity.startActivity(intent);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if ((mMainActivity.mUrlTargetBrowser != null) && (!url.startsWith(mMainActivity.mUrlTargetBrowser))) {
            bukaUrlDiExternal(url);
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
