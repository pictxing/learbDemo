package com.superfuns.healthcode.db.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.superfuns.healthcode.db.table.HealthData;

import com.superfuns.healthcode.db.gen.HealthDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig healthDataDaoConfig;

    private final HealthDataDao healthDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        healthDataDaoConfig = daoConfigMap.get(HealthDataDao.class).clone();
        healthDataDaoConfig.initIdentityScope(type);

        healthDataDao = new HealthDataDao(healthDataDaoConfig, this);

        registerDao(HealthData.class, healthDataDao);
    }
    
    public void clear() {
        healthDataDaoConfig.clearIdentityScope();
    }

    public HealthDataDao getHealthDataDao() {
        return healthDataDao;
    }

}
