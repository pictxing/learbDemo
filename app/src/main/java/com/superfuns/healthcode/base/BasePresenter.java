package com.superfuns.healthcode.base;

import android.util.Log;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> implements Presenter<V>{
	private WeakReference<V> mMvpView;

	@Override
	public void attachView(V mvpView) {
		this.mMvpView = new WeakReference<>(mvpView);
	}


	protected V getView() {
		return mMvpView.get();
	}


	@Override
	public void detachView() {
		if (mMvpView != null) {
			mMvpView.clear();
			mMvpView = null;
		}
	}


}
