package com.kfgame.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.util.SPUtils;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class AutoLoginView extends BaseLinearLayout {
	private boolean canQuickLogin;
	private KFGameUser user;

	public AutoLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AutoLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoLoginView(Context context) {
		super(context);
	}

	private TextView tv_account;
	private SdkTipsTextView switchLogin;

	@Override
	public void initView() {
		tv_account = (TextView) findViewById(findId("tv_phone_account"));

		switchLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("tv_switch_account_login"));
		switchLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				startView(NormalLoginView.createView(getContext()));
				// 发送登录请求
//                AccountRequest.getInstance().normalLogin(username, password);

			}
		});

		String curAccount =(String) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_USERNAME_KEY,"18665972556");
		tv_account.setText("当前账号" + curAccount);


	}

	public static AutoLoginView createView(Context ctx) {
		if (ctx == null)
			return null;
		AutoLoginView view = (AutoLoginView) LayoutInflater.from(ctx).inflate(findLayoutId("kfgame_sdk_view_auto_login"), null);
		// 初始化界面元素和事件
		view.initView();
		return view;
	}

	@Override
	public boolean interceptOnBackEvent() {
		return true;
	}

	@Override
	public boolean isMenuEnable() {
		return true;
	}

	@Override
	public String getViewTitle() {
		return "登陆";
	}
}
