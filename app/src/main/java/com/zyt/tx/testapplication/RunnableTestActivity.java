package com.zyt.tx.testapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunnableTestActivity extends AppCompatActivity {

    @BindView(R.id.btnTest)
    Button btnTest;
    @BindView(R.id.tvContent)
    TextView tvContent;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    int index = 0;
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            index++;
            tvContent.setText(index+"秒");
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runnable_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnTest)
    public void onClick() {
        boolean post = mHandler.post(mRunnable);
        Log.d("taxi", "push result ="+post);
    }
}
