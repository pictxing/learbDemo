package com.superfuns.healthcode.module.main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.idata.fastscan.IScanApp;
import com.idata.ise.scanner.decoder.CamDecodeAPI;
import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.R;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.db.impl.HealthCodeDaoImpl;
import com.superfuns.healthcode.db.table.HealthData;
import com.superfuns.healthcode.module.record.TrafficRecordActivity;
import com.superfuns.healthcode.module.splash.GlobeUserData;
import com.superfuns.healthcode.network.networkutils.NetworkUtil;
import com.superfuns.healthcode.utils.DateUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class Main2Activity extends BaseActivity<MainView, MainPresenter> implements MainView {


	@BindView(R.id.sum_count)
	TextView sumCount;
	@BindView(R.id.exception_count)
	TextView exceptionCount;
	@BindView(R.id.middle_status)
	ImageView middleStatus;
//	@BindView(R.id.middle_temp)
////	TextView middleTemperature;
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


	private int currentStatus = -1;//执行状态  0扫码  1测温  2完成

	private int measureCount = 0;

	HealthCodeDaoImpl healthCodeDao;
	DateChangeReceiver mDateChangeReceiver;

	@Override
	protected MainPresenter createPresenter() {
		return new MainPresenter(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.initScanDecode();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_main2;
	}


	private float clickTime;

	//执行扫码等功能
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (IScanApp.isScanKey(keyCode)) {
			if (SystemClock.uptimeMillis() - clickTime <= Config.DURATION_DOUBLE_TIME) {
				return false;
				//如果两次的时间差＜500ms，就不执行操作
			}
			clickTime = SystemClock.uptimeMillis();
			if (currentStatus == Config.START_MEARSURE_TEMPEARURE) {
				currentStatus=Config.UPLOAD_TEMPEARTUREING;
				mPresenter.startTemperatureDecode();
			} else if (currentStatus == Config.START_SCAN_CODE) {
				healthData = new HealthData();
				measureCount = 0;
				mPresenter.startScanCamDecode();
			} else if (currentStatus == Config.EXAMINE_COMPLETE) {
				measureComplete();
			} else if (currentStatus == Config.HEALTH_CODE_FAIL) {
				mPresenter.startScanCamDecode();
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (SystemClock.uptimeMillis() - clickTime <= Config.DURATION_TIME) {
				//如果两次的时间差＜500ms，就不执行操作
			} else {
				//当前系统时间的毫秒值
				clickTime = SystemClock.uptimeMillis();
				return false;
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
		mPresenter.initIMeasureTemp();
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
		currentStatus = Config.START_SCAN_CODE;
		mPresenter.setIMeasureTempMode();
	}

	@Override
	public void initMeasureTemperatureFail() {
		prompt.setText("测温模块上电失败");
	}


	@Override
	public void showHealthData(MainEntity mainEntity) {
//		middleTemperature.setText("");
		temperature.setText(getResources().getString(R.string.temperature) + healthData.getTemperature());
//		middleStatus.setVisibility(View.VISIBLE);
		healthData.setDate(DateUtils.getCurrentDate());
		healthData.setIdCard(mainEntity.getCard_no());
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
	public void questHealthCodeFail() {
		middleStatus.setImageResource(R.drawable.scan_reset);
		currentStatus = Config.HEALTH_CODE_FAIL;
		prompt.setText(getResources().getString(R.string.please_scan));

	}

	@Override
	public void scanDecodeFail() {

	}

	HealthData healthData;




	@Override
	public void scanDecodeSuccess(String result) {
		Log.e("TAG", "scanDecodeSuccess: "+result);
		healthData.setCode(result);
//		middleStatus.setVisibility(View.GONE);
		middleStatus.setImageResource(R.drawable.scan_success);
//		middleTemperature.setText("扫码成功");
		if (currentStatus == Config.START_SCAN_CODE) {
			currentStatus = Config.START_MEARSURE_TEMPEARURE;
//			middleTemperature.setVisibility(View.VISIBLE);
			prompt.setText(getResources().getString(R.string.please_measure));
			mPresenter.playTextToSpeech("请测温");
		} else {
			startUploadTemperature();
		}



	}


	@Override
	public void measureTemperatureFail() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				measureCount++;
				if (measureCount < 2) {
					mPresenter.playTextToSpeech("请靠近重新测温");
					currentStatus = Config.START_MEARSURE_TEMPEARURE;
//				resetMeasure.setVisibility(View.VISIBLE);
//					middleStatus.setVisibility(View.GONE);
					middleStatus.setImageResource(R.drawable.measure_fail);
					healthData.setTemperature(0f);
					prompt.setText(getResources().getString(R.string.reset_measure));
					temperature.setTextColor(getResources().getColor(R.color.blackColor));
//					middleTemperature.setTextColor(getResources().getColor(R.color.blackColor));
//					middleTemperature.setText("0°");
				} else {
					healthData.setTemperature(0f);
					startUploadTemperature();
				}
			}
		});

	}


	@Override
	public void measureTemperatureSuccess(final float temp) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				measureCount++;
				currentStatus = Config.START_MEARSURE_TEMPEARURE;
//				middleTemperature.setText(temp + "°");
				healthData.setTemperature(temp);

				if ((temp > 37.2 || temp < 35.5) && measureCount < 2) {
					if (temp < 35.5) {
						mPresenter.playTextToSpeech("体温过低，请重新测温");
					} else if (temp > 37.2) {
						mPresenter.playTextToSpeech("体温过高，请重新测温");
						middleStatus.setImageResource(R.drawable.tempearture_high);
					}

					prompt.setText(getResources().getString(R.string.reset_measure));
//					middleTemperature.setTextColor(getResources().getColor(R.color.redColor));
					temperature.setTextColor(getResources().getColor(R.color.redColor));
				} else {
					if ((temp > 37.2 || temp < 35.5)) {
//						middleTemperature.setTextColor(getResources().getColor(R.color.redColor));
						temperature.setTextColor(getResources().getColor(R.color.redColor));
					} else {
//						middleTemperature.setTextColor(getResources().getColor(R.color.greenColor));
						temperature.setTextColor(getResources().getColor(R.color.greenColor));
					}
					//      根据码请求人员信息  获取网络请求
					startUploadTemperature();
				}
//				middleStatus.setVisibility(View.GONE);


			}
		});
	}


	public void startUploadTemperature() {
		if (NetworkUtil.isNetworkConnected(App.getContext())) { //有网络
			currentStatus=Config.UPLOAD_TEMPEARTURE;
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("health_code_str", healthData.getCode());
			hashMap.put("visit_time", DateUtils.getCurrentDate());
			hashMap.put("serial_number", GlobeUserData.getInstance().getSerial_number());
			hashMap.put("temperature", healthData.getTemperature());
			mPresenter.questUserData(hashMap);
		} else {  //无网络
			measureComplete();
			Toast.makeText(this, "网络异常，请重新扫描", Toast.LENGTH_SHORT).show();
		}
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
//		middleStatus.setVisibility(View.VISIBLE);
		middleStatus.setImageResource(R.drawable.health_code);
		temperature.setText("");
		name.setText("");
//		middleTemperature.setText("");
		prompt.setText(getResources().getString(R.string.please_scan));
		currentStatus = Config.START_SCAN_CODE;
	}


	@OnClick({R.id.traffic_record, R.id.reset_measure, R.id.prompt})
	public void onViewClicked(View view) {
		if (SystemClock.uptimeMillis() - clickTime <= Config.DURATION_DOUBLE_TIME) {
			return;
		}
		clickTime = SystemClock.uptimeMillis();
		switch (view.getId()) {
			case R.id.traffic_record://记录
				startActivity(new Intent(Main2Activity.this, TrafficRecordActivity.class));
				break;
			case R.id.reset_measure:
				mPresenter.startTemperatureDecode();
				break;
			case R.id.prompt:
				if (currentStatus == Config.START_MEARSURE_TEMPEARURE) {
					currentStatus=Config.UPLOAD_TEMPEARTUREING;
					mPresenter.startTemperatureDecode();
				} else if (currentStatus == Config.START_SCAN_CODE) {
					healthData = new HealthData();
					measureCount = 0;
					mPresenter.startScanCamDecode();
				} else if (currentStatus == Config.EXAMINE_COMPLETE) {
					measureComplete();
				} else if (currentStatus == Config.HEALTH_CODE_FAIL) {
					mPresenter.startScanCamDecode();
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
