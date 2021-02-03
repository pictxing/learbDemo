package com.superfuns.healthcode.module.main;

public interface MainView {


	void  scanDecodeFail();

	void  scanDecodeSuccess(String result);

	void  measureTemperatureFail();

	void measureTemperatureSuccess(float  temp);


	void questHealthCodeFail();


	void initMeasureTemperatureSuccess();
	void initMeasureTemperatureFail();

	void showHealthData(MainEntity mainEntity);


	void  queryDataCount(InLoadEntity inLoadEntity);
}
