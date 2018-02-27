package com.kfgame.sdk.pay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by Tobin on 2018/2/26.
 */

public class PayWebActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId("kfgame_sdk_layout_pay_web_activity"));
        initView();
    }


    private int getLayoutId(String paramString) {
        return getResources().getIdentifier(paramString, "layout", getPackageName());
    }

    private int getId(String paramString) {
        return getResources().getIdentifier(paramString, "id", getPackageName());
    }

    private void initView(){
        webView = (WebView) findViewById(getId("webview_pay"));

        webView.getSettings().setJavaScriptEnabled(true);
        // DOM存储API是否可用
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // 将图片调整到适合webview大小
        webView.getSettings().setUseWideViewPort(true);
        // 缩放至屏幕大小
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.addJavascriptInterface(new PayInterface(), "okgame");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        webView.setWebViewClient(wvc);
        // webView.getSettings().setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 支持缩放


    }



}
