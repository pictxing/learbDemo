package com.superfuns.healthcode.db.impl;


import android.util.Log;

import com.superfuns.healthcode.db.gen.DaoSession;
import com.superfuns.healthcode.db.gen.HealthDataDao;
import com.superfuns.healthcode.db.table.HealthData;
import com.superfuns.healthcode.utils.DateUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class HealthCodeDaoImpl extends CommonCacheImpl<HealthData> {

	private static HealthCodeDaoImpl sHealthDataDao;

	public static HealthCodeDaoImpl getInstance() {
		if (sHealthDataDao == null) {
			synchronized (HealthCodeDaoImpl.class) {
				if (sHealthDataDao == null) {
					sHealthDataDao = new HealthCodeDaoImpl();
				}
			}
		}
		return sHealthDataDao;
	}

	private HealthCodeDaoImpl() {
		super();
	}


	@Override
	public long saveSingleData(HealthData singleData) {
		HealthDataDao configDataDao = getWDaoSession().getHealthDataDao();
		return configDataDao.insertOrReplace(singleData);
	}

	@Override
	public List<HealthData> getMultiDataFromCache() {
		HealthDataDao healthDataDao = getRDaoSession().getHealthDataDao();
		return healthDataDao.loadAll();
	}

	//查询指定条数
	public List<HealthData> getMultiDataFromCache(int count, int offset) {
		offset *= count;
		QueryBuilder<HealthData> queryBuilder = getRDaoSession().queryBuilder(HealthData.class);
		List<HealthData> healthData=queryBuilder.where(HealthDataDao.Properties.Status.isNotNull()).offset(offset).limit(count).orderDesc(HealthDataDao.Properties.Date).list();//
		return healthData;
	}

	//查询今天通行的总数
	public long getMultiDataFromTodayCount() {
		DaoSession daoSession = getRDaoSession();
		QueryBuilder<HealthData> queryBuilder = daoSession.queryBuilder(HealthData.class);
		long count = queryBuilder.where(HealthDataDao.Properties.DateValue.gt(DateUtils.getTodayDate())).count();
		return count;
	}


	//查询今天的通行的红黄码数量
	public long getMultiDataFromTodayException() {
		DaoSession daoSession = getRDaoSession();
		QueryBuilder<HealthData> queryBuilder = daoSession.queryBuilder(HealthData.class);
		long count = queryBuilder.where(HealthDataDao.Properties.DateValue.gt(DateUtils.getTodayDate()), HealthDataDao.Properties.Status.notEq("0")).count();
		return count;
	}


	public List<HealthData> getMultiDataFromKey() {
		DaoSession daoSession = getRDaoSession();
		QueryBuilder<HealthData> queryBuilder = daoSession.queryBuilder(HealthData.class);
		List<HealthData> healthData = queryBuilder.where(HealthDataDao.Properties.Status.notEq("0")).list();
		return healthData;
	}

	//删除两周之前的数据
	public void deleteTwoWeekAfter() {
		DaoSession daoSession = getRDaoSession();
		QueryBuilder<HealthData> queryBuilder = daoSession.queryBuilder(HealthData.class);
		queryBuilder.where(HealthDataDao.Properties.DateValue.lt(DateUtils.getTwoWeekDate())).buildDelete().executeDeleteWithoutDetachingEntities();
//		queryBuilder.where(HealthDataDao.Properties.DateValue.lt(DateUtils.getTodayDate())).buildDelete().executeDeleteWithoutDetachingEntities();
	}
}
