package com.superfuns.healthcode.Constants;

import com.superfuns.healthcode.entity.VersionEntity;
import com.superfuns.healthcode.module.main.MainEntity;
import com.superfuns.healthcode.module.splash.AuthEntity;
import com.superfuns.healthcode.module.splash.LoginEntity;
import com.superfuns.healthcode.network.entity.HttpResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

	//上传健康码信息
	@POST
	Observable<HttpResult<MainEntity>> questUserData(@Url String url, @Body RequestBody body);


	//获取token
	@FormUrlEncoded
	@POST
	Observable<HttpResult<LoginEntity>> questBindMsg(@Url String url, @FieldMap HashMap<String, String> args);

	//获取token
	@FormUrlEncoded
	@POST
	Call<HttpResult<LoginEntity>> questBindMsg2(@Url String url, @FieldMap HashMap<String, String> args);


	//获取授权token
	@POST
	@FormUrlEncoded
	Observable<HttpResult<LoginEntity>> questAuthToken(@Url String url, @FieldMap HashMap<String, String> args);

	//获取授权码

	@GET("api/device/getAuthInfo/{serial_number}")
	Observable<HttpResult<AuthEntity>> questAuthCode(@Header("access_token") String token, @Path("serial_number") String serial_number);

	/**
	 * 版本检测
	 *
	 * @param url
	 * @param args
	 * @return
	 */
//	@Headers("urlName:version")
	@FormUrlEncoded
	@POST()
	Observable<HttpResult<VersionEntity>> checkVersion(@Url String url, @FieldMap Map<String, Object> args);


	@Streaming  // 大文件直接使用流
	@GET
	Observable<ResponseBody> downloadFile(@Url String url);
}
