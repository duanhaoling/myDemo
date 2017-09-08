package com.example.mydemo.rx.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydemo.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetTestActivity extends AppCompatActivity {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;
    @Bind(R.id.result_TV)
    TextView resultTV;
    Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.click_me_BN)
    public void onClick() {
//        getMovie();
        getMovieRx();
        getMovieRxhm();
//        getWindow().getDecorView().postDelayed(() -> getMovieRxhm(), 6000);
    }

    //原生retrofit进行网络请求
    private void getMovie() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                resultTV.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                resultTV.setText(t.getMessage());
            }
        });
    }


    //添加rxjava ，不封装的用法
    private void getMovieRx() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        movieService.getTopMovieRx(0, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieEntityObserver);
    }

    //将请求过程进行封装
    private void getMovieRxhm() {
        HttpMethods.getInstance().setCache(this).getTopMovie(new ProgressObserver<List<MovieEntity.SubjectsBean>>(NetTestActivity.this) {

            @Override
            public void onNext(@NonNull List<MovieEntity.SubjectsBean> subjects) {
                resultTV.setText(subjects.toString());
            }
        }, 10, 3);
    }


    //这个观察者可以复用吗？可以！Rxjava1中的subscribe不可以复用
    //提到Observer的过程是这样的。由于Subscriber一旦调用了unsubscribe方法之后，就没有用了。
    // 且当事件传递到onError或者onCompleted之后，也会自动的解绑。
    // 这样出现的一个问题就是每次发送请求都要创建新的Subscriber对象。
    Observer<MovieEntity> movieEntityObserver = new Observer<MovieEntity>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
//                        if (!d.isDisposed()) {
//                            d.dispose();
//                        }
        }

        @Override
        public void onNext(MovieEntity movieEntity) {
            resultTV.setText(movieEntity.toString());
        }

        @Override
        public void onComplete() {
            Toast.makeText(NetTestActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Throwable e) {
            resultTV.setText(e.getMessage());
        }
    };

    private Observer<MovieEntity> getMovieEntityObserver() {
        return new Observer<MovieEntity>() {
            @Override
            public void onComplete() {
                Toast.makeText(NetTestActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                resultTV.setText(e.getMessage());
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
//                        if (!d.isDisposed()) {
//                            d.dispose();
//                        }
            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                resultTV.setText(movieEntity.toString());
            }
        };
    }

}
