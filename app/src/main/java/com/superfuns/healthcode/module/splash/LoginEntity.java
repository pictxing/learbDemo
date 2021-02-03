package com.superfuns.healthcode.module.splash;

public class LoginEntity {


	/**
	 * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjZXNoaXNuMiIsImlkIjoyLCJleHAiOjE2MDkzMTc5NTUsInRva2VuX3R5cGUiOiJEZXZpY2UifQ.274jZOf-xhrxdgYLlyTWL-aFuWiNRasmiU9W04_z1uQ
	 * generate_time : 1609231435285
	 * expire_time : 1609317835285
	 * expire_in : 86400000
	 */

	private String access_token;
	private long generate_time;
	private long expire_time;
	private int expire_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getGenerate_time() {
		return generate_time;
	}

	public void setGenerate_time(long generate_time) {
		this.generate_time = generate_time;
	}

	public long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(long expire_time) {
		this.expire_time = expire_time;
	}

	public int getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(int expire_in) {
		this.expire_in = expire_in;
	}
}
