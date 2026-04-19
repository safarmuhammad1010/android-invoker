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

    Integrator(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mJsInterfaceWeb = new JsInterfaceWeb(mMainActivity, this);
        initWebViewServis();
    }

    private void initWebViewServis() {
        mWebViewServis = (WebView) mMainActivity.findViewById(R.id.webview_servis);

        mWebViewServis.setWebViewClient(new WebViewClientServis(this));
        mWebViewServis.setWebChromeClient(new WebChromeClientServis(this));

        WebView.setWebContentsDebuggingEnabled(true);
        mWebViewServis.addJavascriptInterface(new JsInterfaceServis(this), "__integrator");

        WebSettings webSettings = mWebViewServis.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "mulai integrasi, dengan url servis: " + urlServis);
            if (urlServis != null) {
                mMainActivity.runOnUiThread(() -> {
                    mIntegrator.mWebViewServis.loadUrl(urlServis);
                });
            }
        }

    }

    static class JsInterfaceServis {

        Integrator mIntegrator;

        JsInterfaceServis(Integrator integrator) {
            mIntegrator = integrator;
        }

    }

}
