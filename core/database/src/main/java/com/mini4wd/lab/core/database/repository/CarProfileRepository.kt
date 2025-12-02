package com.mini4wd.lab.core.database.repository

import com.mini4wd.lab.core.common.model.CarProfile
import kotlinx.coroutines.flow.Flow

interface CarProfileRepository {
    fun getAllProfiles(): Flow<List<CarProfile>>
    fun getProfileById(id: Long): Flow<CarProfile?>
    fun searchProfiles(query: String): Flow<List<CarProfile>>
    fun getProfilesByTag(tag: String): Flow<List<CarProfile>>

    suspend fun createProfile(profile: CarProfile): Result<Long>
    suspend fun updateProfile(profile: CarProfile): Result<Unit>
    suspend fun deleteProfile(id: Long): Result<Unit>
    suspend fun isNameUnique(name: String, excludeId: Long = 0): Boolean
}
