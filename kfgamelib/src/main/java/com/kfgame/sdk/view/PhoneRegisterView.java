package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kfgame.sdk.dialog.SDKWebViewDialog;
import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class PhoneRegisterView extends BaseLinearLayout {

	public PhoneRegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PhoneRegisterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhoneRegisterView(Context context) {
		super(context);
	}

	private EditText accountEdit;
	private EditText passwordEdit;
    private EditText verificationCodeEdit;
	private TextView tv_verificationCode;
	private TextView tv_policy_agreement;
    private ImageView iv_password_visible;


    @Override
	public void initView() {
		accountEdit = (EditText) findViewById(findId("edt_phone_account"));
		accountEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				String str = accountEdit.getText().toString().trim();
				if (!isFocus) {
				    checkAccount(str);
				}
			}
		});

		passwordEdit = (EditText) findViewById(findId("edt_password"));
		passwordEdit.setTypeface(Typeface.DEFAULT);

        verificationCodeEdit = (EditText) findViewById(findId("edt_identifying_code"));
        verificationCodeEdit.setTypeface(Typeface.DEFAULT);

		tv_verificationCode = (TextView) findViewById(findId("tv_send_identifying_code"));
		final TextView btnRegister = (TextView) findViewById(ResourceUtil.getId("btnRegister"));

        iv_password_visible = (ImageView) findViewById(findId("iv_password_visible"));
        iv_password_visible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                passwordEdit.setInputType();
            }
        });

        // 注册按钮点击事件
		btnRegister.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				String phoneAccount = accountEdit.getText().toString().trim();
				if (!checkAccount(phoneAccount))
					return;

                String verificationCode = verificationCodeEdit.getText().toString().trim();
                if (!checkVerificationCode(verificationCode)){
                    showError(verificationCodeEdit,"请输入正确的验证码");
                    return;
                }

				String password = passwordEdit.getText().toString().trim();
				int passwordCheck = checkPassword(password);
				if (passwordCheck < 1) {
					showError(passwordEdit, passwordCheck == 0 ? passwordEdit.getHint().toString()
							: "密码必须由6-13位数字和字母组成");
					return;
				}

//				requestApi(SdkHttpRequest.registerRequest(phoneAccount, password , verificationCode));
			}
		});

		tv_verificationCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountRequest.getInstance().requestSendIdentifyCode(accountEdit.getText().toString());
				timer.start();
				tv_verificationCode.setEnabled(false);
			}
		});

		tv_policy_agreement = (TextView) findViewById(ResourceUtil.getId("tx_agreement_policy"));
        SpannableStringBuilder styled = new SpannableStringBuilder(tv_policy_agreement.getText());
        styled.setSpan(new ForegroundColorSpan(Color.BLUE), 5, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_policy_agreement.setText(styled);
		tv_policy_agreement.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				new SDKWebViewDialog(getContext(), SDKWebViewDialog.WebViewType.Policy).show();
			}
		});
	}

	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			tv_verificationCode.setText((millisUntilFinished / 1000) + "秒后可重发");
		}

		@Override
		public void onFinish() {
			tv_verificationCode.setEnabled(true);
			tv_verificationCode.setText("获取验证码");
		}
	};

	private boolean checkAccount(String account) {
		if (account == null || account.length() == 0) {
			showError(accountEdit, accountEdit.getHint().toString());
			return false;
		} else if (!checkPhone(account)) {
			showError(accountEdit, "请输入正确的电话号码");
			return false;
		}
		return true;
	}

	public static PhoneRegisterView createView(Context ctx) {
		if (ctx == null)
			return null;
		PhoneRegisterView view = (PhoneRegisterView) LayoutInflater.from(ctx)
				.inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_register_phone"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "账号注册";
	}


}
