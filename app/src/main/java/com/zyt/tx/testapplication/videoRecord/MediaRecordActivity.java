package com.zyt.tx.testapplication.videoRecord;

import android.Manifest;
import android.hardware.Camera;
import android.media.AudioRecord;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MediaRecordActivity extends AppCompatActivity {

    private MediaRecorder mMediaRecord;
    private String[] requestPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);
        ButterKnife.bind(this);
        initRequestPermission();
    }

    private void initRequestPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(requestPermissions).request();
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionSuc() {
        Toast.makeText(this, "Suc", Toast.LENGTH_SHORT).show();
        initVideoRecord2();
    }

    @PermissionFail(requestCode = 100)
    public void permissionFail() {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btnStartRecord, R.id.btnPauseRecord, R.id.btnStopRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartRecord:
                startRecord();
                break;
            case R.id.btnPauseRecord:
                break;
            case R.id.btnStopRecord:
                stopRecord();
                break;
        }
    }

    private void stopRecord() {


    }

    private void startRecord() {
        Toast.makeText(this, (mMediaRecord == null) + "", Toast.LENGTH_LONG).show();
        if (mMediaRecord != null) {
            mMediaRecord.start();
        }
    }


    private void initVideoRecord2() {
        mMediaRecord = new MediaRecorder();
        mMediaRecord.reset();
        Camera camera = Camera.open();
        camera.unlock();
        mMediaRecord.setCamera(camera);
        mMediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecord.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mMediaRecord.setProfile(profile);
        mMediaRecord.setOutputFile(getVidoeFilePath(this));
        try {
            mMediaRecord.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initVideoRecord() {
        mMediaRecord = new MediaRecorder();
        mMediaRecord.reset();
        mMediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
        //这里还可以设置camera，surface,surface是api21以上才能设置
        mMediaRecord.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecord.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecord.setVideoEncodingBitRate(5 * 1024 * 1024);
        mMediaRecord.setVideoFrameRate(30);
        mMediaRecord.setVideoSize(1280, 980);
        mMediaRecord.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        String mNextVideoAbsolutePath = null;
        mNextVideoAbsolutePath = getVidoeFilePath(this);
        mMediaRecord.setOutputFile(mNextVideoAbsolutePath);
        //判断方向后再做更改
        int rotattion = getWindowManager().getDefaultDisplay().getOrientation();
        //...

        mMediaRecord.setOrientationHint(90);
        mMediaRecord.setCamera(Camera.open());
        try {
            mMediaRecord.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String TEST = "test";
    private String VIDEOPATH = "video";

    private String getVidoeFilePath(MediaRecordActivity mediaRecordActivity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            boolean isDir = false;
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("taxi", "rootPath=" + rootPath);
            String videoFilePath = rootPath + File.separator + TEST + File.separator + VIDEOPATH;
            File file = new File(videoFilePath);
            if (!file.exists()) {
                if (file.mkdir()) {
                    isDir = true;
                }
            }
            if (!isDir)
                return null;

            Log.d("taxi", "videopath=" + file.getAbsolutePath());
            String fileName = SystemClock.currentThreadTimeMillis() + ".mp4";
            File itemFile = new File(file, fileName);
            if (!itemFile.exists()) {
                try {
                    itemFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return itemFile.getAbsolutePath();

        }
        return null;
    }
}
