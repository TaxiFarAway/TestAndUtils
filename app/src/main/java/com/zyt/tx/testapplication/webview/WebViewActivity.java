package com.zyt.tx.testapplication.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zyt.tx.testapplication.R;
import com.zyt.tx.testapplication.webview.presenter.IWebPageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity implements IWebPageView {
    private static final String INTENT_URL = "jump_url";
    private static final String INTENT_IS_MOVIE = "jump_is_movie";
    @BindView(R.id.webview_detail)
    WebView webView;
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    private String mUrl;
    private boolean mIsMovie;

    public boolean mProgress90 = false;

    public boolean mPageFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(!mIsMovie ? R.layout.activity_web_view : R.layout.activity_web_view_movie);
        ButterKnife.bind(this);
        setTitle("详情");
        initWebView();
        webView.loadUrl(mUrl);
    }

    private void initWebView() {
        mProgressBar.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        /*网页内容的宽度是否可以大于webView空间的宽度*/
        ws.setLoadWithOverviewMode(false);
        /*保存表单数据*/
        ws.setSaveFormData(true);
        /*是否支持使用屏幕缩放控件和手势缩放*/
        ws.setSupportZoom(true);
        /*设置WebView是否应使用其内置的缩放机制。*/
        ws.setBuiltInZoomControls(true);
        /*设置在使用内置缩放机制时WebView是否应显示屏幕缩放控件。*/
        ws.setDisplayZoomControls(false);
        /*启动应用缓存*/
        ws.setAppCacheEnabled(true);
        /*设置缓存模式*/
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
//        ws.setDefaultZoom();  api19弃用

        //设置此属性，可任意比例缩放
        ws.setUseWideViewPort(true);
        /*缩放比例 1*/
        webView.setInitialScale(1);
        /*告诉webView启用JavaScript执行。默认FALSE*/
        ws.setJavaScriptEnabled(true);
        /*页面加载好以后，再放开图片*/
        ws.setBlockNetworkImage(false);
        /*使用localStorage则必须打开*/
        ws.setDomStorageEnabled(true);
        /*排版适应屏幕*/
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        /*webView是否支持多个窗口*/
        ws.setSupportMultipleWindows(true);
        /*webViwe从5.0开始默认不允许混合模式，https中不能加载http资源，需要设置开启*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /*设置字体缩放默认大小（改变网页字体大小，setTextSize api14弃用）*/
        ws.setTextZoom(100);



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

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWebView() {
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWebView() {
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void startProgress() {
        startProgress90();
    }

    /**
     * 假装假装到90%
     */
    private void startProgress90() {
        for (int i = 0; i < 900; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 900) {
                        mProgress90 = true;
                        if (mPageFinish) {
                            startProgress90to100();
                        }
                    }
                }
            }, (i + 1) * 2);

        }
    }

    /**
     * 进度条加载到100%
     */
    private void startProgress90to100() {
        for (int i = 900; i <= 1000; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 1000) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                }
            }, (i + 1) * 2);
        }
    }

    @Override
    public void progressChanged(int newProgress) {
        if (mProgress90) {
            int progress = newProgress * 100;
            if (progress > 900) {
                mProgressBar.setProgress(progress);
                if (progress == 1000) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void addImageClickListener() {
        //TODO 待续
    }

    @Override
    public void fullViewAddView(View view) {
        //TODO 让view全屏
    }

    @Override
    public void showVideoFullView() {

    }

    @Override
    public void hideVideoFullView() {

    }
}
