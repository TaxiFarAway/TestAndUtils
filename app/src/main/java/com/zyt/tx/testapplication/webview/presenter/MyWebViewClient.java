package com.zyt.tx.testapplication.webview.presenter;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyt.tx.testapplication.webview.WebViewActivity;
import com.zyt.tx.testapplication.webview.utils.CheckNetWork;

/**
 * Created by Taxi on 2017/3/2.
 */

public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;
    private WebViewActivity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = (WebViewActivity) mIWebPageView;
    }


    /*当新的URL即将加载到当前WebView中时，为主机应用程序提供接管控制的机会。如果没有提供WebViewClient，
    默认情况下WebView将要求活动管理器为URL选择适当的处理程序。如果提供WebViewClient，
    返回true表示主机应用程序处理url，而return false表示当前WebView处理URL。*/
    @TargetApi(21)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (url.startsWith("http://v.youku.com/")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(request.getUrl());
            mActivity.startActivity(intent);
            return true;
            /*电话,短信,邮箱*/
        } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(request.getUrl());
                mActivity.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        mIWebPageView.startProgress();
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mActivity.mProgress90) {
            mIWebPageView.hideProgressBar();
        } else {
            mActivity.mPageFinish = true;
        }

        if (!CheckNetWork.isNetworkConnected(mActivity)) {
            mIWebPageView.hideProgressBar();
        }
        mIWebPageView.addImageClickListener();
        super.onPageFinished(view, url);
    }

    /*通知主机应用程序应用于WebView的比例已更改。*/
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            /*异常放大*/
            view.setInitialScale((int) (oldScale / newScale * 100));
        }
    }
}
