package com.superfuns.healthcode.module.splash;

import android.text.TextUtils;
import android.widget.Toast;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BasePresenter;
import com.superfuns.healthcode.entity.VersionEntity;
import com.superfuns.healthcode.utils.ArgumentFromat;
import com.superfuns.healthcode.utils.Sptools;

import java.util.HashMap;

public class SplashPresenter extends BasePresenter<SplashView> implements SplashModel.CallBack {
	SplashModel mSplashModel;

	public SplashPresenter(BaseActivity baseActivity) {
		mSplashModel = new SplashModel(baseActivity);
	}

	public void bindMsg() {
		HashMap<String, String> args = new HashMap<>();
		args.put("serial_number", getView().getSnCode());
		args.put("auth_code", getView().getAuthCode());
		GlobeUserData.getInstance().setSerial_number(getView().getSnCode());
		GlobeUserData.getInstance().setAuth_code(getView().getAuthCode());

		mSplashModel.requestBindMessage(args, this);
	}

	/**
	 * 获取授权码token
	 */
	public void obtainAuthToken() {
		if (TextUtils.isEmpty(getView().getSnCode())) {
			Toast.makeText(App.getContext(), "请输入序列号", Toast.LENGTH_LONG).show();
			return;
		}
		mSplashModel.questAuthToken(this);
	}

	@Override
	public void setAuthTokenInfo(LoginEntity loginEntity) {
		String token = loginEntity.getAccess_token();
		obtainAuthCode(token);
	}

	/**
	 * 获取授权码
	 */
	public void obtainAuthCode(String token) {
		mSplashModel.questAuthCode(this, token, getView().getSnCode());
	}

	@Override
	public void setAuthCodeInfo(AuthEntity authEntity) {
		getView().setAuthCode(authEntity.getAuth_code_str());
	}

	public void startCheckVersion() {
		mSplashModel.checkVersionUpdate(this);
	}

	@Override
	public void setVersionMessageFail() {
		getView().getVersionResultFail();
	}

	@Override
	public void setVersionMessage(VersionEntity versionInfo) {

		getView().getVersionResult(versionInfo);
	}

	@Override
	public void setLoginMessage(LoginEntity loginMessage) {
		GlobeUserData.getInstance().setLoginEntity(loginMessage);

		Sptools.setValue(Config.DEVICE_SERIAL_NO, GlobeUserData.getInstance().getSerial_number());
		Sptools.setValue(Config.DEVICE_AUTH_NO, GlobeUserData.getInstance().getAuth_code());
		Sptools.setValue(Config.DEVICE_BIND_INFO, ArgumentFromat.gson.toJson(loginMessage));

		getView().skipMainActivity();
	}


}
