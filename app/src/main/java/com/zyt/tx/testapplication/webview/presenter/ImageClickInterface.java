package com.zyt.tx.testapplication.webview.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Taxi on 2017/3/2.
 */

public class ImageClickInterface {
    private Context context;


    public ImageClickInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void imageClick(String imgUrl, String hasLink) {
        Toast.makeText(context, "--点击图片", Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public void textClick(String type, String item_pk) {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
            Toast.makeText(context, "--点击文字", Toast.LENGTH_SHORT).show();
        }

    }
}
