package com.zyt.tx.testapplication.floatDraggleButton.floatDraggetButton1;

import android.app.Instrumentation;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

class OperatePanel extends View implements OnClickListener
{
    static FloatView backView;
    static FloatView menuView;
    static FloatView fv;
    private Context mContext;
    private boolean mshowBM = false;

    public OperatePanel(Context context) {
        super(context);
        Log.d("lq", " operatePanel 0");
        mContext = context;
        initView();

    }

    public OperatePanel(Context context, AttributeSet aSet) {
        super(context, aSet);
        Log.d("lq", " operatePanel ");
        mContext = context;
        initView();
    }

    private void initView() {
        Log.d("lq", "operatePanel initView ");

        if (fv == null) {
            fv = new FloatView(mContext, 100, 200);
        }
        fv.setOnClickListener(this);
        if (fv != null) {
            fv.show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("lq", "operatePanel onclick v:" + v);
        if (v == fv) {
            if (mshowBM == false) {
                if (backView == null) {
                    backView = new FloatView(mContext, 100, 100);
                }
                if (menuView == null) {
                    menuView = new FloatView(mContext, 100, 150);
                }
                backView.show();
                menuView.show();
                backView.setOnClickListener(this);
                menuView.setOnClickListener(this);
                mshowBM = true;
            } else {
                mshowBM = false;
                backView.hide();
                menuView.hide();
            }

        }else if(v == backView){
            simulateKey(KeyEvent.KEYCODE_BACK);
        }else if(v == menuView){
            simulateKey(KeyEvent.KEYCODE_MENU);
        }

    }

    public void show(){
        if (backView != null) {
            backView.show();
        }
        if (menuView != null) {
            menuView.show();
        }
        if (fv != null) {
            fv.show();
        }
    }

    public void hide(){
        if (backView != null) {
            backView.hide();
        }
        if (menuView != null) {
            menuView.hide();
        }
        if (fv != null) {
            fv.hide();
        }
    }

    public void simulateKey(final int KeyCode)
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                    Log.d("lq", "sendKeyDownUpSync");
                } catch (Exception e)
                {
                    Log.d("lq", "sendKeyDownUpSync Exception:" + e.toString());
                    Log.e("Exception when sendKeyDownUpSync", e.toString());
                }
            }
        }.start();
    }



}