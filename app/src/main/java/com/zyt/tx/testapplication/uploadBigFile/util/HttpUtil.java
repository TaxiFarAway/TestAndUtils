package com.zyt.tx.testapplication.uploadBigFile.util;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Taxi on 2017/2/6.
 */

public class HttpUtil {

    public interface OnExcuteUpdate {
        void onSuc(byte[] responseBody);

        void onFail(byte[] responseBody);
    }

    public static void cutFileUpload(String fileType, String filePath, final OnExcuteUpdate listener) {
        try {
            FileAccessI fileAccessI = new FileAccessI(filePath, 0);
            Long startPos = 0l;
            Long length = fileAccessI.getFileLength();
            int mBufferSize = 1024 * 100;
            byte[] buffer = new byte[mBufferSize];
            FileAccessI.Detail detail;
            long nRead = 0l;
            String videoFileName = generateFileName();
            long nStart = startPos;
            while (nStart < length) {
                detail = fileAccessI.getContent(nStart);
                nRead = detail.len;
                buffer = detail.b;

                JSONObject data = new JSONObject();
                data.put("FileName", videoFileName);
                data.put("start", nStart);
                data.put("filetype", fileType);

                nStart += nRead;
                startPos = nStart;
                String url = "";

                RequestParams params = new RequestParams();
                params.put("json", data.toString());
                params.put("video", encodeByte(buffer));

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("taxi", "upload success");
                        listener.onSuc(responseBody);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("taxi", "upload fail");
                        listener.onFail(responseBody);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String encodeByte(byte[] buffer) {
        return Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
    }

    private static String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE);
        return sdf.format(new Date()) + ".mp4";
    }

}
