package com.zyt.tx.testapplication;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.utill.tx.txlibrary.Log.L;

public class TempWebViewActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_web_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyClient());
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        webView.loadUrl("http://www.zhangyoobao.cn/merchant/account/login;jsessionid=9E71E1910787444E6E7B1962AB0655C1");
    }

    final class InJavaScriptLocalObj{
        @JavascriptInterface
        public void showSource(String html) {
            Spanned spanned = Html.fromHtml(html);
             html = spanned.toString();
            Toast.makeText(TempWebViewActivity.this,html,Toast.LENGTH_LONG).show();
            L.d("html="+html);
        }
    }


    @Override
    public void onBackPressed() {
        webView.goBack();
    }

    class MyClient extends WebViewClient {
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            L.d("doUpdateVisitedHistory url=" + url);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            L.d("onLoadResource url=" + url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            L.d("onPageCommitVisible url=" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            L.d("onPageFinished url=" + url);
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                    + "document.getElementById('page1').innerHTML+'</head>');");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.d("onPageStarted url=" + url);
        }

        @TargetApi(21)
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
            L.d("onReceivedClientCertRequest request.host=" + request.getHost());
        }

        @TargetApi(21)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            L.d("onReceivedError request.url=" + request.getUrl());
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            L.d("onReceivedHttpAuthRequest host=" + host);
        }

        @TargetApi(21)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            L.d("shouldInterceptRequest request.url=" + request.getUrl());
            return super.shouldInterceptRequest(view, request);
        }

        @TargetApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            L.d("shouldOverrideUrlLoading request.url=" + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

    }
}
