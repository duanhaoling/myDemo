package com.example.mydemo.base;

import com.example.mydemo.widget.WaitDialog;

/**
 * Created by ldh on 2017/9/8.
 */

public interface DialogControl {

    public abstract void hideWaitDialog();

    public abstract WaitDialog showWaitDialog();

    public abstract WaitDialog showWaitDialog(int resid);

    public abstract WaitDialog showWaitDialog(String text);
}
