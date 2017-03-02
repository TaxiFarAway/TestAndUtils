package com.zyt.tx.testapplication.okhttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zyt.tx.testapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BreakPointDownloadActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.btnDownload)
    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_point_download);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnDownload)
    public void onClick() {

    }

    class MyTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
}
