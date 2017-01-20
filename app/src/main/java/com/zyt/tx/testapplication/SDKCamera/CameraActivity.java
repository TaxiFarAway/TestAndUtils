package com.zyt.tx.testapplication.SDKCamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.btnCapturePic)
    Button btnCapturePic;
    @BindView(R.id.btnCaptureVideo)
    Button btnCaptureVideo;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.seekBar)
    SeekBar seekBar;

    private Camera mCamera;
    private CameraView mCameraView;
    //    private CameraZoomView mCameraView;
    private MediaRecorder mMediaRecorder;

    private boolean isCameraOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        initPermission();
    }


    //不请求权限的话会得不到camera，报null
    private void initPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }


    @PermissionSuccess(requestCode = 100)
    public void onPermissionSuc() {
        work();
    }

    @PermissionFail(requestCode = 100)
    public void onPermissionFail() {
        //弹窗提示
        finish();
    }

    private void work() {
        mCamera = getCameraInstance();
        mCameraView = new CameraView(this, mCamera);
//        mCameraView = new CameraZoomView(this, mCasmera);
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.addView(mCameraView);

        initSeekBar();
    }

    private void initSeekBar() {
        Camera.Parameters parameters = mCamera.getParameters();
        if (!parameters.isZoomSupported()) {
            return;
        }
        seekBar.setMax(parameters.getMaxZoom());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mCamera != null) {
                    Camera.Parameters p = mCamera.getParameters();
                    if (p.isZoomSupported()) {
                        p.setZoom(progress);
                        mCamera.setParameters(p);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isCameraOpen && mCamera == null) {
            mCamera = getCameraInstance();
//            mCameraView.preView(mCamera);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            c.setDisplayOrientation(90);
            //调整画面比例
            Camera.Parameters parameters = c.getParameters();
            if (parameters.isZoomSupported()) {
                parameters.setZoom(-1);
                c.setParameters(parameters);
                //seekBar控制
            }

            isCameraOpen = true;
        } catch (Exception e) {
            //不存在或者正在使用中会抛异常  is not available.
            e.printStackTrace();
        }
        return c;
    }


    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    @OnClick({R.id.btnCapturePic, R.id.btnCaptureVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCapturePic:
                mCamera.takePicture(null, null, mPicture);
                break;
            case R.id.btnCaptureVideo:
                beginVideoRecord();
                break;
        }
    }


    private boolean isRecording = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    private int curSeconds = 0;

    Runnable updateRuannable = new Runnable() {
        @Override
        @TargetApi(18)
        public void run() {
            //如果在录制中，就没隔一段时间设置一次。
            tvTime.setText(String.format(Locale.CHINESE, "录制：%s秒", ++curSeconds));

            String available = PhoneInfoUtils.getSDCardAvailableSize(getApplicationContext());
            String totalSize = PhoneInfoUtils.getSDCardTotalSize(getApplicationContext());
            tvRoom.setText(String.format(Locale.CHINESE, "可用容量/总容量:%s/%s", available, totalSize));
            if (isRecording) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };


    private void beginVideoRecord() {
        if (isRecording) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();

            isRecording = false;
            btnCaptureVideo.setText("capture video");
        } else {
            if (prepareVideoRecord()) {
                mMediaRecorder.start();
                btnCaptureVideo.setText("stop");
                //这里开启计时任务
                mHandler.post(updateRuannable);
                isRecording = true;
            } else {
                releaseMediaRecorder();
            }
        }
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

        Log.d("taxi", "video path=" + getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        Toast.makeText(this, "path=" + getOutputMediaFile(MEDIA_TYPE_VIDEO).toString(), Toast.LENGTH_LONG).show();
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setPreviewDisplay(mCameraView.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            curSeconds = 0;
            mHandler.removeCallbacks(updateRuannable);
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            isCameraOpen = false;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //得到file，write data
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("taxi", "请检查存储权限");
                return;
            }
            Log.d("taxi", "pic path =" + pictureFile.getAbsolutePath());
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

            } catch (FileNotFoundException e) {
                Log.d("taxi", "file not found: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };


    public static final int MEDIA_TYPE_IMAGE = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Nullable
    private static File getOutputMediaFile(int type) {
        //getExternalStoragePublicDirectory在删除app后文件不会被删除。
        File mediaStoreDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , "TaxiCameraApp");

        if (!mediaStoreDir.exists()) {
            if (!mediaStoreDir.mkdir()) {
                Log.d("taxi", "fail to create file");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStoreDir.getPath() + File.separator + "IMG_"
                    + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStoreDir.getPath() + File.separator + "VID_"
                    + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }
}
