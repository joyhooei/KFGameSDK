package com.kfgame.sdk.common;


/**
 * Created by Tobin on 2018/1/29.
 */

public class Config {
    // Used to load the 'kfgamelib' library on application startup.
//    static {
//        System.loadLibrary("kfgamelib");
//    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native static String getEncodeKey();
//    public native String getMd5();

    public static String API_VERSION = "1.0.0";
    public static String sdkTitle = "KFGSDK";
    public static String SDK_VERSION = "1.0.0";
    // 0 test、 2 pre 、 其它正式
    public static int isTestMode = 1;

    public static String CHANNEL_ID = "1";
    public static String APP_ID = "1";

    public static String URL_BASE_V1 = "http://120.25.243.133:8082/v1/";

    public static String URL_SDK_INIT = URL_BASE_V1 + "basic/init";
    public static String SEND_IDENTIFY_CODE = URL_BASE_V1 + "account/sendSmsCode";
    public static String NORMAL_LOGIN = URL_BASE_V1 + "account/mobileLogin";
    public static String PHONE_REGISTER = URL_BASE_V1 + "account/mobileRegister";
    public static String MODIFY_PASSWORD = URL_BASE_V1 + "account/resetPassword";
    public static String QUICK_LOGIN = URL_BASE_V1 + "";
    public static String THIRD_LOGIN = URL_BASE_V1 + "";

    public static final String encodeKey = "1";
    public static boolean isAotuLogin = false;


}
