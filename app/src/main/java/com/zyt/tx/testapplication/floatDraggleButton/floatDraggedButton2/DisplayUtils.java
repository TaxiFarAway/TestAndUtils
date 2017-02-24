package com.zyt.tx.testapplication.floatDraggleButton.floatDraggedButton2;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Taxi on 2017/2/21.
 */

public class DisplayUtils {

    private static String TAG = DisplayUtils.class.getSimpleName();

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "status height =" + result);
        return result;
    }


    public static int getPrintHeight(Context context) {
        float rate = getScreenWidth(context) * 1.0f / getScreenHeight(context);
        int height = (int) (getPrintWidth(context) / rate);
        return height;
    }


    public static int getPrintWidth(Context context) {
        return 480;
    }
}
