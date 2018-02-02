package com.kfgame.sdk.callback;

/**
 * Created by Tobin on 2018/1/31.
 */

public interface SDKPayListener {
    void onPaySuccess();

    void onPayCancel();

    void onPayError();
}
