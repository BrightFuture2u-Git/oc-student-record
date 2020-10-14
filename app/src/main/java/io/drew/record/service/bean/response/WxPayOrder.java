package io.drew.record.service.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 4:37 PM
 */
public class WxPayOrder {

    /**
     * appId :
     * nonceStr :
     * orderId :
     * package :
     * partnerId :
     * paySign :
     * prepayId :
     * timeStamp :
     * token :
     */

    private String appId;
    private String nonceStr;
    private String orderId;
    @SerializedName("package")
    private String packageX;
    private String partnerId;
    private String paySign;
    private String prepayId;
    private String timeStamp;
    private String token;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
