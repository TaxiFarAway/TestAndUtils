package com.zyt.tx.testapplication.webview.presenter;

import android.view.View;

/**
 * Created by Taxi on 2017/3/1.
 */

public interface IWebPageView {

    /*隐藏进度条*/
    void hideProgressBar();

    /*显示webView*/
    void showWebView();

    /*隐藏webView*/
    void hideWebView();

    /*进度条先加载到90%，再到100%*/
    void startProgress();

    /*进度条变化*/
    void progressChanged(int newProgress);

    /*添加js监听*/
    void addImageClickListener();

    /*播放网络视频全屏调用*/
    void fullViewAddView(View view);

    void showVideoFullView();

    void hideVideoFullView();

}
