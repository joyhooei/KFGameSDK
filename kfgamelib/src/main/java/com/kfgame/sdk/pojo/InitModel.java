package com.kfgame.sdk.pojo;

import java.io.Serializable;

/**
 * Created by Tobin on 2018/2/9.
 */

public class InitModel implements Serializable {
    //    返回值ModelData:{"id": ID, "appId": "游戏appId", "channelId": "游戏渠道Id",
    // "payServiceQQ": "客服QQ", "payServicePhone": "客服电话", "weChat": "企业微信",
    // "websiteUrl": "企业网址", "servicePhone": "企业电话", "qqnum": "企业qq"}

    private static final long serialVersionUID = 6753210234564872868L;

    public int id;
    public String appId;
    public String channelId;
    public String payServiceQQ;
    public String payServicePhone;
    public String weChat;
    public String websiteUrl;
    public String servicePhone;
    public String qqnum;

    @Override
    public String toString() {
        return "InitModel: \n" +
                "\tappId=" + appId + "\n" +
                "\tchannelId='" + channelId + "\'\n" +
                "\tpayServiceQQ=" + payServiceQQ + "\n" +
                "\tpayServicePhone=" + payServicePhone + "\n" +
                "\tweChat=" + weChat + "\n" +//
                "\twebsiteUrl=" + websiteUrl + "\n" +
                "\tservicePhone=" + servicePhone + "\n" +
                "\tqqnum=" + qqnum + "\n" +
                '\n';
    }
}
