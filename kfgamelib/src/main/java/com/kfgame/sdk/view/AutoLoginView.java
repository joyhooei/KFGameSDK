package com.kfgame.sdk.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.util.SPUtils;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class AutoLoginView extends BaseLinearLayout {

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

    private boolean isLogin = false;

	@Override
	public void initView() {
		tv_account = (TextView) findViewById(findId("tv_phone_account"));

		switchLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("tv_switch_account_login"));
		switchLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
                isLogin = true;
//				OkGo.getInstance().cancelTag(AccountRequest.getInstance());
				startView(NormalLoginView.createView(getContext()));

			}
		});

		final String curAccount =(String) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_USERNAME_KEY,"18665972556");
		final String password = (String) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_PASSWORD_KEY,"");
		tv_account.setText("当前账号" + curAccount);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
                if (!isLogin) {
                    AccountRequest.getInstance().normalLogin(curAccount, password, true);
                }
			}
		}, 2000);
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
