package com.mini4wd.lab.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mini4wd.lab.core.database.entity.CarProfileEntity
import com.mini4wd.lab.core.database.entity.CarProfileWithRefs
import kotlinx.coroutines.flow.Flow

@Dao
interface CarProfileDao {

    // Create
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(profile: CarProfileEntity): Long

    // Read - All profiles
    @Query("SELECT * FROM car_profile ORDER BY updated_at DESC")
    fun getAllProfiles(): Flow<List<CarProfileEntity>>

    // Read - Single profile by ID
    @Query("SELECT * FROM car_profile WHERE id = :id")
    fun getProfileById(id: Long): Flow<CarProfileEntity?>

    @Query("SELECT * FROM car_profile WHERE id = :id")
    suspend fun getProfileByIdOnce(id: Long): CarProfileEntity?

    // Read - Search by name
    @Query("SELECT * FROM car_profile WHERE name LIKE '%' || :query || '%' ORDER BY updated_at DESC")
    fun searchByName(query: String): Flow<List<CarProfileEntity>>

    // Read - Filter by chassis type
    @Query("SELECT * FROM car_profile WHERE chassis_ref_id = :chassisRefId ORDER BY updated_at DESC")
    fun getProfilesByChassisType(chassisRefId: Long): Flow<List<CarProfileEntity>>

    // Read - Filter by motor type
    @Query("SELECT * FROM car_profile WHERE motor_ref_id = :motorRefId ORDER BY updated_at DESC")
    fun getProfilesByMotorType(motorRefId: Long): Flow<List<CarProfileEntity>>

    // Read - Filter by tag (partial match)
    @Query("SELECT * FROM car_profile WHERE tags LIKE '%' || :tag || '%' ORDER BY updated_at DESC")
    fun getProfilesByTag(tag: String): Flow<List<CarProfileEntity>>

    // Read - With relations
    @Transaction
    @Query("SELECT * FROM car_profile WHERE id = :id")
    fun getProfileWithRefsById(id: Long): Flow<CarProfileWithRefs?>

    @Transaction
    @Query("SELECT * FROM car_profile ORDER BY updated_at DESC")
    fun getAllProfilesWithRefs(): Flow<List<CarProfileWithRefs>>

    // Update
    @Update
    suspend fun update(profile: CarProfileEntity)

    // Update timestamp only
    @Query("UPDATE car_profile SET updated_at = :timestamp WHERE id = :id")
    suspend fun updateTimestamp(id: Long, timestamp: Long = System.currentTimeMillis())

    // Delete
    @Delete
    suspend fun delete(profile: CarProfileEntity)

    @Query("DELETE FROM car_profile WHERE id = :id")
    suspend fun deleteById(id: Long)

    // Count
    @Query("SELECT COUNT(*) FROM car_profile")
    suspend fun getCount(): Int

    // Check if name exists (for validation)
    @Query("SELECT COUNT(*) FROM car_profile WHERE name = :name AND id != :excludeId")
    suspend fun countByName(name: String, excludeId: Long = 0): Int
}
