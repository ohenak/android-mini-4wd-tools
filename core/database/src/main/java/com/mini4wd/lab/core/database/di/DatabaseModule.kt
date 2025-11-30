package com.mini4wd.lab.core.database.di

import android.content.Context
import androidx.room.Room
import com.mini4wd.lab.core.database.Mini4WDDatabase
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Mini4WDDatabase {
        return Room.databaseBuilder(
            context,
            Mini4WDDatabase::class.java,
            "mini4wd_lab.db"
        ).build()
    }

    @Provides
    fun provideChassisRefDao(database: Mini4WDDatabase): ChassisRefDao {
        return database.chassisRefDao()
    }

    @Provides
    fun provideMotorRefDao(database: Mini4WDDatabase): MotorRefDao {
        return database.motorRefDao()
    }

    @Provides
    fun provideGearRatioRefDao(database: Mini4WDDatabase): GearRatioRefDao {
        return database.gearRatioRefDao()
    }
}
