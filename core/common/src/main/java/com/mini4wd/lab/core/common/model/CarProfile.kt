package com.mini4wd.lab.core.common.model

data class CarProfile(
    val id: Long = 0,
    val name: String,
    val chassis: ChassisInfo? = null,
    val motor: MotorInfo? = null,
    val gearRatio: GearRatioInfo? = null,
    val tires: TireConfig = TireConfig(),
    val wheels: WheelConfig? = null,
    val rollerConfigJson: String? = null,
    val damperConfigJson: String? = null,
    val brakeConfigJson: String? = null,
    val customModNotes: String? = null,
    val bodyType: String? = null,
    val bodyAeroNotes: String? = null,
    val totalWeightG: Float? = null,
    val batteryType: String? = null,
    val batteryBrand: String? = null,
    val tags: List<String> = emptyList(),
    val thumbnailPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class ChassisInfo(
    val id: Long,
    val code: String,
    val name: String,
    val shaftType: String,
    val motorPosition: String
)

data class MotorInfo(
    val id: Long,
    val name: String,
    val shaftType: String,
    val category: String,
    val rpmMin: Int,
    val rpmMax: Int,
    val breakInStatus: Boolean = false,
    val breakInDate: Long? = null,
    val runCount: Int = 0
)

data class GearRatioInfo(
    val id: Long,
    val ratio: String,
    val ratioValue: Float,
    val shaftType: String,
    val gear1Color: String,
    val gear2Color: String
)

data class TireConfig(
    val frontType: String? = null,
    val frontMaterial: String? = null,
    val frontDiameterMm: Float? = null,
    val rearType: String? = null,
    val rearMaterial: String? = null,
    val rearDiameterMm: Float? = null
)

data class WheelConfig(
    val type: String? = null,
    val material: String? = null
)
