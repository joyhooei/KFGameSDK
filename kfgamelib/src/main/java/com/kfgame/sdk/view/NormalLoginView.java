package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.util.SPUtils;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class NormalLoginView extends BaseLinearLayout {

    private String spUserName;
    private boolean isAutoLogin;
    private String spPassWord;
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
				if (Config.isAotuLogin && !spUserName.equals(str)) {
					Config.isAotuLogin = false;
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
				if (hasFocus && Config.isAotuLogin) {
					Config.isAotuLogin = false;
					edt_password.setText("");
				}
			}
		});
		edt_password.setTypeface(Typeface.DEFAULT);
		accountLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("tv_account_login"));
		accountLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {

				String username = edt_account.getText().toString().trim();
				if (!checkAccount(username)) {
					return;
				}
				String password = edt_password.getText().toString().trim();
				if (checkPassword(password) != 1) {
					return;
				}

				setHttpCallback();
				// 发送登录请求

                if (isAutoLogin){
                    spPassWord =(String) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_PASSWORD_KEY,"");
                    AccountRequest.getInstance().normalLogin(username, spPassWord);
                }else {
                    AccountRequest.getInstance().normalLogin(username, password);
                }
			}
		});

		// 注册一个
		findViewById(findId("ll_account_register")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				startView(PhoneRegisterView.createView(getContext()));
			}
		});

		// 忘记密码
		findViewById(findId("tv_forget_password")).setVisibility(View.VISIBLE);
		findViewById(findId("tv_forget_password")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				startView(ModifyPasswordView.createView(getContext()));
			}
		});

        spUserName =(String) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_USERNAME_KEY, "");

        isAutoLogin = (Boolean) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_ISAUTO_KEY, false);

        int length =(int) SPUtils.get(KFGameSDK.getInstance().getActivity(),SPUtils.LOGIN_PASSWORD_LENGTH_KEY, 6);
        String lengthStr = "";
        for (int i = 0; i < length;  i++){
            lengthStr += "*";
        }
        LogUtil.e("Tobin" + spUserName + " passwoed: " +  spPassWord);
        if (isAutoLogin && !"".equals(spUserName)) {
            edt_account.setText(spUserName);
            edt_password.setText(lengthStr);
        }

	}

	public int checkPassword(String password) {
		if (password == null || password.length() == 0) {
			showError(edt_password, edt_password.getHint().toString());
			return 0;
		}
		if (password.length() < 6) {
			showError(edt_password, "密码长度必须大于6");
			return -1;
		}
		return 1;
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

	public static NormalLoginView createView(Context ctx) {
		if (ctx == null)
			return null;
		NormalLoginView view = (NormalLoginView) LayoutInflater.from(ctx).inflate(findLayoutId("kfgame_sdk_view_normal_login"), null);
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
		return "手机登陆";
	}
}
