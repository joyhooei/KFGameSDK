package com.kfgame.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.Toast;

import com.kfgame.sdk.callback.SDKLoginListener;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.dialog.AccountDialog;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.ResourceUtil;

/**
 * Created by Tobin on 2018/1/29.
 */

public class KFGameSDK {
    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }


    private Activity activity;
    private Context context;

    private SDKLoginListener sdkLoginListener;

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

    public void initSDK(Activity activity){
        this.activity = activity;
        ResourceUtil.init(activity);
        checkSdkCallMethod();
    }

    public void sdkLogin() {
        if (activity.isFinishing())
            return;

        LogUtil.d("Tobin sdkLogin");
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

    private void checkSdkCallMethod() {
        StackTraceElement stack[] = new Throwable().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            if (stack[i].getClassName().equals(Activity.class.getName()) && stack[i].getMethodName().equals("performCreate")) {
                return;
            }
        }
        Toast.makeText(activity, "SDK.initSDK必须在主Activity的onCreate中调用",Toast.LENGTH_LONG).show();
    }

    public SDKLoginListener getSDKLoginListener() {
        return sdkLoginListener;
    }

    public void setGamaterSDKListener(SDKLoginListener sdkLoginListener) {
        this.sdkLoginListener = sdkLoginListener;
    }

}
