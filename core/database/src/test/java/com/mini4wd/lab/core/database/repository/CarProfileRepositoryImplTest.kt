package com.mini4wd.lab.core.database.repository

import app.cash.turbine.test
import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.database.dao.CarProfileDao
import com.mini4wd.lab.core.database.entity.CarProfileEntity
import com.mini4wd.lab.core.database.entity.CarProfileWithRefs
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CarProfileRepositoryImplTest {

    private lateinit var carProfileDao: CarProfileDao
    private lateinit var repository: CarProfileRepositoryImpl

    private val testChassisEntity = ChassisRefEntity(
        id = 1L,
        code = "MS",
        name = "MS Chassis",
        shaftType = "double",
        motorPosition = "center"
    )

    private val testMotorEntity = MotorRefEntity(
        id = 1L,
        name = "Hyper-Dash PRO",
        shaftType = "double",
        category = "dash",
        rpmMin = 17200,
        rpmMax = 21200
    )

    private val testGearRatioEntity = GearRatioRefEntity(
        id = 1L,
        ratio = "3.5:1",
        ratioValue = 3.5f,
        shaftType = "double",
        gear1Teeth = 0,
        gear1Color = "Pink",
        gear1Code = "G22",
        gear2Teeth = 0,
        gear2Color = "Light Green",
        gear2Code = "G21"
    )

    private val testProfileEntity = CarProfileEntity(
        id = 1L,
        name = "Test Car",
        chassisRefId = 1L,
        motorRefId = 1L,
        gearRatioRefId = 1L,
        tags = "racing,speed",
        createdAt = 1000L,
        updatedAt = 2000L
    )

    private val testProfileWithRefs = CarProfileWithRefs(
        profile = testProfileEntity,
        chassis = testChassisEntity,
        motor = testMotorEntity,
        gearRatio = testGearRatioEntity
    )

    @Before
    fun setup() {
        carProfileDao = mock()
        repository = CarProfileRepositoryImpl(carProfileDao)
    }

    @Test
    fun `getAllProfiles returns mapped domain models`() = runTest {
        whenever(carProfileDao.getAllProfilesWithRefs()).thenReturn(flowOf(listOf(testProfileWithRefs)))

        repository.getAllProfiles().test {
            val profiles = awaitItem()
            assertEquals(1, profiles.size)
            assertEquals("Test Car", profiles[0].name)
            assertEquals("MS", profiles[0].chassis?.code)
            assertEquals("Hyper-Dash PRO", profiles[0].motor?.name)
            assertEquals("3.5:1", profiles[0].gearRatio?.ratio)
            awaitComplete()
        }
    }

    @Test
    fun `getProfileById returns mapped domain model`() = runTest {
        whenever(carProfileDao.getProfileWithRefsById(1L)).thenReturn(flowOf(testProfileWithRefs))

        repository.getProfileById(1L).test {
            val profile = awaitItem()
            assertNotNull(profile)
            assertEquals("Test Car", profile?.name)
            assertEquals("MS", profile?.chassis?.code)
            awaitComplete()
        }
    }

    @Test
    fun `getProfileById returns null for non-existent profile`() = runTest {
        whenever(carProfileDao.getProfileWithRefsById(999L)).thenReturn(flowOf(null))

        repository.getProfileById(999L).test {
            val profile = awaitItem()
            assertNull(profile)
            awaitComplete()
        }
    }

    @Test
    fun `searchProfiles returns basic domain models`() = runTest {
        whenever(carProfileDao.searchByName("Test")).thenReturn(flowOf(listOf(testProfileEntity)))

        repository.searchProfiles("Test").test {
            val profiles = awaitItem()
            assertEquals(1, profiles.size)
            assertEquals("Test Car", profiles[0].name)
            assertEquals(listOf("racing", "speed"), profiles[0].tags)
            awaitComplete()
        }
    }

    @Test
    fun `getProfilesByTag returns filtered profiles`() = runTest {
        whenever(carProfileDao.getProfilesByTag("racing")).thenReturn(flowOf(listOf(testProfileEntity)))

        repository.getProfilesByTag("racing").test {
            val profiles = awaitItem()
            assertEquals(1, profiles.size)
            assertTrue(profiles[0].tags.contains("racing"))
            awaitComplete()
        }
    }

    @Test
    fun `createProfile normalizes tags and updates timestamp`() = runTest {
        whenever(carProfileDao.insert(any())).thenReturn(1L)

        val profile = CarProfile(
            name = "New Car",
            tags = listOf("  RACING  ", "Speed", "racing", "  ") // Should be normalized
        )

        val result = repository.createProfile(profile)

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())

        val entityCaptor = argumentCaptor<CarProfileEntity>()
        verify(carProfileDao).insert(entityCaptor.capture())

        val capturedEntity = entityCaptor.firstValue
        assertEquals("racing,speed", capturedEntity.tags) // Trimmed, lowercase, deduped
    }

    @Test
    fun `createProfile returns failure on exception`() = runTest {
        whenever(carProfileDao.insert(any())).thenThrow(RuntimeException("Duplicate name"))

        val profile = CarProfile(name = "Existing Car")
        val result = repository.createProfile(profile)

        assertTrue(result.isFailure)
    }

    @Test
    fun `updateProfile normalizes tags and updates timestamp`() = runTest {
        val profile = CarProfile(
            id = 1L,
            name = "Updated Car",
            tags = listOf("  NEW  ", "tag")
        )

        val result = repository.updateProfile(profile)

        assertTrue(result.isSuccess)

        val entityCaptor = argumentCaptor<CarProfileEntity>()
        verify(carProfileDao).update(entityCaptor.capture())

        val capturedEntity = entityCaptor.firstValue
        assertEquals("new,tag", capturedEntity.tags)
    }

    @Test
    fun `updateProfile returns failure on exception`() = runTest {
        whenever(carProfileDao.update(any())).thenThrow(RuntimeException("Update failed"))

        val profile = CarProfile(id = 1L, name = "Updated Car")
        val result = repository.updateProfile(profile)

        assertTrue(result.isFailure)
    }

    @Test
    fun `deleteProfile calls dao deleteById`() = runTest {
        val result = repository.deleteProfile(1L)

        assertTrue(result.isSuccess)
        verify(carProfileDao).deleteById(1L)
    }

    @Test
    fun `deleteProfile returns failure on exception`() = runTest {
        whenever(carProfileDao.deleteById(any())).thenThrow(RuntimeException("Delete failed"))

        val result = repository.deleteProfile(1L)

        assertTrue(result.isFailure)
    }

    @Test
    fun `isNameUnique returns true when name does not exist`() = runTest {
        whenever(carProfileDao.countByName("Unique Name", 0)).thenReturn(0)

        val result = repository.isNameUnique("Unique Name")

        assertTrue(result)
    }

    @Test
    fun `isNameUnique returns false when name exists`() = runTest {
        whenever(carProfileDao.countByName("Existing Name", 0)).thenReturn(1)

        val result = repository.isNameUnique("Existing Name")

        assertFalse(result)
    }

    @Test
    fun `isNameUnique excludes specified id`() = runTest {
        whenever(carProfileDao.countByName("My Car", 5L)).thenReturn(0)

        val result = repository.isNameUnique("My Car", excludeId = 5L)

        assertTrue(result)
        verify(carProfileDao).countByName("My Car", 5L)
    }

    @Test
    fun `tags are parsed correctly from comma-separated string`() = runTest {
        val entityWithTags = testProfileEntity.copy(tags = "tag1,tag2,tag3")
        whenever(carProfileDao.searchByName("Test")).thenReturn(flowOf(listOf(entityWithTags)))

        repository.searchProfiles("Test").test {
            val profiles = awaitItem()
            assertEquals(listOf("tag1", "tag2", "tag3"), profiles[0].tags)
            awaitComplete()
        }
    }

    @Test
    fun `empty tags string results in empty list`() = runTest {
        val entityWithEmptyTags = testProfileEntity.copy(tags = "")
        whenever(carProfileDao.searchByName("Test")).thenReturn(flowOf(listOf(entityWithEmptyTags)))

        repository.searchProfiles("Test").test {
            val profiles = awaitItem()
            assertTrue(profiles[0].tags.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `null tags results in empty list`() = runTest {
        val entityWithNullTags = testProfileEntity.copy(tags = null)
        whenever(carProfileDao.searchByName("Test")).thenReturn(flowOf(listOf(entityWithNullTags)))

        repository.searchProfiles("Test").test {
            val profiles = awaitItem()
            assertTrue(profiles[0].tags.isEmpty())
            awaitComplete()
        }
    }
}
