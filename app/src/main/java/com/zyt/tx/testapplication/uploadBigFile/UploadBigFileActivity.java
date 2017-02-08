package com.zyt.tx.testapplication.uploadBigFile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;
import com.zyt.tx.testapplication.uploadBigFile.util.HttpUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadBigFileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x11;
    private static final int NOT_SUPPORT_STRING = 0x12;
    private static final int ADD_LOG = 0x13;
    private static final int UPLOAD_SUC = 0x14;
    private static final int UPLOAD_FAIL = 0x15;

    @BindView(R.id.tvData)
    TextView tvData;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOT_SUPPORT_STRING:
                    Toast.makeText(UploadBigFileActivity.this, "暂不支持文件上传", Toast.LENGTH_SHORT).show();
                    break;

                case ADD_LOG:
                    showPop(view);
                    break;

                case UPLOAD_FAIL:
                    dismissPop();
                    tvData.setText("文件上传失败");
                    break;

                case UPLOAD_SUC:
                    dismissPop();
                    tvData.setText("文件上传成功");
                    break;
            }
        }
    };

    private void dismissPop() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private PopupWindow popupWindow;

    private void showPop(View view) {
        popupWindow = new PopupWindow(view, 300, 500);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xPos = (manager.getDefaultDisplay().getWidth() - view.getWidth()) / 2;
        popupWindow.showAsDropDown(view, xPos, 0);
    }

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_big_file);
        ButterKnife.bind(this);
        view = getLayoutInflater().inflate(R.layout.popup_window, null);
        Log.d("taxi", "main threadId =" + Process.myPid());
    }

    @OnClick({R.id.btnChooseFile, R.id.btnChooseAllFile, R.id.btnUpload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChooseFile:
                Intent intentChooseFile = new Intent(this, ChooseFileActivity.class);
                startActivityForResult(intentChooseFile, REQUEST_CODE);
                break;
            case R.id.btnChooseAllFile:
                startActivityForResult(new Intent(this, ChooseFolderActivity.class), REQUEST_CODE);
                break;
            case R.id.btnUpload:


                uploadFile();
                break;
        }
    }

    private void uploadFile() {
        String path = tvData.getText().toString();
        if (Pattern.matches("\"【(\\\\S+)】\"", path)) {
            /*not support url*/
            mHandler.obtainMessage(NOT_SUPPORT_STRING, path).sendToTarget();
        } else {
            mHandler.obtainMessage(ADD_LOG).sendToTarget();
            HttpUtil.cutFileUpload("video", getFilePath(path), new HttpUtil.OnExcuteUpdate() {
                @Override
                public void onSuc(byte[] responseBody) {
                    //TODO print process id to check if in main thread
                    Log.d("taxi", "upload progressId=+Process.myPid()");
                    mHandler.obtainMessage(UPLOAD_SUC).sendToTarget();
                }

                @Override
                public void onFail(byte[] responseBody) {
                    mHandler.obtainMessage(UPLOAD_FAIL).sendToTarget();
                }
            });
        }
    }

    private String getFilePath(String path) {
        if (null == path) {
            return "";
        }
        String res = path.substring(4);
        Log.d("taxi", "subStr=" + res);
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == ChooseFileActivity.RESULTCODE) {
                Bundle bundle = data.getExtras();
                String filePath = bundle.getString("current_path");
                Toast.makeText(UploadBigFileActivity.this, filePath, Toast.LENGTH_SHORT).show();
                tvData.setText(filePath);
            } else if (resultCode == ChooseFolderActivity.RESULTCODE) {
                Bundle bundle = data.getExtras();
                String filePath = bundle.getString("current_path");
                Toast.makeText(UploadBigFileActivity.this, filePath, Toast.LENGTH_SHORT).show();
                tvData.setText(filePath);
            }
        }
    }
}
