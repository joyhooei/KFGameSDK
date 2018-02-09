package com.kfgame.sdk.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class ModifyPasswordView extends BaseLinearLayout {

	public ModifyPasswordView() {
		super(KFGameSDK.getInstance().getActivity());
	}

	public ModifyPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ModifyPasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ModifyPasswordView(Context context) {
		super(context);
	}

	private EditText edt_account;
	private EditText passwordEdit;
	private TextView edt_send_identifying_code;

	@Override
	public void initView() {
//		TextView mesText = (TextView) findViewById(ResourceUtil.getId("text_mes"));
//		mesText.setText("小贴士：输入正确的手机号码，并查看手机短信验证码");
		edt_account = (EditText) findViewById(ResourceUtil.getId("edt_phone_account"));
        edt_account.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String str = edt_account.getText().toString().trim();
                if (isShown() && !hasFocus) {
                    checkAccount(str);
                }
            }
        });

        passwordEdit = (EditText) findViewById(ResourceUtil.getId("edt_account_password"));

        edt_send_identifying_code =(TextView) findViewById(ResourceUtil.getId("edt_send_identifying_code"));
        edt_send_identifying_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountRequest.getInstance().requestSendIdentifyCode(edt_account.getText().toString().trim());
                timer.start();
                edt_account.setEnabled(false);
            }
        });

		final TextView btnForgetPw = (TextView) findViewById(ResourceUtil.getId("btn_forget_password"));
		btnForgetPw.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				String phoneNumber = edt_account.getText().toString().trim();
				if (phoneNumber == null || phoneNumber.length() == 0) {
					showError(edt_account, edt_account.getHint().toString());
					return;
				} else if (!checkPhone(phoneNumber)) {
					showError(edt_account, "请输入正确的手机号码");
					return;
				}

                int passwordCheck = checkPassword(passwordEdit.getText().toString().trim());
                if (passwordCheck < 1) {
                    showError(passwordEdit, passwordCheck == 0 ? passwordEdit.getHint().toString()
                            : "密码必须由6-10位数字和字母组成");
                    return;
                }

                if (!checkVerificationCode(edt_send_identifying_code.getText().toString().trim())){
                    showError(edt_account, "请输入正确格式的验证码");
                }

				hideSoftInput();
//				requestApi(SdkHttpRequest.forgetPasswd(account));
			}
		});


	}


    private boolean checkAccount(String account) {
        if (account == null || account.length() == 0) {
            showError(edt_account, "请输入账号");
            return false;
        } else if (!checkPhone(account)) {
            showError(edt_account, "请输入正确的电话号码");
            return false;
        }
        return true;
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            edt_send_identifying_code.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            edt_send_identifying_code.setEnabled(true);
            edt_send_identifying_code.setText("获取验证码");
        }
    };

	public static ModifyPasswordView createView(Context ctx) {
		if (ctx == null)
			return null;
		ModifyPasswordView view = (ModifyPasswordView) LayoutInflater.from(ctx).inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_modify_password"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "修改密码";
	}

}
