package net.invoker.apk;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


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

        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
    }

}
