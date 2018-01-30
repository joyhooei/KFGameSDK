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
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class NormalLoginView extends BaseLinearLayout {
	private boolean canQuickLogin;
	private KFGameUser user;
	private int passwdErrorCount = 0;

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
		edt_account = (EditText) findViewById(findId("account_edit"));
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
		edt_password = (EditText) findViewById(findId("passwd_edit"));
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
		accountLogin = (SdkTipsTextView) findViewById(ResourceUtil.getId("accountLogin"));
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
				if (!checkPasswd(passwd)) {
					return;
				}
				// 发送登录请求
//				requestApi(SdkHttpRequest.normalLoginRequest(email, passwd));
			}
		});
		// 注册一个
		findViewById(findId("btnLoginViewRegister")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"accountLogin onBaseClick",Toast.LENGTH_SHORT).show();
				startView(RegisterView.createView(getContext()));
			}
		});
		// 忘记密码
		findViewById(findId("btnLoginViewForgetPw")).setVisibility(View.INVISIBLE);
		findViewById(findId("btnLoginViewForgetPw")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
//				startView(ForgetPasswdView.createView(getContext()));
			}
		});
		// 游客试玩
		findViewById(findId("btnQuickLogin")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
//				requestApi(SdkHttpRequest.quickLoginRequest());
			}
		});
//		MobUserManager mum = MobUserManager.getInstance();
//		List<MobUser> list = mum.accountList();
//		if (list != null && list.size() > 0) {
//			for (MobUser u : list) {
//				if (!u.getType().equalsIgnoreCase("0")) {
//					if (u.getEmail() != null && u.getEmail().length() > 0) {
//						user = u;
//						edt_account.setText(user.getEmail());
//						edt_password.setText("******");
//						canQuickLogin = true;
//						break;
//					}
//				}
//			}
//		}

		iv_qqLogin = (ImageView) findViewById(findId("btnLoginViewThirdLogin1"));
		iv_qqLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"iv_qqLogin onBaseClick",Toast.LENGTH_SHORT).show();
//				thirdLogin((ThirdType) v.getTag());
			}
		});
		iv_wxLogin = (ImageView) findViewById(findId("btnLoginViewThirdLogin2"));
		iv_wxLogin.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				Toast.makeText(KFGameSDK.getInstance().getActivity(),"iv_wxLogin onBaseClick",Toast.LENGTH_SHORT).show();
//				thirdLogin((ThirdType) v.getTag());
			}
		});

	}

	private boolean checkPasswd(String passwd) {
		if (passwd == null || passwd.length() == 0) {
			showError(edt_password, edt_password.getHint().toString());
			return false;
		}
		if (passwd.length() < 6) {
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

//	@Override
//	public void requestDidSuccess(HttpRequest httpRequest, String result) {
//		super.requestDidSuccess(httpRequest, result);
//		String funcation = httpRequest.getFuncation();
//		if (funcation.equals(APIs.WEB_API_THIRD_LOGIN))
//			return;
//		try {
//			JSONObject obj = new JSONObject(result);
//			KFGameUser user = new KFGameUser(result);
//			if (user.getStatus() == 1) {
//				String email = edt_account.getText().toString().trim();
//				JSONObject data = obj.getJSONObject("data");
//				if (!funcation.equals(APIs.WEB_API_FREE_LOGIN) && email.length() > 0) {
//					data.put("email", email);
//					user.setEmail(email);
//				}
//				obj.put("data", data);
//				MobUserManager mobUserManager = MobUserManager.getInstance();
//				mobUserManager.saveAccount(user.getUserid(), obj.toString());
//				mobUserManager.setCurrentUser(user);
////				OnLineHelper.getInstance(getContext()).start();
////				GamaterSDK.getInstance().resumeGmae(null);
////				GamaterSDK.getInstance().showNoticeDialog();
//				mobUserManager.setIsLoginIng(false);
//				loginSuccessCallback(user, funcation.equals(APIs.WEB_API_FREE_LOGIN) ? 1 : 2, null);
//				getHandler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						SdkDialogViewManager.dialogDismiss();
//					}
//				}, 300);
//			} else if (obj.optInt("errorCode") == 102) {
//				passwdErrorCount = passwdErrorCount + 1;
//				if (passwdErrorCount > 1) {
//					findViewById(findId("btnLoginViewForgetPw")).setVisibility(View.VISIBLE);
//				}
//			} else if (canQuickLogin) {
//				canQuickLogin = false;
//				edt_password.setText("");
//				showError(edt_password, edt_password.getHint().toString());
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public boolean isMenuEnable() {
		return true;
	}
}
