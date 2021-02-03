package com.superfuns.healthcode.module.record;

import android.util.Log;

import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BaseModule;
import com.superfuns.healthcode.db.impl.HealthCodeDaoImpl;
import com.superfuns.healthcode.db.table.HealthData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RecordModel extends BaseModule {
	HealthCodeDaoImpl mHealthCodeDao;

	public RecordModel(BaseActivity act) {
		super(act);
		mHealthCodeDao = HealthCodeDaoImpl.getInstance();
	}

	public void queryHealthData(CallBack callBack, int count, int offset) {

		Observable.create(new ObservableOnSubscribe<List<HealthData>>() {
			@Override
			public void subscribe(ObservableEmitter<List<HealthData>> emitter) throws Exception {
				List<HealthData> healthData = mHealthCodeDao.getMultiDataFromCache(count, offset);
				Log.i("TAG","healthData:"+healthData.size());
				emitter.onNext(healthData);
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<List<HealthData>>() {
			@Override
			public void onNext(List<HealthData> healthData) {
				callBack.setHealthDataResult(healthData);
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


	public void queryHealthDataCount(CallBack callBack) {

		Observable.create(new ObservableOnSubscribe<List<HealthData>>() {
			@Override
			public void subscribe(ObservableEmitter<List<HealthData>> emitter) throws Exception {
				List<HealthData> healthData = mHealthCodeDao.getMultiDataFromCache();
				emitter.onNext(healthData);
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<List<HealthData>>() {
			@Override
			public void onNext(List<HealthData> healthData) {
				callBack.setHealthCount(healthData.size());
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


	interface CallBack {
		void setHealthDataResult(List<HealthData> healthDataResult);
		void  setHealthCount(int count);
	}
}
