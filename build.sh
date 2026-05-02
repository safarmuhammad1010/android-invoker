
PACKAGE='net/invoker/apk'
NAMA_APK='invoker'


"$BUILD_TOOLS/aapt" package -f -m -J build/gen/ -S src/res \
    -M src/AndroidManifest.xml -I "$PLATFORM/android.jar" &&


javac --release 11 -classpath "$PLATFORM/android.jar" -d build/obj \
      build/gen/$PACKAGE/R.java \
      src/java/$PACKAGE/K.java \
      src/java/$PACKAGE/V.java \
      src/java/$PACKAGE/D.java \
      src/java/$PACKAGE/MainActivity.java \
      src/java/$PACKAGE/WebKlien.java \
      src/java/$PACKAGE/PortalWebKlien.java \
      src/java/$PACKAGE/BrowserWebKlien.java \
      src/java/$PACKAGE/AdvertiserWebKlien.java \
      src/java/$PACKAGE/Inisiator.java \
      src/java/$PACKAGE/Integrator.java \
      src/java/$PACKAGE/RepoLokal.java \
      src/java/$PACKAGE/Loading.java &&


"$BUILD_TOOLS/d8" --release --lib "$PLATFORM/android.jar" \
      --output build/apk/ build/obj/$PACKAGE/*.class &&


"$BUILD_TOOLS/aapt" package -f -M src/AndroidManifest.xml -S src/res/ \
      -I "$PLATFORM/android.jar" \
      -F build/$NAMA_APK.unsigned.apk build/apk/ &&


"$BUILD_TOOLS/zipalign" -f -p 4 \
      build/$NAMA_APK.unsigned.apk build/$NAMA_APK.aligned.apk &&


"$BUILD_TOOLS/apksigner" \
    sign \
    --ks keystore.jks \
    --ks-key-alias androidkey --ks-pass pass:android \
    --key-pass pass:android --out build/$NAMA_APK.apk \
    --v1-signing-enabled true \
    --v2-signing-enabled true \
    --v3-signing-enabled true \
    build/$NAMA_APK.aligned.apk

