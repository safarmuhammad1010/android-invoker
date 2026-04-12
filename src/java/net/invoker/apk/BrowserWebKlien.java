package net.invoker.apk;


import android.webkit.WebViewClient;


public class BrowserWebKlien extends WebViewClient {

    MainActivity mainActivity;

    BrowserWebKlien(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

}
