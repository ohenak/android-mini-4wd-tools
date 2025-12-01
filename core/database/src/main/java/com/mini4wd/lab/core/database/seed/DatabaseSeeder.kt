package com.mini4wd.lab.core.database.seed

import androidx.room.withTransaction
import com.mini4wd.lab.core.database.Mini4WDDatabase
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val database: Mini4WDDatabase,
    private val chassisRefDao: ChassisRefDao,
    private val motorRefDao: MotorRefDao,
    private val gearRatioRefDao: GearRatioRefDao
) {
    suspend fun seedIfEmpty() {
        database.withTransaction {
            if (chassisRefDao.getCount() == 0) {
                chassisRefDao.insertAll(ReferenceDataProvider.getChassisData())
            }

            if (motorRefDao.getCount() == 0) {
                motorRefDao.insertAll(ReferenceDataProvider.getMotorData())
            }

            if (gearRatioRefDao.getCount() == 0) {
                gearRatioRefDao.insertAll(ReferenceDataProvider.getGearRatioData())
            }
        }
    }
}
