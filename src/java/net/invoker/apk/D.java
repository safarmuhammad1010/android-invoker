package net.invoker.apk;


import android.content.Context;
import android.content.SharedPreferences;


class D {

    static String versiWeb() {
        SharedPreferences pref = MainActivity.instance.getSharedPreferences(K.NAMA_PREF_METADATA, Context.MODE_PRIVATE);
        String versiWebUnduhan = pref.getString(K.N_VERSI_WEB, null);
        return versiWebUnduhan;
    }

    static String versiWeb(String x) {
        SharedPreferences pref = MainActivity.instance.getSharedPreferences(K.NAMA_PREF_METADATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(K.N_VERSI_WEB, x);
        editor.apply();
        return x;
    }

}
