package com.superfuns.healthcode;

import android.app.Application;
import android.content.Context;

import com.idata.fastscan.IScanApp;
import com.superfuns.healthcode.db.UpDBHelper;
import com.superfuns.healthcode.db.impl.CommonCacheImpl;

public class App extends IScanApp {
//public class App extends Application {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;

		UpDBHelper upDBHelper = CommonCacheImpl.getUpDBHelper();
		upDBHelper.getWritableDatabase();
		upDBHelper.close();
	}


	public static Context getContext() {
		return mContext;
	}


}
