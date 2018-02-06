package com.kfgame.sdk.okgo.callback;

import com.google.gson.stream.JsonReader;
import com.kfgame.sdk.model.GankResponse;
import com.kfgame.sdk.okgo.Convert;
import com.kfgame.sdk.util.LogUtil;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class NewsCallback<T> extends AbsCallback<T> {

    /**
     * 这里的数据解析是根据 http://gank.io/api/data/Android/10/1 返回的数据来写的
     * 实际使用中,自己服务器返回的数据格式和上面网站肯定不一样,所以以下是参考代码,根据实际情况自己改写
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        LogUtil.e("convertResponse0000000000");
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        LogUtil.e("convertResponse111111111");
        if (!(type instanceof ParameterizedType))
            throw new IllegalStateException("没有填写泛型参数");
        LogUtil.e("convertResponse222222222222");
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        LogUtil.e("convertResponse44444441" + jsonReader!=null ? jsonReader.toString(): "jsonReader is null");
        Type rawType = ((ParameterizedType) type).getRawType();
        LogUtil.e("convertResponse44444442" + rawType!=null ? rawType.toString(): "rawType is null");
        LogUtil.e("convertResponse3333333333");
        if (rawType == GankResponse.class) {
            LogUtil.e("convertResponse4444444" + response.body().string());
            GankResponse gankResponse = Convert.fromJson(jsonReader, type);
//            GankResponse gankResponse = Convert.fromJson(jsonReader, GankResponse.class);
//            GankResponse gankResponse = JsonUtil.Json2T(response.body().string(),GankResponse.class);

            if (gankResponse == null){
                LogUtil.e("convertResponse  gankResponse is null");
            }
            LogUtil.e("convertResponse44444445");
            if (!gankResponse.error) {
                response.close();
                LogUtil.e("convertResponse5555555555");
                //noinspection unchecked
                return (T) gankResponse;
            } else {
                response.close();
                LogUtil.e("convertResponse6666666666");
                throw new IllegalStateException("服务端接口错误");
            }
        } else {
            LogUtil.e("convertResponse777777");
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }
}
