package com.yjy998.common;

public class Constant {
    /**
     * 手势密码相关参数
     */
    public static final String PRE_GESTURE = "gesture_password";//SharePreference名称
    public static final String PRE_GESTURE_PASSWORD = "password";//手势密码
    public static final String PRE_GESTURE_ENABLED = "enabled";//是否启动手势密码
    public static final String PRE_GESTURE_FAILS = "failTimes";//失败次数
    public static final String PRE_GESTURE_DATE = "date";//解锁日期
    public static final int PRE_GESTURE_MAX_FAILED = 5;//手势密码一天做多错误次数

    /**
     * 登录相关参数
     */
    public static final String PRE_LOGIN = "login";
    public static final String PRE_LOGIN_PASSWORD = "password";
    public static final String PRE_LOGIN_PASSWORD_RSA = "password_rsa";
    public static final String PRE_LOGIN_PHONE = "login_name";

}
