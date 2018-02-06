package com.kfgame.sdk.model;

import java.io.Serializable;

public class KFGameResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int status;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "KFGameResponse{\n" +//
               "\tcode=" + status + "\n" +//
               "\tmsg='" + msg + "\'\n" +//
               "\tdata=" + data + "\n" +//
               '}';
    }
}
