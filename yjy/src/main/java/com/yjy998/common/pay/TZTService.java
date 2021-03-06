package com.yjy998.common.pay;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;

/**
 * 投资通接口范例--WAP版
 * @author	：yingjie.wang    
 * @since	：2015-05-24 
 */

public class TZTService {

	/**
	 * 取得商户编号
	 */
	public static String getMerchantAccount() {
		return Configuration.getInstance().getValue("merchantAccount");
	}

	/**
	 * 取得商户私钥
	 */
	public static String getMerchantPrivateKey() {
		return Configuration.getInstance().getValue("merchantPrivateKey");
	}

	/**
	 * 取得商户AESKey
	 */
	public static String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

	/**
	 * 取得易宝公玥
	 */
	public static String getYeepayPublicKey() {
		return Configuration.getInstance().getValue("yeepayPublicKey");
	}

	/**
	 * 格式化字符串
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * String2Integer
	 */
	public static int String2Int(String text) throws NumberFormatException {
		return text == null ? 0 : Integer.valueOf(text);
	}

	/**
	 * 支付请求地址
	 */
	public static String getPayRequestURL() {
		return Configuration.getInstance().getValue("payRequestURL");
	}

	/**
	 * 订单查询请求地址
	 */
	public static String getPaymentQueryURL() {
		return Configuration.getInstance().getValue("paymentQueryURL");
	}

	/**
	 * 取现接口请求地址
	 */
	public static String getWithdrawURL() {
		return Configuration.getInstance().getValue("withdrawURL");
	}

	/**
	 * 取现查询请求地址
	 */
	public static String getQueryWithdrawURL() {
		return Configuration.getInstance().getValue("queryWithdrawURL");
	}

	/**
	 * 取现查询请求地址
	 */
	public static String getQueryAuthbindListURL() {
		return Configuration.getInstance().getValue("queryAuthbindListURL");
	}

	/**
	 * 银行卡信息查询请求地址
	 */
	public static String getBankCardCheckURL() {
		return Configuration.getInstance().getValue("bankCardCheckURL");
	}

	/**
	 * 清算文件下载请求地址
	 */
	public static String getPayClearDataURL() {
		return Configuration.getInstance().getValue("payClearDataURL");
	}

	/**
	 * 单笔退款请求地址
	 */
	public static String getRefundURL() {
		return Configuration.getInstance().getValue("refundURL");
	}

	/**
	 * 退款查询请求地址
	 */
	public static String getRefundQueryURL() {
		return Configuration.getInstance().getValue("refundQueryURL");
	}

	/**
	 * 退款清算文件请求地址
	 */
	public static String getRefundClearDataURL() {
		return Configuration.getInstance().getValue("refundClearDataURL");
	}

	/**
	 * 银行卡解绑接口请求地址
	 */
	public static String getUnbindBankcardURL() {
		return Configuration.getInstance().getValue("unbindBankcardURL");
	}



	/**
	 * 解析http请求返回
	 */
	public static Map<String, String> parseHttpResponseBody(int statusCode, String responseBody) throws Exception {

		String merchantPrivateKey	= getMerchantPrivateKey();
		String yeepayPublicKey		= getYeepayPublicKey();

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";

		if(statusCode != 200) {
			customError	= "Request failed, response code : " + statusCode;
			result.put("customError", customError);
			return (result);
		}

		Map<String, String> jsonMap	= JSON.parseObject(responseBody,
											new TypeReference<TreeMap<String, String>>() {});

		if(jsonMap.containsKey("error_code")) {
			result	= jsonMap;
			return (result);
		}

		String dataFromYeepay		= formatString(jsonMap.get("data"));
		String encryptkeyFromYeepay	= formatString(jsonMap.get("encryptkey"));

		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay,
										yeepayPublicKey, merchantPrivateKey);
		if(!signMatch) {
			customError	= "Sign not match error";
			result.put("customError",	customError);
			return (result);
		}

		String yeepayAESKey		= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
		String decryptData		= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);

		result	= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return(result);
	}



	/**
	 * getPayURL() : 返回网页支付链接
	 */

	public static String getPayURL(Map<String, String> params) {

		String payurl				= "";

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payRequestURL		= getPayRequestURL();

		System.out.println("##### getPayURL() #####");
		System.out.println("params : " + params);
		System.out.println("payRequestURL : " + payRequestURL);

		String orderid              = formatString(params.get("orderid"));
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String identityid           = formatString(params.get("identityid"));
		String userip               = formatString(params.get("userip"));
		String terminalid           = formatString(params.get("terminalid"));
		String userua               = formatString(params.get("userua"));
		String productdesc          = formatString(params.get("productdesc"));
		String fcallbackurl         = formatString(params.get("fcallbackurl"));
		String callbackurl          = formatString(params.get("callbackurl"));
		String paytypes             = formatString(params.get("paytypes"));
		String cardno				= formatString(params.get("cardno"));
		String idcardtype			= formatString(params.get("idcardtype"));
		String idcard				= formatString(params.get("idcard"));
		String owner 				= formatString(params.get("owner"));

		int transtime				= 0;
		int amount              	= 0;
		int identitytype        	= 0;
		int terminaltype        	= 0;
		int orderexpdate        	= 0;
		int currency	        	= 0;
		int version		        	= 0;

		try {
			//transtime、identitytype、amount是必填参数
			if(params.get("transtime") == null) {
				throw new Exception("transtime is null!!!!!");
			} else {
				transtime			= String2Int(params.get("transtime"));
			}

			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype        	= String2Int(params.get("identitytype"));
			}

			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount              	= String2Int(params.get("amount"));
			}

			if(params.get("terminaltype") == null) {
				throw new Exception("terminaltype is null!!!!!");
			} else {
				terminaltype           	= String2Int(params.get("terminaltype"));
			}

			orderexpdate        	= String2Int(params.get("orderexpdate"));
			currency	        	= String2Int(params.get("currency"));

			if("".equals(params.get("version"))) {
				version				= 0;
			} else {
				version				= String2Int(params.get("version"));
			}

		} catch(Exception e) {
			e.printStackTrace();
			StringBuffer buffer		= new StringBuffer();
			buffer.append("error - the following parameters must be int  : ");
			buffer.append("[transtime = " + formatString(params.get("transtime")) + "], ");
			buffer.append("[amount = " + formatString(params.get("amount")) + "], ");
			buffer.append("[identitytype = " + formatString(params.get("identitytype")) + "], ");
			buffer.append("[terminaltype = " + formatString(params.get("terminaltype")) + "], ");
			buffer.append("[orderexpdate = " + formatString(params.get("orderexpdate")) + "], ");
			buffer.append("[currency = " + formatString(params.get("currency")) + "], ");
			buffer.append("[version = " + formatString(params.get("verision")) + "].");
			payurl = buffer.toString();
			return (payurl);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid", 			orderid);
		dataMap.put("productcatalog", 	productcatalog);
		dataMap.put("productname", 		productname);
		dataMap.put("identityid", 		identityid);
		dataMap.put("userip", 			userip);
		dataMap.put("terminalid", 		terminalid);
		dataMap.put("userua", 			userua);
		dataMap.put("transtime", 		transtime);
		dataMap.put("amount", 			amount);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("terminaltype", 	terminaltype);
		dataMap.put("productdesc", 		productdesc);
		dataMap.put("fcallbackurl", 	fcallbackurl);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("paytypes", 		paytypes);
		dataMap.put("currency", 		currency);
		dataMap.put("orderexpdate", 	orderexpdate);
		dataMap.put("version", 			version);
		dataMap.put("cardno", 			cardno);
		dataMap.put("idcardtype", 		idcardtype);
		dataMap.put("idcard", 			idcard);
		dataMap.put("owner", 			owner);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("dataMap : " + dataMap);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data = " + data);
			System.out.println("encryptkey = " + encryptkey);

			StringBuffer buffer			= new StringBuffer();
			buffer.append(payRequestURL);
			buffer.append("?merchantaccount=").append(URLEncoder.encode(merchantaccount, "UTF-8"));
			buffer.append("&data=").append(URLEncoder.encode(data, "UTF-8"));
			buffer.append("&encryptkey=").append(URLEncoder.encode(encryptkey, "UTF-8"));

			payurl						= buffer.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("payurl : " + payurl);

		return (payurl);
	}


	/**
	 * decryptCallbackData() : 解密支付回调参数data
	 *
	 */

	public static Map<String, String> decryptCallbackData(String data, String encryptkey) {

		System.out.println("##### decryptCallbackData() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();

		System.out.println("data : " + data);
		System.out.println("encryptkey : " + encryptkey);

		Map<String, String> callbackResult	= new HashMap<String, String>();
		String customError			= "";

		try {
			boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptkey, yeepayPublicKey, merchantPrivateKey);

			if(!signMatch) {
				customError	= "Sign not match error";
				callbackResult.put("customError",	customError);
				return callbackResult;
			}

			String yeepayAESKey	= RSA.decrypt(encryptkey, merchantPrivateKey);
			String decryptData	= AES.decryptFromBase64(data, yeepayAESKey);
			callbackResult		= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			callbackResult.put("customError", customError);
			e.printStackTrace();
		}

		System.out.println("callbackResult : " + callbackResult);

		return (callbackResult);
	}


	/**
	 * queryByOrder() : 查询接口
	 */

	public static Map<String, String> queryByOrder(String orderid, String yborderid) {

		System.out.println("##### queryByOrder() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String paymentQueryURL		= getPaymentQueryURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid",			orderid);
		dataMap.put("yborderid",		yborderid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("paymentQueryURL : " + paymentQueryURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url				= paymentQueryURL +
									  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
									  "&data=" + URLEncoder.encode(data, "UTF-8") +
									  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			System.out.println("url	 : " + url);

			getMethod				= new GetMethod(url);
			int statusCode			= httpClient.executeMethod(getMethod);
			String responseBody		= getMethod.getResponseBodyAsString();

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}


	/**
	 * withdraw() : 提现接口
	 */

	public static Map<String, String> withdraw(Map<String, String> params) {

		System.out.println("##### withdraw() #####");

		Map<String, String> result			= new HashMap<String, String>();
        String customError	   				= "";	//自定义，非接口返回

		String merchantaccount				= getMerchantAccount();
		String merchantPrivateKey			= getMerchantPrivateKey();
		String merchantAESKey				= getMerchantAESKey();
		String yeepayPublicKey				= getYeepayPublicKey();
		String withdrawURL					= getWithdrawURL();

		String requestid            		= formatString(params.get("requestid"));
		String identityid           		= formatString(params.get("identityid"));
		String card_top             		= formatString(params.get("card_top"));
		String card_last            		= formatString(params.get("card_last"));
		String drawtype             		= formatString(params.get("drawtype"));
		String imei                 		= formatString(params.get("imei"));
		String userip               		= formatString(params.get("userip"));
		String ua                   		= formatString(params.get("ua"));

		int amount              			= 0;
		int identitytype        			= 0;
		int currency	        			= 0;

		try {
			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype        	= String2Int(params.get("identitytype"));
			}

			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount              	= String2Int(params.get("amount"));
			}

			currency	        	= String2Int(params.get("currency"));

		} catch(Exception e) {
			StringBuffer buffer		= new StringBuffer();
			buffer.append("error - the following parameters must be int  : ");
			buffer.append("[amount = " + formatString(params.get("amount")) + "], ");
			buffer.append("[identitytype = " + formatString(params.get("identitytype")) + "], ");
			buffer.append("[currency = " + formatString(params.get("currency")) + "]. ");
			e.printStackTrace();
			customError	= buffer.toString();
			result.put("customError", customError);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount",	merchantaccount);
		dataMap.put("requestid", 		requestid);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("card_top", 		card_top);
		dataMap.put("card_last", 		card_last);
		dataMap.put("amount", 			amount);
		dataMap.put("currency", 		currency);
		dataMap.put("drawtype", 		drawtype);
		dataMap.put("imei", 			imei);
		dataMap.put("userip", 			userip);
		dataMap.put("ua", 				ua);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("withdrawURL : " + withdrawURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient			= new HttpClient();
		PostMethod postMethod			= new PostMethod(withdrawURL);

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas		= {new NameValuePair("merchantaccount", merchantaccount),
										   new NameValuePair("data", data),
										   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode				= httpClient.executeMethod(postMethod);
			byte[] responseByte			= postMethod.getResponseBody();
			String responseBody			= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

	
	/**
	 * queryWithdraw() : 提现查询接口
	 */
	
	public static Map<String, String> queryWithdraw(String requestid, String ybdrawflowid) {

		System.out.println("##### queryWithdraw() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String queryWithdrawURL		= getQueryWithdrawURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("requestid",		requestid);
		dataMap.put("ybdrawflowid",		ybdrawflowid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("requestid : " + requestid);
		System.out.println("ybdrawflowid : " + ybdrawflowid);
		System.out.println("merchantaccount : " + merchantaccount);
		System.out.println("merchantPrivateKey : " + merchantPrivateKey);
		System.out.println("yeepayPublicKey : " + yeepayPublicKey);
		System.out.println("queryWithdrawURL : " + queryWithdrawURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result		= new HashMap<String, String>();
        String customError     			= "";  // 自定义参数，非接口返回。

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= queryWithdrawURL +
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);
			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();

			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

		
	/**
	 * queryAuthbindList() : 绑卡查询接口 
	 *
	 */

	public static Map<String, String> queryAuthbindList(String identityid, String identitytype) {

		System.out.println("##### queryAuthbindList() #####");

		Map<String, String> result		= new HashMap<String, String>();
		String customError				= "";	//自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String queryAuthbindListURL	= getQueryAuthbindListURL();

		int identitytype2Int		= -1;

		try {
			identitytype2Int	= String2Int(identitytype);
		} catch(Exception e) {
			e.printStackTrace();
			customError			= "String 2 Int Error!!! - identitytype = [" + identitytype + "]";
			result.put("customError", customError);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", merchantaccount);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype2Int);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("queryAuthbindListURL : " + queryAuthbindListURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= queryAuthbindListURL +
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);

			int statusCode				= httpClient.executeMethod(getMethod);
			String responseBody			= getMethod.getResponseBodyAsString();

			System.out.println("url	 : " + url);
			System.out.println("responseBody : " + responseBody);

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

				
	/**
	 * bankCardCheck() : 银行卡信息查询接口 
	 *
	 */

	public static Map<String, String> bankCardCheck(String cardno) {

		System.out.println("##### bankCardCheck() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankCardCheckURL		= getBankCardCheckURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("cardno", 			cardno);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("bankCardCheckURL : " + bankCardCheckURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";

		HttpClient httpClient		= new HttpClient();
		PostMethod postMethod		= new PostMethod(bankCardCheckURL);

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			NameValuePair[] datas	= {new NameValuePair("merchantaccount", merchantaccount),
									   new NameValuePair("data", data),
									   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode			= httpClient.executeMethod(postMethod);
			byte[] responseByte		= postMethod.getResponseBody();
			String responseBody		= new String(responseByte, "UTF-8");

			System.out.println("responseBody : " + responseBody);

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

				
	/**
	 * getPathOfPayClearData()：获取清算数据
	 *
 	 * 返回说明：
	 *
	 * filePath				- 批量查询结果文件的路径
	 * error_code			- 错误返回码
	 * error				- 错误信息
	 * customError			- 自定义，非接口返回
	 *
	 */

	public static Map<String, String> getPathOfPayClearData(String startdate, String enddate, String sysPath) {

		System.out.println("##### getPathOfPayClearData() #####");


		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payClearDataURL		= getPayClearDataURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		Map<String, String> queryResult	= new HashMap<String, String>();
		String filePath					= "";
		String error_code              	= "";
		String error                   	= "";
		String customError				= "";

		HttpClient httpClient			= new HttpClient();
		GetMethod getMethod				= new GetMethod();

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= payClearDataURL +
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);

			int statusCode				= httpClient.executeMethod(getMethod);

			if(statusCode != 200) {
				customError = "Get request failed, response code = " + statusCode;
				queryResult.put("customError", customError);
				return (queryResult);
			}

			InputStream	responseStream	= getMethod.getResponseBodyAsStream();
			BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
			//BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream));

			String line					= reader.readLine();
			if(line.startsWith("{")) {
				Map<String, Object> jsonMap	= JSON.parseObject(line, TreeMap.class);

				if(jsonMap.containsKey("error_code")) {
					error_code					= formatString((String)jsonMap.get("error_code"));
					error						= formatString((String)jsonMap.get("error"));
				} else {
					String dataFromYeepay		= formatString((String)jsonMap.get("data"));
					String encryptkeyFromYeepay	= formatString((String)jsonMap.get("encryptkey"));

					String yeepayAESKey					= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
					String decryptData					= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
					Map<String, Object> decryptDataMap	= JSON.parseObject(decryptData, TreeMap.class);

					error_code 					= formatString((String)decryptDataMap.get("error_code"));
					error						= formatString((String)decryptDataMap.get("error"));

					System.out.println("decryptData : " + decryptData);
				}
			} else {
				String outputFilePath				= sysPath + File.separator + "clearData";
				File file							= new File(outputFilePath);
				file.mkdir();

				String time							= String.valueOf(System.currentTimeMillis());
				String fileName						= "payClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
				String absolutePathOfOutputFile		= outputFilePath + File.separator + fileName;
				filePath							= absolutePathOfOutputFile;

				File outputFile						= new File(absolutePathOfOutputFile);
				FileWriter fileWriter				= new FileWriter(outputFile);
				BufferedWriter writer				= new BufferedWriter(fileWriter);

				System.out.println("filePath : " + filePath);

				writer.write(line);
				writer.write(System.getProperty("line.separator"));
				while((line = reader.readLine()) != null) {
					writer.write(line);
					writer.write(System.getProperty("line.separator"));
				}

				writer.close();
			}
		} catch(Exception e) {
			customError = "Caught an Exception. " + e.toString();
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		queryResult.put("filePath",		filePath);
		queryResult.put("error_code",	error_code);
		queryResult.put("error",		error);
		queryResult.put("customError",	customError);

		return (queryResult);
	}


	/**
	 * refund() : 单笔退款方法
	 */

	public static Map<String, String> refund(Map<String, String> params) {

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回

		System.out.println("##### refund() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundURL			= getRefundURL();

		String origyborderid		= formatString(params.get("origyborderid"));
		String orderid          	= formatString(params.get("orderid"));
		String cause            	= formatString(params.get("cause"));

		int amount              	= 0;
		int currency	        	= 0;

		try {
			//amount、currency是必填参数
			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount              	= String2Int(params.get("amount"));
			}

			if(params.get("currency") == null) {
				throw new Exception("currency is null!!!!!");
			} else {
				currency              	= String2Int(params.get("currency"));
			}

		} catch(Exception e) {
			e.printStackTrace();
			customError	= "******input params error : String to Int Exception - " +
								"], amount=[" + amount +
								"], currency=[" + currency + "]" + e.toString();
			result.put("customError", customError);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("origyborderid",	origyborderid);
		dataMap.put("orderid", 			orderid);
		dataMap.put("cause",			cause);
		dataMap.put("amount", 			amount);
		dataMap.put("currency", 		currency);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("refundURL : " + refundURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient		= new HttpClient();
		PostMethod postMethod		= new PostMethod(refundURL);

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas	= {new NameValuePair("merchantaccount", merchantaccount),
									   new NameValuePair("data", data),
									   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode			= httpClient.executeMethod(postMethod);
			byte[] responseByte		= postMethod.getResponseBody();
			String responseBody		= new String(responseByte, "UTF-8");

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError	= "Caught an Exception. " + e.toString();
			result.put("customError", 	customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}


	/**
	 * refundQuery() : 退款查询
	 */

	public static Map<String, String> refundQuery(String orderid) {

		System.out.println("##### refundQuery() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundQueryURL		= getRefundQueryURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid",			orderid);

		String sign	= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("refundQueryURL : " + refundQueryURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
    	String customError        	= "";

		HttpClient httpClient		= new HttpClient();
		GetMethod getMethod			= new GetMethod();

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url				= refundQueryURL +
									  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
									  "&data=" + URLEncoder.encode(data, "UTF-8") +
									  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod				= new GetMethod(url);
			int statusCode			= httpClient.executeMethod(getMethod);
			String responseBody		= getMethod.getResponseBodyAsString();

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError		= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}


	/**
	 * getPathOfRefundClearData() 
	 *
	 */

	public static Map<String, String> getPathOfRefundClearData(String startdate, String enddate, String sysPath) {

		System.out.println("##### getPathOfRefundClearData() #####");


		String merchantaccount			= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String refundClearDataURL		= getRefundClearDataURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("refundClearDataURL : " + refundClearDataURL);
		System.out.println("dataMap : " + dataMap);

		Map<String, String> queryResult	= new HashMap<String, String>();
		String filePath					= "";
		String error_code              	= "";
		String error                   	= "";
		String customError             	= "";

		HttpClient httpClient			= new HttpClient();
		GetMethod getMethod				= new GetMethod();

		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= refundClearDataURL +
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			getMethod					= new GetMethod(url);
			int statusCode				= httpClient.executeMethod(getMethod);

			if(statusCode != 200) {
				customError = "Get request failed, response code = " + statusCode;
				queryResult.put("customError", customError);
				return (queryResult);
			}

			InputStream	responseStream	= getMethod.getResponseBodyAsStream();
			BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
			//BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream));

			String line					= reader.readLine();
			if(line.startsWith("{")) {
				Map<String, Object> jsonMap	= JSON.parseObject(line, TreeMap.class);

				if(jsonMap.containsKey("error_code")) {
					error_code					= formatString((String)jsonMap.get("error_code"));
					error						= formatString((String)jsonMap.get("error"));
				} else {
					String dataFromYeepay		= formatString((String)jsonMap.get("data"));
					String encryptkeyFromYeepay	= formatString((String)jsonMap.get("encryptkey"));

					String yeepayAESKey					= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
					String decryptData					= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
					Map<String, Object> decryptDataMap	= JSON.parseObject(decryptData, TreeMap.class);

					error_code 					= formatString((String)decryptDataMap.get("error_code"));
					error						= formatString((String)decryptDataMap.get("error"));

					System.out.println("decryptData : " + decryptData);
				}
			} else {
				String outputFilePath				= sysPath + File.separator + "clearData";
				File file							= new File(outputFilePath);
				file.mkdir();

				String time							= String.valueOf(System.currentTimeMillis());
				String fileName						= "refundClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
				String absolutePathOfOutputFile		= outputFilePath + File.separator + fileName;
				filePath							= absolutePathOfOutputFile;

				File outputFile						= new File(absolutePathOfOutputFile);
				FileWriter fileWriter				= new FileWriter(outputFile);
				BufferedWriter writer				= new BufferedWriter(fileWriter);

				System.out.println("filePath : " + filePath);

				writer.write(line);
				writer.write(System.getProperty("line.separator"));
				while((line = reader.readLine()) != null) {
					writer.write(line);
					writer.write(System.getProperty("line.separator"));
				}

				writer.close();
			}
		} catch(Exception e) {
			customError	= "Caught an Exception. " + e.toString();
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		queryResult.put("filePath",		filePath);
		queryResult.put("error_code",	error_code);
		queryResult.put("error",		error);
		queryResult.put("customError", customError);

		return (queryResult);
	}


	/**
	 * unbindBankcard() : 银行卡解绑方法
	 */

	public static Map<String, String> unbindBankcard(Map<String, String> params) {

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回

		System.out.println("##### unbindBankcard() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String unbindBankcardURL	= getUnbindBankcardURL();

		String card_top          	= formatString(params.get("card_top"));
		String card_last          	= formatString(params.get("card_last"));
		String identityid			= formatString(params.get("identityid"));

		int identitytype	        = 0;

		try {
			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype              	= String2Int(formatString(params.get("identitytype")));
			}
		} catch(Exception e) {
			e.printStackTrace();
			customError	= "******input params error : String to Int Exception - " +
								"identitytype = [" + identitytype + "]" + e.toString();
			result.put("customError", customError);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("card_top", 		card_top);
		dataMap.put("card_last", 		card_last);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 	identitytype);

		String sign						= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		System.out.println("unbindBankcardURL : " + unbindBankcardURL);
		System.out.println("dataMap : " + dataMap);

		HttpClient httpClient		= new HttpClient();
		PostMethod postMethod		= new PostMethod(unbindBankcardURL);

		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			System.out.println("data : " + data);
			System.out.println("encryptkey : " + encryptkey);

			NameValuePair[] datas	= {new NameValuePair("merchantaccount", merchantaccount),
									   new NameValuePair("data", data),
									   new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode			= httpClient.executeMethod(postMethod);
			byte[] responseByte		= postMethod.getResponseBody();
			String responseBody		= new String(responseByte, "UTF-8");

			result					= parseHttpResponseBody(statusCode, responseBody);

		} catch(Exception e) {
			customError	= "Caught an Exception. " + e.toString();
			result.put("customError", 	customError);
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		System.out.println("result : " + result);

		return (result);
	}

}
