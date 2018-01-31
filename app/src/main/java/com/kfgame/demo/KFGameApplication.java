package com.kfgame.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.kfgame.sdk.KFGameSDK;


/**
 * @author Tobin
 *
 */
public class KFGameApplication extends Application{
	
	public void onCreate(){
		super.onCreate();
		KFGameSDK.getInstance().initSDK(this);
	}
	
	/**
	 * 注意：这个attachBaseContext方法是在onCreate方法之前调用的
	 */
	public void attachBaseContext(Context base){
		super.attachBaseContext(base);

	}
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}

	
}
