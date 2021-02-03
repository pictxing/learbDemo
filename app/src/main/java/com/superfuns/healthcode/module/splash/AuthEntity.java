package com.superfuns.healthcode.module.splash;

public class AuthEntity {

	private String serial_number;
	private String auth_code_str;
	private int status;
	private String  status_str;

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getAuth_code_str() {
		return auth_code_str;
	}

	public void setAuth_code_str(String auth_code_str) {
		this.auth_code_str = auth_code_str;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatus_str() {
		return status_str;
	}

	public void setStatus_str(String status_str) {
		this.status_str = status_str;
	}
}
