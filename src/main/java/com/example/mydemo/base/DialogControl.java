package com.example.mydemo.base;

import com.example.mydemo.widget.WaitDialog;

/**
 * Created by ldh on 2017/9/8.
 */

public interface DialogControl {

    void hideWaitDialog();

    WaitDialog showWaitDialog();

    WaitDialog showWaitDialog(int resid);

    WaitDialog showWaitDialog(String text);
}
