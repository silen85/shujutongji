package com.lesso.data.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/6/16.
 */
public class RoatAnimate extends Animator {

    private Context context;
    private View layout;

    public RoatAnimate(Context context) {
        this.context = context;
    }

    @Override
    public void init(View layout) {
        // TODO Auto-generated method stub
        this.layout = layout;
    }

    @Override
    public void start(final AnimatorCallback callback) {
        // TODO Auto-generated method stub

        Animation anim = AnimationUtils.loadAnimation(context,
                R.anim.roat);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.callback();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(anim);

    }

    @Override
    public void dismiss(final AnimatorCallback callback) {
        // TODO Auto-generated method stub
    }


}