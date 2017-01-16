package com.zyt.tx.testapplication.videoRecord;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MediaRecord2Activity extends AppCompatActivity {

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    private MediaRecorder mediaRecorder;

    private String[] requestPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record2);
        ButterKnife.bind(this);
        initRequestPermission();
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
    }


    private void initRequestPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(requestPermissions).request();
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionSuc() {
        Toast.makeText(this, "Suc", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void permissionFail() {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btnStartRecord, R.id.btnStopRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartRecord:
                try{
                    File videoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+ ".mp4");
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setVideoSize(320, 240);
                    mediaRecorder.setVideoFrameRate(5);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
                    mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnStopRecord:
                if(mediaRecorder!=null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                break;
        }
    }
}
