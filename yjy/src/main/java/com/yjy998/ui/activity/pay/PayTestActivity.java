package com.yjy998.ui.activity.pay;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.pay.AES;
import com.yjy998.common.pay.RSA;
import com.yjy998.common.pay.RandomUtil;
import com.yjy998.common.pay.SortUtils;
import com.yjy998.common.pay.TZTService;
import com.yjy998.ui.activity.base.YJYActivity;
import com.yjy998.ui.activity.main.more.WebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class PayTestActivity extends YJYActivity {

    private EditText priceText;
    private Button confirm;
    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALT/ZlB6OP/0593a1efVxcYCXSEL0HrV6vsrvnvLcrkj9XSNIn3PsSOx2t9tJzed3X29qtZ/Q3I/4HFUqw18UuzjD0o/BLdE7w7+hdhfv10gR52jx/AtQ0htlmzS7sWAht3z6h+VT3LQZs1WI2xNzPZ7meX7qU61X7UbWOn+wyyDAgMBAAECgYEAsV2uB6MeBEUcip2ODFfCLR3X4TBQpm7GjEf7rBhcXe1M9V/wstU6Qz5gu52dVrUOfc/Ff9jKYUUTTjuoO9je8LbjtbSo9/CoNg2003zNZsy3gB/g68xSdfMrH50hmhgC/JxsghL6s2vuOS94OcJhfeSTDtfCyL9CDbF/8sVFjQECQQD0nLA99usUFx+Zcfn5DFvQS4UlLrN/hxbPi363gNMbvoyqCYnrU4Es9WSDwm/iswtL8VcaMFpRGLibrOU5T5jBAkEAvWyMVonmlrap6H+L5ZduLKJY/keNvIv1xTsAD1KWvsELVWadHLofY6RI1oTTJKL8dDmLqBre+EfcB3VX3LiyQwJAGGcRRf0+EnEEHAC82VWcSzldfQodqhlF80qNR604YkouKkBtW+amul94uZRKKSmHdPoMoHY2bGS09gLXk1IXgQJAMTmvMqH9iXcIqoHS0iRLHlBGPjZMEA5zHQEbH+A/imzSTCwxchCwLY242/6CfEfawT1fHJ13CiBqfomw/owEzwJAUakNggIgh4Oc+bn1pwL8SAKFnGhBGggYlKpD0azoASQ8F6Zs9l8PDmeLuraEvUOPuBMsnSCqNSD2gGX4BgQhQQ==";
    private String yibaoPublicKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_test);
        initialize();
    }

    private void initialize() {

        priceText = (EditText) findViewById(R.id.priceText);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(payListener);
    }

    private View.OnClickListener payListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // mytest();
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Map<String, String> json = new Hashtable<String, String>();
                    json.put("merchantaccount", "10012466312");
                    json.put("orderid", "12321");
                    json.put("amount", "100");//以"分"为单
                    json.put("productname", "测试产品");//
                    json.put("productdesc", "好产品");//最
                    json.put("identityid", "1332");//最
                    json.put("identitytype", "1");
                    json.put("transtime", new Date().getTime() / 1000 + "");
                    json.put("terminaltype", "3");
                    json.put("currency", "156");
                    json.put("version", "0");
                    json.put("productcatalog", "30");
                    json.put("userip", "192.198.22.1");
                    json.put("userua", "192.198.22.1");
                    json.put("orderexpdate", "100");//
                    json.put("paytypes", "1");//格式：1|2
                    //                    json.put("cardno", "6227001700130618827");
                    json.put("idcardtype", "01");//01：身
                    json.put("idcard", "341223198709223510");
                    json.put("owner", "朱超");//持卡人姓名

                    return TZTService.getPayURL(json);
                }

                @Override
                protected void onPostExecute(String s) {
                    jumpPage(new SRequest(s));
                }
            }.execute("");

        }
    };

    private void mytest() {
        //            request.put("orderid","1231");//商户生成的唯一订单号，最长50位
        //            request.put("transtime",new Date().getTime()/1000);//时间戳，例如：1361324896，精确到秒
        //            request.put("amount","100");//以"分"为单位的整型，必须大于零
        //            request.put("productname","测试产品");//最长50位，出于风控考虑，请按下面的格式传递值：应用-商品名称，如“诛仙-3阶成品天琊”
        //            request.put("productdesc","好产品");//最长200位
        //            request.put("identityid","1332");//最长50位，商户生成的用户账号唯一标识
        //            request.put("identitytype","1");
        //            request.put("orderexpdate","100");//订单有效期时间
        //            request.put("paytypes","1");//格式：1|2|3|4  1- 借记卡支付； 2- 信用卡支付； 3- 手机充值卡支付； 4- 游戏点卡支付 注：该参数若不传此参数，则默认选择运营后台为该商户开通的支付方式。
        //            request.put("cardno","6227001700130618827");//银行卡号
        //            request.put("idcardtype","1");//01：身份证，注意：证件类型和证件号必须同时为空或者同时不为空
        //            request.put("idcard","341223198709223510");//证件号
        //            request.put("owner", "朱超");//持卡人姓名
        //            request.put("sign", "d");//签名

        JSONObject json = new JSONObject();
        try {

            json.put("merchantaccount", "10012466312");
            json.put("orderid", "1231");
            json.put("amount", "100");//以"分"为单
            json.put("productname", "测试产品");//
            json.put("productdesc", "好产品");//最
            json.put("identityid", "1332");//最
            json.put("identitytype", "1");
            json.put("orderexpdate", "100");//
            json.put("paytypes", "1");//格式：1|2
            json.put("cardno", "6227001700130618827");
            json.put("idcardtype", "1");//01：身
            json.put("idcard", "341223198709223510");
            json.put("owner", "朱超");//持卡人姓名

            String sign = RSA.sign(SortUtils.getValueParams(json), privateKey);
            json.put("sign", sign);
            String randomKey = RandomUtil.getRandom(16);

            SLog.debug(json.toString(4));
            String data = AES.encryptToBase64(json.toString(), randomKey);


            String encryptkey = RSA.encrypt(randomKey, yibaoPublicKey);


            SRequest request = new SRequest("https://ok.yeepay.com/payapi/api/bankcard/check");
            request.put("merchantaccount", "10012466312");
            request.put("sign", sign);
            request.put("cardno", "6227001700130618827");
            request.put("data", data);
            request.put("encryptkey", encryptkey);
            //                jumpPage(request);
            post(request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void jumpPage(SRequest request) {
        startActivity(new Intent(PayTestActivity.this, WebViewActivity.class)
                .putExtra(WebViewActivity.EXTRA_URL, request.getUrlWithParams()));
    }

    void post(SRequest request) {
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                SLog.debug(response.message);
            }
        });
    }

}
