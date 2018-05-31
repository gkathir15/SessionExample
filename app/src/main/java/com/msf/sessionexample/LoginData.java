package com.msf.sessionexample;

import com.google.gson.annotations.SerializedName;

public class LoginData {


    private String pwdStatus;

    private String pwdChangeNotifyMsg;

    private String pwdAttempts;


    private String twoFARequired;

    private String accountType;

    private String accountActivatedFlag;

    private String pwdChangeNotifyStatus;

    private String code;

    @SerializedName("msg")
    private String msg;

    private String lastLoginTime;



    public String getPwdStatus() {
        return pwdStatus;
    }

    public void setPwdStatus(String pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public String getPwdChangeNotifyMsg() {
        return pwdChangeNotifyMsg;
    }

    public void setPwdChangeNotifyMsg(String pwdChangeNotifyMsg) {
        this.pwdChangeNotifyMsg = pwdChangeNotifyMsg;
    }

    public String getPwdAttempts() {
        return pwdAttempts;
    }

    public void setPwdAttempts(String pwdAttempts) {
        this.pwdAttempts = pwdAttempts;
    }

    public String getTwoFARequired() {
        return twoFARequired;
    }

    public void setTwoFARequired(String twoFARequired) {
        this.twoFARequired = twoFARequired;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountActivatedFlag() {
        return accountActivatedFlag;
    }

    public void setAccountActivatedFlag(String accountActivatedFlag) {
        this.accountActivatedFlag = accountActivatedFlag;
    }

    public String getPwdChangeNotifyStatus() {
        return pwdChangeNotifyStatus;
    }

    public void setPwdChangeNotifyStatus(String pwdChangeNotifyStatus) {
        this.pwdChangeNotifyStatus = pwdChangeNotifyStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
