package com.example.mydemo.view.popu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.mydemo.R;

public class LiuyanActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private EditText mEt;
    private Button mBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liuyan);
        mBt = (Button) findViewById(R.id.bt_mes_board);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopuWindow();
            }
        });
    }


    private void showPopuWindow() {
        if (null == popupWindow) {
            initPopupWindow();
        }
        popupWindow.showAtLocation(mBt, Gravity.BOTTOM, 0, 0);

        mEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        },100);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                showSoftKeyboard();
//            }
//        }, 100);

    }

    private void initPopupWindow() {
        View view = View.inflate(this, R.layout.popu_window_messageboard, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(123));
//        popupWindow.setAnimationStyle(R.style.popup_window_animation_bottom);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftKeyboard();
            }
        });
        mEt = (EditText) view.findViewById(R.id.et_mes_board);
        mEt.setFocusable(true);
        mEt.setFocusableInTouchMode(true);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard() {
        mEt.requestFocus();
        ((InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(mEt,
                0);//这里的参数必须为0，才可以正常隐藏软键盘
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        ((InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE)) .hideSoftInputFromWindow(
                mEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                ((InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

}
