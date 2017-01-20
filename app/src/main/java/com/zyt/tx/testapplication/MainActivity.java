package com.zyt.tx.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyt.tx.testapplication.PropertyAnimation.BasePropertyActivity;
import com.zyt.tx.testapplication.PropertyAnimation.PropertyActivity;
import com.zyt.tx.testapplication.SDKCamera.CameraActivity;
import com.zyt.tx.testapplication.schoolRecord.SchoolCameraActivity;
import com.zyt.tx.testapplication.schoolRecord.SchoolRecordActivity;
import com.zyt.tx.testapplication.videoRecord.MediaRecord2Activity;
import com.zyt.tx.testapplication.view.ViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_mediaplayer, R.id.btn_testRunnable, R.id.btnMediaPlayer
            , R.id.btnSchoolCamera, R.id.btnSchoolRecord, R.id.btnSDKCamera, R.id.btnView
            , R.id.btnProperty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mediaplayer:
                startActivity(new Intent(this, MediaPlayerTestActivity.class));
                break;

            case R.id.btn_testRunnable:
                startActivity(new Intent(this, RunnableTestActivity.class));
                break;

            case R.id.btnMediaPlayer:
                startActivity(new Intent(this, MediaRecord2Activity.class));
                break;

            case R.id.btnSchoolCamera:
                startActivity(new Intent(this, SchoolCameraActivity.class));
                break;

            case R.id.btnSchoolRecord:
                startActivity(new Intent(this, SchoolRecordActivity.class));
                break;

            case R.id.btnSDKCamera:
                startActivity(new Intent(this, CameraActivity.class));
                break;

            case R.id.btnView:
                startActivity(new Intent(this, ViewActivity.class));
                break;

            case R.id.btnProperty:
                startActivity(new Intent(this, BasePropertyActivity.class));
                break;
        }
    }
}
