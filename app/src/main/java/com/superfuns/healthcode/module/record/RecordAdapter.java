package com.superfuns.healthcode.module.record;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.superfuns.healthcode.R;
import com.superfuns.healthcode.db.table.HealthData;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<HealthData, BaseViewHolder> {

	public RecordAdapter(int layoutResId, @Nullable List<HealthData> data) {
		super(layoutResId, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, HealthData item) {
		helper.setText(R.id.item_name, item.getName()).setText(R.id.item_time, item.getDate()).setText(R.id.item_status, item.getStatus().equals("0") ? "绿码" : item.getStatus().equals("1") ? "黄码" : "红码");
	}

}
