package com.superfuns.healthcode.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.service.InstallAppService;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Dam on 2018/1/10.
 * <p>
 * 增加配置
 */

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends AppCompatActivity {

	protected P mPresenter;
	private Unbinder mUnbinder;
	protected Activity mActivity;





	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mUnbinder = ButterKnife.bind(this);
		mActivity = this;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(getLayoutId());

		mPresenter = createPresenter();
		mPresenter.attachView((V) this);

		hideSoftInput();

		initData();


	}




	protected abstract P createPresenter();


	public abstract int getLayoutId();


	/**
	 * 隐藏键盘
	 */
	private void hideSoftInput() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}


	@Override
	public void onDestroy() {
		if (mPresenter != null) {
			mPresenter.detachView();
		}
		super.onDestroy();
		mUnbinder.unbind();


	}


	public abstract void initData();


}
