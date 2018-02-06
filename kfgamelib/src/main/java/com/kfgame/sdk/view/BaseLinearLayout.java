package com.kfgame.sdk.view;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.dialog.SdkDialogViewManager;
import com.kfgame.sdk.pojo.KFGameUser;
import com.kfgame.sdk.pojo.ThirdType;
import com.kfgame.sdk.util.NetCheckUtil;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseSdkView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseLinearLayout extends LinearLayout implements BaseSdkView {

	public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BaseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseLinearLayout(Context context) {
		super(context);
	}

	public LayoutParams getSdkViewLayoutParams() {
		return new LayoutParams(dip2px(320), LayoutParams.WRAP_CONTENT);
//		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public Animation getStartAnimation(AnimationListener listener) {
		AnimationSet set = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1);
		aa.setDuration(100);
		ScaleAnimation sa = new ScaleAnimation(1.1f, 1f, 1.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(100);
		set.addAnimation(aa);
		set.addAnimation(sa);
		if (listener != null)
			set.setAnimationListener(listener);
		return set;
	}

	@Override
	public Animation getEndAnimation(AnimationListener listener) {
		AnimationSet set = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(1, 0.5f);
		aa.setDuration(100);
		ScaleAnimation sa = new ScaleAnimation(1, 1.1f, 1, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(100);
		set.addAnimation(aa);
		set.addAnimation(sa);
		if (listener != null)
			set.setAnimationListener(listener);
		return set;
	}

	@Override
	public boolean doViewGoBack() {
		if (getChildCount() > 1) {
			View view = getChildAt(getChildCount() - 1);
            return view instanceof BaseSdkView && ((BaseSdkView) view).doViewGoBack();
		}
		return false;
	}

	@Override
	public void initView() {

	}

	public int dip2px(float dpValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	protected void startView(View newView) {
		SdkDialogViewManager.doAddView(newView);
	}

	protected void hideSoftInput() {
		SdkDialogViewManager.hideSoftInput(getContext());
	}

	@Override
	public String getViewTitle() {
		return null;
	}

	@Override
	public boolean interceptOnBackEvent() {
		return false;
	}

	private SdkTipsTextView errorView;

	protected void showError(String errorStr) {
		if (errorView == null) {
			errorView = (SdkTipsTextView) findViewWithTag(SdkTipsTextView.TIP_VIEW_TAG);
		}
		if (errorView != null) {
			errorView.showError(errorStr);
		} else {
			showToast(errorStr);
		}
	}

	protected void showToast(String str) {
		Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
	}

	protected void showError(int errorStrRes) {
		String error = getContext().getResources().getString(errorStrRes);
		showError(error);
	}

	protected void makeEditTextWarning(EditText et) {
		if (et == null)
			return;
		if (et.getParent() instanceof WarningLinearLayout) {
			((WarningLinearLayout) et.getParent()).makeWarning();
		}
	}

	public boolean checkEmail(String email) {
		boolean flag;
		try {
			String check = ".*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public boolean checkPhone(String phone) {
		boolean flag;
		try {
			String check = "^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9])\\d{8}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(phone);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	protected static int findLayoutId(String layoutName) {
		return ResourceUtil.getLayoutId(layoutName);
	}

	protected static int findId(String name) {
		return ResourceUtil.getId(name);
	}

	protected static int findStringId(String stringName) {
		return ResourceUtil.getStringId(stringName);
	}

//	protected void requestApi(HttpRequest request) {
//		if (request == null)
//			return;
//		request.setHttpEventListener(this);
//		request.asyncStart();
//	}

	@Override
	public boolean isMenuEnable() {
		return false;
	}

	public void showError(EditText et, String e) {
		showError(e);
		makeEditTextWarning(et);
	}

	public void showError(EditText et, int eRes) {
		showError(eRes);
		makeEditTextWarning(et);
	}

	protected void thirdLogin(ThirdType type) {
		if (NetCheckUtil.isNetworkStatus(getContext())) {
			if (type == ThirdType.QQ) {
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"QQ登陆",Toast.LENGTH_SHORT).show();

			} else if (type == ThirdType.wechat) {
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"微信登陆",Toast.LENGTH_SHORT).show();

			} else if (type == ThirdType.weibo) {
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"微博登陆",Toast.LENGTH_SHORT).show();

			} else if (type == ThirdType.alipay) {
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"淘宝登陆",Toast.LENGTH_SHORT).show();

			} else if (type == ThirdType.baidu) {
                Toast.makeText(KFGameSDK.getInstance().getActivity(),"百度登陆",Toast.LENGTH_SHORT).show();

			}
		} else {
			showError("网络错误");
		}
	}

	private static void setLoginToday(Context ctx) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		Editor editor = ctx.getSharedPreferences("sdk_login_today", 0).edit();
		editor.putBoolean(date, true).commit();
	}

	private static boolean isLoginToday(Context ctx) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		return ctx.getSharedPreferences("sdk_login_today", 0).getBoolean(date, false);
	}

	protected void loginSuccessCallback(KFGameUser user, int type, String typeName) {
//		SDKMenuManager.getInstance(null).hideMenuView();
		if (!isLoginToday(getContext())) {
//			SDKMenuManager.getInstance(null).updateMenuViewLoginToday();
		}
//		if (KFGameSDK.getInstance().getSDKLoginListener() != null && user != null) {
//			KFGameSDK.getInstance().getSDKLoginListener().onLoginSuccess(user);
//		}
		setLoginToday(getContext());
	}
}
