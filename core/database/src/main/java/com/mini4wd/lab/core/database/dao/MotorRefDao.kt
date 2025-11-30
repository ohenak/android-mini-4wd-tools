package com.mini4wd.lab.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mini4wd.lab.core.database.entity.MotorRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MotorRefDao {
    @Query("SELECT * FROM motor_ref ORDER BY rpm_min ASC")
    fun getAllMotors(): Flow<List<MotorRefEntity>>

    @Query("SELECT * FROM motor_ref WHERE shaft_type = :shaftType ORDER BY rpm_min ASC")
    fun getMotorsByShaftType(shaftType: String): Flow<List<MotorRefEntity>>

    @Query("SELECT * FROM motor_ref WHERE category = :category ORDER BY rpm_min ASC")
    fun getMotorsByCategory(category: String): Flow<List<MotorRefEntity>>

    @Query("SELECT * FROM motor_ref WHERE id = :id")
    suspend fun getMotorById(id: Long): MotorRefEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(motors: List<MotorRefEntity>)

    @Query("SELECT COUNT(*) FROM motor_ref")
    suspend fun getCount(): Int
}
