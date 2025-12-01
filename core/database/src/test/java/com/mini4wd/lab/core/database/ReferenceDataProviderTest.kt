package com.mini4wd.lab.core.database

import com.mini4wd.lab.core.database.seed.ReferenceDataProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class ReferenceDataProviderTest {

    @Test
    fun `chassis data has correct count`() {
        val chassisData = ReferenceDataProvider.getChassisData()
        assertEquals(10, chassisData.size)
    }

    @Test
    fun `chassis data has correct shaft types`() {
        val chassisData = ReferenceDataProvider.getChassisData()
        val doubleShaft = chassisData.filter { it.shaftType == "double" }
        val singleShaft = chassisData.filter { it.shaftType == "single" }

        assertEquals(2, doubleShaft.size)  // MS, MA
        assertEquals(8, singleShaft.size)
    }

    @Test
    fun `motor data has correct count`() {
        val motorData = ReferenceDataProvider.getMotorData()
        assertEquals(15, motorData.size)
    }

    @Test
    fun `motor data has correct shaft distribution`() {
        val motorData = ReferenceDataProvider.getMotorData()
        val singleShaft = motorData.filter { it.shaftType == "single" }
        val doubleShaft = motorData.filter { it.shaftType == "double" }

        assertEquals(9, singleShaft.size)
        assertEquals(6, doubleShaft.size)
    }

    @Test
    fun `gear ratio data has correct count`() {
        val gearData = ReferenceDataProvider.getGearRatioData()
        assertEquals(8, gearData.size)
    }

    @Test
    fun `gear ratio data has correct shaft distribution`() {
        val gearData = ReferenceDataProvider.getGearRatioData()
        val singleShaft = gearData.filter { it.shaftType == "single" }
        val doubleShaft = gearData.filter { it.shaftType == "double" }

        assertEquals(5, singleShaft.size)
        assertEquals(3, doubleShaft.size)
    }
}
