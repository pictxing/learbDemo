package com.superfuns.healthcode.module.splash;

public class GlobeUserData {
	private static GlobeUserData sGlobeUserData;

	public GlobeUserData() {
	}

	public static GlobeUserData getInstance() {
		if (sGlobeUserData == null) {

			synchronized (GlobeUserData.class) {

				sGlobeUserData = new GlobeUserData();

			}

		}

		return sGlobeUserData;
	}


	private  LoginEntity mLoginEntity;
	private String serial_number;
	private String auth_code;

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public LoginEntity getLoginEntity() {
		return mLoginEntity;
	}

	public void setLoginEntity(LoginEntity loginEntity) {
		mLoginEntity = loginEntity;
	}
}
