package com.mini4wd.lab.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CarProfileWithRefs(
    @Embedded
    val profile: CarProfileEntity,

    @Relation(
        parentColumn = "chassis_ref_id",
        entityColumn = "id"
    )
    val chassis: ChassisRefEntity?,

    @Relation(
        parentColumn = "motor_ref_id",
        entityColumn = "id"
    )
    val motor: MotorRefEntity?,

    @Relation(
        parentColumn = "gear_ratio_ref_id",
        entityColumn = "id"
    )
    val gearRatio: GearRatioRefEntity?
)
