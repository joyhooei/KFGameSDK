package com.kfgame.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordView extends BaseLinearLayout {

	public ForgetPasswordView() {
		super(KFGameSDK.getInstance().getActivity());
	}

	public ForgetPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ForgetPasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ForgetPasswordView(Context context) {
		super(context);
	}

	private EditText accountEdit;

	@Override
	public void initView() {
		TextView mesText = (TextView) findViewById(ResourceUtil.getId("text_mes"));
		mesText.setText("小贴士：输入正确的手机号码，并查看手机短信验证码");
		accountEdit = (EditText) findViewById(ResourceUtil.getId("account_edit"));
		final TextView btnForgetPw = (TextView) findViewById(ResourceUtil.getId("btnForgetPw"));
		btnForgetPw.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				String account = accountEdit.getText().toString().trim();
				if (account == null || account.length() == 0) {
					showError(accountEdit, accountEdit.getHint().toString());
					return;
				} else if (!checkEmail(account)) {
					showError(accountEdit, findStringId("okgame_email_format_error"));
					return;
				}
//				long lastTime = SPUtil.getLastGetBackTime(getContext(), account);
//				if (lastTime > 0) {
//					if (System.currentTimeMillis() - lastTime < (1000 * 60 * 60)) {
//						showError(findStringId("okgame_forget_pw_tips"));
//						return;
//					}
//				}
				JSONObject data = new JSONObject();
				try {
					data.put("id", 2);
					data.put("email", account);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				hideSoftInput();
//				requestApi(SdkHttpRequest.forgetPasswd(account));
			}
		});
		JSONObject data = new JSONObject();
		try {
			data.put("id", 1);
		} catch (JSONException e) {
		}
	}

	public static ForgetPasswordView createView(Context ctx) {
		if (ctx == null)
			return null;
		ForgetPasswordView view = (ForgetPasswordView) LayoutInflater.from(ctx).inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_forget_password"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "找回密码";
	}

}
