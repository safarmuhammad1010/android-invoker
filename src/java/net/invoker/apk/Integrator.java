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
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "login: nama='" + nama + "', kata_sandi='" + kata_sandi + "'");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.evaluateJavascript("__login('" + nama + "', '" + kata_sandi + "')", null);
            });
        }

        @JavascriptInterface
        public void bukaPeti() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "BUKA PETI");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.evaluateJavascript("__buka_peti()", null);
            });
        }

        @JavascriptInterface
        public void bukaKartu() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "BUKA KARTU");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.evaluateJavascript("__buka_kartu()", null);
            });
        }

        @JavascriptInterface
        public void ambilInsentifReferal() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceWeb", "AMBIL INSENTIF REFERAL");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewServis.evaluateJavascript("__ambil_insentif_referal()", null);
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
        public String dTahapApk() {
            return K.TAHAP_APK;
        }

        @JavascriptInterface
        public int dSdkInt() {
            return android.os.Build.VERSION.SDK_INT;
        }

        @JavascriptInterface
        public String dNamaPerangkat() {
            return mMainActivity.namaPerangkat();
        }

        @JavascriptInterface
        public void loadingSelesai() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "loading selesai");
            mMainActivity.mSudahSiap = true;
            mMainActivity.runOnUiThread(() -> {
                mMainActivity.mLoading.sembunyikan();
            });
        }

        @JavascriptInterface
        public void harusLogin() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "harusLogin");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__harus_login()", null);
                loadingSelesai();
            });
        }

        @JavascriptInterface
        public void gagalLogin(int kodeGagal, int overlimit) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "gagalLogin: kodeGagal=" + kodeGagal + ", overlimit=" + overlimit);
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__gagal_login(" + kodeGagal + "," + overlimit + ")", null);
            });
        }

        @JavascriptInterface
        public void berhasilLogin(String data) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "berhasilLogin: " + data);
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__berhasil_login(%s)", data),
                null);
            });
        }

        @JavascriptInterface
        public void harusSinkron() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "harusSinkron");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__harus_sinkron()", null);
                loadingSelesai();
            });
        }

        @JavascriptInterface
        public void sinkron(String data) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "sinkron: " + data);
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__sinkron(%s)", data),
                null);
                loadingSelesai();
            });
        }

        @JavascriptInterface
        public void tokenInvalid() {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", "tokenInvalid");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript("__harus_login(true)", null);
                loadingSelesai();
            });
        }

        @JavascriptInterface
        public void error(String pesan, String kausa) {
            android.util.Log.e("Invoker.Integrator.JsInterfaceServis", "error('" + kausa + "')");
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__error('%s', '%s')", pesan, kausa),
                null);
            });
        }

        @JavascriptInterface
        public void berhasilBukaPeti(int kredit) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", String.format("berhasilBukaPeti(%d)", kredit));
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__berhasilBukaPeti(%d)", kredit),
                null);
            });
        }

        @JavascriptInterface
        public void berhasilBukaKartu(String namaKartu, int kredit) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", String.format("berhasilBukaKartu('%s', %d)", namaKartu, kredit));
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__berhasilBukaKartu('%s', %d)", namaKartu, kredit),
                null);
            });
        }

        @JavascriptInterface
        public void berhasilAmbilInsentifReferal(int kredit) {
            android.util.Log.d("Invoker.Integrator.JsInterfaceServis", String.format("berhasilAmbilInsentifReferal(%d)", kredit));
            mMainActivity.runOnUiThread(() -> {
                mIntegrator.mWebViewUtama.evaluateJavascript(
                    String.format("__berhasilAmbilInsentifReferal('%s', %d)", kredit),
                null);
            });
        }

    }

}
