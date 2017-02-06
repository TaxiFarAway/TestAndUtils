package com.zyt.tx.testapplication.file;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zyt.tx.testapplication.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class FileTestActivity extends AppCompatActivity {


    private static final int CREATE_MEDIA = 0x01;
    private static final int CREATE_PIC = 0x02;

    private static final String ROOT_FILE_NAME = "z0taxiDatas";

    @BindView(R.id.tvContent)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        ButterKnife.bind(this);

        initPermission();

    }

    @TargetApi(16)
    private void initPermission() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @PermissionSuccess(requestCode = 100)
    public void onPRSuc() {
    }

    @PermissionFail(requestCode = 100)
    public void onPRFail() {
        finish();
    }

    @OnClick({R.id.btnCreateMedia, R.id.btnCreatePic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateMedia:
                String res = getOutputFile(CREATE_MEDIA).getAbsolutePath();
                Log.e("taxi", "filePath=" + res);
                break;
            case R.id.btnCreatePic:
                getOutputFile(CREATE_PIC);
                break;
        }
        updateTextContent();
    }

    private void updateTextContent() {
        tvContent.setText(getFileContent());
    }

    private String getFileContent() {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + ROOT_FILE_NAME;
        File root = new File(rootPath);
        if (!root.isDirectory()) {
            return "";
        }
        return getResFile(root, 0);
    }

    public String getResFile(File root, int n) {
        StringBuilder sb = new StringBuilder();

        sb.append(root.getName());
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(" ");
        }
        File[] files = root.listFiles();
        Log.d("taxi", "root.childCount=" + files.length);
        if (files.length > 0) {
            for (File item : files) {
                if (item.isDirectory()) {
                    getResFile(item, n++);
                }
                if (item.isFile()) {
                    sb.append(item.getName());
                }
            }
        }
        return sb.toString();
    }

    @Nullable
    private File getOutputFile(int type) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }

        File rootPath = new File(Environment.getExternalStorageDirectory(), ROOT_FILE_NAME);
        if (!rootPath.exists()) {
            if (!rootPath.mkdirs()) {
                return null;
            }
        }

        File file = null;
        String path;

        if (type == CREATE_MEDIA) {
//            path = "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date()) + ".mp4";
//            file = new File(rootPath, path);
            path = PathUtils.getVideoPath(getApplicationContext()) + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date()) + ".mp4";
            file = new File(path);
        } else if (type == CREATE_PIC) {
            path = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date()) + ".jpg";
            file = new File(rootPath, path);
        }
        if (file != null) {
            Log.d("taxi", "file path =" + file.getAbsolutePath());
        }
        return file;
    }
}
