package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
		accountEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String editable;
                editable = accountEdit.getText().toString();
                String str = stringFilter(editable);
				if (!editable.equals(str)) {
					accountEdit.setText(str);
					accountEdit.setSelection(str.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		passwordEdit = (EditText) findViewById(findId("edt_password"));
		passwordEdit.setTypeface(Typeface.DEFAULT);

        verificationCodeEdit = (EditText) findViewById(findId("edt_identifying_code"));
        verificationCodeEdit.setTypeface(Typeface.DEFAULT);

		tv_verificationCode = (TextView) findViewById(findId("tv_send_identifying_code"));
		final TextView btnRegister = (TextView) findViewById(ResourceUtil.getId("btnRegister"));

		final CheckBox policyCheckBox = (CheckBox) findViewById(ResourceUtil.getId("chk_policy"));
		policyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnRegister.setEnabled(isChecked);
			}
		});
		btnRegister.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				String phoneAccount = accountEdit.getText().toString().trim();
				if (!checkAccount(phoneAccount))
					return;
				String password = passwordEdit.getText().toString().trim();
				int passwordCheck = checkPassword(password);
				if (passwordCheck < 1) {
					showError(passwordEdit, passwordCheck == 0 ? passwordEdit.getHint().toString()
							: "密码必须由6-10位数字和字母组成");
					return;
				}
				String verificationCode = verificationCodeEdit.getText().toString().trim();
                if (!checkVerificationCode(verificationCode)){
                    showError(verificationCodeEdit,"请输入正确的验证码");
                }
//				requestApi(SdkHttpRequest.registerRequest(phoneAccount, password , verificationCode));
			}
		});

		tv_verificationCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.start();
				tv_verificationCode.setEnabled(false);
			}
		});

		findViewById(ResourceUtil.getId("tx_agreement_policy")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
//				new SdkGameWebViewDialog(getContext(), WebViewType.Policy).show();
//				policyCheckBox.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						policyCheckBox.setChecked(true);
//					}
//				}, 500);
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

	public static String stringFilter(String str) throws PatternSyntaxException {
		String regEx = "[^a-zA-Z0-9.@_-]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim().toLowerCase();
	}

	private int checkPassword(String passwd) {
		if (passwd == null || passwd.length() == 0) {
			return 0;
		}
		if (passwd.length() < 6) {
			return -1;
		}
		return 1;
	}

    private boolean checkVerificationCode(String verificationCode) {
        boolean flag;
        if (verificationCode == null || verificationCode.length() == 0) {
            return false;
        }
        if (verificationCode.length() < 4) {
            return false;
        }
        try {
            String check = "/\\b\\d{4,6}\\b/g";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(verificationCode);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

	public static PhoneRegisterView createView(Context ctx) {
		if (ctx == null)
			return null;
		PhoneRegisterView view = (PhoneRegisterView) LayoutInflater.from(ctx).inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_register_phone"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "账号注册";
	}


}
