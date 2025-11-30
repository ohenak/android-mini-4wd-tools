package com.mini4wd.lab.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity

@Database(
    entities = [
        ChassisRefEntity::class,
        MotorRefEntity::class,
        GearRatioRefEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class Mini4WDDatabase : RoomDatabase() {
    abstract fun chassisRefDao(): ChassisRefDao
    abstract fun motorRefDao(): MotorRefDao
    abstract fun gearRatioRefDao(): GearRatioRefDao
}
