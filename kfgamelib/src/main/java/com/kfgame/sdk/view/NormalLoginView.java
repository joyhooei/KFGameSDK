package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class NormalLoginView extends BaseLinearLayout {
	private boolean canQuickLogin;
	private KFGameUser user;
	private int passwordErrorCount = 0;

	public NormalLoginView() {
		super(KFGameSDK.getInstance().getActivity());
	}

	public NormalLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public NormalLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NormalLoginView(Context context) {
		super(context);
	}

	private EditText edt_account;
	private EditText edt_password;
	private ImageView iv_qqLogin;
	private ImageView iv_wxLogin;
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
					edt_password.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edt_password = (EditText) findViewById(findId("edt_password"));
		edt_password.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && canQuickLogin) {
					canQuickLogin = false;
					edt_password.setText("");
				}
			}
		});
		edt_password.setTypeface(Typeface.DEFAULT);
		accountLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("tv_account_login"));
		accountLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"accountLogin onBaseClick",Toast.LENGTH_SHORT).show();
				if (canQuickLogin && user != null) {
//					requestApi(SdkHttpRequest.eLoginRequest(user.getUserid(), user.getToken()));
					return;
				}
				String email = edt_account.getText().toString().trim();
				if (!checkAccount(email)) {
					return;
				}
				String passwd = edt_password.getText().toString().trim();
				if (!checkPassword(passwd)) {
					return;
				}

                AccountRequest.getInstance().requestTestString();
				// 发送登录请求
//				requestApi(SdkHttpRequest.normalLoginRequest(email, passwd));
			}
		});
		// 注册一个
		findViewById(findId("ll_account_register")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				startView(PhoneRegisterView.createView(getContext()));
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"account_register onBaseClick",Toast.LENGTH_SHORT).show();
			}
		});
		// 忘记密码
		findViewById(findId("btnLoginViewForgetPw")).setVisibility(View.INVISIBLE);
		findViewById(findId("btnLoginViewForgetPw")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				startView(ModifyPasswordView.createView(getContext()));
			}
		});

		// 游客试玩
		findViewById(findId("tv_quick_login")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"游客试玩",Toast.LENGTH_SHORT).show();
//				requestApi(SdkHttpRequest.quickLoginRequest());
			}
		});

		iv_qqLogin = (ImageView) findViewById(findId("iv_icon_qq_login"));
		iv_qqLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"iv_qqLogin onBaseClick",Toast.LENGTH_SHORT).show();
//				thirdLogin((ThirdType) v.getTag());
                KFGameSDK.getInstance().QQLogin();
			}
		});

		iv_wxLogin = (ImageView) findViewById(findId("iv_icon_wechat_login"));
		iv_wxLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"iv_wxLogin onBaseClick",Toast.LENGTH_SHORT).show();
//				thirdLogin((ThirdType) v.getTag());
                KFGameSDK.getInstance().WeChatLogin();
			}
		});

	}

	private boolean checkPassword(String password) {
		if (password == null || password.length() == 0) {
			showError(edt_password, edt_password.getHint().toString());
			return false;
		}
		if (password.length() < 6) {
			showError(edt_password, "密码长度必须大于6");
			return false;
		}
		return true;
	}

	private boolean checkAccount(String account) {
		if (account == null || account.length() == 0) {
			showError(edt_account, "请输入账号");
			return false;
		} else if (!checkEmail(account)) {
			showError(edt_account, "邮箱错误");
			return false;
		} else if (!checkPhone(account)) {
			showError(edt_account, "请输入正确的电话号码");
			return false;
		}
		return true;
	}

	public static NormalLoginView createView(Context ctx) {
		if (ctx == null)
			return null;
		NormalLoginView view = (NormalLoginView) LayoutInflater.from(ctx).inflate(findLayoutId("kfgame_sdk_view_login"), null);
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
		return "账号登陆";
	}
}
