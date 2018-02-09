package com.kfgame.sdk.request;

import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.common.Encryption;
import com.kfgame.sdk.model.KFGameResponse;
import com.kfgame.sdk.model.SimpleResponse;
import com.kfgame.sdk.okgo.callback.JsonCallback;
import com.kfgame.sdk.pojo.InitModel;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.DeviceUtils;
import com.kfgame.sdk.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

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

    public void normalLogin(String userName, String passWord) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("appId",Config.APP_ID);
        paraMap.put("channelId",Config.CHANNEL_ID);
        paraMap.put("mobile", userName);
        paraMap.put("passWord", passWord);
        paraMap.put("udid",DeviceUtils.getUniqueId(KFGameSDK.getInstance().getActivity()));
        paraMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
        paraMap.put("version",Config.API_VERSION);

        String signpre = "";
        PostRequest postRequest = OkGo.<KFGameResponse<KFGameUser>>post(Config.NORMAL_LOGIN);
        postRequest.cacheMode(CacheMode.NO_CACHE);
        for (Map.Entry<String, String> entry: paraMap.entrySet()) {
            signpre +=entry.getKey() + "=" + entry.getValue() + "&";
            postRequest.params(entry.getKey(),entry.getValue());
            LogUtil.e("Tobin getKey: " + entry.getKey() + " getValue: " + entry.getValue());
        }

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + Config.encodeKey);
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);

        postRequest.upJson(jsons);

        postRequest.tag("initSDK");
        postRequest.execute(new JsonCallback<KFGameResponse<KFGameUser>>() {
            @Override
            public void onStart(Request<KFGameResponse<KFGameUser>, ? extends Request> request) {
                LogUtil.e("Tobin: sdkInit onStart: ");
            }

            @Override
            public void onError(Response<KFGameResponse<KFGameUser>> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin onError" + error);
                LogUtil.e("Tobin: sdkInit onError: ");
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"SDK初始化: " + error,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: sdkInit onFinish: ");
            }

            @Override
            public void onSuccess(Response<KFGameResponse<KFGameUser>> response) {
                LogUtil.e("Tobin: sdkInit onSuccess: ");
            }
        });

    }

    public void modifyPassword(String phoneNumber, String passWord, String verificationCode) {

    }

    public void phoneRegister(String phoneNumber, String passWord, String verificationCode) {

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

        LogUtil.e("Tobin: signpre md5Crypt: " + signpre + Config.encodeKey);
        String sign = Encryption.md5Crypt(signpre + Config.encodeKey);
        LogUtil.e("Tobin: sign md5Crypt: " + sign);

        paraMap.put("sign",sign);

        JSONObject jsons = new JSONObject(paraMap);

        postRequest.upJson(jsons);

        postRequest.tag("initSDK");
        postRequest.execute(new JsonCallback<SimpleResponse>() {
            @Override
            public void onStart(Request<SimpleResponse, ? extends Request> request) {
                LogUtil.e("Tobin: sdkInit onStart: ");
            }

            @Override
            public void onError(Response<SimpleResponse> response) {
                String error = response.getException().getMessage();
                LogUtil.e("Tobin onError" + error);
                LogUtil.e("Tobin: sdkInit onError: ");
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"SDK初始化: " + error,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                LogUtil.e("Tobin: sdkInit onFinish: ");
            }

            @Override
            public void onSuccess(Response<SimpleResponse> response) {
                LogUtil.e("Tobin: sdkInit onSuccess: ");
            }
        });
    }



}
