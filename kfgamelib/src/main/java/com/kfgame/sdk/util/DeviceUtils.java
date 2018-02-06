package com.kfgame.sdk.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Tobin on 2018/2/5.
 */

public class DeviceUtils {

    /**
     * 获取Android设备的唯一标识符
     * AndroidId 和 Serial Number 组成
     * @param context
     * @return
     */
    public static String getUniqueId(Context context){
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return MD5Utils.encode(id);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    /**
     *获取AndroidID
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
