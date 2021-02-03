package com.superfuns.healthcode.db;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.superfuns.healthcode.db.gen.DaoMaster;
import com.superfuns.healthcode.db.gen.HealthDataDao;

import org.greenrobot.greendao.database.Database;
public class UpDBHelper extends DaoMaster.OpenHelper {

    public UpDBHelper(Context context, String name) {
        super(context, name, null);
    }

    // 注意选择GreenDao参数的onUpgrade方法
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {


        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, HealthDataDao.class);
    }
}
