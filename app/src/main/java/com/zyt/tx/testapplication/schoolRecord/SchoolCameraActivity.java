package com.zyt.tx.testapplication.schoolRecord;

import android.Manifest;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.IOException;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class SchoolCameraActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Camera camera;
    private SurfaceHolder holder;
    private SurfaceView mForeGroundSurfaceView;
    private SurfaceHolder foreHolder;
    private Camera foreCamera;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_camera);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mForeGroundSurfaceView = (SurfaceView) findViewById(R.id.surfaceViewForeground);
        holder = mSurfaceView.getHolder();
        foreHolder = mForeGroundSurfaceView.getHolder();

        initPermission();
    }

    private void initPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(Manifest.permission.CAMERA).request();
    }

    @PermissionSuccess(requestCode = 100)
    public void onPermissionSuc() {
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.release();
            }
        });


        foreHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initForeCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (foreCamera != null) {
                    foreCamera.release();
                }
            }
        });
    }

    private void initForeCamera() {
        try {
            foreCamera = Camera.open(1);
            Camera.Parameters parameters = foreCamera.getParameters();
            parameters.setJpegQuality(50);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setPictureSize(800, 400);

            parameters.setPreviewFrameRate(24);
            parameters.setPreviewSize(100, 100);
            parameters.setZoom(1);
            foreCamera.setDisplayOrientation(90);
            foreCamera.setPreviewDisplay(foreHolder);
            foreCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PermissionFail(requestCode = 100)
    public void onPermissionFail() {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
    }

    private void initCamera() {
        try {
            Toast.makeText(this, "初始化", Toast.LENGTH_SHORT).show();
            camera = Camera.open(0);
            Camera.Parameters parameters = camera.getParameters();
            System.out.println("参数：" + parameters.flatten());
            parameters.setJpegQuality(100);//图片质量 1~100
            //设置图片的格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            //设置图片大小
            parameters.setPictureSize(800, 480);
            //预览的帧率fps:frames per second
            parameters.setPreviewFrameRate(24);
            //设置预览大小
            parameters.setPreviewSize(800, 480);
            //放大：The valid range is 0 to getMaxZoom()
            parameters.setZoom(1);
            //旋转90度，解决预览倒过来的问题
            camera.setDisplayOrientation(90);

            //设置预览到SurfaceView上面
            camera.setPreviewDisplay(holder);
            //开始预览
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
        }

        if (foreCamera != null) {
            foreCamera.release();
            foreCamera = null;
        }
    }
}
