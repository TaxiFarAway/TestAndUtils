package com.zyt.tx.testapplication.SDKCamera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by MJS on 2017/1/17.
 */

public class CameraZoomView extends SurfaceView implements SurfaceHolder.Callback {

    private final Camera mCamera;
    private long downTime = 0l;
    private static final int DOUBLE_CLICK_INTERVAL = 300;
    private ZoomRunnable mZoomRunnable;

    public CameraZoomView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        getHolder().addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        float ration = 1f * size.width / size.height;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / ration);
        int wms = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int hms = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(wms, hms);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCamera.getParameters().isZoomSupported() && event.getDownTime() - downTime < DOUBLE_CLICK_INTERVAL) {
                    zoomPreview();
                }
                downTime = event.getDownTime();
                break;


        }

        return super.onTouchEvent(event);
    }


    private void zoomPreview() {
        Camera.Parameters parameters = mCamera.getParameters();
        int currentZoom = parameters.getZoom();
        int maxZoom = (int) (parameters.getMaxZoom() / 2f + 0.5);
        int destZoom = 0 == currentZoom ? maxZoom : 0;
        if (/*parameters.isSmoothZoomSupported()*/false) {
            mCamera.stopSmoothZoom();
            mCamera.startSmoothZoom(destZoom);
        } else {
            Handler handler = getHandler();
            if (null == handler)
                return;
            handler.removeCallbacks(mZoomRunnable);
            handler.post(mZoomRunnable = new ZoomRunnable(destZoom, currentZoom, mCamera));
        }
    }

    /**
     * 放大预览视图任务
     *
     * @author Martin
     */
    private static class ZoomRunnable implements Runnable {

        int destZoom, currentZoom;
        WeakReference<Camera> cameraWeakRef;

        public ZoomRunnable(int destZoom, int currentZoom, Camera camera) {
            this.destZoom = destZoom;
            this.currentZoom = currentZoom;
            cameraWeakRef = new WeakReference<>(camera);
        }

        @Override
        public void run() {
            Camera camera = cameraWeakRef.get();
            if (null == camera)
                return;

            boolean zoomUp = destZoom > currentZoom;
            for (int i = currentZoom; zoomUp ? i <= destZoom : i >= destZoom; i = (zoomUp ? ++i : --i)) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setZoom(i);
                camera.setParameters(parameters);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
