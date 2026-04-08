package net.invoker.apk;


import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.view.animation.LinearInterpolator;


class Loading {

    MainActivity mMainActivity;

    ObjectAnimator mAnimator1;
    ObjectAnimator mAnimator2;


    Loading(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mAnimator1 = buatAnimator1();
    }

    ObjectAnimator buatAnimator1() {
        ImageView gambar1 = mMainActivity.findViewById(R.id.ikon_loading_1);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(gambar1, "rotation", 0f, 360f);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.setDuration(1000 * 10);
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        return animator1;
    }

    void mulai() {
        mAnimator1.start();
    }

    void stop() {
        mAnimator1.cancel();
    }

}
