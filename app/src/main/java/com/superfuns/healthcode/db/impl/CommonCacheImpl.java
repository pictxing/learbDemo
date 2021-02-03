package com.superfuns.healthcode.db.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.superfuns.healthcode.App;
import com.superfuns.healthcode.db.UpDBHelper;
import com.superfuns.healthcode.db.base.IDataBaseOperate;
import com.superfuns.healthcode.db.gen.DaoMaster;
import com.superfuns.healthcode.db.gen.DaoSession;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

import java.util.List;

public class CommonCacheImpl<T> implements IDataBaseOperate<T> {
	private static final String DB_NAME = "health_db";
	private static final UpDBHelper sUpDBHelper = new UpDBHelper(App.getContext(), DB_NAME);
	private StringBuffer sqlBuffer = new StringBuffer();

	public static UpDBHelper getUpDBHelper() {
		return sUpDBHelper;
	}

	public CommonCacheImpl() {
	}

	/**
	 * 获取可读数据库
	 */
	protected SQLiteDatabase getReadableDatabase() {
		return sUpDBHelper.getReadableDatabase();

	}

	/**
	 * 获取可写数据库
	 */
	protected SQLiteDatabase getWritableDatabase() {
		return sUpDBHelper.getWritableDatabase();
	}

	/**
	 * 获取可写数据库的DaoMaster
	 */
	protected DaoMaster getWDaoMaster() {
		return new DaoMaster(getWritableDatabase());
	}

	/**
	 * 获取可写数据库的DaoSession
	 */
	protected DaoSession getWDaoSession() {
		return getWDaoMaster().newSession(IdentityScopeType.None);
	}

	/**
	 * 获取可写数据库的DaoMaster
	 */
	protected DaoMaster getRDaoMaster() {
		return new DaoMaster(getWritableDatabase());
	}

	/**
	 * 获取可写数据库的DaoSession
	 */
	public DaoSession getRDaoSession() {
		return getRDaoMaster().newSession();
	}

	/**
	 * 执行特定的sql语句
	 * 如单独更新
	 *
	 * @param sql
	 */
	public void execSQL(String sql) {

		SQLiteDatabase database = getWritableDatabase();

		try {
			database.beginTransaction();

			database.execSQL(sql);

			database.setTransactionSuccessful();

		} finally {
			database.endTransaction();
		}
	}

	@Override
	public long saveSingleData(T singleData) {
		return 0;
	}

	@Override
	public void saveMultiData(List<T> multiData) {

	}

	@Override
	public boolean isInvalide() {
		return false;
	}

	@Override
	public T getSingleDataFromCache(Long primaryKey) {
		return null;
	}

	@Override
	public List<T> getMultiDataFromCache() {
		return null;
	}

	@Override
	public void clearMemoryTable() {

	}

	@Override
	public void deleteSingleCache(Long primaryKey) {

	}

	@Override
	public void deleteSingleCache(T dta) {

	}

	@Override
	public void updateSingleData(T newData) {

	}

	@Override
	public long insertOrReplace(T newData) {
		return 0;
	}

	public long query(String sql, String[] values) {
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(sql, values);
		if (cursor != null) {
			long id = -1;
			if (cursor.moveToNext()) {
				id = cursor.getLong(0);
			}
			cursor.close();

			return id;
		}

		return -1;
	}
}
