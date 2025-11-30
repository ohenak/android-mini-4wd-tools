package com.mini4wd.lab.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GearRatioRefDao {
    @Query("SELECT * FROM gear_ratio_ref ORDER BY ratio_value ASC")
    fun getAllGearRatios(): Flow<List<GearRatioRefEntity>>

    @Query("SELECT * FROM gear_ratio_ref WHERE shaft_type = :shaftType ORDER BY ratio_value ASC")
    fun getGearRatiosByShaftType(shaftType: String): Flow<List<GearRatioRefEntity>>

    @Query("SELECT * FROM gear_ratio_ref WHERE id = :id")
    suspend fun getGearRatioById(id: Long): GearRatioRefEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gearRatios: List<GearRatioRefEntity>)

    @Query("SELECT COUNT(*) FROM gear_ratio_ref")
    suspend fun getCount(): Int
}
