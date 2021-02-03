package com.superfuns.healthcode.module.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.R;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.db.impl.HealthCodeDaoImpl;
import com.superfuns.healthcode.entity.VersionEntity;
import com.superfuns.healthcode.module.main.Main2Activity;
import com.superfuns.healthcode.module.main.MainScanActivity;
import com.superfuns.healthcode.service.InstallAppService;
import com.superfuns.healthcode.utils.ArgumentFromat;
import com.superfuns.healthcode.utils.PackageUtils;
import com.superfuns.healthcode.utils.Sptools;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity<SplashView, SplashPresenter> implements SplashView {
	@BindView(R.id.sn_code)
	EditText snCode;
	@BindView(R.id.auth_code)
	EditText authCode;
	@BindView(R.id.bind)
	Button bind;
	@BindView(R.id.obtain_auth)
	Button obtainAuth;

	public final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

	@Override
	protected SplashPresenter createPresenter() {
		return new SplashPresenter(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_splash;
	}

	@Override
	public void initData() {

		applyPermission();

		Intent intent = new Intent(this, InstallAppService.class);
		bindService(intent, mConnection, BIND_AUTO_CREATE);
		registerBroadCast();

		mPresenter.startCheckVersion();


	}

	@Override
	public void getVersionResultFail() {
		initView();
	}

	@Override
	public void getVersionResult(VersionEntity versionEntity) {
		if (versionEntity != null && versionEntity.getVersionNumber() > PackageUtils.getLocalVersion() && !TextUtils.isEmpty(versionEntity.getUrl())) {
			showUpdateDialog(versionEntity.getUrl());
			//开始更新

		} else {
			initView();
		}
	}


	private void initView() {

		if (TextUtils.isEmpty((String) Sptools.getValue(Config.DEVICE_BIND_INFO, ""))) {
			snCode.setVisibility(View.VISIBLE);
			authCode.setVisibility(View.VISIBLE);
			obtainAuth.setVisibility(View.VISIBLE);
			snCode.setText((String) Sptools.getValue(Config.DEVICE_SERIAL_NO, ""));
			authCode.setText((String) Sptools.getValue(Config.DEVICE_AUTH_NO, ""));
			bind.setVisibility(View.VISIBLE);
		} else {
			Observable.create(new ObservableOnSubscribe<Integer>() {
				@Override
				public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
					GlobeUserData.getInstance().setLoginEntity(ArgumentFromat.getGson().fromJson((String) Sptools.getValue(Config.DEVICE_BIND_INFO, ""), LoginEntity.class));
					GlobeUserData.getInstance().setSerial_number((String) Sptools.getValue(Config.DEVICE_SERIAL_NO, ""));
					GlobeUserData.getInstance().setAuth_code((String) Sptools.getValue(Config.DEVICE_AUTH_NO, ""));
					HealthCodeDaoImpl healthCodeDao = HealthCodeDaoImpl.getInstance();
					healthCodeDao.deleteTwoWeekAfter();
					emitter.onComplete();
				}
			}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Integer>() {
				@Override
				public void onNext(Integer integer) {
				}

				@Override
				public void onError(Throwable e) {
				}

				@Override
				public void onComplete() {
					skipMainActivity();
					SplashActivity.this.finish();
					dispose();
				}
			});
		}
	}


	@Override
	public void setAuthCode(String authCodes) {
		authCode.setText(authCodes);
	}

	private void applyPermission() {
		RxPermissions rxPermissions = new RxPermissions(this);
		rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
			@Override
			public void accept(Permission permission) throws Exception {
				if (permission.granted) {
					// 用户已经同意该权限
				} else if (permission.shouldShowRequestPermissionRationale) {
					// 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
					getAppDetailSettingIntent(SplashActivity.this);
				} else {
					// 用户拒绝了该权限，而且选中『不再询问』
				}
			}
		});
	}

	private static Intent getAppDetailSettingIntent(Context context) {
		Intent localIntent = new Intent();
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));

		return localIntent;
	}

	@Override
	public void skipMainActivity() {
		startActivity(new Intent(SplashActivity.this, Main2Activity.class));
//		startActivity(new Intent(SplashActivity.this, MainScanActivity.class));
		SplashActivity.this.finish();
	}

	@OnClick({R.id.bind, R.id.obtain_auth})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.bind:
				if (TextUtils.isEmpty(snCode.getText().toString())) {
					Toast.makeText(SplashActivity.this, "请输入设备序列号", Toast.LENGTH_SHORT).show();

				} else if (TextUtils.isEmpty(authCode.getText().toString())) {
					Toast.makeText(SplashActivity.this, "请输入设备授权码", Toast.LENGTH_SHORT).show();
				} else {
					mPresenter.bindMsg();
				}

				break;
			case R.id.obtain_auth:
				mPresenter.obtainAuthToken();
				break;
		}


	}

	@Override
	public String getSnCode() {
		return snCode.getText().toString();
	}

	@Override
	public String getAuthCode() {
		return authCode.getText().toString();
	}


	UpdateReceiver mUpdateReceiver = new UpdateReceiver();

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			switch (msg.what) {
				case 0:
					PackageUtils.installApk(SplashActivity.this, Config.DOWNLOAD_UPDATE_APP_PATH);
					break;
			}
			return true;
		}
	});


	public InstallAppService.UpdateBinder mBinder;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinder = (InstallAppService.UpdateBinder) service;

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	private void registerBroadCast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Config.UPDATE_VERSION);
		registerReceiver(mUpdateReceiver, filter);
	}


	private class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//开始更新app
			mHandler.sendEmptyMessage(0);

		}
	}


	private void showUpdateDialog(String url) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("版本更新");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mBinder != null)
					mBinder.downLoadApk(HttpApi.URL_Version + url);
				dialog.dismiss();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mConnection != null)
			unbindService(mConnection);

		unregisterReceiver(mUpdateReceiver);
	}
}
