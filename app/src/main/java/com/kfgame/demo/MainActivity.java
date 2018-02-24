package com.kfgame.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
        kfGameSDK.initSDK(this, "1", "1");
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KFGameSDK.getInstance().sdkLogin(new SDKLoginListener() {

                    @Override
                    public void onLoginSuccess(KFGameUser user) {
                        Log.e("Tobin","MainActivity onLoginSuccess" + " Token:" + user.getToken() + " Uid" + user.getUid());
                        Toast.makeText(MainActivity.this,"游戏内弹出登陆成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLogoutSuccess() {
                        Log.e("Tobin","MainActivity onLogoutSuccess");
                        Toast.makeText(MainActivity.this,"游戏内弹出登出成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoginCancel() {
                        Log.e("Tobin","MainActivityonLoginCancel");
                        Toast.makeText(MainActivity.this,"游戏内弹出取消",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoginFail(String msg) {
                        if (msg != null)
                            Log.e("Tobin","MainActivity onLoginFail" + msg);
                        Toast.makeText(MainActivity.this,"游戏内弹出登陆失败",Toast.LENGTH_SHORT).show();

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
