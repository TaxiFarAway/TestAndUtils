package com.zyt.tx.testapplication.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyt.tx.testapplication.R;
import com.zyt.tx.testapplication.webview.test.TestWebViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,R.id.btnMy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                String baiduUrl = "http://www.baidu.com";
                WebViewActivity.loadUrl(this,baiduUrl,false);
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;

            case R.id.btnMy:
                startActivity(new Intent(this,TestWebViewActivity.class));
                break;
        }
    }
}
