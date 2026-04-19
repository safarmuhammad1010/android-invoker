package net.invoker.apk;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


class RepoLokal {

    MainActivity mMainActivity;

    RepoLokal(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    InputStream buka(String namaBerkas) throws Exception {
        android.util.Log.d("Invoker.RepoLokal", "buka repo_lokal: " + namaBerkas);

        String versiWebInternal = mMainActivity.mInisiator.mMetadataGlobal.getString(K.N_VERSI_WEB);
        if (K.VERSI_WEB.equals(versiWebInternal)) {
            return _bukaDariApk(namaBerkas);
        } else {
            return _bukaDariInternal(namaBerkas);
        }
    }

    private InputStream _bukaDariApk(String namaBerkas) throws Exception {
        android.util.Log.d("Invoker.RepoLokal", "buka repo_lokal dari apk: " + namaBerkas);
        int id;
        if ("index.html".equals(namaBerkas)) {
            id = R.raw.index;
        } else if ("konten.data".equals(namaBerkas)) {
            id = R.raw.konten;
        } else if ("latar.png".equals(namaBerkas)) {
            id = R.raw.latar;
        } else {
            throw new Exception("nama berkas invalid: " + namaBerkas);
        }
        InputStream input = mMainActivity.getResources().openRawResource(id);
        input = new BufferedInputStream(input);
        return input;
    }

    private InputStream _bukaDariInternal(String namaBerkas) throws Exception {
        android.util.Log.d("Invoker.RepoLokal", "buka repo_lokal dari internal: " + namaBerkas);
        File file = new File(mMainActivity.getFilesDir(), namaBerkas);
        InputStream input = new FileInputStream(file);
        input = new BufferedInputStream(input);
        return input;
    }

}
