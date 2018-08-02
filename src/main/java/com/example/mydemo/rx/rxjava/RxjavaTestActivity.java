package com.example.mydemo.rx.rxjava;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.mydemo.R;
import com.example.mydemo.rx.rxjava.bean.Employee;
import com.example.mydemo.databinding.ActivityRxjavaTestBinding;
import com.ldh.androidlib.utils.LogUtil;
import com.example.mydemo.util.MyToast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.mydemo.R.id.testRxjava1;
import static com.example.mydemo.R.id.testRxjava2;
import static com.example.mydemo.R.id.testRxjava3;
import static com.example.mydemo.R.id.testRxjava4;
import static com.example.mydemo.R.id.testRxjava5;
import static com.example.mydemo.R.id.testRxjava6;
import static com.example.mydemo.R.id.testRxjava7;
import static com.example.mydemo.R.id.testRxjava8;
import static com.example.mydemo.R.id.testRxjava9;

public class RxjavaTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxjavaTestActivity";
    private ActivityRxjavaTestBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava_test);
        mBinding.testRxjava1.setOnClickListener(v -> {
            testDemo1();
            Observable.just("hello word!")
                    .map(s -> s + "I am ldh!")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> Log.i(TAG, s));
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case testRxjava1:
//                testDemo1();
                break;
            case testRxjava2:
                testDemo2();
                break;
            case testRxjava3:
                testDemo3();
                break;
            case testRxjava4:
                testDemo4();
                break;
            case testRxjava5:
                testDemo5();
                break;
            case testRxjava6:
                testDemo6();
                break;
            case testRxjava7:
                break;
            case testRxjava8:
                break;
            case testRxjava9:
                break;
            default:
                break;
        }
    }

    private void testDemo6() {

    }

    private void testDemo5() {
        String phoneNum = mBinding.etPhone.getText().toString();
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=" + phoneNum)
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, MoblieAddressEntity>() {

            @Override
            public MoblieAddressEntity apply(@NonNull Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.i(TAG, "map:转换前:" + response.body().string());
                        return JSON.parseObject(body.string(), MoblieAddressEntity.class);
//                        return new Gson().fromJson(body.string(), MoblieAddressEntity.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(moblieAddressEntity -> Log.i(TAG, "doOnNext：保存成功：" + moblieAddressEntity.toString() + "\n")).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    Log.i(TAG, "sucess:" + entity.toString() + "\n");
                    mBinding.tvReadme.setText(entity.getResult().getMobilearea()
                            + entity.getResult().getMobiletype());

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "fail:" + throwable.getMessage() + "\n");
                    }
                });

    }

    private void testDemo4() {
        /**
         *     flatMap 返回的是一个Observable对象，而 map 返回的是一个普通转换后的对象;

         flatMap 返回的Observable对象并不是直接发送到Subscriber的回调中，而是重新创建一个Observable对象，
         并激活这个Observable对象，使之开始发送事件；而 map 变换后返回的对象直接发到Subscriber回调中；

         flatMap 变换后产生的每一个Observable对象发送的事件，最后都汇入同一个Observable，进而发送给Subscriber回调；

         map返回类型 与 flatMap 返回的Observable事件类型，可以与原来的事件类型一样；

         可以对一个Observable多次使用 map 和 flatMap；
         */
        List<Employee.Mission> missions = new ArrayList<Employee.Mission>() {
            {
                add(new Employee.Mission("make Luncher"));
                add(new Employee.Mission("go to fish"));
            }
        };
        List<Employee> list = new ArrayList<Employee>() {
            {
                add(new Employee("Jack", missions));
                add(new Employee("Lucy", missions));
            }
        };

        Observable.fromIterable(list)
                .flatMap(new Function<Employee, ObservableSource<Employee.Mission>>() {
                    @Override
                    public ObservableSource<Employee.Mission> apply(@NonNull Employee employee) throws Exception {
                        return Observable.fromIterable(employee.getMission());
                    }
                })
                .subscribe(new Observer<Employee.Mission>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(Employee.Mission mission) {
                        LogUtil.i(TAG, mission.desc);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void testDemo3() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("http://img4.duitang.com/uploads/item/201406/28/20140628141104_PXLRN.thumb.700_0.jpeg");
                e.onComplete();
            }
        }).map(new Function<String, Drawable>() {
            @Override
            public Drawable apply(@NonNull String s) throws Exception {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(s).openStream(), "iv");

                    return drawable;
                } catch (IOException e) {

                }
                return null;
            }
        })
                //指定subscribe()所在的线程，也就是上面call()方法调用的线程
                .subscribeOn(Schedulers.io())
                //指定Subscriber 回调方法所在的线程，也就是onNext，onError，onComplete回调的线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mBinding.ivLogo.setImageDrawable(drawable);
                    }
                });

    }


    private void testDemo2() {
        //onComplete() Action0 表示一个无回调参数的Action；
        Action onCompleteAction = new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "complete");
            }
        };
        //onNext(T t) Action1 表示一个含有一个回调参数的Action；
        Consumer<String> onNextAction = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.i(TAG, s);
            }
        };
        //onError(Throwable t) 每个Action，都有一个 call() 方法，通过泛型，来指定对应参数的类型；
        Consumer<Throwable> onErrorAction = new Consumer<Throwable>() {

            @Override
            public void accept(Throwable throwable) {

            }
        };
        // 创建一个observable
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //发送一个"hello World"事件
                e.onNext("local picture!");
                //事件发送完成
                e.onComplete();
            }
        });
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);

        //从res/mipmap中取出一张图片，显示在界面上。
        MyToast.showtoast(this, "从res/mipmap中取出一张图片，显示在界面上。");
        Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Drawable> e) throws Exception {

                Drawable drawable = ContextCompat.getDrawable(RxjavaTestActivity.this, R.mipmap.a3);

                e.onNext(drawable);

                e.onComplete();

            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Drawable drawable) {
                mBinding.ivLogo.setImageDrawable(drawable);
            }
        });

    }

    //基本用法
    private void testDemo1() {
        MyToast.showtoast(this, "基本用法");
        //不适用lambda
        Observable.just("Hello World!")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s + "I am ldh!";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        Log.i(TAG, s);
                    }
                });

        // 创建一个observable
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //发送一个"hello World"事件
                e.onNext("Hello word!");
                //事件发送完成
                e.onComplete();
            }
        });
        // 创建一个observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onComplete() {
                LogUtil.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.i(TAG, "observer->" + s);
            }
        };

        //订阅事件
        observable.subscribe(observer);

    }
}
