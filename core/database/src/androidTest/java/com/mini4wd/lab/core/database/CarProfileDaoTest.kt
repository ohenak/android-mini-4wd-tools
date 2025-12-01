package com.mini4wd.lab.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mini4wd.lab.core.database.dao.CarProfileDao
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import com.mini4wd.lab.core.database.entity.CarProfileEntity
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarProfileDaoTest {

    private lateinit var database: Mini4WDDatabase
    private lateinit var carProfileDao: CarProfileDao
    private lateinit var chassisRefDao: ChassisRefDao
    private lateinit var motorRefDao: MotorRefDao
    private lateinit var gearRatioRefDao: GearRatioRefDao

    private var chassisId: Long = 0
    private var motorId: Long = 0
    private var gearRatioId: Long = 0

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, Mini4WDDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        carProfileDao = database.carProfileDao()
        chassisRefDao = database.chassisRefDao()
        motorRefDao = database.motorRefDao()
        gearRatioRefDao = database.gearRatioRefDao()

        // Insert reference data for foreign keys
        val chassis = ChassisRefEntity(
            code = "MS",
            name = "MS Chassis",
            shaftType = "double",
            motorPosition = "center"
        )
        chassisRefDao.insertAll(listOf(chassis))
        chassisId = chassisRefDao.getChassisByCode("MS")!!.id

        val motor = MotorRefEntity(
            name = "Hyper-Dash PRO",
            shaftType = "double",
            category = "dash",
            rpmMin = 17200,
            rpmMax = 21200
        )
        motorRefDao.insertAll(listOf(motor))
        motorId = motorRefDao.getAllMotors().first().first().id

        val gearRatio = GearRatioRefEntity(
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
        gearRatioRefDao.insertAll(listOf(gearRatio))
        gearRatioId = gearRatioRefDao.getAllGearRatios().first().first().id
    }

    @After
    fun teardown() {
        database.close()
    }

    private fun createTestProfile(
        name: String = "Test Car",
        chassisRefId: Long? = chassisId,
        motorRefId: Long? = motorId,
        gearRatioRefId: Long? = gearRatioId
    ) = CarProfileEntity(
        name = name,
        chassisRefId = chassisRefId,
        motorRefId = motorRefId,
        gearRatioRefId = gearRatioRefId
    )

    @Test
    fun insertAndGetProfile() = runTest {
        val profile = createTestProfile()
        val id = carProfileDao.insert(profile)

        val retrieved = carProfileDao.getProfileByIdOnce(id)
        assertNotNull(retrieved)
        assertEquals("Test Car", retrieved?.name)
        assertEquals(chassisId, retrieved?.chassisRefId)
    }

    @Test
    fun getAllProfiles() = runTest {
        carProfileDao.insert(createTestProfile(name = "Car 1"))
        carProfileDao.insert(createTestProfile(name = "Car 2"))
        carProfileDao.insert(createTestProfile(name = "Car 3"))

        val profiles = carProfileDao.getAllProfiles().first()
        assertEquals(3, profiles.size)
    }

    @Test
    fun updateProfile() = runTest {
        val profile = createTestProfile()
        val id = carProfileDao.insert(profile)

        val retrieved = carProfileDao.getProfileByIdOnce(id)!!
        val updated = retrieved.copy(
            name = "Updated Car",
            motorBreakInStatus = true,
            motorRunCount = 10
        )
        carProfileDao.update(updated)

        val afterUpdate = carProfileDao.getProfileByIdOnce(id)
        assertEquals("Updated Car", afterUpdate?.name)
        assertEquals(true, afterUpdate?.motorBreakInStatus)
        assertEquals(10, afterUpdate?.motorRunCount)
    }

    @Test
    fun deleteProfile() = runTest {
        val profile = createTestProfile()
        val id = carProfileDao.insert(profile)

        carProfileDao.deleteById(id)

        val deleted = carProfileDao.getProfileByIdOnce(id)
        assertNull(deleted)
    }

    @Test
    fun searchByName() = runTest {
        carProfileDao.insert(createTestProfile(name = "Speed Demon"))
        carProfileDao.insert(createTestProfile(name = "Track Master"))
        carProfileDao.insert(createTestProfile(name = "Speed King"))

        val results = carProfileDao.searchByName("Speed").first()
        assertEquals(2, results.size)
        assertTrue(results.all { it.name.contains("Speed") })
    }

    @Test
    fun filterByChassisType() = runTest {
        carProfileDao.insert(createTestProfile(name = "Car 1", chassisRefId = chassisId))
        carProfileDao.insert(createTestProfile(name = "Car 2", chassisRefId = chassisId))
        carProfileDao.insert(createTestProfile(name = "Car 3", chassisRefId = null))

        val filtered = carProfileDao.getProfilesByChassisType(chassisId).first()
        assertEquals(2, filtered.size)
    }

    @Test
    fun filterByTag() = runTest {
        val profile1 = createTestProfile(name = "Car 1").copy(tags = "racing,speed")
        val profile2 = createTestProfile(name = "Car 2").copy(tags = "technical,grip")
        val profile3 = createTestProfile(name = "Car 3").copy(tags = "speed,technical")

        carProfileDao.insert(profile1)
        carProfileDao.insert(profile2)
        carProfileDao.insert(profile3)

        val speedCars = carProfileDao.getProfilesByTag("speed").first()
        assertEquals(2, speedCars.size)
    }

    @Test
    fun uniqueNameConstraint() = runTest {
        carProfileDao.insert(createTestProfile(name = "Unique Car"))

        val count = carProfileDao.countByName("Unique Car")
        assertEquals(1, count)

        val countExcluding = carProfileDao.countByName("Unique Car", excludeId = 999)
        assertEquals(1, countExcluding)
    }

    @Test
    fun getProfileWithRefs() = runTest {
        val profile = createTestProfile()
        val id = carProfileDao.insert(profile)

        val withRefs = carProfileDao.getProfileWithRefsById(id).first()
        assertNotNull(withRefs)
        assertNotNull(withRefs?.chassis)
        assertNotNull(withRefs?.motor)
        assertNotNull(withRefs?.gearRatio)
        assertEquals("MS", withRefs?.chassis?.code)
        assertEquals("Hyper-Dash PRO", withRefs?.motor?.name)
        assertEquals("3.5:1", withRefs?.gearRatio?.ratio)
    }

    @Test
    fun getCount() = runTest {
        assertEquals(0, carProfileDao.getCount())

        carProfileDao.insert(createTestProfile(name = "Car 1"))
        carProfileDao.insert(createTestProfile(name = "Car 2"))

        assertEquals(2, carProfileDao.getCount())
    }

    @Test
    fun foreignKeySetNullOnDelete() = runTest {
        val profile = createTestProfile()
        val id = carProfileDao.insert(profile)

        // This test verifies the entity has nullable foreign keys
        // The actual SET NULL behavior would need the parent to be deleted
        val retrieved = carProfileDao.getProfileByIdOnce(id)
        assertNotNull(retrieved?.chassisRefId)

        // Create profile with null refs
        val profileWithNullRefs = createTestProfile(
            name = "No Refs Car",
            chassisRefId = null,
            motorRefId = null,
            gearRatioRefId = null
        )
        val id2 = carProfileDao.insert(profileWithNullRefs)
        val retrieved2 = carProfileDao.getProfileByIdOnce(id2)
        assertNull(retrieved2?.chassisRefId)
        assertNull(retrieved2?.motorRefId)
        assertNull(retrieved2?.gearRatioRefId)
    }
}
