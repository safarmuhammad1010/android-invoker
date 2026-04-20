package net.invoker.apk;


import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;


class Integrator {

    MainActivity mMainActivity;

    WebView mWebViewUtama;
    WebView mWebViewServis;

    JsInterfaceWeb mJsInterfaceWeb;

    Integrator(MainActivity mainActivity, WebView webViewUtama) {
        mMainActivity = mainActivity;
        mWebViewUtama = webViewUtama;
        mJsInterfaceWeb = new JsInterfaceWeb(mMainActivity, this);
        initWebViewServis();
    }

    private void initWebViewServis() {
        mWebViewServis = (WebView) mMainActivity.findViewById(R.id.webview_servis);

        mWebViewServis.setWebViewClient(new WebViewClientServis(this));
        mWebViewServis.setWebChromeClient(new WebChromeClientServis(this));
        mWebViewServis.addJavascriptInterface(new JsInterfaceServis(mMainActivity, this), "__integrator");

        WebView.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = mWebViewServis.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }

    static class WebViewClientServis extends WebViewClient {

        Integrator mIntegrator;

        WebViewClientServis(Integrator integrator) {
            mIntegrator = integrator;
        }

    }

    static class WebChromeClientServis extends WebChromeClient {

        Integrator mIntegrator;

        WebChromeClientServis(Integrator integrator) {
            mIntegrator = integrator;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            android.util.Log.d("Invoker.WebViewServis.Console", consoleMessage.message());
            return true;
        }

    }

    static class JsInterfaceWeb {

        static final String K_URL_SERVIS = "url_servis";

        MainActivity mMainActivity;
        Integrator mIntegrator;

        JsInterfaceWeb(MainActivity mainActivity, Integrator integrator) {
            mMainActivity = mainActivity;
            mIntegrator = integrator;
        }

        @JavascriptInterface
        public void mulai() {
            final String urlServis;
            try {
                urlServis = mMainActivity.mInisiator.mMetadataGlobal.getString(K_URL_SERVIS);
            } catch (Exception e) {
                android.util.Log.e("Invoker.Integrator.JsInterfaceWeb", e.getMessage());
                return;
            }
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "mulai integrasi, dengan url_servis='" + urlServis + "'");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.loadUrl(urlServis);
            });
        }

        @JavascriptInterface
        public void login(String nama, String kata_sandi) {
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.evaluateJavascript("__login('" + nama + "', '" + kata_sandi + "')", null);
            });
        }

    }

    static class JsInterfaceServis {

        MainActivity mMainActivity;
        Integrator mIntegrator;

        JsInterfaceServis(MainActivity mainActivity, Integrator integrator) {
            mMainActivity = mainActivity;
            mIntegrator = integrator;
        }

        @JavascriptInterface
        public void login() {
            android.util.Log.e("Invoker.Integrator.JsInterfaceServis", "meminta login...");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__harus_login()", null);
            });
        }

        @JavascriptInterface
        public void sinkron(String data) {
            android.util.Log.e("Invoker.Integrator.JsInterfaceServis", "sinkron: " + data);
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__sinkron(" + data + ")", null);
            });
        }

    }

}
