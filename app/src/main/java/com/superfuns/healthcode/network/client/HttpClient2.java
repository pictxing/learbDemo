package com.superfuns.healthcode.network.client;


import com.superfuns.healthcode.Constants.ApiService;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.network.converter.HttpCovertFactory;
import com.superfuns.healthcode.network.intercepter.NetWorkInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpClient2 {

	private Retrofit retrofit;

	private static final int DEFAULT_TIMEOUT = 5;

	private static ApiService mApiService;


	public static ApiService getApiServiceInstance() {
		if (mApiService == null) {
			synchronized (HttpClient2.class) {
				if (mApiService == null) {
					HttpClient2.mApiService = getService(ApiService.class);
				}
			}
		}
		return mApiService;
	}

	//构造方法私有
	private HttpClient2() {
		// 创建一个OkHttpClient
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		// 设置网络请求超时时间
		builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
		builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
		builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
		// 失败后尝试重新请求
		builder.retryOnConnectionFailure(false);
		//----------------------------基本设置------------------------------------------------------
		retrofit = new Retrofit.Builder()
				.client(builder.build())
				.addConverterFactory(HttpCovertFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.baseUrl(HttpApi.BASEURL)
				.build();
	}

	/**
	 * 调用单例对象
	 */
	private static HttpClient2 getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 创建单例对象
	 */
	private static class SingletonHolder {
		static HttpClient2 INSTANCE = new HttpClient2();
	}

	/**
	 * @return 指定service实例
	 */
	public static <T> T getService(Class<T> clazz) {
		return HttpClient2.getInstance().retrofit.create(clazz);
	}

}
