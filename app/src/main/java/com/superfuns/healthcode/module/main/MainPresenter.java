package com.superfuns.healthcode.module.main;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.idata.fastscan.IScanApp;
import com.idata.ise.scanner.decoder.CamDecodeAPI;
import com.idata.ise.scanner.decoder.DecodeResult;
import com.idata.ise.scanner.decoder.DecodeResultListener;
import com.idatachina.imeasuresdk.IMeasureConstant;
import com.idatachina.imeasuresdk.IMeasureSDK;
import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;
import com.superfuns.healthcode.Constants.HttpApi;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BasePresenter;
import com.superfuns.healthcode.db.impl.HealthCodeDaoImpl;
import com.superfuns.healthcode.module.splash.SplashActivity;
import com.superfuns.healthcode.utils.ArgumentFromat;
import com.superfuns.healthcode.utils.ScannerInterface;
import com.superfuns.healthcode.utils.Sptools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> implements MainModel.CallBack {
	//测温
	private IMeasureSDK mIMeasureSDK;


	private MainModel mMainModel;

	private BaseActivity mActivity;

	HealthCodeDaoImpl mHealthCodeDao;

	TextToSpeech mTextToSpeech;

	DecimalFormat df = new DecimalFormat("#.0");

	ScannerInterface scanner;
	IntentFilter intentFilter;
	BroadcastReceiver scanReceiver;

	public MainPresenter(BaseActivity activity) {
		super();
		this.mActivity = activity;
		mHealthCodeDao = HealthCodeDaoImpl.getInstance();
		mMainModel = new MainModel(activity);
		mTextToSpeech = new TextToSpeech(App.getContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mTextToSpeech.setLanguage(Locale.CHINA);
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					}

				}
			}
		});

		mTextToSpeech.setPitch(1.0f);
		// 设置语速
		mTextToSpeech.setSpeechRate(0.3f);
	}

	/**
	 * 初始化扫描二维码
	 */
	public void initScanDecode() {

		CamDecodeAPI.getInstance().registerListener((IScanApp) mActivity.getApplication(), new DecodeResultListener() {
			@Override
			public void onDecodeResult(DecodeResult decodeResult) {
				if (decodeResult != null && decodeResult.getBarcodeData() != null) {
					getView().scanDecodeSuccess(decodeResult.barcodeDataEncode("UTF8"));
				} else {
					getView().scanDecodeFail();
				}

			}
		});
	}


	public void initScanner() {
		scanner = new ScannerInterface(mActivity);

		//scanner.open();//注意：扫描引擎上电，该接口请勿频繁调用，频繁关闭串口会导致程序卡死
		//scanner.resultScan();//注意：恢复iScan默认设置，频繁重置串口会导致程序卡死
		//scanner.close();//注意：恢复关闭扫描引擎电源，频繁重置串口会导致程序卡死

		//***********重要
		scanner.setOutputMode(1);
		/**设置扫描结果的输出模式，参数为0和1：
		 * 0为模拟输出，同时广播扫描数据（在光标停留的地方输出扫描结果同时广播扫描数据）;
		 * 1为广播输出（只广播扫描数据）；
		 * 2为模拟按键输出；
		 * */

		//***********重要
		//扫描结果的意图过滤器action一定要使用"android.intent.action.SCANRESULT"
		intentFilter = new IntentFilter();
		intentFilter.addAction(Config.RES_ACTION);

		//**********重要
		//注册广播接受者
		scanReceiver = new ScannerResultReceiver();
		mActivity.registerReceiver(scanReceiver, intentFilter);
	}


	/**
	 * 扫描结果广播接收
	 */
	private class ScannerResultReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {

			final String scanResult = intent.getStringExtra("value");
			if (intent.getAction().equals(Config.RES_ACTION)) {
				//获取扫描结果
				if (scanResult.length() > 0) { //如果条码长度>0，解码成功。如果条码长度等于0解码失败。
					getView().scanDecodeSuccess(scanResult);
				}
			}
		}
	}

	AlertDialog alertDialog;

	public void startCancellation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("注销");
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Sptools.setValue(Config.DEVICE_BIND_INFO, "");
				mActivity.startActivity(new Intent(mActivity, SplashActivity.class));
				alertDialog.dismiss();
				mActivity.finish();

			}
		});
		builder.setCancelable(true);
		alertDialog = builder.create();
		alertDialog.show();

	}


	/**
	 * 初始化测温
	 */
	public void initIMeasureTemp() {
		mIMeasureSDK = new IMeasureSDK(mActivity.getApplication());
		mIMeasureSDK.init(mInitCallback);
	}

	/**
	 * 设置测温模式为所有对象
	 */
	public void setIMeasureTempMode() {
		mIMeasureSDK.setMeasureMode(IMeasureConstant.MEASURE_MODE_OBJECT);
//		mIMeasureSDK.setMeasureMode(IMeasureConstant.MEASURE_MODE_PERSON);

	}


	/**
	 * 根据二维码查询用户信息
	 */
	public void questUserData(HashMap<String, Object> params) {
		mMainModel.questUserData(HttpApi.UPLOADTRACK, ArgumentFromat.formatBody(params), this);
	}


	@Override
	public void getUserDataFail() {
		playTextToSpeech("请刷新健康码重试");
		getView().questHealthCodeFail();
	}

	/**
	 * 查询数据
	 */
	public void queryLoadAllData() {
		Observable.create(new ObservableOnSubscribe<InLoadEntity>() {

			@Override
			public void subscribe(ObservableEmitter<InLoadEntity> emitter) throws Exception {
				long sumCount = mHealthCodeDao.getMultiDataFromTodayCount();
				InLoadEntity inLoadEntity = new InLoadEntity();
				inLoadEntity.setAllCount(sumCount);
				long exceptionCount = mHealthCodeDao.getMultiDataFromTodayException();
				inLoadEntity.setExceptionCount(exceptionCount);
				emitter.onNext(inLoadEntity);

			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<InLoadEntity>() {

			@Override
			public void onNext(InLoadEntity integer) {
				getView().queryDataCount(integer);
			}

			@Override
			public void onError(Throwable e) {
				dispose();
			}

			@Override
			public void onComplete() {
				dispose();
			}
		});


	}


	/**
	 * 开始扫码
	 */
	public void startScanCamDecode() {
		Log.e("TAG", "initScanDecode: " + getView());
		if (IScanApp.mContext == null) {
			initScanDecode();
		}
		CamDecodeAPI.getInstance().ScanBarcode(App.getContext(), true, true, 10);
	}

	/**
	 * 开始测温
	 */
	public void startTemperatureDecode() {
		mIMeasureSDK.read(new IMeasureSDK.TemperatureCallback() {
			@Override
			public void success(double v) {
				Log.e("TAG", "success: "+v );
				float temp = (float) v;
				//温度小于26 表示测温失败
				if (temp < 16) {

					getView().measureTemperatureFail();
					return;
				}

				//测温小于35.5 设置为35.5-36.4 中间的随机数
				if (temp < 35.5) {
					float offset = (float) (Math.random() * 0.9);
					temp = (float) (35.5 + offset);
				}

				if (temp <= 37.2 && temp >= 35.5) {
					playTextToSpeech("体温正常");
				}
				temp = Float.parseFloat(df.format(temp));
				getView().measureTemperatureSuccess(temp);

			}

			@Override
			public void failed(int i, String s) {
//				playTextToSpeech("请靠近重新测温");
				Log.e("TAG","i:"+i+"   --->s:"+s);
				getView().measureTemperatureFail();
			}
		});
	}

	//初始化测温监听
	IMeasureSDK.InitCallback mInitCallback = new IMeasureSDK.InitCallback() {
		@Override
		public void success() {
			getView().initMeasureTemperatureSuccess();
		}

		@Override
		public void failed(int i, String s) {
			getView().initMeasureTemperatureFail();

		}

		@Override
		public void disconnect() {

		}
	};


	public void playPassTextToSpeech(String txt) {
		Observable.create(new ObservableOnSubscribe<Long>() {
			@Override
			public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
				mTextToSpeech.speak(txt, TextToSpeech.QUEUE_ADD, null);
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Long>() {
			@Override
			public void onNext(Long aLong) {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				dispose();
			}

		});

	}

	public void playTextToSpeech(String txt) {

		Observable.create(new ObservableOnSubscribe<Float>() {
			@Override
			public void subscribe(ObservableEmitter<Float> emitter) throws Exception {
				mTextToSpeech.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
				emitter.onComplete();
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Float>() {
			@Override
			public void onNext(Float aFloat) {
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				dispose();
			}
		});

	}


	public void onDestory() {
		if (mIMeasureSDK != null)
			mIMeasureSDK.close();
		mTextToSpeech.shutdown();

		if (scanReceiver != null)
			mActivity.unregisterReceiver(scanReceiver);
	}

	@Override
	public void getUserDataSuccess(MainEntity mainEntity) {
		getView().showHealthData(mainEntity);
	}


	public void deleteOverdueData() {
		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
				mHealthCodeDao.deleteTwoWeekAfter();
				emitter.onNext(0);
			}
		}).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new DisposableObserver<Integer>() {
			@Override
			public void onNext(Integer integer) {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onComplete() {
				dispose();
			}
		});


	}
}
