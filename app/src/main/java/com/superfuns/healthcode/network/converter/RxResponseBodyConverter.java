package com.superfuns.healthcode.network.converter;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.superfuns.healthcode.network.entity.HttpResult;
import com.superfuns.healthcode.network.networkutils.ResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class RxResponseBodyConverter<T> implements Converter<ResponseBody, HttpResult<T>> {
	private final Gson gson;
	private final Type type;

	RxResponseBodyConverter(Gson gson, Type type) {
		this.gson = gson;
		this.type = type;
	}

	@Override
	public HttpResult<T> convert(ResponseBody value) throws IOException {

		String responseStr = value.string();
		Log.e("convert", "responseStr:" + responseStr);
		HttpResult<T> httpResult;
		try {
			JSONObject jsonObject = new JSONObject(responseStr);
			try {
				int code = jsonObject.getInt(ResponseParams.RES_CODE);
				String msg = jsonObject.getString(ResponseParams.RES_MSG);
				if (code == ResponseParams.RES_SUCCEED) {
					httpResult = gson.fromJson(responseStr, type);
				} else {
					httpResult = new HttpResult<>();
					httpResult.setCode(code);
					httpResult.setMsg(msg);
				}
			} catch (Exception e) {

				httpResult = gson.fromJson(responseStr, type);

			}


		} catch (JSONException e) {
			e.printStackTrace();
			httpResult = new HttpResult<>();
			httpResult.setCode(401);
			httpResult.setMsg("解析异常");
		} finally {
			value.close();
		}
		return httpResult;
	}
}
