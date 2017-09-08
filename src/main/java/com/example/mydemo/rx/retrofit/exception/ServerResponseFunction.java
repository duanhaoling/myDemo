package com.example.mydemo.rx.retrofit.exception;

import com.ldh.androidlib.net.config.HttpResult;
import com.ldh.androidlib.net.exception.ServerException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by ldh on 2017/8/14.
 */

public class ServerResponseFunction<T> implements Function<HttpResult<T>,T> {
    @Override
    public T apply(@NonNull HttpResult<T> response) throws Exception {
        //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
        if (response.state != 0) {
            //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
            throw new ServerException(response.state,response.message);
        }
        //服务器请求数据成功，返回里面的数据实体
        return response.data;
    }
}
