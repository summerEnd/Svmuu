package com.sp.lib.common.util;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {

    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static <T> T get(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static <T> T get(JSONObject object, Class<T> cls) {
        return get(object.toString(), cls);
    }

    /**
     * 返回一个新的列表
     *
     * @throws JSONException
     */
    public static <T> ArrayList<T> getArray(JSONArray array, Class<T> cls) throws JSONException {
        ArrayList<T> list = new ArrayList<T>();

        if (array==null){
            return list;
        }

        for (int i = 0; i < array.length(); i++) {
            T t = gson.fromJson(array.get(i).toString(), cls);
            list.add(t);
        }
        return list;
    }

    /**
     * @param array 用来解析的数据源
     * @param cls   解析后的实体类型
     * @param list  用来存放数据的集合
     * @throws JSONException
     */
    public static <T> void getArray(JSONArray array, Class<T> cls, List<T> list) throws JSONException {
        if (array==null){
            return;
        }
        for (int i = 0; i < array.length(); i++) {
            T t = gson.fromJson(array.get(i).toString(), cls);

            list.add(t);
        }
    }

    public static void debugJsonObject(JSONObject object) {
        if (object == null) {
            SLog.debug("null");
        }else{
            Iterator<String> keys = object.keys();
            while (keys.hasNext()){
                try {
                    String key = keys.next();
                    Object o = object.get(key);
                    SLog.debug_format("key->%s value->%s",key,String.valueOf(o));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
