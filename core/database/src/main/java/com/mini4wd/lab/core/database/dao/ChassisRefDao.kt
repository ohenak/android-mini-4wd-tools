package com.mini4wd.lab.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChassisRefDao {
    @Query("SELECT * FROM chassis_ref ORDER BY code ASC")
    fun getAllChassis(): Flow<List<ChassisRefEntity>>

    @Query("SELECT * FROM chassis_ref WHERE shaft_type = :shaftType ORDER BY code ASC")
    fun getChassisByShaftType(shaftType: String): Flow<List<ChassisRefEntity>>

    @Query("SELECT * FROM chassis_ref WHERE id = :id")
    suspend fun getChassisById(id: Long): ChassisRefEntity?

    @Query("SELECT * FROM chassis_ref WHERE code = :code")
    suspend fun getChassisByCode(code: String): ChassisRefEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chassis: List<ChassisRefEntity>)

    @Query("SELECT COUNT(*) FROM chassis_ref")
    suspend fun getCount(): Int
}
