package com.zyt.tx.testapplication.schoolRecord;

import android.Manifest;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zyt.tx.testapplication.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class SchoolRecordActivity extends AppCompatActivity {

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.btnStartRecord)
    Button btnStartRecord;
    @BindView(R.id.btnStopRecord)
    Button btnStopRecord;
    private MediaRecorder mr;
    private SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_record);
        ButterKnife.bind(this);
        holder = surfaceView.getHolder();
        initPermission();
    }

    private void initPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .request();
    }


    @PermissionSuccess(requestCode = 100)
    public void onPermissionSuc() {
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                initRecorder();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void initRecorder() {
        try {
            mr = new MediaRecorder();
            mr.reset();
            Camera camera = Camera.open();
            camera.stopPreview();
            camera.unlock();
            mr.setCamera(camera);
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mr.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";
            mr.setOutputFile(path);
            mr.setVideoSize(400, 800);
            mr.setVideoFrameRate(24);
            mr.setPreviewDisplay(surfaceView.getHolder().getSurface());
            mr.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PermissionFail(requestCode = 100)
    public void onPermissionFail() {

    }

    @OnClick({R.id.btnStartRecord, R.id.btnStopRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartRecord:
//                mr.start();
                record();
                break;
            case R.id.btnStopRecord:
                mr.stop();
                mr.release();
                break;
        }
    }


    public void record() {
        //录制视频
        try {
            mr = new MediaRecorder();
            mr.reset();//重置
            //设置音频和视频来源
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            mr.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //设置输出格式
            mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //设置音频和视频的编码
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mr.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/coldcold.mp4";

            //输出路径
            mr.setOutputFile(path);
            //设置视频大小
            mr.setVideoSize(480, 800);
            //设置视频帧率
            mr.setVideoFrameRate(24);
            //设置预览
            mr.setPreviewDisplay(surfaceView.getHolder().getSurface());
            //准备
            mr.prepare();
            //开始
            mr.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
