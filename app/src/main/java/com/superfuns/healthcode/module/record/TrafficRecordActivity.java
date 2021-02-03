package com.superfuns.healthcode.module.record;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.superfuns.healthcode.R;
import com.superfuns.healthcode.base.BaseActivity;
import com.superfuns.healthcode.db.table.HealthData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TrafficRecordActivity extends BaseActivity<RecordView, RecordPresenter> implements RecordView {


	@BindView(R.id.record_count)
	TextView recordCount;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.traffic_record)
	RecyclerView trafficRecord;

	private final int count = 10;

	int offset = 0;

	RecordAdapter mRecordAdapter;


	List<HealthData> healthData = new ArrayList<>();

	@Override
	protected RecordPresenter createPresenter() {
		return new RecordPresenter(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_traffic_record;
	}

	@Override
	public void initData() {
		setSupportActionBar(toolbar);
		mPresenter.queryHealthCount();
		mRecordAdapter = new RecordAdapter(R.layout.item_record, healthData);
		trafficRecord.setLayoutManager(new LinearLayoutManager(this));
		trafficRecord.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
		trafficRecord.setAdapter(mRecordAdapter);

		initEvent();

		View view = LayoutInflater.from(this).inflate(R.layout.empth_layout, null);
		mRecordAdapter.setEmptyView(view);
		mPresenter.queryHealthData(count, offset);
		mRecordAdapter.disableLoadMoreIfNotFullPage();
	}

	private void initEvent() {
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		mRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
			@Override
			public void onLoadMoreRequested() {
				offset++;
				mPresenter.queryHealthData(count, offset);
			}
		}, trafficRecord);
	}

	@Override
	public void obtainHealthDataCount(int count) {
		recordCount.setText("扫码数：" + count);
	}

	@Override
	public void obtainHealthData(List<HealthData> healthDataResult) {
		mRecordAdapter.loadMoreComplete();
		if (healthDataResult.size() > 0) {
			healthData.addAll(healthDataResult);
			mRecordAdapter.notifyDataSetChanged();
		} else {
			mRecordAdapter.loadMoreEnd();
			mRecordAdapter.setEnableLoadMore(false);
		}
	}
}
