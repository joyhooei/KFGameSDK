package com.kfgame.sdk.callback;

import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.LogUtil;

public abstract class SDKLoginListener {
	public abstract void didLoginSuccess(KFGameUser user);

	public abstract void didLogout();

	public void didCancel() {
		LogUtil.d("user cancel login...");
	}
}
