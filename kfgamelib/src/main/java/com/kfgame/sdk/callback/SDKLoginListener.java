package com.kfgame.sdk.callback;

import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.LogUtil;

public interface SDKLoginListener {
	void onLoginSuccess(KFGameUser user);

	void onLogoutSuccess();

	void onLoginCancel();

	void onLoginError();
}
