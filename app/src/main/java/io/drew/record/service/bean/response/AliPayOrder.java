package io.drew.record.service.bean.response;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 3:38 PM
 */
public class AliPayOrder {

    /**
     * ans : alipay_root_cert_sn=687b59193f3f462dd5336e5abf83c5d8_02941eef3187dddf3d3b83462e1dfcf6&alipay_sdk=alipay-easysdk-java-2.0.0&app_cert_sn=747f5186c9c36984ba0ea948addd0216&app_id=2021001165601076&biz_content=%7B%22out_trade_no%22%3A%224311300110606799%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%22%E6%9C%80%E9%95%BF%E7%9A%84%E5%9B%B4%E5%B7%BE%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Ftest.brightfuture360.com%2Faccount%2Fnotify%2Falipay&sign=rEIh8qFCIOrl30fdsVYL5TivF5k%2Bft85YUANL46o6wmSffIFylbVqj5gv8H7zuSJ%2Bbq52acvCfT41SgMdvf%2BW2q3hb%2FYnXpn9bPlKHWCJeVFn2LW1HEJ77um6SPFizxgoQck2%2BENipsVKMBubErK2%2BPOmOy3YEL0wwQJyte4HrGZoXMPZmmvCGLaVTGdaTedA%2FhqzxIGBuhcUF0ecdPsoskF7xEsi76wlINCPXIh4qwQsqIQU60VrrLWpG13gkWv6MFVixdi9VYCmWnCx0kFgkit0BNRJglTBIB11PMlmRKeMCYLKw3sxstuxEl1PJyztCrGkvPk%2FS2TInx7guZWOw%3D%3D&sign_type=RSA2&timestamp=2020-09-10+15%3A37%3A14&version=1.0
     * token : g5fw2aQpz5ifxKbC
     * orderId : 4311300110606799
     */

    private String ans;
    private String token;
    private String orderId;

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
