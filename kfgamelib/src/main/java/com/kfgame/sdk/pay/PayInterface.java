package com.kfgame.sdk.pay;

import android.webkit.JavascriptInterface;

/**
 * Created by Tobin on 2018/2/26.
 */

public class PayInterface {

    @JavascriptInterface
    public void paySuccess(String info) {

    }


    @JavascriptInterface
    public void payFail(String error){

    }

    @JavascriptInterface
    public void payCancel(){

    }

}
