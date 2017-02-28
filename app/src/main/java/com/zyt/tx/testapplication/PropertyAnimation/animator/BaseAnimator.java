package com.zyt.tx.testapplication.PropertyAnimation.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by admin on 2017/2/7.
 */

public abstract class BaseAnimator {

    private ValueAnimator animator;

    protected View target;

    public BaseAnimator(final View target, float startValue, float endValue) {
        this.target = target;
        animator = ValueAnimator.ofFloat(startValue, endValue);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.d("taxi", "value=" + value);
                doAnimate(value);
            }
        });
    }

    abstract void doAnimate(float value);

    public void start(long duration) {
        animator.setDuration(duration);
        animator.start();
    }

    public void setListener(Animator.AnimatorListener listener) {
        animator.addListener(listener);
    }
}
