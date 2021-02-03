package com.superfuns.healthcode.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.superfuns.healthcode.Constants.HttpApi;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by tyl on 2017/9/9.
 */

public class ArgumentFromat {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

//    private static Map<String, Object> args = Collections.synchronizedMap(new HashMap<String, Object>());

    public static synchronized RequestBody formatBody(Map<String, Object> args) {
        String json = gson.toJson(args);
        Log.d("test", "formatBody:" + json);
        RequestBody body = RequestBody.create(HttpApi.JSON, json);
        return body;
    }

    public static synchronized RequestBody format(Object object) {

        String json = gson.toJson(object);

        RequestBody body = RequestBody.create(HttpApi.JSON, json);

        return body;
    }

    public static Gson getGson() {
        return gson;
    }

    public static String getRequestString(Map<String, Object> args) {
        return gson.toJson(args);
    }
}
