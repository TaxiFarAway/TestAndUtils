package com.zyt.tx.testapplication.floatDraggleButton.floatDraggedButton2;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

import java.io.File;


/**
 * Created by Taxi on 2017/2/21.
 */

public class DragFloatActionButton extends FloatingActionButton implements ViewTreeObserver.OnGlobalLayoutListener {

    private int screenWidth;
    private int screenWidthHalf;
    private int screenHeight;
    private int statusHeight;
    private View mView;

    public DragFloatActionButton(Context context) {
        this(context, null);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mView = this;
        setImageResource(R.mipmap.print);
        screenWidth = DisplayUtils.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        screenHeight = DisplayUtils.getScreenHeight(getContext());
        statusHeight = DisplayUtils.getStatusHeight(getContext());
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private int lastX, lastY;
    private boolean isDrag;


    private long currentTime = 0L;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                if (System.currentTimeMillis() - currentTime < 300) {
                    showDialog();
                    return true;
                }
                currentTime = System.currentTimeMillis();

                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                //
                break;

            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                /*计算手指移动*/
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                float x = dx + getX();
                float y = dy + getY();

                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                y = y < statusHeight ? statusHeight : y + getHeight() > screenHeight ? screenHeight - getHeight() : y;

                setX(x);
                setY(y);

                lastX = rawX;
                lastY = rawY;
                break;

            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    setPressed(false);
                    if (rawX > screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(screenWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                }
                break;
            default:
                break;

        }
        return isDrag || super.onTouchEvent(event);
    }

    @Override
    @TargetApi(16)
    public void onGlobalLayout() {
        this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        setX(screenWidth - getWidth());
        setY(screenHeight - getHeight());
    }


    private void showDialog() {

        TextView msg = new TextView(getContext());
        msg.setText("是否打印当前信息？");
        msg.setPadding(10, 30, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(18);


        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mView.setVisibility(View.INVISIBLE);
                        ScreenCaptureUtils.captureScreen((Activity) getContext(), new ScreenCaptureUtils.onCaptureListener() {
                            @Override
                            public void onSuccess(String savePath) {
                                mView.setVisibility(View.VISIBLE);
                                Log.d("taxi", "capture suc:" + savePath);
                                final File file = new File(savePath);
                                Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                                deleteFile(file);
                                bitmap = BitmapUtils.decodeSampledBitmapFromBitmap(bitmap, DisplayUtils.getPrintWidth(getContext())
                                        , DisplayUtils.getPrintHeight(getContext()));
//                                PrintManager printManager = new PrintManager((Activity) getContext());
//                                printManager.printImg(bitmap, new PrintManager.onPrintListener() {
//                                    @Override
//                                    public void onSuccess() {
//                                        Toast.makeText(getContext(), "打印成功", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onFailed(String msg) {
//                                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                            }

                            @Override
                            public void onError(String msg) {
                                Toast.makeText(getContext(), "截屏失败", Toast.LENGTH_SHORT).show();
                                mView.setVisibility(View.VISIBLE);
                                Log.d("taxi", "capture fail:" + msg);
                            }
                        });
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }

    }
}
