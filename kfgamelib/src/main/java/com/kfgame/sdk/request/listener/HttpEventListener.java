package com.kfgame.sdk.request.listener;

/**
 * Created by Tobin on 2018/2/11.
 */

public interface HttpEventListener {

    void requestDidSuccess();

    void requestDidStart();

    void requestDidFailed(String msg);
}
