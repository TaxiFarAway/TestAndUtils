package com.zyt.tx.testapplication.PropertyAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyt.tx.testapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasePropertyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_property);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnPropertyOne, R.id.btnPropertyTwo, R.id.btnMyPropertyAnimation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPropertyOne:
                startActivity(new Intent(this, PropertyActivity.class));
                break;
            case R.id.btnPropertyTwo:
                startActivity(new Intent(this, PropertyMixActivity.class));
                break;
            case R.id.btnMyPropertyAnimation:
                startActivity(new Intent(this, TestPropertyAnimationActivity.class));
                break;
        }
    }
}
