package com.superfuns.healthcode.network.intercepter;


import android.text.TextUtils;
import android.util.Log;

import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.module.splash.GlobeUserData;
import com.superfuns.healthcode.module.splash.LoginEntity;
import com.superfuns.healthcode.network.client.HttpClient;
import com.superfuns.healthcode.network.client.HttpClient2;
import com.superfuns.healthcode.network.entity.HttpResult;
import com.superfuns.healthcode.network.networkutils.ResponseParams;
import com.superfuns.healthcode.utils.ArgumentFromat;
import com.superfuns.healthcode.utils.Sptools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.internal.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class NetWorkInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		if (GlobeUserData.getInstance().getLoginEntity() != null && !TextUtils.isEmpty(GlobeUserData.getInstance().getLoginEntity().getAccess_token())) {

//			if (GlobeUserData.getInstance().getLoginEntity().getExpire_time() < System.currentTimeMillis() - 10 * 6 * 10000) {
//				String snCode = GlobeUserData.getInstance().getSerial_number();
//				String authCode = GlobeUserData.getInstance().getAuth_code();
//
//				HashMap<String, String> args = new HashMap<>();
//				args.put("serial_number", snCode);
//				args.put("auth_code", authCode);
//
//
//				Call<HttpResult<LoginEntity>> call = HttpClient2.getApiServiceInstance().questBindMsg2(HttpApi.BINDINFO, args);
//				retrofit2.Response<HttpResult<LoginEntity>> execute = call.execute();
//				if (execute.code() == 200) {
//
//					LoginEntity loginEntity = execute.body().getData();
//					GlobeUserData.getInstance().setLoginEntity(loginEntity);
//					Log.e("TAG", "loginEntity:" + ArgumentFromat.getGson().toJson(GlobeUserData.getInstance().getLoginEntity()));
//					Sptools.setValue(Config.DEVICE_SERIAL_NO, GlobeUserData.getInstance().getSerial_number());
//				}
//			}
//
			Request request = chain.request()
					.newBuilder()
					.removeHeader("access_token")
					.addHeader("access_token", GlobeUserData.getInstance().getLoginEntity().getAccess_token())
					.build();
//
//			return chain.proceed(request);
//
//
			Response response = chain.proceed(request);
			if (response.code() == 401) {
				Log.e("TAG", "response.code():" + 401);
				String snCode = GlobeUserData.getInstance().getSerial_number();
				String authCode = GlobeUserData.getInstance().getAuth_code();

				HashMap<String, String> args = new HashMap<>();
				args.put("serial_number", snCode);
				args.put("auth_code", authCode);


				Call<HttpResult<LoginEntity>> call = HttpClient2.getApiServiceInstance().questBindMsg2(HttpApi.BINDINFO, args);
				retrofit2.Response<HttpResult<LoginEntity>> execute = call.execute();
				if (execute.code() == 200) {

					LoginEntity loginEntity = execute.body().getData();
					int code = execute.body().getCode();
					if (code != 200 || loginEntity == null) {
						return response;
					}
					GlobeUserData.getInstance().setLoginEntity(loginEntity);
					Log.e("TAG", "loginEntity:" + ArgumentFromat.getGson().toJson(GlobeUserData.getInstance().getLoginEntity()));
					Sptools.setValue(Config.DEVICE_BIND_INFO, ArgumentFromat.getGson().toJson(GlobeUserData.getInstance().getLoginEntity()));
				}

				Request newRequest = chain.request()
						.newBuilder()
						.removeHeader("access_token")
						.addHeader("access_token", GlobeUserData.getInstance().getLoginEntity().getAccess_token())
						.build();
				return chain.proceed(newRequest);

			} else {
				return response;
			}

		}
		return chain.proceed(chain.request());
	}
}
