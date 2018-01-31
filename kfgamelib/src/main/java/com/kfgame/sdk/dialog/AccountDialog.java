package com.kfgame.sdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.NormalLoginView;
import com.kfgame.sdk.view.SdkSettingView;
import com.kfgame.sdk.view.circularprogress.CircularProgressBar;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;
import com.kfgame.sdk.view.viewinterface.BaseSdkView;

/**
 * Created by Tobin on 2018/1/29.
 */
public class AccountDialog extends Dialog {
    public enum ViewType {
        Login, UpdateAccount, ChangePassword
    }

    private Activity activity;
    private RelativeLayout rootView;
    private RelativeLayout containerView;
    private View progressView;
    private ImageView iv_account_back;
    private ImageView menuImage;
    private TextView tv_title;
    private boolean isProgressLoading;
    private ViewType type;

    public AccountDialog(@NonNull Context context, ViewType type) {
        super(context, getStyleId((Activity) context,"kfgame_sdk_dialog_bg"));
        this.activity = (Activity) context;this.type = type;
        this.type = type;
    }

    public AccountDialog(Activity activity) {
        this(activity, ViewType.Login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SdkDialogViewManager.getManager().init(this);
        rootView =(RelativeLayout)  getLayoutInflater().inflate(getLayoutId("kfgame_layout_account_base"), null);
        setContentView(rootView);
        containerView = (RelativeLayout) rootView.findViewById(ResourceUtil.getId("container_view"));

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(200);
        rootView.startAnimation(animation);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        iv_account_back = (ImageView) rootView.findViewById(ResourceUtil.getId("iv_account_back"));
        iv_account_back.setOnClickListener(new BaseOnClickListener() {
            @Override
            public void onBaseClick(View v) {
                onBackPressed();
            }
        });
        menuImage = (ImageView) findViewById(ResourceUtil.getId("iv_login_top_menu"));
        menuImage.setOnClickListener(new BaseOnClickListener() {
            @Override
            public void onBaseClick(View v) {
                SdkDialogViewManager.doAddView(SdkSettingView.createView(getContext()));
            }
        });
        tv_title = (TextView) rootView.findViewById(ResourceUtil.getId("kfgame_tv_title"));

        progressView = findViewById(ResourceUtil.getId("progress_view"));

        BaseSdkView sdkView;
        if (type == ViewType.UpdateAccount) {
//            sdkView = UpdateAccountView.createView(getContext());
        } else if (type == ViewType.ChangePassword) {
//            sdkView = ChangePasswdView.createView(getContext());
        } else {
//            sdkView = NormalLoginView.createView(getContext());
        }
        sdkView = NormalLoginView.createView(getContext());
        containerView.addView((View) sdkView, sdkView.getSdkViewLayoutParams());
        updateView(sdkView);

        if (showAfterCallback != null) {
            showAfterCallback.run();
        }

    }

    private static int getStyleId(Activity activity, String paramString) {
        if (activity == null)
            return 0;
        return activity.getResources().getIdentifier(paramString, "style",activity.getPackageName());
    }

    private int getLayoutId(String paramString) {
        if (activity == null)
            return 0;
        return activity.getResources().getIdentifier(paramString, "layout", activity.getPackageName());
    }

    private int getId(String paramString) {
        return activity.getResources().getIdentifier(paramString, "id",activity.getPackageName());
    }

    private Runnable showAfterCallback;
    public void showWithCallback(Runnable callback) {
        showAfterCallback = callback;
        show();
    }

    public void showLoadingView() {
        progressView.setVisibility(View.VISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(200);
        progressView.startAnimation(animation);
        CircularProgressBar p = (CircularProgressBar) rootView.findViewById(ResourceUtil.getId("progress_bar"));
        p.start();
        isProgressLoading = true;
    }

    public void hideLoadingView() {
        progressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation animation = new AlphaAnimation(1, 0);
                animation.setDuration(200);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        progressView.setVisibility(View.GONE);
                    }
                });
                progressView.startAnimation(animation);
                CircularProgressBar p = (CircularProgressBar) rootView.findViewById(ResourceUtil.getId("progress_bar"));
                p.stop();
                isProgressLoading = false;
            }
        }, 500);
    }

    public void addBaseSdkView(View view) {
        if (containerView != null)
            SdkDialogViewManager.addBaseSdkView(containerView, view);
    }

    public void updateView(BaseSdkView v) {
        updateViewTitle(v.getViewTitle());
        updateViewBack(v.interceptOnBackEvent());
        updateViewMenu(v.isMenuEnable());
    }

    private void updateViewMenu(boolean isMenuEnable) {
        if (isMenuEnable) {
            menuImage.setVisibility(View.VISIBLE);
        } else {
            menuImage.setVisibility(View.GONE);
        }
    }

    private void updateViewBack(boolean intercept) {
        if (intercept) {
            iv_account_back.setVisibility(View.GONE);
        } else {
            iv_account_back.setVisibility(View.VISIBLE);
        }
    }

    private void updateViewTitle(String title) {
        if (title == null) {
            tv_title.setVisibility(View.GONE);
//            logoImage.setVisibility(View.VISIBLE);
        } else {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
//            logoImage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SdkDialogViewManager.destory();
    }

    @Override
    public void dismiss() {
//        SDKMenuManager.getInstance(mActivity).resetIconAlphaCallback();
//        SDKMenuManager.getInstance(mActivity).initParentView(null);
        AlphaAnimation aa = new AlphaAnimation(1, 0.3f);
        aa.setDuration(100);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AccountDialog.super.dismiss();
            }
        });
        rootView.startAnimation(aa);
    }

    private float touchY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            touchY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (Math.abs(ev.getY() - touchY) > 5)
                return super.dispatchTouchEvent(ev);
            View localView = getCurrentFocus();
            if (shouldHandleEvent(ev)) {
                InputMethodManager localInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if ((localView != null) && (localInputMethodManager != null)) {
                    localInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void hideSoftInput(Context context) {
        try {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldHandleEvent(MotionEvent ev) {
        return !isTouchEditText(this.rootView, ev);
    }

    private boolean isTouchEditText(ViewGroup paramViewGroup, MotionEvent paramMotionEvent) {
        try {
            for (int i = 0; i < paramViewGroup.getChildCount(); i++) {
                View localView = paramViewGroup.getChildAt(i);
                if ((localView instanceof ViewGroup)) {
                    if (isTouchEditText((ViewGroup) localView, paramMotionEvent))
                        return true;
                } else if ((localView instanceof EditText)) {
                    int[] arrayOfInt = new int[2];
                    localView.getLocationInWindow(arrayOfInt);
                    int j = arrayOfInt[0];
                    int k = arrayOfInt[1];
                    int m = k + localView.getHeight();
                    int n = j + localView.getWidth();
                    if ((paramMotionEvent.getX() > j) && (paramMotionEvent.getX() < n) && (paramMotionEvent.getY() > k) && (paramMotionEvent.getY() < m)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (((BaseSdkView) containerView.getChildAt(containerView.getChildCount() - 1)).interceptOnBackEvent() || isProgressLoading)
            return;
        if (SdkDialogViewManager.doViewBackPressed(containerView)) {
            return;
        }
        super.onBackPressed();
    }
}
