package com.zyt.tx.testapplication.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.zyt.tx.testapplication.R;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView = (ImageView) findViewById(R.id.ivContent);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

//                imageView.setX(event.getX());
//                imageView.setY(event.getY());

                imageView.getY();
                imageView.getTop();
                imageView.getTranslationY();
                imageView.setTranslationX(event.getX() - imageView.getWidth() / 2);
                imageView.setTranslationY(event.getY() - imageView.getHeight() / 2);
                break;

            case MotionEvent.ACTION_UP:

                break;


        }
        return super.onTouchEvent(event);
    }
}
