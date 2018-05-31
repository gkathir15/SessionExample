package com.msf.sessionexample;

import com.google.gson.annotations.SerializedName;

public class PreSessionData {

    @SerializedName("msg")
    String msg;

    @SerializedName("code")
    int code;

   @SerializedName("sessionID")
    String sessionID;

   @SerializedName("randomNo")
    String randomNo;

    @SerializedName("touchIDEnabled")
    boolean isTocuchEnabled;

    @SerializedName("publicKey")
    String publicKey;

    //URLS urls;


    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getRandomNo() {
        return randomNo;
    }

    public boolean isTocuchEnabled() {
        return isTocuchEnabled;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
