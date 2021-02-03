package com.superfuns.healthcode.module.splash;

import com.superfuns.healthcode.entity.VersionEntity;

public interface SplashView {
	void skipMainActivity();
	String getSnCode();
	String getAuthCode();


	void  getVersionResult(VersionEntity versionEntity);
	void  getVersionResultFail();

	void setAuthCode(String authCode);
}
