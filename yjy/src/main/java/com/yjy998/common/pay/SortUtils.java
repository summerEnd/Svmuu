package com.yjy998.common.pay;

import com.sp.lib.common.util.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Lincoln on 15/6/17.
 */
public class SortUtils {
    /**
     业务请求参数按参数名排序(按照字母顺序排序,从 a-z)
     Step3:排序后的业务请求参数值拼接成字符串,如将查询绑卡支付结果接口中的基本业务参数值拼接 成的字符串为
     */
    public static String getValueParams(JSONObject object){
        StringBuilder stringBuilder=new StringBuilder();
        Iterator<String> keys = object.keys();
        String[] values=new String[object.length()];
        int i=0;
        while (keys.hasNext()){
            String key=keys.next();
            try {
                values[i]=object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }

        Arrays.sort(values, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        for (String value:values){
            stringBuilder.append(value);
        }
        SLog.debug(stringBuilder);
        return stringBuilder.toString();
    }
}
