package net.invoker.apk;


import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.net.Uri;


public class PortalWebKlien extends WebViewClient {

    MainActivity mMainActivity;

    PortalWebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if ((mMainActivity.mUrlTargetPortal != null) && (!url.startsWith(mMainActivity.mUrlTargetPortal))) {
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
    public void buka(String url) {
        mMainActivity.bukaPortal(url);
    }

    @JavascriptInterface
    public void tutup() {
        mMainActivity.tutupPortal();
    }

}
