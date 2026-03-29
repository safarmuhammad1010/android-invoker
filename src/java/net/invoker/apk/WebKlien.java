package net.invoker.apk;


import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import java.io.File;
import java.io.FileInputStream;

public class WebKlien extends WebViewClient {

    MainActivity mainActivity;

    WebKlien(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String path = request.getUrl().getPath();
        android.util.Log.d("INVOKER", "mengunduh: " + path);

        if (path != null && path.startsWith("/invoker/berkas_lokal/")) {
            try {
                String filename = request.getUrl().getLastPathSegment();
                String mime = WebKlien.getMimeType(filename);
                android.util.Log.d("INVOKER", "permintaan diintersepsi: " + mime);
                // Akali cache <img>
                if (path.startsWith("/invoker/berkas_lokal/foto_profil/")) {
                    filename = "foto_profil";
                }
                File file = new File(this.mainActivity.getFilesDir(), filename);
                FileInputStream input = new FileInputStream(file);
                return new WebResourceResponse(mime, "UTF-8", input);
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
