package com.superfuns.healthcode.db.table;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.superfuns.healthcode.db.gen.DaoSession;
import com.superfuns.healthcode.db.gen.HealthDataDao;

@Entity(active = true, nameInDb = "HealthData_v1")
public class HealthData {
	@Id(autoincrement = true)
	private Long id;

	//姓名
	private String name;
	//身份证
	private String idCard;
	//日期
	private String date;
	//健康码
	private String code;
	//温度
	private float  temperature;
	//状态
	private String  status;

	private long dateValue;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 2129185179)
	private transient HealthDataDao myDao;

	@Generated(hash = 663150155)
	public HealthData(Long id, String name, String idCard, String date, String code,
			float temperature, String status, long dateValue) {
		this.id = id;
		this.name = name;
		this.idCard = idCard;
		this.date = date;
		this.code = code;
		this.temperature = temperature;
		this.status = status;
		this.dateValue = dateValue;
	}

	@Generated(hash = 1743795997)
	public HealthData() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getDateValue() {
		return dateValue;
	}

	public void setDateValue(long dateValue) {
		this.dateValue = dateValue;
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1806215286)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getHealthDataDao() : null;
	}
}
