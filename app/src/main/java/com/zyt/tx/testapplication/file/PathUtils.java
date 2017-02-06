package com.zyt.tx.testapplication.file;

import android.content.Context;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Taxi on 2017/2/6.
 */

public class PathUtils {

    public static String getExternCachePath(Context context) {
        //SDCard
        String info = context.getPackageName();
        String rootPath = getRootFilePath(context);
        if (TextUtils.isEmpty(rootPath)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("/data/");
        sb.append(info).append("/");
        String filePath = sb.toString();
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return filePath;
            }
        }
        Log.d("taxi", "ExternCachePath=" + filePath);
        return filePath;
    }


    public static String getImageCachePath(Context context) {
        StringBuffer sbf = new StringBuffer(getExternCachePath(context));
        sbf.append("img/");
        String filePath = sbf.toString();
        Log.d("taxi", "imgPath =" + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return filePath;
            }
        }
        return filePath;
    }


    public static String getVideoPath(Context context) {
        StringBuffer sbf = new StringBuffer(getExternCachePath(context));
        sbf.append("video/");
        String filePath = sbf.toString();

        File file = new File(filePath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return filePath;
            }
        }
        return filePath;
    }

    public static String getDownloadPath(Context context) {
        StringBuffer sbf = new StringBuffer(getExternCachePath(context));
        sbf.append("download/");
        String filePath = sbf.toString();
        if (new File(filePath).mkdirs()) {
            return filePath;
        }
        return filePath;
    }

    private static String getRootFilePath(Context context) {

        /*replaced by Environment*/
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
//            String[] paths = (String[]) storageManager.getClass()
//                    .getMethod("getVolumePaths", null).invoke(storageManager, null);
            String[] paths = (String[]) storageManager.getClass()
                    .getMethod("getVolumePaths", null)
                    .invoke(storageManager, null);
            for (int i = 0; i < paths.length; i++) {
                Log.e("taxi", "path[" + i + "]=" + paths[i]);
                if (checkFileWriteAble(paths[i])) {
                    return paths[i];
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "SD卡不可用", Toast.LENGTH_SHORT).show();
        return null;
    }

    private static boolean checkFileWriteAble(String path) {
        return new File(path).canWrite();
    }
}
