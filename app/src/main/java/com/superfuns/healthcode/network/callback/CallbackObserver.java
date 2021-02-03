package com.superfuns.healthcode.network.callback;

import android.util.Log;


import com.superfuns.healthcode.network.entity.HttpResult;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;


public abstract class CallbackObserver<T> extends DisposableObserver<HttpResult<T>> {

	private static final String TAG = "RxRequestCallBack";
	private long mStartCurrentLong;

	/**
	 * 网络请求成功的回调方法，必须重写
	 */
	public abstract void onSuccess(HttpResult<T> httpResult);

	/**
	 * 默认访问请求
	 */
	protected CallbackObserver() {

	}

	@Override
	public void onStart() {
		super.onStart();
		mStartCurrentLong = System.currentTimeMillis();
	}


	@Override
	public void onComplete() {
		Log.e(TAG, "onCompleted: ");
		dispose();
	}

	@Override
	public void onError(Throwable e) {
		HttpResult httpResult=new HttpResult();
		if (e instanceof SocketTimeoutException) {
			httpResult.setMsg("网络中断，请检查您的网络状态");
			Log.e(TAG, "SocketTimeoutException: 网络中断，请检查您的网络状态");
		} else if (e instanceof ConnectException) {
			httpResult.setMsg("网络中断，请检查您的网络状态");
			Log.e(TAG, "ConnectException: 网络中断，请检查您的网络状态");
		} else if (e instanceof UnknownHostException) {
			httpResult.setMsg("网络中断，请检查您的网络状态");
			Log.e(TAG, "UnknownHostException: 网络中断，请检查您的网络状态");
		} else {
			httpResult.setMsg("onError:其他错误：" + e.getMessage() + "  cause: " + e.getCause());
			Log.e(TAG, "onError:其他错误：" + e.getMessage() + "  cause: " + e.getCause());
		}

		onFailed(httpResult);
		e.printStackTrace();
		dispose();
	}

	@Override
	public void onNext(HttpResult<T> tHttpResult) {
//		Log.e(TAG, "onNext:   code--" + tHttpResult.getCode() + "--msg--" + tHttpResult.getMsg());
		if (tHttpResult.getCode() == 401) {
			Log.e(TAG, "onNext:  Json_error");
			onFailed(tHttpResult);
		} else if (tHttpResult.getCode() != 200  && tHttpResult.getCode()!=0) {
			onFailed(tHttpResult);
			Log.e(TAG, "onNext:  onFailed");
		} else {
			onSuccess(tHttpResult);
			long callBackTime = System.currentTimeMillis() - mStartCurrentLong;
			Log.e(TAG, "onNext:  onSuccess , callback time： " + callBackTime + " ms");
		}
	}

	protected void onFailed(HttpResult<T> tHttpResult) {

	}


}
