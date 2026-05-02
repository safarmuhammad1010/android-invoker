package net.invoker.apk;


import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class WebKlien extends WebViewClient {

    MainActivity mMainActivity;
    RepoLokal mRepoLokal;

    WebKlien(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mRepoLokal = new RepoLokal(mMainActivity);
    }

    private void bukaUrlDiExternal(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mMainActivity.startActivity(intent);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (! url.startsWith(K.URL_REPO_LOKAL)) {
            bukaUrlDiExternal(url);
            return true;
        }
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        String path = request.getUrl().getPath();
        android.util.Log.d("Invoker.WebKlien", "mengunduh: " + path);

        if (url.startsWith(K.URL_FOLDER_LOKAL)) {
            try {
                String mime = WebKlien.getMimeType(path);

                if (url.startsWith(K.URL_REPO_LOKAL)){
                    String[] ss = path.split("/");
                    String namaBerkas = ss[ss.length-1];
                    try {
                        InputStream input = mRepoLokal.buka(namaBerkas);
                        return new WebResourceResponse(mime, "UTF-8", input);
                    } catch (Exception e) {
                        android.util.Log.e("Invoker.WebKlien", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

                // Akali cache <img>
                if (url.startsWith(K.URL_FOLDER_LOKAL + "foto_profil/")) {
                    String berkas = "foto_profil";
                    android.util.Log.d("Invoker.WebKlien", "permintaan diintersepsi: " + mime);
                    File file = new File(mMainActivity.getFilesDir(), berkas);
                    FileInputStream input = new FileInputStream(file);
                    return new WebResourceResponse(mime, "UTF-8", input);
                }

                return super.shouldInterceptRequest(view, request);
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
