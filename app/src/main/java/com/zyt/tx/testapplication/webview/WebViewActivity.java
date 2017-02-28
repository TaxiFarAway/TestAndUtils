package com.zyt.tx.testapplication.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zyt.tx.testapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    private static final String INTENT_URL = "jump_url";
    private static final String INTENT_IS_MOVIE = "jump_is_movie";
    @BindView(R.id.webview_detail)
    WebView webviewDetail;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    private String mUrl;
    private boolean mIsMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(!mIsMovie ? R.layout.activity_web_view : R.layout.activity_web_view_movie);
        ButterKnife.bind(this);
        setTitle("详情");
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra(INTENT_URL);
            mIsMovie = intent.getBooleanExtra(INTENT_IS_MOVIE, false);
        }
    }

    public static void loadUrl(Context context, String url, boolean isMove) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(INTENT_IS_MOVIE, isMove);
        context.startActivity(intent);
    }

}
