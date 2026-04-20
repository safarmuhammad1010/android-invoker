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

        String versiWebUnduhan = D.versiWeb();
        String versiWebApk = K.VERSI_WEB;

        if (versiWebUnduhan == null) {
            return _bukaDariApk(namaBerkas);
        }
        if (versiWebApk.equals(versiWebUnduhan)) {
            return _bukaDariApk(namaBerkas);
        }

        int derajatMayor = RepoLokal.derajatMayorVersiWebDiApkIni();
        int derajatMinor = RepoLokal.derajatMinorVersiWebDiApkIni();

        if (derajatMayor > 0) {
            return _bukaDariApk(namaBerkas);
        } else if (derajatMayor < 0) {
            return _bukaDariInternal(versiWebUnduhan, namaBerkas);
        } else {
            if (derajatMinor < 0) {
                return _bukaDariInternal(versiWebUnduhan, namaBerkas);
            } else {
                return _bukaDariApk(namaBerkas);
            }
        }
    }

    private InputStream _bukaDariApk(String namaBerkas) throws Exception {
        android.util.Log.d("Invoker.RepoLokal", "buka repo_lokal dari APK: " + namaBerkas);
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

    private InputStream _bukaDariInternal(String versiWebUnduhan, String namaBerkas) throws Exception {
        android.util.Log.d("Invoker.RepoLokal", "buka repo_lokal dari INTERNAL: " + namaBerkas);
        File folderRepo = new File(mMainActivity.getFilesDir(), K.NAMA_FOLDER_REPO_LOKAL);
        folderRepo = new File(folderRepo, versiWebUnduhan);
        File berkas = new File(folderRepo, namaBerkas);
        android.util.Log.d("Invoker.RepoLokal", "membuka berkas: " + berkas);
        InputStream input = new FileInputStream(berkas);
        input = new BufferedInputStream(input);
        return input;
    }

    static int derajatMayorVersiWebDiApkIni() {
        String versiWebUnduhan = D.versiWeb();
        String versiWebUnduhanMayor = versiWebUnduhan.substring(0, versiWebUnduhan.indexOf('.'));

        String versiWebApk = K.VERSI_WEB;
        String versiWebApkMayor = versiWebApk.substring(0, versiWebApk.indexOf('.'));

        try {
            int v1 = Integer.parseInt(versiWebApkMayor);
            int v2 = Integer.parseInt(versiWebUnduhanMayor);
            return v1 - v2;
        } catch (Exception e) {
            android.util.Log.e("Invoker.RepoLokal", "gagal cek versi mayor: " + e.getMessage());
            throw e;
        }
    }

    static int derajatMinorVersiWebDiApkIni() {
        String versiWebUnduhan = D.versiWeb();
        String versiWebUnduhanMinor = versiWebUnduhan.substring(versiWebUnduhan.indexOf('.')+1, versiWebUnduhan.length());

        String versiWebApk = K.VERSI_WEB;
        String versiWebApkMinor = versiWebApk.substring(versiWebApk.indexOf('.')+1, versiWebApk.length());

        try {
            int v1 = Integer.parseInt(versiWebApkMinor);
            int v2 = Integer.parseInt(versiWebUnduhanMinor);
            return v1 - v2;
        } catch (Exception e) {
            android.util.Log.e("Invoker.RepoLokal", "gagal cek versi minor: " + e.getMessage());
            throw e;
        }
    }

}
