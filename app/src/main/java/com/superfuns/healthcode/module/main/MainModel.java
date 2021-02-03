package com.superfuns.healthcode.module.main;

import android.widget.Toast;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BaseModule;
import com.superfuns.healthcode.network.callback.CallbackObserver;
import com.superfuns.healthcode.network.client.HttpClient;
import com.superfuns.healthcode.network.entity.HttpResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class MainModel extends BaseModule {
	public MainModel(BaseActivity act) {
		super(act);
	}


	public void questUserData(String url, RequestBody body, CallBack callBack) {

		HttpClient.getApiServiceInstance().questUserData(url, body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackObserver<MainEntity>() {
			@Override
			public void onSuccess(HttpResult<MainEntity> httpResult) {
				callBack.getUserDataSuccess(httpResult.getData());
			}

			@Override
			protected void onFailed(HttpResult<MainEntity> mainEntityHttpResult) {
				callBack.getUserDataFail();
			}
		});

	}


	interface CallBack {
		void getUserDataSuccess(MainEntity mainEntity);
		void getUserDataFail();
	}
}
