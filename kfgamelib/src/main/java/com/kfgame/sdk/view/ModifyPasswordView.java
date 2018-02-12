package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kfgame.sdk.request.AccountRequest;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class ModifyPasswordView extends BaseLinearLayout {

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
    private EditText verificationCodeEdit;
	private TextView edt_send_identifying_code;
    private ImageView iv_password_visible;

    private boolean isChecked = false;

	@Override
	public void initView() {

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

        verificationCodeEdit = (EditText) findViewById(findId("edt_identifying_code"));
        verificationCodeEdit.setTypeface(Typeface.DEFAULT);

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

        iv_password_visible = (ImageView) findViewById(findId("iv_password_visible"));
        iv_password_visible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isChecked = true;
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isChecked = false;
                }
            }
        });

		final TextView btnForgetPw = (TextView) findViewById(ResourceUtil.getId("btn_forget_password"));
		btnForgetPw.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				String phoneNumber = edt_account.getText().toString().trim();
                if (!checkAccount(phoneNumber))
                    return;

                String identifyingCode = verificationCodeEdit.getText().toString().trim();
                if (!checkVerificationCode(identifyingCode)){
                    showError(edt_account, "请输入正确格式的验证码");
                    return;
                }

                String newPassword = passwordEdit.getText().toString().trim();
                int passwordCheck = checkPassword(newPassword);
                if (passwordCheck < 1) {
                    showError(passwordEdit, passwordCheck == 0 ? passwordEdit.getHint().toString()
                            : "密码必须由6-10位数字和字母组成");
                    return;
                }

				hideSoftInput();
                setHttpCallback();
                AccountRequest.getInstance().modifyPassword(phoneNumber, newPassword, identifyingCode);
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
