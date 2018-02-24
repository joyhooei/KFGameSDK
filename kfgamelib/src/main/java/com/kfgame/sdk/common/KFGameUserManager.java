package com.kfgame.sdk.common;

import android.content.SharedPreferences;

import com.kfgame.sdk.pojo.KFGameUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tobin on 2018/2/24.
 */

public class KFGameUserManager {

    private SharedPreferences accountSP;

    private KFGameUser currentUser;

    public KFGameUser getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserId() {
        return accountSP.getString("currentUser", null);
    }

    public void setCurrentUser(KFGameUser currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 保存一个登录账号
     *
     * @param userid
     * @param jsonStr
     */
    public void saveAccount(String userid, String jsonStr) {
        accountSP.edit().putString(userid, jsonStr).commit();
    }

    /**
     * 移除一个登录账号
     *
     * @param userid
     */
    public void removeAccount(String userid) {
        Map<String, ?> list = accountSP.getAll();
        for (String key : list.keySet()) {
            if (key.equals(userid)) {
                accountSP.edit().remove(key).commit();
                accountList();
                break;
            }
        }

    }

    /**
     * 获得登录过的账号
     *
     * @return
     */
    public List<KFGameUser> accountList() {
        Map<String, ?> list = accountSP.getAll();
        List<KFGameUser> userList = new ArrayList<KFGameUser>();

        return userList;
    }

    /**
     * 获得一个登录过的账号
     *
     * @param userid
     * @return
     */
    public KFGameUser userByUserId(String userid) {

        return null;
    }
}
