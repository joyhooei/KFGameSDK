package com.kfgame.sdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.circularprogress.CircularProgressBar;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class SDKWebViewDialog extends Dialog {

	public enum WebViewType {
		Policy, baidu, Other
	}

	private WebViewType type;
	private View rootView;
	private WebView webView;
	private View progressView;
	private boolean isProgressLoading;
	private String url;
	private ImageView logoImage;

	public SDKWebViewDialog(Context context, WebViewType type) {
		super(context, ResourceUtil.getStyleId("kfgame_dialog_theme_full"));
		this.type = type;
	}

	public SDKWebViewDialog(Context context, String url) {
		this(context, WebViewType.Other);
		this.url = url;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = getLayoutInflater().inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_webview_dialog"), null);
		webView = (WebView) rootView.findViewById(ResourceUtil.getId("webview"));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);

		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("Tobin SDKWebViewDialog shouldOverrideUrlLoading");
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
                LogUtil.e("Tobin SDKWebViewDialog onPageFinished");
				hideLoadingView();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
                LogUtil.e("Tobin SDKWebViewDialog onPageStarted");
				showLoadingView();
			}

		});
		setContentView(rootView);

		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(200);
		rootView.startAnimation(animation);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		rootView.findViewById(ResourceUtil.getId("btnBack")).setOnClickListener(new BaseOnClickListener() {
			@Override
			public void onBaseClick(View v) {
				onBackPressed();
			}
		});

		rootView.findViewById(ResourceUtil.getId("iv_webview_close")).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		progressView = rootView.findViewById(ResourceUtil.getId("progress_view"));
		loadUrl();
	}

	private void loadUrl() {
		if (this.type == WebViewType.Policy) {
			String fileName = "privacy_policy";
			url = "file:///android_asset/" + fileName + ".html";
			webView.loadUrl(url);
		}else if (this.type == WebViewType.baidu) {
            webView.loadUrl("https://www.baidu.com/");
        }else if (this.type == WebViewType.Other) {
			webView.loadUrl(url);
		}
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
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {
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

	@Override
	public void dismiss() {
		AlphaAnimation aa = new AlphaAnimation(1, 0.3f);
		aa.setDuration(100);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				SDKWebViewDialog.super.dismiss();
			}
		});
		rootView.startAnimation(aa);
	}

	@Override
	public void onBackPressed() {
		if (isProgressLoading) {
			hideLoadingView();
			return;
		}
		if (webView.canGoBack()) {
			webView.goBack();
			return;
		}
		super.onBackPressed();
	}
}
