package com.zyt.tx.testapplication.print;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyt.tx.testapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasePrintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_print);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnPrintPicture, R.id.btnPrintHtml, R.id.btnPrintCustom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrintPicture:
                
                break;
            case R.id.btnPrintHtml:

                break;
            case R.id.btnPrintCustom:

                break;
        }
    }
}
