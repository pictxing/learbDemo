package com.superfuns.healthcode.module.record;

import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.base.BasePresenter;
import com.superfuns.healthcode.db.table.HealthData;

import java.util.List;

public class RecordPresenter extends BasePresenter<RecordView> implements RecordModel.CallBack {

	RecordModel mRecordModel;


	public RecordPresenter(BaseActivity activity) {
		mRecordModel = new RecordModel(activity);

	}

	public void queryHealthCount() {
		mRecordModel.queryHealthDataCount(this);
	}

	public void queryHealthData(int count, int offset) {
		mRecordModel.queryHealthData(this, count, offset);

	}

	@Override
	public void setHealthDataResult(List<HealthData> healthDataResult) {
		if (healthDataResult != null)
			getView().obtainHealthData(healthDataResult);

	}

	@Override
	public void setHealthCount(int count) {
		getView().obtainHealthDataCount(count);
	}
}
