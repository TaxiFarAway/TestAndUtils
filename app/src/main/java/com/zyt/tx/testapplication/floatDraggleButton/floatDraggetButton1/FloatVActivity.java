package com.zyt.tx.testapplication.floatDraggleButton.floatDraggetButton1;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class FloatVActivity extends AppCompatActivity {

    private OperatePanel mOperatePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_v);

        ButterKnife.bind(this);
        initPermission();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mOperatePanel = new OperatePanel(getApplicationContext());
                            mOperatePanel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(FloatVActivity.this, "click", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initPermission() {
        PermissionGen.with(this).addRequestCode(100).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .request();
    }

    @PermissionSuccess(requestCode = 100)
    public void onPermissionSuc() {

    }

    @PermissionFail(requestCode = 100)
    public void onPermissionFail() {
        Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick({R.id.btnShow, R.id.btnHide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShow:
                mOperatePanel.show();
                break;
            case R.id.btnHide:
                mOperatePanel.hide();
                break;
        }
    }
}
