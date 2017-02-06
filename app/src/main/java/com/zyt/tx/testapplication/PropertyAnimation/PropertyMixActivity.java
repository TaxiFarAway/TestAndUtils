package com.zyt.tx.testapplication.PropertyAnimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zyt.tx.testapplication.R;

public class PropertyMixActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_mix);

        imageView = (ImageView) findViewById(R.id.ivContent);
        findViewById(R.id.btnVerticalRun).setOnClickListener(this);
        findViewById(R.id.btnPaowuxian).setOnClickListener(this);

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWidth = manager.getDefaultDisplay().getWidth();
        mHeight = manager.getDefaultDisplay().getHeight();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnVerticalRun:
                ValueAnimator animator = ValueAnimator.ofFloat(0, mHeight - imageView.getHeight());
                animator.setTarget(imageView);
                animator.setDuration(1000).start();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        imageView.setTranslationY((Float) animation.getAnimatedValue());
                    }
                });
                break;

            case R.id.btnPaowuxian:
                ValueAnimator animator1 = new ValueAnimator();
                animator1.setDuration(3000);
                animator1.setObjectValues(new PointF(0, 0));
                animator1.setInterpolator(new LinearInterpolator());
                animator1.setEvaluator(new TypeEvaluator<PointF>() {
                    @Override
                    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

                        PointF point = new PointF();
                        point.x = 200 * fraction * 3;
                        point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                        return point;
                    }
                });
                animator1.start();
                animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF) animation.getAnimatedValue();
                        imageView.setTranslationX(pointF.x);
                        imageView.setTranslationY(pointF.y);
                    }
                });

                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewGroup viewGroup = (ViewGroup) imageView.getParent();
                        if (viewGroup != null) {
                            viewGroup.removeView(imageView);
                        }
                    }
                });
                break;
        }
    }
}
