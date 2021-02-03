package com.superfuns.healthcode.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.idata.fastscan.IScanApp;
import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.R;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.db.impl.HealthCodeDaoImpl;
import com.superfuns.healthcode.db.table.HealthData;
import com.superfuns.healthcode.module.record.TrafficRecordActivity;
import com.superfuns.healthcode.module.splash.GlobeUserData;
import com.superfuns.healthcode.module.splash.SplashActivity;
import com.superfuns.healthcode.network.networkutils.NetworkUtil;
import com.superfuns.healthcode.utils.DateUtils;
import com.superfuns.healthcode.utils.PackageUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MainScanActivity extends BaseActivity<MainView, MainPresenter> implements MainView {


	@BindView(R.id.sum_count)
	TextView sumCount;
	@BindView(R.id.exception_count)
	TextView exceptionCount;
	@BindView(R.id.middle_status)
	ImageView middleStatus;
	@BindView(R.id.middle_temp)
	TextView middleTemperature;
	@BindView(R.id.name)
	TextView name;
	@BindView(R.id.prompt)
	TextView prompt;
	@BindView(R.id.temp)
	TextView temperature;
	@BindView(R.id.reset_measure)
	Button resetMeasure;
	@BindView(R.id.green_code_count)
	TextView greenCount;

	@BindView(R.id.layout)
	ConstraintLayout mLayout;

	private int currentStatus = 0;

	HealthCodeDaoImpl healthCodeDao;
	DateChangeReceiver mDateChangeReceiver;

	@Override
	protected MainPresenter createPresenter() {
		return new MainPresenter(this);

	}


	@Override
	public int getLayoutId() {
		return R.layout.activity_main_scan;
	}


	private float clickTime;


	@Override
	public void questHealthCodeFail() {

	}

	//执行扫码等功能
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (IScanApp.isScanKey(keyCode)) {

			if (SystemClock.uptimeMillis() - clickTime <= Config.DURATION_TIME) {
				clickTime = SystemClock.uptimeMillis();
				return false;
			}
			if (currentStatus == Config.START_MEARSURE_TEMPEARURE)
				mPresenter.startScanCamDecode();
			else {
				measureComplete();
			}

		}

		return super.onKeyDown(keyCode, event);
	}


	//初始化
	@Override
	public void initData() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Config.ACTION_DATE_CHANGED);
		registerReceiver(mDateChangeReceiver = new DateChangeReceiver(), intentFilter);

		healthCodeDao = HealthCodeDaoImpl.getInstance();
		//扫码初始化
		mPresenter.initScanner();
		mPresenter.queryLoadAllData();

		mLayout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mPresenter.startCancellation();
				return false;
			}
		});
	}


	@Override
	public void initMeasureTemperatureSuccess() {
	}

	@Override
	public void initMeasureTemperatureFail() {

	}


	@Override
	public void showHealthData(MainEntity mainEntity) {
		healthData = new HealthData();
		healthData.setDate(DateUtils.getCurrentDate());
		healthData.setCode(mainEntity.getCard_no());
		healthData.setDateValue(System.currentTimeMillis());
		if (mainEntity.getHealth_str().contains(Config.GREEN_CODE_TXT)) {
			mPresenter.playPassTextToSpeech("绿码请通行");
			middleStatus.setImageResource(R.drawable.green_code);
			healthData.setStatus(Config.GREEN_CODE);
		} else if (mainEntity.getHealth_str().contains(Config.YELLOW_CODE_TXT)) {
			mPresenter.playPassTextToSpeech("黄码请注意");
			middleStatus.setImageResource(R.drawable.yellow_code);
			healthData.setStatus(Config.YELLOW_CODE);
		} else if (mainEntity.getHealth_str().contains(Config.RED_CODE_TXT)) {
			mPresenter.playPassTextToSpeech("红码请注意");
			middleStatus.setImageResource(R.drawable.red_code);
			healthData.setStatus(Config.RED_CODE);
		}
		healthData.setName(mainEntity.getName());
		healthCodeDao.saveSingleData(healthData);
		mPresenter.queryLoadAllData();
		prompt.setText(getResources().getString(R.string.complete));
		name.setText(getResources().getString(R.string.name) + mainEntity.getName());
		currentStatus = Config.EXAMINE_COMPLETE;
	}


	@Override
	public void scanDecodeFail() {
	}

	HealthData healthData;


	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			switch (msg.what) {
				case 0:
					String code = (String) msg.obj;
					upLoadHealthCodeMessage(code);
					break;
			}
			return true;
		}
	});


	@Override
	public void scanDecodeSuccess(String result) {

		Message message = new Message();

		message.obj = result;
		message.what = 0;
		mHandler.sendMessage(message);

	}


	private void upLoadHealthCodeMessage(String result) {

		//根据码请求人员信息  获取网络请求
		if (NetworkUtil.isNetworkConnected(App.getContext())) { //有网络
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("health_code_str", result);
			hashMap.put("visit_time", DateUtils.getCurrentDate());
			hashMap.put("serial_number", GlobeUserData.getInstance().getSerial_number());
//			hashMap.put("temperature", healthData.getTemperature());
			mPresenter.questUserData(hashMap);
		} else {  //无网络
			measureComplete();
			Toast.makeText(this, "网络异常，请重新扫描", Toast.LENGTH_SHORT).show();
		}


	}


	@Override
	public void measureTemperatureFail() {

	}


	@Override
	public void measureTemperatureSuccess(final float temp) {
	}

	@Override
	public void queryDataCount(InLoadEntity inLoadEntity) {
		sumCount.setText(String.valueOf(inLoadEntity.getAllCount()));
		greenCount.setText(String.valueOf(inLoadEntity.getAllCount() - inLoadEntity.getExceptionCount()));
		exceptionCount.setText(String.valueOf(inLoadEntity.getExceptionCount()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.onDestory();
		unregisterReceiver(mDateChangeReceiver);
		System.exit(0);
	}

	public void measureComplete() {
		middleStatus.setImageResource(R.drawable.health_code);
		name.setText("");
		prompt.setText(getResources().getString(R.string.please_scan));
		currentStatus = Config.START_MEARSURE_TEMPEARURE;
	}


	@OnClick({R.id.traffic_record, R.id.reset_measure, R.id.prompt})
	public void onViewClicked(View view) {
		if (SystemClock.uptimeMillis() - clickTime <= Config.DURATION_TIME) {
			clickTime = SystemClock.uptimeMillis();
			return;
		}

		switch (view.getId()) {
			case R.id.traffic_record://记录
				startActivity(new Intent(MainScanActivity.this, TrafficRecordActivity.class));

				break;
			case R.id.reset_measure:
				mPresenter.startTemperatureDecode();
				break;
			case R.id.prompt:
				if (currentStatus == Config.START_MEARSURE_TEMPEARURE) {
					mPresenter.startScanCamDecode();
				} else if (currentStatus == Config.EXAMINE_COMPLETE) {
					measureComplete();
				}
				break;
		}
	}

	//日期发生了改变
	public class DateChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Config.ACTION_DATE_CHANGED)) {
				mPresenter.deleteOverdueData();
				mPresenter.queryLoadAllData();
			}
		}
	}
}
