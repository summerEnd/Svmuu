package com.yjy998.common.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Configuration {
	

	private static Object lock              = new Object();
	private  Map<String,String> config     = null;
	private static Configuration instance;

	private Configuration(Map<String, String> config) {
		this.config = config;
	}

	public static Configuration getInstance() {
		synchronized(lock) {
			if(null == instance) {
				instance=new Configuration(MerchantInfo.map);

			}
		}
		return (instance);
	}
	
	public String getValue(String key) {
		String s = config.get(key);
		return s;
	}
}