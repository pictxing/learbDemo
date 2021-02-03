package com.superfuns.healthcode.module.record;

import com.superfuns.healthcode.db.table.HealthData;

import java.util.List;

public interface RecordView {

	void obtainHealthData(List<HealthData> healthDataResult);

	void obtainHealthDataCount(int count);

}
