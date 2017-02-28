package com.zyt.tx.testapplication.PropertyAnimation.animator;

import android.view.View;

/**
 * Created by admin on 2017/2/7.
 */

public class FlyAnimator extends BaseAnimator {


    public FlyAnimator(View target, float startValue, float endValue) {
        super(target, startValue, endValue);
    }
    @Override
    void doAnimate(float value) {
        target.setTranslationY(value);

        //
    }
}
