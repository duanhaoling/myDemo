package com.example.mydemo.rx.retrofit;


import com.example.mydemo.rx.retrofit.MovieEntity.SubjectsBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ldh on 2017/8/11.
 */

public interface MovieService {

    String BASE_URL = "https://api.douban.com/v2/movie/";


    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);


    @GET("top250")
    Observable<MovieEntity> getTopMovieRx(@Query("start") int start, @Query("count") int count);

    /**
     * https://api.douban.com/v2/movie/top250?start=0&count=10
     *
     * @param start
     * @param count
     * @return
     */
    @GET("top250")
    Observable<HttpResult<List<SubjectsBean>>> getTopMovieRxMy(@Query("start") int start, @Query("count") int count);


    @GET("demo")
    Observable<HttpResult<TestData>> login();
}
