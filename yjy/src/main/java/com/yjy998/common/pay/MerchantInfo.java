package com.yjy998.common.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lincoln on 15/6/17.
 */
public class MerchantInfo {

    public static Map<String, String> map = new HashMap<>();

   static  {
        //投资通生产环境下的测试商户编号
        map.put("merchantAccount", "10012466312");
        //商户公钥
        map.put("merchantPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0/2ZQejj/9Ofd2tXn1cXGAl0hC9B61er7K757y3K5I/V0jSJ9z7EjsdrfbSc3nd19varWf0NyP+BxVKsNfFLs4w9KPwS3RO8O/oXYX79dIEedo8fwLUNIbZZs0u7FgIbd8+oflU9y0GbNViNsTcz2e5nl+6lOtV+1G1jp/sMsgwIDAQAB");
        //商户私钥
        map.put("merchantPrivateKey", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALT/ZlB6OP/0593a1efVxcYCXSEL0HrV6vsrvnvLcrkj9XSNIn3PsSOx2t9tJzed3X29qtZ/Q3I/4HFUqw18UuzjD0o/BLdE7w7+hdhfv10gR52jx/AtQ0htlmzS7sWAht3z6h+VT3LQZs1WI2xNzPZ7meX7qU61X7UbWOn+wyyDAgMBAAECgYEAsV2uB6MeBEUcip2ODFfCLR3X4TBQpm7GjEf7rBhcXe1M9V/wstU6Qz5gu52dVrUOfc/Ff9jKYUUTTjuoO9je8LbjtbSo9/CoNg2003zNZsy3gB/g68xSdfMrH50hmhgC/JxsghL6s2vuOS94OcJhfeSTDtfCyL9CDbF/8sVFjQECQQD0nLA99usUFx+Zcfn5DFvQS4UlLrN/hxbPi363gNMbvoyqCYnrU4Es9WSDwm/iswtL8VcaMFpRGLibrOU5T5jBAkEAvWyMVonmlrap6H+L5ZduLKJY/keNvIv1xTsAD1KWvsELVWadHLofY6RI1oTTJKL8dDmLqBre+EfcB3VX3LiyQwJAGGcRRf0+EnEEHAC82VWcSzldfQodqhlF80qNR604YkouKkBtW+amul94uZRKKSmHdPoMoHY2bGS09gLXk1IXgQJAMTmvMqH9iXcIqoHS0iRLHlBGPjZMEA5zHQEbH+A/imzSTCwxchCwLY242/6CfEfawT1fHJ13CiBqfomw/owEzwJAUakNggIgh4Oc+bn1pwL8SAKFnGhBGggYlKpD0azoASQ8F6Zs9l8PDmeLuraEvUOPuBMsnSCqNSD2gGX4BgQhQQ==");

        //易宝公玥
        map.put("yeepayPublicKey", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgQcy/1O2wOviQ70ALwa7wNzgOPaFFPrP7hAZn5RQoRxKwen8vV1CbTDVPHnPJtff4noKcqLc1K1hq1SFmXHoM6Xqnpc10cBb95aY6ggSQ01LXKBg/yFQApor4Noxg03g65iHqRp1m4O99a6tyEQJ3JqbQrejZoUPy+8Wrkxs4dwIDAQAB");
        //订单支付请求地址
        map.put("payRequestURL", "https://ok.yeepay.com/paymobile/api/pay/request");


        //取现查询接口请求地址
        map.put("queryWithdrawURL", "https://ok.yeepay.com/payapi/api/tzt/drawrecord");

        //订单查询请求地址
        map.put("paymentQueryURL", "https://ok.yeepay.com/merchant/query_server/pay_single");

        //取现接口请求地址
        map.put("withdrawURL", "https://ok.yeepay.com/payapi/api/tzt/withdraw");

        //绑卡查询接口请求地址
        map.put("queryAuthbindListURL", "https://ok.yeepay.com/payapi/api/bankcard/authbind/list");

        //银行卡信息查询接口请求地址
        map.put("bankCardCheckURL", "https://ok.yeepay.com/payapi/api/bankcard/check");
        //
        //清算数据下载请求地址
        map.put("payClearDataURL", "https://ok.yeepay.com/merchant/query_server/pay_clear_data");
        //
        //单笔退款请求地址
        map.put("refundURL", "https://ok.yeepay.com/merchant/query_server/direct_refund");
        //
        //退款查询请求地址
        map.put("refundQueryURL", "https://ok.yeepay.com/merchant/query_server/refund_single");
        //
        //退款清算文件请求地址
        map.put("refundClearDataURL", "https://ok.yeepay.com/merchant/query_server/refund_clear_data");
        //
        //银行卡解绑请求地址
        map.put("unbindBankcardURL", "https://ok.yeepay.com/payapi/api/tzt/unbind");
    }
}
