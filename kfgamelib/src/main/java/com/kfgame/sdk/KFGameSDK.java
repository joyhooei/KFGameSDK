package com.kfgame.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kfgame.sdk.callback.SDKLoginListener;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.dialog.AccountDialog;
import com.kfgame.sdk.dialog.SdkDialogViewManager;
import com.kfgame.sdk.request.SDKInit;
import com.kfgame.sdk.util.AppSysUtil;
import com.kfgame.sdk.util.DeviceUtils;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.ResourceUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by Tobin on 2018/1/29.
 */

public class KFGameSDK {
    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    private SDKLoginListener sdkLoginListener;

    private Activity activity;
    private Context context;
    private Application application;



    private static KFGameSDK ourInstance  = null;

    public Context getContext() {
        if (context == null && activity != null)
            context = activity.getApplicationContext();
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public static KFGameSDK getInstance() {
        if( ourInstance == null){
            synchronized (KFGameSDK.class){
                if( ourInstance == null){
                    ourInstance = new KFGameSDK();
                }
            }
        }
        return ourInstance;
    }

    public SDKLoginListener getSDKLoginListener() {
        return sdkLoginListener;
    }

    public void setGamaterSDKListener(SDKLoginListener sdkLoginListener) {
        this.sdkLoginListener = sdkLoginListener;
    }

    private Tencent mTencent;   // QQ登陆相关
    private IWXAPI api; // 微信登录相关
    public void initSDK(Activity activity,String APP_ID, String CHANNEL_ID){
        this.activity = activity;
        Config.APP_ID = APP_ID;
        Config.CHANNEL_ID = CHANNEL_ID;
        ResourceUtil.init(activity);
        checkSdkCallMethod();

        SDKInit.getInstance().sdkInit();

    }

    public void initSDK(Application application){
        this.application = application;
        initOkGo();
    }

    public IWXAPI getIWXAPI(){
        return api;
    }

    public void QQLogin(){
        if (!mTencent.isSessionValid())
        {
            //注销登录 mTencent.logout(this);
            mTencent.login(getActivity(), "all", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    LogUtil.e("" + o.toString());


                    SdkDialogViewManager.dialogDismiss();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    public void WeChatLogin(){
        /**
         snsapi_base属于基础接口，若应用已拥有其它scope权限，则默认拥有snsapi_base的权限。
         使用snsapi_base可以让移动端网页授权绕过跳转授权登录页请求用户授权的动作，
         直接跳转第三方网页带上授权临时票据（code），但会使得用户已授权作用域（scope）仅为snsapi_base，
         从而导致无法获取到需要用户授权才允许获得的数据和基础功能。
         */
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
        req.state = "wechat_sdk_微信登录";
        api.sendReq(req);
    }




    public void sdkLogin(SDKLoginListener sdkLoginListener) {
        setGamaterSDKListener(sdkLoginListener);

        if (activity.isFinishing()) {
            getSDKLoginListener().onLoginError();
            return;
        }

        LogUtil.d("Tobin sdkLogin");
        if(isFastClick())
            return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.d("Tobin AccountDialog");
                    AccountDialog dialog = new AccountDialog(activity);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onNewIntent(Intent intent) {

    }

    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void checkSdkCallMethod() {
        StackTraceElement stack[] = new Throwable().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            if (stack[i].getClassName().equals(Activity.class.getName()) && stack[i].getMethodName().equals("performCreate")) {
                return;
            }
        }
        Toast.makeText(activity, "SDK.initSDK必须在主Activity的onCreate中调用",Toast.LENGTH_LONG).show();
    }

    /**
     * 防止控件被重复点击
     */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private void initOkGo() {
        if (application == null){
            LogUtil.e("KFGame", "必须在Application中初始化");
            return;
        }
        //统一请求头部字段
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("udid", DeviceUtils.getUniqueId(application));    //header不支持中文，不允许有特殊字符
//        headers.put("androidId", DeviceUtils.getAndroidID(application) + "");
////        headers.put("phoneModule", AppSysUtil.getSysModel() + "");
//        headers.put("platform", "android");
//        HttpParams params = new HttpParams();
//        params.put("phoneModule", AppSysUtil.getSysModel() + "");    // 获取手机型号 //param支持中文,直接传,不要自己编码
//
//        LogUtil.e("UniqueId:" + DeviceUtils.getUniqueId(application) + " AndroidID:" + DeviceUtils.getAndroidID(application) + "");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(application)));   //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));   //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));          //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(application)                    //必须调用初始化
                .setOkHttpClient(builder.build())                //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)  //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                       //全局公共头
//                .addCommonParams(params);                        //全局公共参数
    }


}
