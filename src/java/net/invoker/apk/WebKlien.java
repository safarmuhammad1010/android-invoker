package net.invoker.apk;


import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import java.io.File;
import java.io.FileInputStream;


public class WebKlien extends WebViewClient {

    static final String FOLDER_LOKAL = "/invoker/berkas_lokal/";

    MainActivity mMainActivity;

    WebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (! url.startsWith("https://app.local" + FOLDER_LOKAL)) {
            mMainActivity.bukaDiBrowser(url);
            return true;
        }
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        String path = request.getUrl().getPath();
        android.util.Log.d("Invoker.WebKlien", "mengunduh: " + path);

        if (url.startsWith("https://app.local" + FOLDER_LOKAL)) {
            try {
                String berkas;
                // Akali cache <img>
                if (path.startsWith(FOLDER_LOKAL + "foto_profil/")) {
                    berkas = "foto_profil";
                } else {
                    String[] ss = url.split(FOLDER_LOKAL);
                    berkas = ss[1];
                }

                String mime = WebKlien.getMimeType(path);
                android.util.Log.d("Invoker.WebKlien", "permintaan diintersepsi: " + mime);

                File file = new File(mMainActivity.getFilesDir(), berkas);
                FileInputStream input = new FileInputStream(file);
                WebResourceResponse respon = new WebResourceResponse(mime, "UTF-8", input);

                return respon;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.shouldInterceptRequest(view, request);
    }

    static String getMimeType(String filename) {
        if (filename.endsWith(".png")) return "image/png";
        else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        else if (filename.endsWith(".json")) return "application/json";
        else if (filename.endsWith(".txt")) return "text/plain";
        else if (filename.endsWith(".html")) return "text/html";
        else return "application/octet-stream";
    }

}
