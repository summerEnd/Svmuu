package com.sp.lib.common.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static <T> T get(String json, Class<T> cls) {
        return new Gson().fromJson(json, cls);
    }


    /**
     * 返回一个新的列表
     *
     * @throws JSONException
     */
    public static <T> ArrayList<T> getArray(JSONArray array, Class<T> cls) throws JSONException {
        ArrayList<T> list = new ArrayList<T>();
        Gson gson = new Gson();

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
        Gson gson = new Gson();

        for (int i = 0; i < array.length(); i++) {
            T t = gson.fromJson(array.get(i).toString(), cls);

            list.add(t);
        }
    }

}
