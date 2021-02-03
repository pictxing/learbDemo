package com.superfuns.healthcode.Constants;

import android.content.Intent;
import android.os.Environment;

public class Config {
	public static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;
	public static final String DEVICE_SERIAL_NO = "serial_no";
	public static final String DEVICE_AUTH_NO = "auth_no";
	public static final String DEVICE_BIND_INFO = "bind_info";

	public static final int START_MEARSURE_TEMPEARURE = 0;
	public static final int START_SCAN_CODE = 1;
	public static final int EXAMINE_COMPLETE = 2;
	public static final int HEALTH_CODE_FAIL = 3;
	public static final int UPLOAD_TEMPEARTURE=4;
	public static final int UPLOAD_TEMPEARTUREING=5;

	public static final int DURATION_TIME = 500;
	public static final int DURATION_DOUBLE_TIME = 1000;

	public static final String GREEN_CODE = "0";
	public static final String GREEN_CODE_TXT = "绿码";
	public static final String YELLOW_CODE = "1";
	public static final String YELLOW_CODE_TXT = "黄码";
	public static final String RED_CODE = "10";
	public static final String RED_CODE_TXT = "红码";

	public static final String RES_ACTION = "android.intent.action.SCANRESULT";

	public static final String UPDATE_VERSION="INSTALL_APP";
	public static final String INSTALL_URL="INSTALL_URL";

	/**
	 * 获取授权码账号
	 */
	public static final String USERNAME="chaoyoufanapi";
	public static final String PASSWORD="chaoyoufanapi";

	/**
	 * App 下载更新路径
	 */
	public static final String DOWNLOAD_UPDATE_APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/superfuns/healthcode.apk";

}
