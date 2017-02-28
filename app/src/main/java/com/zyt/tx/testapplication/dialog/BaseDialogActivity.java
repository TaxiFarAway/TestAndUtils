package com.zyt.tx.testapplication.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyt.tx.testapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_dialog);
        ButterKnife.bind(this);

    }


    private String[] multiItems = {"a", "b", "c"};

    @OnClick({R.id.btn1, R.id.btnList, R.id.btnMultiChoose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                ChooseDialogFragment dialogFragment = new ChooseDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;

            case R.id.btnList:
                ListDialogFragment listDialogFragment = new ListDialogFragment();
                listDialogFragment.show(getSupportFragmentManager(), "listDialog");
                break;

            case R.id.btnMultiChoose:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("复选框").setMultiChoiceItems(multiItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            /*add item*/
                        }
                    }
                }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
                break;
        }
    }
}
