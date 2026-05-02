package net.invoker.apk;


import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.net.Uri;


public class AdvertiserWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    AdvertiserWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
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

    private void bukaUrlDiExternal(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mMainActivity.startActivity(intent);
    }

    @JavascriptInterface
    public void tutupAdvertiser() {
        mMainActivity.tutupAdvertiser();
    }

}
