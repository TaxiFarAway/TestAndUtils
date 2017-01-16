package com.zyt.tx.testapplication;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MediaPlayerTestActivity extends AppCompatActivity {


    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String music_path = rootPath + "/netease/cloudmusic/Music/周杰伦 - 告白气球.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_test);
        ButterKnife.bind(this);
        initPermissions();
    }

    private void initPermissions() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }


    @PermissionSuccess(requestCode = 100)
    public void requestSuc() {
        Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void requestFail() {
        Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnPlay)
    public void onClick() {
        play();
    }


    public void play() {
        try {
            File file = new File(music_path);
            Log.d("taxi", "文件存在=" + file.exists());
            MediaPlayer player = new MediaPlayer();
            player.reset();
            player.setDataSource(music_path);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
