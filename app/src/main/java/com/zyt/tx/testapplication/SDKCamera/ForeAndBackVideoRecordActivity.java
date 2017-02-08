package com.zyt.tx.testapplication.SDKCamera;

import android.Manifest;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class ForeAndBackVideoRecordActivity extends AppCompatActivity {

    private static final String TEST_PATH = "ZzTaxiDatas";

    @BindView(R.id.btnBackCamera)
    Button btnBackCamera;
    @BindView(R.id.btnForeCamera)
    Button btnForeCamera;
    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.foreSurfaceView)
    SurfaceView mForeSurfaceView;

    private Camera backCamera;

    private int backTime = 0;
    private Runnable mBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            btnBackCamera.setText(String.format("后置录制%s秒", backTime++));
            if (isBackRecording) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };


    private int foreTime = 0;
    private Runnable mForeTimeRunnable = new Runnable() {
        @Override
        public void run() {
            btnForeCamera.setText(String.format("前置录制%s秒", foreTime++));
            if (isForeRecording) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private boolean isBackRecording = false;
    private boolean isForeRecording = false;
    private MediaRecorder mBackMediaRecorder;
    private Handler mHandler;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private MediaRecorder mForeRecorder;
    private Camera foreCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_and_back_video_record);
        ButterKnife.bind(this);
        initPermission();
        mHandler = new Handler();
    }

    private void initPermission() {
        PermissionGen.with(this).addRequestCode(100)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @PermissionSuccess(requestCode = 100)
    public void onPRSuc() {

    }

    @PermissionFail(requestCode = 100)
    public void onPRFail() {
        finish();
    }

    @OnClick({R.id.btnBackCamera, R.id.btnForeCamera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackCamera:
//                initBackCamera();
                if (isBackRecording) {
                    // 停止录像
                    mBackMediaRecorder.stop();
//                    mMediaRecorder.stop();
                    releaseMediaRecord();
                    isBackRecording = false;
                    btnBackCamera.setText("后置摄像头录制");
                } else {
                    if (prepareBackCamera()/*prepareVideoRecord()*/) {
                        mBackMediaRecorder.start();
//                        mMediaRecorder.start();
                        //更新录制时间
                        isBackRecording = true;
                        mHandler.post(mBackTimeRunnable);
                    } else {
                        releaseMediaRecord();
                    }
                }

                break;
            case R.id.btnForeCamera:
                if (isForeRecording) {
                    mForeRecorder.stop();
                    releaseForeRecord();
                    btnForeCamera.setText("前置摄像头录制");
                } else {
                    if (prepareForeCamera()) {
                        mForeRecorder.start();
                        isForeRecording = true;
                        //开启计时任务
                        mHandler.post(mForeTimeRunnable);
                    } else {
                        releaseForeRecord();
                    }
                }
                break;
        }
    }

    private void releaseForeRecord() {
        isForeRecording = false;
        if (mForeRecorder != null) {
            mForeRecorder.reset();
            mForeRecorder.release();
            mForeRecorder = null;
            foreCamera.lock();
            foreCamera = null;

            foreTime = 0;
            mHandler.removeCallbacks(mForeTimeRunnable);
        }

    }

    private void releaseMediaRecord() {
        if (mBackMediaRecorder != null) {
            isBackRecording = false;
            mBackMediaRecorder.reset();
            mBackMediaRecorder.release();
            mBackMediaRecorder = null;
            backCamera.lock();
            backCamera = null;
        }
        backTime = 0;
        mHandler.removeCallbacks(mBackTimeRunnable);
    }


    private boolean prepareForeCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                foreCamera = Camera.open(i);
                mForeRecorder = new MediaRecorder();
                foreCamera.unlock();
                mForeRecorder.setCamera(foreCamera);

                mForeRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mForeRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                mForeRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

                mForeRecorder.setOutputFile(getForeVideoPath());
                //先用不同的surfaceview
                mForeRecorder.setPreviewDisplay(mForeSurfaceView.getHolder().getSurface());

                try {
                    mForeRecorder.prepare();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    private boolean prepareBackCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                mBackMediaRecorder = new MediaRecorder();
                /*录制mp4视频，非预览*/
                backCamera.unlock();
                mBackMediaRecorder.setCamera(backCamera);

                mBackMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mBackMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                mBackMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
                mBackMediaRecorder.setOutputFile(getVideoPath());
                /*这里不需要预览,必须要写，否则报错start failed -22*/
                /*伪造的抛出无效异常,使用1X1方案*/
                mBackMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
                try {
                    mBackMediaRecorder.prepare();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    releaseMediaRecord();
                    return false;
                }
            }
        }
        return false;
    }


    private String getVideoPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.CHINESE);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + TEST_PATH + File.separator + sdf.format(new Date()) + "_back.mp4";
        Log.d("taxi", "record path =" + path);
        return path;
    }


    private String getForeVideoPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + TEST_PATH + File.separator + sdf.format(new Date()) + "_fore.mp4";
        Log.d("taxi", "fore video path =" + path);
        return path;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaRecord();
    }


    private boolean prepareVideoRecord() {
        mMediaRecorder = new MediaRecorder();
        mCamera = getCameraInstance();
        //对camera进行设置

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        String path = Environment.getExternalStorageDirectory() + "/taxi.mp4";

//        Log.d("taxi", "video path=" + getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
//        Toast.makeText(this, "path=" + getOutputMediaFile(MEDIA_TYPE_VIDEO).toString(), Toast.LENGTH_LONG).show();
        mMediaRecorder.setOutputFile(getVideoPath());

        mMediaRecorder.setOrientationHint(90);
//        mMediaRecorder.setPreviewDisplay(mCameraView.getHolder().getSurface());
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
//            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();

            Log.d("taxi", "可用的camera的数量=" + Camera.getNumberOfCameras());
            Toast.makeText(ForeAndBackVideoRecordActivity.this, "可用的camera的数量=" + Camera.getNumberOfCameras(), Toast.LENGTH_LONG).show();
            c.setDisplayOrientation(90);
            //调整画面比例
            Camera.Parameters parameters = c.getParameters();
//            if (parameters.isZoomSupported()) {
//                parameters.setZoom(-1);
//                c.setParameters(parameters);
//                //seekBar控制
//            }
        } catch (Exception e) {
            //不存在或者正在使用中会抛异常  is not available.
            e.printStackTrace();
        }
        return c;
    }


}
