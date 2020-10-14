package io.drew.record;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/6 11:48 AM
 */
public class ConfigConstant {
    /**
     * 微信分享
     */
    public static final String WX_APP_ID = "wx8d809dc781f41754";
    /**
     * 7陌id
     */
    public static final String CUSTOMER_7MOOR = "ac1168f0-ab10-11ea-b2bd-935fb43f33c7";

    //乐播投屏配置
    public static final String LEBO_APP_ID = "15324";
    public static final String LEBO_APP_SECRET = "f4cf18eb79b17d45dfa05d519c6fbc1b";
    /**
     * web url
     */

    public static final String url_user_agreement = "https://wap.hualeme.com/rule/";//用户协议
    public static final String url_privacy = "https://www.hualeme.com/privacy.html";//隐私政策
    public static final String url_about = "https://www.hualeme.com/";//关于画了么，官网
    /**
     * Sharepreference key
     */
    public static final String SP_USER_INFO = "userinfo";
    public static final String SP_ALLOW_SERVICE = "allow_service";
    public static final String SP_GUIDE_CHANGE_BABY_MINE = "guide_change_baby_mine";
    public static final String SP_AUTH_INFO = "authInfo";
    public static final String SP_ACCOUNT = "account";
    public static final String SP_NICKNAME = "nickname";
    public static final String SP_TOKEN = "token";
    public static final String SP_REFRESH_TOKEN = "refreshToken";
    public static final String SP_DEVICE_UUID = "device_uuid";
    public static final String SP_USER_ID = "id";


    /**
     * eventBus code
     */
    public static final int EB_UPDATE_NICKNAME = 10002;//更新昵称
    public static final int EB_UPDATE_HEAD = 10003;//更新头像
    public static final int EB_KID_CHANGE = 10004;//宝宝切换
    public static final int EB_LOGIN = 10005;//登录成功
    public static final int EB_LOGOUT = 10006;//退出登录

    public static final int EB_PAD_CHANGE_TAB = 10009;//我的item切换

    public static final int EB_WORK_DRESSED = 10014;//作品装饰完成
    public static final int EB_ADDRESS_ADDED = 10018;//新建地址成功
    public static final int EB_ADDRESS_EDITED = 10019;//编辑地址成功
    public static final int EB_ADDRESS_REMOVE = 10020;//删除地址成功
    public static final int EB_REPAY = 10021;//重新支付
    public static final int EB_WX_PAYED = 10022;//微信支付后
    public static final int EB_RECORD_WORK_UPLOADED = 10023;//录播作品上传完成
    public static final int EB_ADDRESS_CHOOSE = 10024;//选择地址
    public static final int EB_ORDER_CANCEL = 10025;//支付失败，取消订单
    public static final int EB_FIND_RECORD_COURSE = 10026;//发现好课
}
