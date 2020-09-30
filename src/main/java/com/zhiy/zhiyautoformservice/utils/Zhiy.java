package com.zhiy.zhiyautoformservice.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;


/**
 * @description:
 * @author: liukun
 * @create: 2020-08-19 16:30
 */
public class Zhiy extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Zhiy() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    public static Zhiy error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static Zhiy error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static Zhiy error(int code, String msg) {
        Zhiy r = new Zhiy();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Zhiy ok(String msg) {
        Zhiy r = new Zhiy();
        r.put("msg", msg);
        return r;
    }

    public static Zhiy ok(Map<String, Object> map) {
        Zhiy r = new Zhiy();
        r.putAll(map);
        return r;
    }

    public static Zhiy ok() {
        return new Zhiy();
    }

    public Zhiy put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}