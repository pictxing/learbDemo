package com.superfuns.healthcode.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.network.client.HttpClient;
import com.superfuns.healthcode.utils.PackageUtils;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class InstallAppService extends Service {

	public UpdateBinder mUpdateBinder;

	@Override
	public void onCreate() {
		super.onCreate();
		mUpdateBinder = new UpdateBinder();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		if (mUpdateBinder != null)
			return mUpdateBinder;
		return null;
	}

	public class UpdateBinder extends Binder {


		//开始下载apk
		public void downLoadApk(String url) {

			HttpClient.getApiServiceInstance().downloadFile(url).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<ResponseBody>() {
				@Override
				public void onSubscribe(Disposable d) {

				}

				@Override
				public void onNext(ResponseBody body) {
					Log.e("TAG", "getVersionResult3: ");
					File downloadApk = new File(Config.DOWNLOAD_UPDATE_APP_PATH);
					if (downloadApk.exists()) {
						downloadApk.delete();
					}
					boolean file = PackageUtils.writeFileFromIS(downloadApk, body.byteStream(), false);
					if (file) {
						Intent intent =new Intent();
						intent.setAction(Config.UPDATE_VERSION);
						App.getContext().sendBroadcast(intent);
					}else{
					}

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onComplete() {

				}
			});


		}
	}
}
