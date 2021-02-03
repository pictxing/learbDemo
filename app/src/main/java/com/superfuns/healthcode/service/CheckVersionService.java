package com.superfuns.healthcode.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.entity.VersionEntity;
import com.superfuns.healthcode.network.client.HttpClient;
import com.superfuns.healthcode.utils.PackageUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CheckVersionService extends Service {


	@Override
	public void onCreate() {
		super.onCreate();


		startCheckVersion();
	}

	private void startCheckVersion() {

//		Observable.interval(0, 30 * 60 * 1000, TimeUnit.MILLISECONDS).flatMap(new Function<Long, ObservableSource<VersionEntity>>() {
//			@Override
//			public ObservableSource<VersionEntity> apply(Long aLong) throws Exception {
//				Map<String, Object> args = new HashMap<>();
//
//				args.put("applicationName", App.getContext().getPackageName());
//				args.put("enforce", false);
//
//				return HttpClient.getApiServiceInstance().checkVersion(HttpApi.CHECK_VERSION, args);
//			}
//		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
//				.subscribe(new Observer<VersionEntity>() {
//							   @Override
//							   public void onSubscribe(Disposable d) {
//
//							   }
//
//							   @Override
//							   public void onNext(VersionEntity versionInfo) {
//
//								   if (versionInfo.isSuccess()) {
//									   if (versionInfo.getData() != null && versionInfo.getData().getName() > PackageUtils.getLocalVersion()) {
//										   //提示开始更新app
//										   Intent intent=new Intent(Config.UPDATE_VERSION);
//										   intent.putExtra(Config.INSTALL_URL,versionInfo.getData().getUrl());
//										   LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(new Intent(Config.UPDATE_VERSION));
//
//									   }
//								   }
//
//							   }
//
//							   @Override
//							   public void onError(Throwable e) {
//
//							   }
//
//							   @Override
//							   public void onComplete() {
//
//							   }
//						   }
//				);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
