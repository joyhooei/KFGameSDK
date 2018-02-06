package com.kfgame.sdk.model;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int status;
    public String msg;

    public KFGameResponse tokfgResponse() {
        KFGameResponse kfgResponse = new KFGameResponse();
        kfgResponse.status = status;
        kfgResponse.msg = msg;
        return kfgResponse;
    }
}
