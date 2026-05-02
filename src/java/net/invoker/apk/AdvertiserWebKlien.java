package net.invoker.apk;


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

    private void bukaUrlDiExternal(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mMainActivity.startActivity(intent);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if ((mMainActivity.mUrlTargetAdvertiser != null) && (!url.startsWith(mMainActivity.mUrlTargetAdvertiser))) {
            bukaUrlDiExternal(url);
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
