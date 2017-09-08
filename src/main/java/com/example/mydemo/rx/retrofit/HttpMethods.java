package com.example.mydemo.rx.retrofit;

import android.content.Context;

import com.ldh.androidlib.net.exception.ExceptionEngine;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ldh on 2017/8/11.
 * 用一个单例来封装该对象，在构造方法中创建Retrofit和对应的Service。
 * 如果需要访问不同的基地址，那么你可能需要创建多个Retrofit对象，或者干脆根据不同的基地址封装不同的HttpMethod类。
 * <p>
 * <p>
 * 现在我们再写一个新的网络请求，步骤是这样的：
 * 1. 在Service中定义一个新的方法。
 * 2. 在HttpMethods封装对应的请求（代码基本可以copy）
 * 3. 创建一个SubscriberOnNextListener处理请求数据并刷新UI。
 */

public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 20;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;//10M
    private Retrofit retrofit;
    private MovieService movieService;
    private String baseUrl = MovieService.BASE_URL;
    private OkHttpClient okHttpClient;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, DEFAULT_TIMEOUT, TimeUnit.SECONDS));

        builder.addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(loggingInterceptor);

        okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        movieService = retrofit.create(MovieService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param observer 由调用者传过来的观察者对象
     * @param start    起始位置
     * @param count    获取长度
     */
    public void getTopMovie(Observer<List<MovieEntity.SubjectsBean>> observer, int start, int count) {
        movieService.getTopMovieRxMy(start, count)
                .map(new ServerResultFunc<List<MovieEntity.SubjectsBean>>())
                .compose(getTransformer())
                .subscribe(observer);
    }

    /**
     * 这里可以对数据异常进行处理，也可以处理线程
     *
     * @return
     */
    private <T> ObservableTransformer<T, T> getTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .onErrorResumeNext(new HttpResultFunc<>())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public HttpMethods setCache(Context appContext) {
        final File baseDir = appContext.getApplicationContext().getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            okHttpClient.newBuilder().cache(new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
        }
        return this;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class ServerResultFunc<T> implements Function<HttpResult<T>, T> {
        @Override
        public T apply(@NonNull HttpResult<T> httpResult) throws Exception {
//            if (httpResult.getResultCode() != 200) {
//                try {
//                    throw new ApiException(httpResult.getResultCode());
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
            return httpResult.getSubjects();
        }
    }


    private class HttpResultFunc<T> implements Function<Throwable, Observable<T>> {

        @Override
        public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }

}
