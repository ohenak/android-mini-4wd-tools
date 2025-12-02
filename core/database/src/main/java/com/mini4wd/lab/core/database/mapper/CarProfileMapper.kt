package com.mini4wd.lab.core.database.mapper

import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.common.model.ChassisInfo
import com.mini4wd.lab.core.common.model.GearRatioInfo
import com.mini4wd.lab.core.common.model.MotorInfo
import com.mini4wd.lab.core.common.model.TireConfig
import com.mini4wd.lab.core.common.model.WheelConfig
import com.mini4wd.lab.core.database.entity.CarProfileEntity
import com.mini4wd.lab.core.database.entity.CarProfileWithRefs

fun CarProfileWithRefs.toDomain(): CarProfile {
    return CarProfile(
        id = profile.id,
        name = profile.name,
        chassis = chassis?.let {
            ChassisInfo(
                id = it.id,
                code = it.code,
                name = it.name,
                shaftType = it.shaftType,
                motorPosition = it.motorPosition
            )
        },
        motor = motor?.let {
            MotorInfo(
                id = it.id,
                name = it.name,
                shaftType = it.shaftType,
                category = it.category,
                rpmMin = it.rpmMin,
                rpmMax = it.rpmMax,
                breakInStatus = profile.motorBreakInStatus,
                breakInDate = profile.motorBreakInDate,
                runCount = profile.motorRunCount
            )
        },
        gearRatio = gearRatio?.let {
            GearRatioInfo(
                id = it.id,
                ratio = it.ratio,
                ratioValue = it.ratioValue,
                shaftType = it.shaftType,
                gear1Color = it.gear1Color,
                gear2Color = it.gear2Color
            )
        },
        tires = TireConfig(
            frontType = profile.tireFrontType,
            frontMaterial = profile.tireFrontMaterial,
            frontDiameterMm = profile.tireFrontDiameterMm,
            rearType = profile.tireRearType,
            rearMaterial = profile.tireRearMaterial,
            rearDiameterMm = profile.tireRearDiameterMm
        ),
        wheels = if (profile.wheelType != null || profile.wheelMaterial != null) {
            WheelConfig(
                type = profile.wheelType,
                material = profile.wheelMaterial
            )
        } else null,
        rollerConfigJson = profile.rollerConfigJson,
        damperConfigJson = profile.damperConfigJson,
        brakeConfigJson = profile.brakeConfigJson,
        customModNotes = profile.customModNotes,
        bodyType = profile.bodyType,
        bodyAeroNotes = profile.bodyAeroNotes,
        totalWeightG = profile.totalWeightG,
        batteryType = profile.batteryType,
        batteryBrand = profile.batteryBrand,
        tags = profile.tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
        thumbnailPath = profile.thumbnailPath,
        createdAt = profile.createdAt,
        updatedAt = profile.updatedAt
    )
}

fun CarProfileEntity.toDomainBasic(): CarProfile {
    return CarProfile(
        id = id,
        name = name,
        tags = tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
        thumbnailPath = thumbnailPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CarProfile.toEntity(): CarProfileEntity {
    return CarProfileEntity(
        id = id,
        name = name,
        chassisRefId = chassis?.id,
        motorRefId = motor?.id,
        gearRatioRefId = gearRatio?.id,
        motorBreakInStatus = motor?.breakInStatus ?: false,
        motorBreakInDate = motor?.breakInDate,
        motorRunCount = motor?.runCount ?: 0,
        customModNotes = customModNotes,
        tireFrontType = tires.frontType,
        tireFrontMaterial = tires.frontMaterial,
        tireFrontDiameterMm = tires.frontDiameterMm,
        tireRearType = tires.rearType,
        tireRearMaterial = tires.rearMaterial,
        tireRearDiameterMm = tires.rearDiameterMm,
        wheelType = wheels?.type,
        wheelMaterial = wheels?.material,
        rollerConfigJson = rollerConfigJson,
        damperConfigJson = damperConfigJson,
        brakeConfigJson = brakeConfigJson,
        bodyType = bodyType,
        bodyAeroNotes = bodyAeroNotes,
        totalWeightG = totalWeightG,
        batteryType = batteryType,
        batteryBrand = batteryBrand,
        tags = tags.joinToString(","),
        thumbnailPath = thumbnailPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
