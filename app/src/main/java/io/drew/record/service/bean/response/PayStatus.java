package io.drew.record.service.bean.response;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 5:27 PM
 * 支付结果
 */
public class PayStatus {

    /**
     * ans :
     * orderId :
     * token :
     */

    private String ans;
    private String orderId;
    private String token;

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
