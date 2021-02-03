package com.superfuns.healthcode.Constants;

import okhttp3.MediaType;

public interface HttpApi {

	String HEADER = "Content-type:application/json; charset=UTF-8";
	MediaType JSON = MediaType.parse("application/json; charset=UTF-8");

//	String BASEURL = "http://test.code.first-zone.cn";
	String BASEURL = "http://jkm.first-zone.cn";

//	String API = "/track_api";
	String API = "/api";
	String UPLOADTRACK = API + "/track/uploadTrack";
	String BINDINFO = API + "/device/getDeviceToken";

	String URL_Version = "http://www.first-zone.cn:38002"; // 版本更新地址
//	String URL_Version = "http://192.168.0.241:9999"; // 版本更新地址


	String CHECK_VERSION = "/version/getByApplicationName";


	//获取授权码token
	String AUTH_TOKEN= API +"/sysUser/getToken";
	//获取授权码
	String AUTH_INFO= API +"/device/getAuthInfo/{serial_number}";

}
