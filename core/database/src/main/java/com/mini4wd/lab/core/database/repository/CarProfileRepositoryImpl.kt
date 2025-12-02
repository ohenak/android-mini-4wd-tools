package com.mini4wd.lab.core.database.repository

import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.database.dao.CarProfileDao
import com.mini4wd.lab.core.database.mapper.toDomain
import com.mini4wd.lab.core.database.mapper.toDomainBasic
import com.mini4wd.lab.core.database.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarProfileRepositoryImpl @Inject constructor(
    private val carProfileDao: CarProfileDao
) : CarProfileRepository {

    override fun getAllProfiles(): Flow<List<CarProfile>> {
        return carProfileDao.getAllProfilesWithRefs().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProfileById(id: Long): Flow<CarProfile?> {
        return carProfileDao.getProfileWithRefsById(id).map { it?.toDomain() }
    }

    override fun searchProfiles(query: String): Flow<List<CarProfile>> {
        return carProfileDao.searchByName(query).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override fun getProfilesByTag(tag: String): Flow<List<CarProfile>> {
        return carProfileDao.getProfilesByTag(tag).map { list ->
            list.map { it.toDomainBasic() }
        }
    }

    override suspend fun createProfile(profile: CarProfile): Result<Long> {
        return try {
            val normalizedProfile = profile.normalizeTagsAndTimestamp()
            val entity = normalizedProfile.toEntity()
            val id = carProfileDao.insert(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: CarProfile): Result<Unit> {
        return try {
            val normalizedProfile = profile.normalizeTagsAndTimestamp()
            val entity = normalizedProfile.toEntity()
            carProfileDao.update(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProfile(id: Long): Result<Unit> {
        return try {
            carProfileDao.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isNameUnique(name: String, excludeId: Long): Boolean {
        return carProfileDao.countByName(name, excludeId) == 0
    }

    private fun CarProfile.normalizeTagsAndTimestamp(): CarProfile {
        val normalizedTags = tags
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }
            .distinct()
        val now = System.currentTimeMillis()
        return copy(tags = normalizedTags, updatedAt = now)
    }
}
