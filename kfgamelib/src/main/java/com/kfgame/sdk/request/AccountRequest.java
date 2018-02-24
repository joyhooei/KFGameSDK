package com.kfgame.sdk.request;

import android.content.Context;
import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.common.Encryption;
import com.kfgame.sdk.dialog.SdkDialogViewManager;
import com.kfgame.sdk.model.KFGameResponse;
import com.kfgame.sdk.model.SimpleResponse;
import com.kfgame.sdk.okgo.callback.JsonCallback;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.request.listener.HttpEventListener;
import com.kfgame.sdk.util.DeviceUtils;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by Tobin on 2018/1/31.
 */

public class AccountRequest {
    private static AccountRequest ourInstance = null;
    private KFGameUser kfGameUser;

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

    public void normalLogin(String userName, final String passWord, final boolean isFastLogin) {
        Context context = KFGameSDK.getInstance().getActivity();
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("appId",Config.APP_ID);
        paraMap.put("channelId",Config.CHANNEL_ID);
        paraMap.put("userName",userName);
        paraMap.put("password", passWord);
        paraMap.put("udid",DeviceUtils.getUniqueId(context));
        paraMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paraMap.put("version",Config.API_VERSION);

        String signpre = "";
        PostRequest postRequest = OkGo.<KFGameResponse<KFGameUser>>post(Config.NORMAL_LOGIN);
        postRequest.tag(getInstance());
        for (Map.Entry<String, String> entry: paraMap.entrySet()) {
            signpre +=entry.getKey() + "=" + entry.getValue() + "&";
            postRequest.params(entry.getKey(),entry.getValue());
            LogUtil.e("Tobin getKey: " + entry.getKey() + " getValue: " + entry.getValue());
        }

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + "+key");
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);
        postRequest.upJson(jsons);

        postRequest.execute(new JsonCallback<KFGameResponse<KFGameUser>>() {
            @Override
            public void onStart(Request<KFGameResponse<KFGameUser>, ? extends Request> request) {
                LogUtil.e("Tobin: normalLogin onStart: ");
                SdkDialogViewManager.showLoadingView();
            }

            @Override
            public void onError(Response<KFGameResponse<KFGameUser>> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin normalLogin onError" + error);
                LogUtil.e("Tobin: normalLogin onError: response code: " + response.code());
                if (response.code() >= -1){
                    getHttpEventListener().requestDidFailed("无法连接服务器");
                }else {
                    getHttpEventListener().requestDidFailed(error);
                }
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: normalLogin onFinish: ");
                SdkDialogViewManager.hideLoadingView();
            }

            @Override
            public void onSuccess(Response<KFGameResponse<KFGameUser>> response) {
                kfGameUser = response.body().data;
                LogUtil.e("Tobin: normalLogin onSuccess: " + kfGameUser.toString() + "　Uid：" +kfGameUser.getUid() + " Token: " + kfGameUser.getToken());
                KFGameSDK.getInstance().getSDKLoginListener().onLoginSuccess(kfGameUser);
                SdkDialogViewManager.dialogDismiss();

                // 用户名
                SPUtils.put(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_USERNAME_KEY,"" + kfGameUser.getUserName());

                // 密码
                SPUtils.put(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_PASSWORD_KEY,"" + passWord);
                LogUtil.e("Tobin SPUtils save username " + kfGameUser.getUserName());

                // 自动登录标记
                SPUtils.put(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_ISAUTO_KEY, true);
                Config.isAotuLogin = true;

                // 密码长度
//                SPUtils.put(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_PASSWORD_LENGTH_KEY, passWord.length());

                //登陆时间
                SimpleDateFormat time = new SimpleDateFormat("YYYY-MM-dd");
                SPUtils.put(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_LOGIN_TIME_KEY, time.format(new Date()));
            }
        });

    }

    public void modifyPassword(String phoneNumber, String passWord, String verificationCode) {
        Context context = KFGameSDK.getInstance().getActivity();
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("appId",Config.APP_ID);
        paraMap.put("channelId",Config.CHANNEL_ID);
        paraMap.put("mobile",phoneNumber);
        paraMap.put("newPwd", Encryption.md5Crypt(passWord));
        paraMap.put("smsCode",verificationCode);
        paraMap.put("udid",DeviceUtils.getUniqueId(context));
        paraMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paraMap.put("version",Config.API_VERSION);

        String signpre = "";
        PostRequest postRequest = OkGo.<SimpleResponse>post(Config.MODIFY_PASSWORD);

        for (Map.Entry<String, String> entry: paraMap.entrySet()) {
            signpre +=entry.getKey() + "=" + entry.getValue() + "&";
            postRequest.params(entry.getKey(),entry.getValue());
            LogUtil.e("Tobin getKey: " + entry.getKey() + " getValue: " + entry.getValue());
        }

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + "+key");
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);
        postRequest.upJson(jsons);
        postRequest.tag("modifyPassword");
        postRequest.execute(new JsonCallback<SimpleResponse>() {
            @Override
            public void onStart(Request<SimpleResponse, ? extends Request> request) {
                LogUtil.e("Tobin: modifyPassword onStart: ");
                SdkDialogViewManager.showLoadingView();
            }

            @Override
            public void onError(Response<SimpleResponse> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin onError" + error);
                LogUtil.e("Tobin: modifyPassword onError: response code: " + response.code());
                if (response.code() >= 500){
                    getHttpEventListener().requestDidFailed("服务器错误");
                }else {
                    getHttpEventListener().requestDidFailed(error);
                }
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: modifyPassword onFinish: ");
                SdkDialogViewManager.hideLoadingView();

            }

            @Override
            public void onSuccess(Response<SimpleResponse> response) {
                getHttpEventListener().requestDidSuccess();
                LogUtil.e("Tobin: modifyPassword onSuccess: ");
                SdkDialogViewManager.doBackPressed();
            }
        });

    }

    public void phoneRegister(String phoneNumber, String passWord, String verificationCode) {
        Context context = KFGameSDK.getInstance().getActivity();
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("appId",Config.APP_ID);
        paraMap.put("channelId",Config.CHANNEL_ID);
        paraMap.put("mobile",phoneNumber);
        paraMap.put("password", Encryption.md5Crypt(passWord));
        paraMap.put("smsCode",verificationCode);
        paraMap.put("udid",DeviceUtils.getUniqueId(context));
        paraMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paraMap.put("version",Config.API_VERSION);

        String signpre = "";
        PostRequest postRequest = OkGo.<SimpleResponse>post(Config.PHONE_REGISTER);

        for (Map.Entry<String, String> entry: paraMap.entrySet()) {
            signpre +=entry.getKey() + "=" + entry.getValue() + "&";
            postRequest.params(entry.getKey(),entry.getValue());
            LogUtil.e("Tobin getKey: " + entry.getKey() + " getValue: " + entry.getValue());
        }

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + "+key");
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);
        postRequest.upJson(jsons);
        postRequest.tag("phoneRegister");
        postRequest.execute(new JsonCallback<KFGameResponse<KFGameUser>>() {
            @Override
            public void onStart(Request<KFGameResponse<KFGameUser>, ? extends Request> request) {
                LogUtil.e("Tobin: phoneRegister onStart: ");
                SdkDialogViewManager.showLoadingView();
            }

            @Override
            public void onError(Response<KFGameResponse<KFGameUser>> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin onError" + error);
                LogUtil.e("Tobin: phoneRegister onError: ");
                getHttpEventListener().requestDidFailed(error);
                KFGameSDK.getInstance().getSDKLoginListener().onLoginFail(error);
                SdkDialogViewManager.dialogDismiss();
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: phoneRegister onFinish: ");
                SdkDialogViewManager.hideLoadingView();
            }

            @Override
            public void onSuccess(Response<KFGameResponse<KFGameUser>> response) {
                kfGameUser = response.body().data;
                KFGameSDK.getInstance().getSDKLoginListener().onLoginSuccess(kfGameUser);
                LogUtil.e("Tobin: phoneRegister onSuccess: " + kfGameUser.toString() +kfGameUser.getUid() + kfGameUser.getToken());
                SdkDialogViewManager.dialogDismiss();
            }
        });
    }

    //send_identifying_code
    public void requestSendIdentifyCode(String phoneNumber) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("appId",Config.APP_ID);
        paraMap.put("channelId",Config.CHANNEL_ID);
        paraMap.put("mobile", phoneNumber);
        paraMap.put("udid",DeviceUtils.getUniqueId(KFGameSDK.getInstance().getActivity()));
        paraMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paraMap.put("version",Config.API_VERSION);

        String signpre = "";
        PostRequest postRequest = OkGo.<SimpleResponse>post(Config.SEND_IDENTIFY_CODE);
        postRequest.cacheMode(CacheMode.NO_CACHE);
        for (Map.Entry<String, String> entry: paraMap.entrySet()) {
            signpre +=entry.getKey() + "=" + entry.getValue() + "&";
            postRequest.params(entry.getKey(),entry.getValue());
            LogUtil.e("Tobin getKey: " + entry.getKey() + " getValue: " + entry.getValue());
        }

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + "+key");
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);

        postRequest.upJson(jsons);

        postRequest.tag("requestSendIdentifyCode");
        postRequest.execute(new JsonCallback<SimpleResponse>() {
            @Override
            public void onStart(Request<SimpleResponse, ? extends Request> request) {
                LogUtil.e("Tobin: sdkInit onStart: ");
            }

            @Override
            public void onError(Response<SimpleResponse> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin requestSendIdentifyCode onError" + error);
                getHttpEventListener().requestDidFailed(error);
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: requestSendIdentifyCode onFinish: ");
            }

            @Override
            public void onSuccess(Response<SimpleResponse> response) {
                LogUtil.e("Tobin: requestSendIdentifyCode onSuccess: ");
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"验证码发送成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private HttpEventListener httpEventListener;
    public HttpEventListener getHttpEventListener() {
        return httpEventListener;
    }

    public void setHttpEventListener(HttpEventListener httpEventListener) {
        this.httpEventListener = httpEventListener;
    }

}
