package com.example.mydemo.rx.retrofit;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by ldh on 2017/8/14.
 */

public abstract class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {

    private Disposable mDisposable;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;
    public ProgressObserver(Context context) {
        this.context = context;
        //初始化progressDialog
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
        showProgressDialog();
    }


    @Override
    public void onError(@NonNull Throwable e) {
        dismissProgressDialog();
        Log.i("okhttp", "error:" + e.getMessage());
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Log.i("okhttp", "Get Top Movie Completed");
    }

    /**
     * 我们希望当cancel掉ProgressDialog的时候，能够取消订阅，也就取消了当前的Http请求。
     */
    @Override
    public void onCancelProgress() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
