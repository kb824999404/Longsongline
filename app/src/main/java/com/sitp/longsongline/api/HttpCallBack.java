package com.sitp.longsongline.api;

public interface HttpCallBack {
    void onSuccess(String res);

    void onFailure(Exception e);
}
