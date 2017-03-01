package com.zyt.tx.testapplication.webview;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.webkit.WebChromeClient;

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

    /*播放网络视频时全屏会被调用的方法*/

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mIwebPageView.hideWebView();
        /*如果一个视图已经存在，那么立刻终止并新建一个*/
        if (mXCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        mActivity.fullViewAddView(view);
        mXCustomView = view;
        mXCustomViewCallBack = callback;
        mIwebPageView.showVideoFullView();
    }
}
