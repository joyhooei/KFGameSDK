package com.kfgame.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.callback.SDKLoginListener;
import com.kfgame.sdk.pojo.KFGameUser;

public class MainActivity extends AppCompatActivity {

    private KFGameSDK kfGameSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kfGameSDK = KFGameSDK.getInstance();
        kfGameSDK.initSDK(this);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KFGameSDK.getInstance().sdkLogin(new SDKLoginListener() {

                    @Override
                    public void onLoginSuccess(KFGameUser user) {
                        Log.e("Tobin","onLoginSuccess");
                    }

                    @Override
                    public void onLogoutSuccess() {
                        Log.e("Tobin","onLogoutSuccess");
                    }

                    @Override
                    public void onLoginCancel() {
                        Log.e("Tobin","onLoginCancel");
                    }

                    @Override
                    public void onLoginError() {
                        Log.e("Tobin","onLoginError");

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KFGameSDK.getInstance().onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KFGameSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        KFGameSDK.getInstance().onNewIntent(intent);
    }

}
