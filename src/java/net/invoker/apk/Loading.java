package net.invoker.apk;


import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.view.animation.LinearInterpolator;
import android.view.View;


class Loading {

    MainActivity mMainActivity;

    View mView;

    ObjectAnimator mAnimator1;
    ObjectAnimator mAnimator2;
    ObjectAnimator mAnimator3;
    ObjectAnimator mAnimator4;

    Loading(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mView = mMainActivity.findViewById(R.id.loading);
        mAnimator1 = buatAnimator1();
        mAnimator2 = buatAnimator2();
        mAnimator3 = buatAnimator3();
        mAnimator4 = buatAnimator4();
    }

    ObjectAnimator buatAnimator1() {
        ImageView gambar1 = mMainActivity.findViewById(R.id.ikon_loading_1);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(gambar1, "rotation", 0f, 360f);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.setDuration(1000 * 2);
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        return animator1;
    }

    ObjectAnimator buatAnimator2() {
        ImageView gambar2 = mMainActivity.findViewById(R.id.ikon_loading_2);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(gambar2, "rotation", 360f, 0f);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setDuration(1000 * 2);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);
        return animator2;
    }

    ObjectAnimator buatAnimator3() {
        ImageView gambar3 = mMainActivity.findViewById(R.id.ikon_loading_3);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(gambar3, "rotation", 0f, 360f);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setDuration(1000 * 5);
        animator3.setRepeatCount(ObjectAnimator.INFINITE);
        return animator3;
    }

    ObjectAnimator buatAnimator4() {
        ImageView gambar4 = mMainActivity.findViewById(R.id.ikon_loading_4);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(gambar4, "rotation", 0f, 360f);
        animator4.setInterpolator(new LinearInterpolator());
        animator4.setDuration(1000 * 15);
        animator4.setRepeatCount(ObjectAnimator.INFINITE);
        return animator4;
    }

    void mulai() {
        mAnimator1.start();
        mAnimator2.start();
        mAnimator3.start();
        mAnimator4.start();
    }

    void stop() {
        mAnimator1.cancel();
        mAnimator2.cancel();
        mAnimator3.cancel();
        mAnimator4.cancel();
    }

    void sembunyikan() {
        mMainActivity.runOnUiThread(() -> {
            mView.setVisibility(View.GONE);
        });
    }

}
