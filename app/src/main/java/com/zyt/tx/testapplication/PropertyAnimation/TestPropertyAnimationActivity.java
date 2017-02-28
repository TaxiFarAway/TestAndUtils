package com.zyt.tx.testapplication.PropertyAnimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.zyt.tx.testapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestPropertyAnimationActivity extends AppCompatActivity {

    @BindView(R.id.ivContent)
    ImageView ivContent;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_property_animation);
        ButterKnife.bind(this);

        mWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    @OnClick(R.id.btnMove)
    public void onClick() {
//        moveImageView();
        moveObjectAnimation();

    }

    private void moveObjectAnimation() {
        ObjectAnimator.ofFloat(ivContent, "rotationX", 60.0f, 270.0f)
                .setDuration(2000).start();
    }

    private void moveImageView() {
        ValueAnimator animation = ValueAnimator.ofFloat(0.0f, mWidth - ivContent.getWidth());
        animation.setDuration(3000);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float change = (float) animation.getAnimatedValue();
                Log.d("taxi", "animation value=" + change);
//                ivContent.setAlpha(1-change);
                ivContent.setTranslationX(change);
            }
        });

    }
}
