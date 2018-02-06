package com.kfgame.sdk.request;

import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.common.Encryption;
import com.kfgame.sdk.dialog.SdkDialogViewManager;
import com.kfgame.sdk.model.GankModel;
import com.kfgame.sdk.model.GankResponse;
import com.kfgame.sdk.model.KFGameResponse;
import com.kfgame.sdk.okgo.callback.JsonCallback;
import com.kfgame.sdk.okgo.callback.NewsCallback;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.AppSysUtil;
import com.kfgame.sdk.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

/**
 * Created by Tobin on 2018/1/31.
 */

public class AccountRequest {
    private static AccountRequest ourInstance = null;

    public static AccountRequest getInstance() {
        if (ourInstance == null) {
            synchronized (AccountRequest.class) {
                if (ourInstance == null) {
                    ourInstance = new AccountRequest();
                }
            }
        }
        return ourInstance;
    }

    private void login(String userName, String passWord) {
        OkGo.<KFGameResponse<KFGameUser>>get(Config.NORMAL_LOGIN)
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

    private void PhoneRegister(String phoneNumber, String passWord, String verificationCode) {
        OkGo.<KFGameResponse<KFGameUser>>get(Config.PHONE_REGISTER)
                .tag(this)
                .headers("platform", "Android")
                .params("phoneNumber", phoneNumber)
                .params("passWord", passWord)
                .params("verificationCode", verificationCode)
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

                    }
                });
    }

    public void requestTest() {
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

    //send_identifying_code
    public void requestSendIdentifyCode(String phoneNumber) {
        String appId = Config.APP_ID;
        String channelId = Config.CHANNEL_ID;
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String phone = phoneNumber;
        String udid = AppSysUtil.getAndroidID(KFGameSDK.getInstance().getActivity());
        String version = Config.API_VERSION;
        String sign = Encryption.md5Crypt(appId + channelId + phone + timestamp + udid + version + 1);
        LogUtil.e("Tobin: sign " + "appId: " + appId + " channelId: " + channelId + " phone: "
                + phone + "timestamp: " + timestamp + " udid: " + udid + " version: " + version);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);
        OkGo.<String>post(Config.SEND_IDENTIFY_CODE)
                .params("appId", appId)
                .params("channelId", channelId)
                .params("timestamp", timestamp)
                .params("sign", sign)
                .params("version", version)
                .params("udid", udid)
                .params("phone", phoneNumber)
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtil.e("Tobin requestTest onSuccess: " + "/message: " + response.message() + "/code: "
                                + response.code() + "/body:" + response.body());
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
                        LogUtil.e("Tobin requestTest convertResponse" + response.body() != null ? response.body().string() : "ddd");
                        return null;
                    }
                });
    }

    //send_identifying_code
    public void requestSendIdentifyCode2(String phoneNumber) {
        String appId = "1";
        String channelId = "1";
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String phone = phoneNumber;
        String udid = AppSysUtil.getAndroidID(KFGameSDK.getInstance().getActivity());
        String version = "1.0.0";
        String sign = Encryption.md5Crypt(appId + channelId + phone + timestamp + udid + version + 1);
        LogUtil.e("Tobin: sign " + "appId: " + appId + " channelId: " + channelId + " phone: "
                + phone + "timestamp: " + timestamp + " udid: " + udid + " version: " + version);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);
        OkGo.<KFGameResponse<String>>post(Config.SEND_IDENTIFY_CODE)
                .params("appId", appId)
                .params("channelId", channelId)
                .params("timestamp", timestamp)
                .params("sign", sign)
                .params("version", version)
                .params("udid", udid)
                .params("phone", phoneNumber)
                .execute(new JsonCallback<KFGameResponse<String>>() {
                    @Override
                    public void onStart(Request<KFGameResponse<String>, ? extends Request> request) {
                        LogUtil.e("Tobin requestTest onStart");
                    }

                    @Override
                    public void onSuccess(Response<KFGameResponse<String>> response) {
                        LogUtil.e("Tobin requestTest onSuccess: " + "/message: " +
                                response.message() + "/code: " + response.code() + "/body:" + response.body());
                        Toast.makeText(KFGameSDK.getInstance().getActivity(), "验证码发送成功，请注意查收！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Response<KFGameResponse<String>> response) {
                        LogUtil.e("Tobin requestTest onError: " + response.getException() + " /message:" + response.message());
                        Toast.makeText(KFGameSDK.getInstance().getActivity(), "验证码发送失败," + response.body().msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        LogUtil.e("Tobin requestTest onFinish");
                    }

                    @Override
                    public KFGameResponse<String> convertResponse(okhttp3.Response response) throws Throwable {
                        return super.convertResponse(response);
                    }
                });
    }

    public void requestTestString() {
        OkGo.<String>get(Config.URL_GANK_BASE).execute(new AbsCallback<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e("Tobin requestTest onSuccess " + "code: " + response.code()
                        + " /body:" + response.body() == null ? "response is null" : response.body().toString());
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
                LogUtil.e("Tobin requestTest convertResponse" + response.body() != null ? response.body().string() : "ddd");
                return null;
            }
        });
    }

}
