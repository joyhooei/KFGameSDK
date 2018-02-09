package com.kfgame.sdk.common;

import android.content.Context;
import android.content.res.Resources;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.SPUtil;

/**
 * Created by Tobin on 2018/2/6.
 */

public class ConfigUtil {



    public static final String LOGIN_QQ = "qq_login";
    public static final String LOGIN_WEIXIN = "weixin_login";
    public static final String LOGIN_WEIBO = "weibo_login";
    public static final String LOGIN_ALIPAY = "alipay_login";
    public static final String LOGIN_BAIDU = "baidu_login";

    public static final String MENU_ACCOUNT = "menu_account";
    public static final String MENU_HOMEPAGE = "menu_homepage";
    public static final String MENU_SERVICES = "menu_service";

    public static final String LOGIN_CLOSE = "login_close";


    public static void setConfig(String configName, boolean enable) {
        try {
//            SPUtil.setConfigSettings(KFGameSDK.getInstance().getActivity(), configName, enable ? 1 : -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getConfigEnable(Context ctx, String configName) {
        return true;

//        int enable = SPUtil.getConfigSettings(ctx, configName);
//
//        if (enable != 0) {
//            return enable == 1 ? true : false;
//        }

//        Resources res = ctx.getResources();
//        try {
//            boolean b = res.getBoolean(res.getIdentifier("enable_" + configName, "bool", ctx.getPackageName()));
//            return b;
//        } catch (Exception e) {
//            LogUtil.i(configName);
//            e.printStackTrace();
//            return false;
//        }
    }

    public static boolean getConfigEnable(String configName) {
        if (KFGameSDK.getInstance() == null)
            return false;
        Context c = KFGameSDK.getInstance().getActivity();
        if (c == null)
            return false;
        return getConfigEnable(c, configName);
    }
}
