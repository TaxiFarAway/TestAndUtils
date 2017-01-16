package com.zyt.tx.testapplication.SDKCamera;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.params.Face;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by MJS on 2017/1/16.
 */

public class PhoneInfoUtils {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDCardTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs fs = new StatFs(path.getPath());
        long blockSize =fs.getBlockSizeLong();
        long totalBlocks = fs.getBlockCountLong();

        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDCardAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs fs = new StatFs(path.getPath());
        long blockSize = fs.getBlockSizeLong();
        long totalBlocks = fs.getAvailableBlocksLong();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }
}
