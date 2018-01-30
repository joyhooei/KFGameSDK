package com.kfgame.sdk.view;

import android.content.Context;
import android.graphics.Typeface;
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
//
//import com.gamater.account.MobUserManager;
//import com.gamater.account.http.SdkHttpRequest;
//import com.gamater.account.po.MobUser;
//import com.gamater.common.http.HttpRequest;
//import com.gamater.dialog.SdkDialogViewManager;
//import com.gamater.dialog.SdkGameWebViewDialog;
//import com.gamater.dialog.SdkGameWebViewDialog.WebViewType;


import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegisterView extends BaseLinearLayout {

	public RegisterView() {
		super(KFGameSDK.getInstance().getActivity());
	}

	public RegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public RegisterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RegisterView(Context context) {
		super(context);
	}

	private EditText accountEdit;
	private EditText passwdEdit;
	private EditText commitPasswdEdit;

	@Override
	public void initView() {
		accountEdit = (EditText) findViewById(findId("account_edit"));
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
				String editable = accountEdit.getText().toString();
				String str = stringFilter(editable.toString());
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
		passwdEdit = (EditText) findViewById(findId("passwd_edit"));
		passwdEdit.setTypeface(Typeface.DEFAULT);
		commitPasswdEdit = (EditText) findViewById(findId("commit_passwd_edit"));
		commitPasswdEdit.setTypeface(Typeface.DEFAULT);
		final TextView btnRegister = (TextView) findViewById(ResourceUtil.getId("btnRegister"));
		final CheckBox policyCheckBox = (CheckBox) findViewById(ResourceUtil.getId("policy_checkbox"));
		policyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnRegister.setEnabled(isChecked);
			}
		});
		btnRegister.setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				// startView(NormalLoginView.createView(getContext()));
				String account = accountEdit.getText().toString().trim();
				if (!checkAccount(account))
					return;
				String passwd = passwdEdit.getText().toString().trim();
				int passwdCheck = checkPasswd(passwd);
				if (passwdCheck < 1) {
					showError(passwdEdit, passwdCheck == 0 ? passwdEdit.getHint().toString()
							: getResources().getString(findStringId("vsgm_tony_common_passwd_length_err")));
					return;
				}
				String commitP = commitPasswdEdit.getText().toString().trim();
				int commitCheck = checkPasswd(commitP);
				if (commitCheck < 1) {
					showError(commitPasswdEdit, commitCheck == 0 ? commitPasswdEdit.getHint().toString()
							: getResources().getString(findStringId("vsgm_tony_common_passwd_length_err")));
					return;
				}
				if (!passwd.equals(commitP)) {
					showError(commitPasswdEdit, findStringId("vsgm_tony_change_dlg_disagree"));
					return;
				}
//				requestApi(SdkHttpRequest.registerRequest(account, passwd));
			}
		});
		findViewById(ResourceUtil.getId("txtAgreement")).setOnClickListener(new BaseOnClickListener() {
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

	private boolean checkAccount(String account) {
		if (account == null || account.length() == 0) {
			showError(accountEdit, accountEdit.getHint().toString());
			return false;
		} else if (!checkEmail(account)) {
			showError(accountEdit, findStringId("okgame_email_format_error"));
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

	private int checkPasswd(String passwd) {
		if (passwd == null || passwd.length() == 0) {
			return 0;
		}
		if (passwd.length() < 6) {
			return -1;
		}
		return 1;
	}

	public static RegisterView createView(Context ctx) {
		if (ctx == null)
			return null;
		RegisterView view = (RegisterView) LayoutInflater.from(ctx).inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_register_phone"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return getContext().getString(ResourceUtil.getStringId("vsgm_reg_account"));
	}


}
