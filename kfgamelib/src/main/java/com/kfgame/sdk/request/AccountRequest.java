package com.kfgame.sdk.request;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.dialog.SdkDialogViewManager;
import com.kfgame.sdk.model.GankModel;
import com.kfgame.sdk.model.GankResponse;
import com.kfgame.sdk.model.KFGameResponse;
import com.kfgame.sdk.okgo.callback.JsonCallback;
import com.kfgame.sdk.okgo.callback.NewsCallback;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

/**
 * Created by Tobin on 2018/1/31.
 */

public class AccountRequest {
    private static AccountRequest ourInstance = null;

    public static AccountRequest getInstance() {
        if( ourInstance == null){
            synchronized (AccountRequest.class){
                if( ourInstance == null){
                    ourInstance = new AccountRequest();
                }
            }
        }
        return ourInstance;
    }

    private void login(String userName, String passWord){
        OkGo.<KFGameResponse<KFGameUser>>get(Config.URL_LOGIN)
                .tag(this)//
                .headers("header", "headerValue")//
                .params("userName", "userName")
                .params("passWord", "passWord")
                .execute(new JsonCallback<KFGameResponse<KFGameUser>>() {

                    @Override
                    public void onSuccess(Response<KFGameResponse<KFGameUser>> response) {
                        LogUtil.e("" + response.body());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }



                    @Override
                    public void onError(Response<KFGameResponse<KFGameUser>> response) {
//                        handleError(response);
                    }
                });
    }

    private void PhoneRegister(String phoneNumber, String passWord, String verificationCode){
        OkGo.<KFGameResponse<KFGameUser>>get(Config.URL_LOGIN)
                .tag(this)
                .headers("platform", "Android")
                .params("phoneNumber", phoneNumber)
                .params("passWord", passWord)
                .params("verificationCode",verificationCode)
                .execute(new JsonCallback<KFGameResponse<KFGameUser>>() {

                    @Override
                    public void onSuccess(Response<KFGameResponse<KFGameUser>> response) {
                        LogUtil.e("" + response.body());
//                        handleResponse(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onError(Response<KFGameResponse<KFGameUser>> response) {
//                        handleError(response);
                    }
                });
    }

    public void requestTest(){
        OkGo.<GankResponse<List<GankModel>>>get(Config.URL_GANK_BASE)
                .execute(new NewsCallback<GankResponse<List<GankModel>>>() {
                    @Override
                    public void onStart(Request<GankResponse<List<GankModel>>, ? extends Request> request) {
                        SdkDialogViewManager.showLoadingView();
                        LogUtil.e("Tobin requestTest onStart");
                    }

                    @Override
                    public void onSuccess(Response<GankResponse<List<GankModel>>> response) {
                        List<GankModel> results = response.body().results;
                        LogUtil.e("Tobin requestTest onSuccess");
                        KFGameSDK.getInstance().getSDKLoginListener().onLoginSuccess(new KFGameUser());
                    }

                    @Override
                    public void onError(Response<GankResponse<List<GankModel>>> response) {
                        //网络请求失败的回调,一般会弹个Toast
                        LogUtil.e("Tobin requestTest onError");
                        LogUtil.e("Tobin requestTest onError: " + response.getException() + "" + response.getRawResponse());
                        KFGameSDK.getInstance().getSDKLoginListener().onLoginError();
                    }

                    @Override
                    public void onFinish() {
                        LogUtil.e("Tobin requestTest onFinish");
                        SdkDialogViewManager.hideLoadingView();
                    }
                });
    }

    public void requestTestString(){
        OkGo.<String>get(Config.URL_GANK_BASE)
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtil.e("Tobin requestTest onSuccess" + response.body());
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        LogUtil.e("Tobin requestTest onStart");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //网络请求失败的回调,一般会弹个Toast
                        LogUtil.e("Tobin requestTest onError");
                        LogUtil.e("Tobin requestTest onError: " + response.getException() + "" + response.getRawResponse());
                    }

                    @Override
                    public void onFinish() {
                        LogUtil.e("Tobin requestTest onFinish");
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtil.e("Tobin requestTest convertResponse" + response.body() != null ? response.body().string():"ddd");
                        return null;
                    }
                });
    }

}
