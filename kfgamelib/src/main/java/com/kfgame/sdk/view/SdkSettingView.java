package com.kfgame.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class SdkSettingView extends BaseLinearLayout {

	public SdkSettingView() {
		super(KFGameSDK.getInstance().getActivity());
	}

	public SdkSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SdkSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SdkSettingView(Context context) {
		super(context);
	}

	@Override
	public void initView() {
		findViewById(ResourceUtil.getId("other_login")).setOnClickListener(
				new BaseOnClickListener() {
					@Override
					public void onBaseClick(View v) {
						startView(ThirdLoginView.createView(getContext()));
					}
				});
		findViewById(ResourceUtil.getId("update_account")).setOnClickListener(
				new BaseOnClickListener() {
					@Override
					public void onBaseClick(View v) {
//						startView(UpdateAccountView.createView(getContext()));
					}
				});
		findViewById(ResourceUtil.getId("change_password")).setOnClickListener(
				new BaseOnClickListener() {
					@Override
					public void onBaseClick(View v) {
//						startView(ChangePasswdView.createView(getContext()));
					}
				});
		findViewById(ResourceUtil.getId("modify_password")).setOnClickListener(
				new BaseOnClickListener() {
					@Override
					public void onBaseClick(View v) {
						startView(ModifyPasswordView.createView(getContext()));
					}
				});
	}

	public static SdkSettingView createView(Context ctx) {
		if (ctx == null)
			return null;
		SdkSettingView view = (SdkSettingView) LayoutInflater.from(ctx).inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_account_setting"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "账号设置";
	}

}
