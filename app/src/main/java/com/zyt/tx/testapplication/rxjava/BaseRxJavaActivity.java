package com.zyt.tx.testapplication.rxjava;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class BaseRxJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_rx_java);


        loadImgNormal();


        loadImgByRxJava();
    }

    private void loadImgByRxJava() {
//        File[] folder = Environment.getExternalStorageDirectory().listFiles();
//        Observable.fromArray(folder).flatMap(new Function<File, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(File file) throws Exception {
//                return Observable.fromArray(file.listFiles());
//            }
//        }).filter(new Function<File, Boolean>() {
//            @Override
//            public Boolean apply(File file) throws Exception {
//                return file.getName().endsWith(".png");
//            }
//        })
    }

    private void loadImgNormal() {
        final File[] folder = Environment.getExternalStorageDirectory().listFiles();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (File file : folder) {
                    File[] files = file.listFiles();
                    for (File item : files) {
                        if (item.getName().endsWith(".png")) {
                            final Bitmap bitmap = getBitmapFormFile(item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showBitmapOnUIThread(bitmap);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    private void showBitmapOnUIThread(Bitmap bitmap) {

    }

    private Bitmap getBitmapFormFile(File item) {
        return null;
    }
}
