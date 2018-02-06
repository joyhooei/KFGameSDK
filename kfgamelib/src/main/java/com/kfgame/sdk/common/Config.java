package com.kfgame.sdk.common;

import android.content.Context;

/**
 * Created by Tobin on 2018/1/29.
 */

public class Config {
    public static String API_VERSION = "1.0.0";
    public static String sdkTitle = "KFGameSDK";
    public static String SDK_VERSION = "1.0.0";
    // 0 test、 2 pre 、 其它正式
    public static int isTestMode = 1;

    public static String CHANNEL_ID = "1";
    public static String APP_ID = "1";

    public static String URL_BASE_V1 = "http://192.168.1.88:8081/v1/";
    public static String URL_BASE_V2 = "http://192.168.1.88:8081/v2/";

    public static String SEND_IDENTIFY_CODE = URL_BASE_V1 + "account/sendSmsCode";
    public static String NORMAL_LOGIN = URL_BASE_V1 + "account/sendSmsCode";
    public static String PHONE_REGISTER = URL_BASE_V1 + "account/sendSmsCode";
    public static String FORGET_PASSWORD = URL_BASE_V1 + "account/sendSmsCode";
    public static String QUICK_LOGIN = URL_BASE_V1 + "account/sendSmsCode";
    public static String THIRD_LOGIN = URL_BASE_V1 + "account/sendSmsCode";

    public static final String URL_GANK_BASE = "http://gank.io/api/data/Android/10/1";

}
