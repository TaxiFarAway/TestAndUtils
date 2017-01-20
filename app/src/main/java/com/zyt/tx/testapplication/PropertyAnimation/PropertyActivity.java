package com.zyt.tx.testapplication.PropertyAnimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zyt.tx.testapplication.R;

public class PropertyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        ImageView imageView = (ImageView) findViewById(R.id.ivContent);

        imageView.setOnClickListener(new rotationOnClick());
        imageView.setOnClickListener(new mixAnimation());
        imageView.setOnClickListener(new propertyValuseHolderListener());
    }


    class rotationOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ObjectAnimator.ofFloat(v, "rotationX", 60.0f, 360.f)
                    .setDuration(500)
                    .start();
        }
    }

    class mixAnimation implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "taxi", 1.0f, 0.0f);
            objectAnimator.start();
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    v.setAlpha(value);
                    v.setScaleX(value);
                    v.setScaleY(value);
                }
            });
        }
    }

    class propertyValuseHolderListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("alpha", 1.0f, .0f, 1f);
            PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat("scaleX", 1, 0, 1);
            PropertyValuesHolder pvhz = PropertyValuesHolder.ofFloat("scaleY", 1, 0, 1);

            ObjectAnimator.ofPropertyValuesHolder(v, pvhx, pvhy, pvhz).setDuration(1000).start();
        }
    }

}
