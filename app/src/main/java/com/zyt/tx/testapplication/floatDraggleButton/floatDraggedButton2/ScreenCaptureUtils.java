package com.zyt.tx.testapplication.floatDraggleButton.floatDraggedButton2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Taxi on 2017/2/21.
 */

public class ScreenCaptureUtils {

    public static interface onCaptureListener {
        void onSuccess(String savePath);

        void onError(String msg);
    }


    public static void captureScreen(Activity context, onCaptureListener listener) {
        //调用截屏，返回数据。
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        // 2.获取屏幕
        View decorview = context.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();

        String rootPath = getSDCardPath();
        if (TextUtils.isEmpty(rootPath)) {
            listener.onError("SD卡不能为空");
            return;
        }
        String SavePath = rootPath + "/PrintScreenDemo/ScreenImage";
        // 3.保存Bitmap
        try {
            File path = new File(SavePath);
            // 文件
            String filepath = SavePath + "/Screen_" + getTimeStamp() + ".png";
            Log.d("taxi", "screenPath=" + filepath);
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                listener.onSuccess(filepath);
            }
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }

    private static String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE);
        return sdf.format(new Date());
    }

    private static String getSDCardPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            StringBuffer sb = new StringBuffer(path);
            sb.append(File.separator).append("screenCapture");
            return sb.toString();
        }
        return null;
    }
}
