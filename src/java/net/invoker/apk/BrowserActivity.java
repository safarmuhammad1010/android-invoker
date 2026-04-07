package net.invoker.apk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;


public class BrowserActivity extends Activity {

    WebView mWebView;
    WebViewClient mWebClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebClient = new WebViewClient() {};
        mWebView.setWebViewClient(mWebClient);
        mWebView.addJavascriptInterface(new JsInterface(this), "__android");

        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        );
        startActivity(intent);
    }

    static class JsInterface {
        BrowserActivity mBrowserActivity;

        JsInterface(BrowserActivity browserActivity) {
            mBrowserActivity = browserActivity;
        }

        @JavascriptInterface
        public void keluar() {
            mBrowserActivity.finish();
        }
    }

}
