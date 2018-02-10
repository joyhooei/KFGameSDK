package com.kfgame.sdk.okgo.callback;

import com.kfgame.sdk.common.Config;
import com.kfgame.sdk.util.MD5Utils;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public abstract class EncryptCallback<T> extends JsonCallback<T> {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        //以下加密代码，根据业务需求和服务器的配合，算法自行决定
        sign(request.getParams());

        JSONObject jsonObject = new JSONObject(request.getParams().urlParamsMap);
        request.removeAllParams();
        request.params("","");
//        request.upJson(jsonObject);
    }

    /**
     * 针对URL进行签名
     */
    private void sign(HttpParams params) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().get(0));
        }
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
//        sb.delete(sb.length() - 1, sb.length());
        sb.append(Config.encodeKey);
        String sign = MD5Utils.encode(sb.toString());
        params.put("sign", sign);
    }

    /** 按照key的自然顺序进行排序，并返回 */
    private Map<String, String> getSortedMapByKey(Map<String, String> map) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };

        Map<String, String> treeMap = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }
}
