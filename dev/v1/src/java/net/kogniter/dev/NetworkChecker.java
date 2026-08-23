package net.kogniter.dev;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;


public final class NetworkChecker {

    /**
     * Mengecek apakah Private DNS sedang aktif.
     *
     * Android 9 (API 28) atau lebih baru.
     */
    public static boolean isPrivateDnsActive(Context context) {

        // Private DNS API tersedia mulai Android 9 / API 28
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return false;
        }

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        Network network = cm.getActiveNetwork();

        if (network == null) {
            return false;
        }

        LinkProperties linkProperties =
                cm.getLinkProperties(network);

        if (linkProperties == null) {
            return false;
        }

        return linkProperties.isPrivateDnsActive();
    }


    /**
     * Mendapatkan hostname Private DNS jika menggunakan
     * mode Strict.
     *
     * Return:
     * null       = tidak tersedia / bukan Strict
     * hostname   = hostname Private DNS
     */
    public static String getPrivateDnsHostname(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return null;
        }

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return null;
        }

        Network network = cm.getActiveNetwork();

        if (network == null) {
            return null;
        }

        LinkProperties linkProperties =
                cm.getLinkProperties(network);

        if (linkProperties == null) {
            return null;
        }

        if (!linkProperties.isPrivateDnsActive()) {
            return null;
        }

        return linkProperties.getPrivateDnsServerName();
    }


    /**
     * Mengembalikan informasi Private DNS.
     *
     * Return:
     *
     * unsupported
     * no_network
     * unknown
     * off
     * opportunistic
     * strict:hostname
     */
    public static String getPrivateDnsInfo(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return "unsupported";
        }

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return "unknown";
        }

        Network network = cm.getActiveNetwork();

        if (network == null) {
            return "no_network";
        }

        LinkProperties linkProperties =
                cm.getLinkProperties(network);

        if (linkProperties == null) {
            return "unknown";
        }

        if (!linkProperties.isPrivateDnsActive()) {
            return "off";
        }

        String hostname =
                linkProperties.getPrivateDnsServerName();

        if (hostname != null) {
            return "strict:" + hostname;
        }

        return "opportunistic";
    }


    /**
     * Mengecek apakah koneksi aktif menggunakan VPN.
     *
     * Membutuhkan Android 6.0 / API 23 atau lebih baru.
     */
    public static boolean isVpnActive(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        Network network = cm.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                cm.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_VPN);
    }


    /**
     * Mengecek apakah ada koneksi network.
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return false;
        }

        Network network = cm.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                cm.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
