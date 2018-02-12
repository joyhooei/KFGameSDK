package com.kfgame.sdk.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.util.ResourceUtil;
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

	private EditText edt_account;
	private SdkTipsTextView accountLogin;

	@Override
	public void initView() {
		edt_account = (EditText) findViewById(findId("edt_phone_account"));
		edt_account.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				String str = edt_account.getText().toString().trim();
				if (isShown() && !hasFocus) {
					checkAccount(str);
				}
			}
		});
		edt_account.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = edt_account.getText().toString().trim();
				if (canQuickLogin && !user.getEmail().equals(str)) {
					canQuickLogin = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		accountLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("tv_switch_account_login"));
		accountLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				if (canQuickLogin && user != null) {
//					requestApi(SdkHttpRequest.eLoginRequest(user.getUserid(), user.getToken()));
					return;
				}
				String username = edt_account.getText().toString().trim();
				if (!checkAccount(username)) {
					return;
				}


				setHttpCallback();
				// 发送登录请求
//                AccountRequest.getInstance().normalLogin(username, password);

			}
		});



	}

	private boolean checkAccount(String account) {
		if (account == null || account.length() == 0) {
			showError(edt_account, "请输入账号");
			return false;
		}else if (!checkPhone(account)) {
			showError(edt_account, "请输入正确的电话号码");
			return false;
		}
		return true;
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
