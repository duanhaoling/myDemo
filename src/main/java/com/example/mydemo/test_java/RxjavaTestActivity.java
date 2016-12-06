package com.example.mydemo.test_java;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mydemo.R;
import com.example.mydemo.bean.Employee;
import com.example.mydemo.util.LogUtil;
import com.example.mydemo.util.MyToast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxjavaTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxjavaTestActivity";
    @Bind(R.id.testRxjava1)
    Button testRxjava1;
    @Bind(R.id.testRxjava2)
    Button testRxjava2;
    @Bind(R.id.testRxjava3)
    Button testRxjava3;
    @Bind(R.id.testRxjava4)
    Button testRxjava4;
    @Bind(R.id.testRxjava5)
    Button testRxjava5;
    @Bind(R.id.testRxjava6)
    Button testRxjava6;
    @Bind(R.id.testRxjava7)
    Button testRxjava7;
    @Bind(R.id.testRxjava8)
    Button testRxjava8;
    @Bind(R.id.testRxjava9)
    Button testRxjava9;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_test);
        ButterKnife.bind(this);
        testRxjava1.setOnClickListener(v -> {
//        testDemo1();
            Observable.just("hello word!")
                    .map(s -> s + "I am ldh!")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> Log.i(TAG, s));
        });

        testRxjava2.setOnClickListener(this);
        testRxjava3.setOnClickListener(this);
        testRxjava4.setOnClickListener(this);
        testRxjava5.setOnClickListener(this);
        testRxjava6.setOnClickListener(this);
        testRxjava7.setOnClickListener(this);
        testRxjava8.setOnClickListener(this);
        testRxjava9.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testRxjava2:
                testDemo2();
                break;
            case R.id.testRxjava3:
                testDemo3();
                break;
            case R.id.testRxjava4:
                testDemo4();
                break;
            case R.id.testRxjava5:
                testDemo5();
                break;
            case R.id.testRxjava6:
                break;
            case R.id.testRxjava7:
                break;
            case R.id.testRxjava8:
                break;
            case R.id.testRxjava9:
                break;
            default:
                break;
        }
    }

    private void testDemo5() {


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

        Observable.from(list)
                .flatMap(new Func1<Employee, Observable<Employee.Mission>>() {
                    @Override
                    public Observable<Employee.Mission> call(Employee employee) {
                        return Observable.from(employee.getMission());
                    }
                })
                .subscribe(new Subscriber<Employee.Mission>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Employee.Mission mission) {
                        LogUtil.i(TAG, mission.desc);
                    }
                });


    }

    private void testDemo3() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("http://img4.duitang.com/uploads/item/201406/28/20140628141104_PXLRN.thumb.700_0.jpeg");
            }
        }).map(new Func1<String, Drawable>() {
            @Override
            public Drawable call(String s) {

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
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        ivLogo.setImageDrawable(drawable);
                    }
                });

    }


    private void testDemo2() {
        //onComplete() Action0 表示一个无回调参数的Action；
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                Log.i(TAG, "complete");
            }
        };
        //onNext(T t) Action1 表示一个含有一个回调参数的Action；
        Action1 onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                LogUtil.i(TAG, s);
            }
        };
        //onError(Throwable t) 每个Action，都有一个 call() 方法，通过泛型，来指定对应参数的类型；
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {

            @Override
            public void call(Throwable throwable) {

            }
        };
        // 创建一个observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //发送一个"hello World"事件
                subscriber.onNext("Hello word!");
                //事件发送完成
                subscriber.onCompleted();
            }
        });
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);

        //从res/mipmap中取出一张图片，显示在界面上。
        MyToast.showtoast(this, "从res/mipmap中取出一张图片，显示在界面上。");
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {

                Drawable drawable = ContextCompat.getDrawable(RxjavaTestActivity.this, R.mipmap.a3);

                subscriber.onNext(drawable);

                subscriber.onCompleted();

            }
        }).subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onNext(Drawable drawable) {
                ivLogo.setImageDrawable(drawable);
            }
        });

    }

    //基本用法
    private void testDemo1() {
        MyToast.showtoast(this, "基本用法");
        //不适用lambda
        Observable.just("Hello World!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "I am ldh!";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, s);
                    }
                });

        // 创建一个observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //发送一个"hello World"事件
                subscriber.onNext("Hello word!");
                //事件发送完成
                subscriber.onCompleted();
            }
        });
        // 创建一个observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                LogUtil.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.i(TAG, "observer->" + s);
            }
        };
        //或者创建一个subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                LogUtil.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.i(TAG, "subscriber->" + s);
            }
        };
        //订阅事件
        observable.subscribe(observer);
        observable.subscribe(subscriber);
    }
}
