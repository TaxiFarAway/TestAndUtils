package com.zyt.tx.testapplication.print;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.zyt.tx.testapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintPictureActivity extends AppCompatActivity {

    @BindView(R.id.ivContent)
    ImageView ivContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_picture);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnPrintPicture)
    public void onClick() {
        doPhotoPrint();
    }

    private void doPhotoPrint() {
        PrintHelper printHelper = new PrintHelper(this);
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
        printHelper.printBitmap("droids.jpg - test print",bitmap);
    }
}
