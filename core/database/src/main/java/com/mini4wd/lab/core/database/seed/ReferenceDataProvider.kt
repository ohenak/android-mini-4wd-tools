package com.mini4wd.lab.core.database.seed

import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity

object ReferenceDataProvider {

    fun getChassisData(): List<ChassisRefEntity> = listOf(
        // Double Shaft (MS, MA)
        ChassisRefEntity(code = "MS", name = "MS Chassis", shaftType = "double", motorPosition = "center"),
        ChassisRefEntity(code = "MA", name = "MA Chassis", shaftType = "double", motorPosition = "center"),

        // Single Shaft - Rear Motor
        ChassisRefEntity(code = "AR", name = "AR Chassis", shaftType = "single", motorPosition = "rear"),
        ChassisRefEntity(code = "VZ", name = "VZ Chassis", shaftType = "single", motorPosition = "rear"),
        ChassisRefEntity(code = "VS", name = "VS Chassis", shaftType = "single", motorPosition = "rear"),
        ChassisRefEntity(code = "Super-II", name = "Super-II Chassis", shaftType = "single", motorPosition = "rear"),
        ChassisRefEntity(code = "Super-TZ", name = "Super-TZ Chassis", shaftType = "single", motorPosition = "rear"),
        ChassisRefEntity(code = "Super-XX", name = "Super-XX Chassis", shaftType = "single", motorPosition = "rear"),

        // Single Shaft - Front Motor
        ChassisRefEntity(code = "FM-A", name = "FM-A Chassis", shaftType = "single", motorPosition = "front"),
        ChassisRefEntity(code = "Super FM", name = "Super FM Chassis", shaftType = "single", motorPosition = "front")
    )

    fun getMotorData(): List<MotorRefEntity> = listOf(
        // Single Shaft Motors
        MotorRefEntity(name = "Normal Motor", shaftType = "single", category = "stock", rpmMin = 12000, rpmMax = 13000),
        MotorRefEntity(name = "Torque-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 12800, rpmMax = 14700),
        MotorRefEntity(name = "Atomic-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 12700, rpmMax = 14900),
        MotorRefEntity(name = "Rev-Tuned 2", shaftType = "single", category = "tuned", rpmMin = 13400, rpmMax = 15200),
        MotorRefEntity(name = "Light-Dash", shaftType = "single", category = "dash", rpmMin = 14600, rpmMax = 17800),
        MotorRefEntity(name = "Hyper-Dash 3", shaftType = "single", category = "dash", rpmMin = 17200, rpmMax = 21200),
        MotorRefEntity(name = "Power-Dash", shaftType = "single", category = "dash", rpmMin = 19900, rpmMax = 23600),
        MotorRefEntity(name = "Sprint-Dash", shaftType = "single", category = "dash", rpmMin = 20700, rpmMax = 27200),
        MotorRefEntity(name = "Mach-Dash", shaftType = "single", category = "dash", rpmMin = 25000, rpmMax = 28000),

        // Double Shaft Motors (PRO)
        MotorRefEntity(name = "Torque-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 12200, rpmMax = 14400),
        MotorRefEntity(name = "Atomic-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 12300, rpmMax = 14500),
        MotorRefEntity(name = "Rev-Tuned 2 PRO", shaftType = "double", category = "tuned", rpmMin = 13200, rpmMax = 14900),
        MotorRefEntity(name = "Light-Dash PRO", shaftType = "double", category = "dash", rpmMin = 14000, rpmMax = 17800),
        MotorRefEntity(name = "Hyper-Dash PRO", shaftType = "double", category = "dash", rpmMin = 17200, rpmMax = 21200),
        MotorRefEntity(name = "Mach-Dash PRO", shaftType = "double", category = "dash", rpmMin = 20000, rpmMax = 24500)
    )

    fun getGearRatioData(): List<GearRatioRefEntity> = listOf(
        // Single Shaft Gear Ratios (Spur + Pinion)
        GearRatioRefEntity(
            ratio = "3.5:1", ratioValue = 3.5f, shaftType = "single",
            gear1Teeth = 35, gear1Color = "Yellow", gear1Code = "G18",
            gear2Teeth = 10, gear2Color = "Light Blue", gear2Code = "G17"
        ),
        GearRatioRefEntity(
            ratio = "3.7:1", ratioValue = 3.7f, shaftType = "single",
            gear1Teeth = 37, gear1Color = "Yellow", gear1Code = "G18",
            gear2Teeth = 10, gear2Color = "Green", gear2Code = "G24"
        ),
        GearRatioRefEntity(
            ratio = "4:1", ratioValue = 4.0f, shaftType = "single",
            gear1Teeth = 36, gear1Color = "Light Brown", gear1Code = "G11",
            gear2Teeth = 9, gear2Color = "Black", gear2Code = "G14"
        ),
        GearRatioRefEntity(
            ratio = "4.2:1", ratioValue = 4.2f, shaftType = "single",
            gear1Teeth = 38, gear1Color = "Light Brown", gear1Code = "G11",
            gear2Teeth = 9, gear2Color = "Red", gear2Code = "G9"
        ),
        GearRatioRefEntity(
            ratio = "5:1", ratioValue = 5.0f, shaftType = "single",
            gear1Teeth = 40, gear1Color = "Light Green", gear1Code = "G6",
            gear2Teeth = 8, gear2Color = "Blue", gear2Code = "G10"
        ),

        // Double Shaft Gear Ratios (Spur + Counter)
        GearRatioRefEntity(
            ratio = "3.5:1", ratioValue = 3.5f, shaftType = "double",
            gear1Teeth = 0, gear1Color = "Pink", gear1Code = "G22",
            gear2Teeth = 0, gear2Color = "Light Green", gear2Code = "G21"
        ),
        GearRatioRefEntity(
            ratio = "3.7:1", ratioValue = 3.7f, shaftType = "double",
            gear1Teeth = 0, gear1Color = "Pink", gear1Code = "G22",
            gear2Teeth = 0, gear2Color = "Yellow", gear2Code = "G23"
        ),
        GearRatioRefEntity(
            ratio = "4:1", ratioValue = 4.0f, shaftType = "double",
            gear1Teeth = 0, gear1Color = "Orange", gear1Code = "G20",
            gear2Teeth = 0, gear2Color = "Blue", gear2Code = "G19"
        )
    )
}
