package com.zyt.tx.testapplication.webview;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zyt.tx.testapplication.webview.presenter.IWebPageView;

/**
 * Created by Taxi on 2017/3/1.
 */

public class MyWebChromeClient extends WebChromeClient {

    private final IWebPageView mIwebPageView;
    private final WebViewActivity mActivity;

    private View mXCustomView;
    private CustomViewCallback mXCustomViewCallBack;

    public MyWebChromeClient(IWebPageView iWebPageView) {
        this.mIwebPageView = iWebPageView;
        this.mActivity = (WebViewActivity) iWebPageView;
    }

    /*播放网络视频时全屏会被调用的方法
    * 通知主机应用程序当前页面已进入全屏模式。
    * 主机应用程序必须以全屏模式显示包含Web内容（视频或其他HTML内容）的自定义视图。
    * */

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        mIwebPageView.hideWebView();
        /*如果一个视图已经存在，那么立刻终止并新建一个*/
//        if (mXCustomView != null) {
//            callback.onCustomViewHidden();
//            return;
//        }
//        mActivity.fullViewAddView(view);
//        mXCustomView = view;
//        mXCustomViewCallBack = callback;
//        mIwebPageView.showVideoFullView();
    }

    /*视频播放退出全屏调用
    *通知主机应用程序当前页面已退出全屏模式。
    * */
    @Override
    public void onHideCustomView() {
        super.onHideCustomView();

//        mIwebPageView.showWebView();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        mIwebPageView.progressChanged(newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mActivity.setTitle(title);
    }
}
