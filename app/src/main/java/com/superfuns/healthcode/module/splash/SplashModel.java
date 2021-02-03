package com.superfuns.healthcode.module.splash;

import android.widget.Toast;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BaseModule;
import com.superfuns.healthcode.entity.VersionEntity;
import com.superfuns.healthcode.network.callback.CallbackObserver;
import com.superfuns.healthcode.network.client.HttpClient;
import com.superfuns.healthcode.network.entity.HttpResult;
import com.superfuns.healthcode.utils.PackageUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashModel extends BaseModule {
	public SplashModel(BaseActivity act) {
		super(act);
	}


	public void requestBindMessage(HashMap<String, String> args, CallBack callBack) {

		HttpClient.getApiServiceInstance().questBindMsg(HttpApi.BINDINFO, args)
				.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new CallbackObserver<LoginEntity>() {
					@Override
					public void onSuccess(HttpResult<LoginEntity> httpResult) {
						callBack.setLoginMessage(httpResult.getData());
					}

					@Override
					protected void onFailed(HttpResult<LoginEntity> loginEntityHttpResult) {
						Toast.makeText(mActivity, loginEntityHttpResult.getMsg(), Toast.LENGTH_SHORT).show();
					}
				});


	}


	public void questAuthToken(CallBack callBack) {
		HashMap<String, String> args = new HashMap<>();
		args.put("username", Config.USERNAME);
		args.put("password", Config.PASSWORD);
		HttpClient.getApiServiceInstance().questAuthToken(HttpApi.AUTH_TOKEN, args)
				.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new CallbackObserver<LoginEntity>() {
					@Override
					public void onSuccess(HttpResult<LoginEntity> httpResult) {
						callBack.setAuthTokenInfo(httpResult.getData());
					}

					@Override
					protected void onFailed(HttpResult<LoginEntity> loginEntityHttpResult) {
						Toast.makeText(mActivity, loginEntityHttpResult.getMsg(), Toast.LENGTH_SHORT).show();

					}
				});


	}


	public void questAuthCode(CallBack callBack, String token, String serial_number) {
		HttpClient.getApiServiceInstance().questAuthCode(token, serial_number).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackObserver<AuthEntity>() {
			@Override
			public void onSuccess(HttpResult<AuthEntity> httpResult) {
				callBack.setAuthCodeInfo(httpResult.getData());
			}

			@Override
			protected void onFailed(HttpResult<AuthEntity> authEntityHttpResult) {
				Toast.makeText(mActivity, authEntityHttpResult.getMsg(), Toast.LENGTH_SHORT).show();
			}
		});
	}


	public void checkVersionUpdate(CallBack callBack) {

		Map<String, Object> args = new HashMap<>();
		args.put("applicationName", App.getContext().getPackageName());
		args.put("enforce", false);
		HttpClient.getApiServiceInstance().checkVersion(HttpApi.URL_Version + HttpApi.CHECK_VERSION, args).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackObserver<VersionEntity>() {
			@Override
			public void onSuccess(HttpResult<VersionEntity> httpResult) {
//				if (httpResult.getData() != null && httpResult.getData().getName() > PackageUtils.getLocalVersion()) {
				callBack.setVersionMessage(httpResult.getData());
//				}
			}

			@Override
			protected void onFailed(HttpResult<VersionEntity> versionEntityHttpResult) {
				callBack.setVersionMessageFail();
				super.onFailed(versionEntityHttpResult);
			}
		});


	}


	interface CallBack {
		void setLoginMessage(LoginEntity loginMessage);

		void setVersionMessage(VersionEntity versionInfo);

		void setVersionMessageFail();


		void setAuthTokenInfo(LoginEntity loginEntity);

		void setAuthCodeInfo(AuthEntity authEntity);
	}
}
