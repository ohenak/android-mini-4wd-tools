package com.mini4wd.lab.feature.profile.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.common.model.ChassisInfo
import com.mini4wd.lab.core.common.model.GearRatioInfo
import com.mini4wd.lab.core.common.model.MotorInfo
import com.mini4wd.lab.core.common.model.TireConfig
import com.mini4wd.lab.core.common.model.WheelConfig
import com.mini4wd.lab.core.database.dao.ChassisRefDao
import com.mini4wd.lab.core.database.dao.GearRatioRefDao
import com.mini4wd.lab.core.database.dao.MotorRefDao
import com.mini4wd.lab.core.database.entity.ChassisRefEntity
import com.mini4wd.lab.core.database.entity.GearRatioRefEntity
import com.mini4wd.lab.core.database.entity.MotorRefEntity
import com.mini4wd.lab.core.database.repository.CarProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileEditUiState(
    val isEditMode: Boolean = false,
    val profileId: Long? = null,
    val name: String = "",
    val nameError: String? = null,
    val selectedChassis: ChassisRefEntity? = null,
    val selectedMotor: MotorRefEntity? = null,
    val selectedGearRatio: GearRatioRefEntity? = null,
    val motorBreakInStatus: Boolean = false,
    val motorRunCount: Int = 0,
    val customModNotes: String = "",
    val tireFrontType: String = "",
    val tireFrontMaterial: String = "",
    val tireFrontDiameter: String = "",
    val tireRearType: String = "",
    val tireRearMaterial: String = "",
    val tireRearDiameter: String = "",
    val wheelType: String = "",
    val wheelMaterial: String = "",
    val bodyType: String = "",
    val totalWeight: String = "",
    val batteryType: String = "",
    val batteryBrand: String = "",
    val tags: String = "",
    val chassisList: List<ChassisRefEntity> = emptyList(),
    val motorList: List<MotorRefEntity> = emptyList(),
    val gearRatioList: List<GearRatioRefEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CarProfileRepository,
    private val chassisRefDao: ChassisRefDao,
    private val motorRefDao: MotorRefDao,
    private val gearRatioRefDao: GearRatioRefDao
) : ViewModel() {

    private val profileId: Long? = savedStateHandle.get<Long>("profileId")?.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(ProfileEditUiState(
        isEditMode = profileId != null,
        profileId = profileId
    ))
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        loadReferenceData()
        profileId?.let { loadProfile(it) }
    }

    private fun loadReferenceData() {
        viewModelScope.launch {
            combine(
                chassisRefDao.getAllChassis(),
                motorRefDao.getAllMotors(),
                gearRatioRefDao.getAllGearRatios()
            ) { chassis, motors, gearRatios ->
                Triple(chassis, motors, gearRatios)
            }.collect { (chassis, motors, gearRatios) ->
                _uiState.value = _uiState.value.copy(
                    chassisList = chassis,
                    motorList = motors,
                    gearRatioList = gearRatios,
                    isLoading = profileId != null // Still loading if editing
                )
            }
        }
    }

    private fun loadProfile(id: Long) {
        viewModelScope.launch {
            repository.getProfileById(id).first()?.let { profile ->
                _uiState.value = _uiState.value.copy(
                    name = profile.name,
                    selectedChassis = _uiState.value.chassisList.find { it.id == profile.chassis?.id },
                    selectedMotor = _uiState.value.motorList.find { it.id == profile.motor?.id },
                    selectedGearRatio = _uiState.value.gearRatioList.find { it.id == profile.gearRatio?.id },
                    motorBreakInStatus = profile.motor?.breakInStatus ?: false,
                    motorRunCount = profile.motor?.runCount ?: 0,
                    customModNotes = profile.customModNotes ?: "",
                    tireFrontType = profile.tires.frontType ?: "",
                    tireFrontMaterial = profile.tires.frontMaterial ?: "",
                    tireFrontDiameter = profile.tires.frontDiameterMm?.toString() ?: "",
                    tireRearType = profile.tires.rearType ?: "",
                    tireRearMaterial = profile.tires.rearMaterial ?: "",
                    tireRearDiameter = profile.tires.rearDiameterMm?.toString() ?: "",
                    wheelType = profile.wheels?.type ?: "",
                    wheelMaterial = profile.wheels?.material ?: "",
                    bodyType = profile.bodyType ?: "",
                    totalWeight = profile.totalWeightG?.toString() ?: "",
                    batteryType = profile.batteryType ?: "",
                    batteryBrand = profile.batteryBrand ?: "",
                    tags = profile.tags.joinToString(", "),
                    isLoading = false
                )
            }
        }
    }

    fun getFilteredMotors(): List<MotorRefEntity> {
        val shaftType = _uiState.value.selectedChassis?.shaftType ?: return _uiState.value.motorList
        return _uiState.value.motorList.filter { it.shaftType == shaftType }
    }

    fun getFilteredGearRatios(): List<GearRatioRefEntity> {
        val shaftType = _uiState.value.selectedChassis?.shaftType ?: return _uiState.value.gearRatioList
        return _uiState.value.gearRatioList.filter { it.shaftType == shaftType }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name, nameError = null)
    }

    fun onChassisSelect(chassis: ChassisRefEntity?) {
        val currentMotor = _uiState.value.selectedMotor
        val currentGearRatio = _uiState.value.selectedGearRatio

        val clearSelections = chassis?.shaftType != _uiState.value.selectedChassis?.shaftType

        _uiState.value = _uiState.value.copy(
            selectedChassis = chassis,
            selectedMotor = if (clearSelections) null else currentMotor,
            selectedGearRatio = if (clearSelections) null else currentGearRatio
        )
    }

    fun onMotorSelect(motor: MotorRefEntity?) {
        _uiState.value = _uiState.value.copy(selectedMotor = motor)
    }

    fun onGearRatioSelect(gearRatio: GearRatioRefEntity?) {
        _uiState.value = _uiState.value.copy(selectedGearRatio = gearRatio)
    }

    fun onMotorBreakInStatusChange(status: Boolean) {
        _uiState.value = _uiState.value.copy(motorBreakInStatus = status)
    }

    fun onMotorRunCountChange(count: String) {
        _uiState.value = _uiState.value.copy(motorRunCount = count.toIntOrNull() ?: 0)
    }

    fun onCustomModNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(customModNotes = notes)
    }

    fun onTireFrontTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(tireFrontType = type)
    }

    fun onTireFrontMaterialChange(material: String) {
        _uiState.value = _uiState.value.copy(tireFrontMaterial = material)
    }

    fun onTireFrontDiameterChange(diameter: String) {
        _uiState.value = _uiState.value.copy(tireFrontDiameter = diameter)
    }

    fun onTireRearTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(tireRearType = type)
    }

    fun onTireRearMaterialChange(material: String) {
        _uiState.value = _uiState.value.copy(tireRearMaterial = material)
    }

    fun onTireRearDiameterChange(diameter: String) {
        _uiState.value = _uiState.value.copy(tireRearDiameter = diameter)
    }

    fun onWheelTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(wheelType = type)
    }

    fun onWheelMaterialChange(material: String) {
        _uiState.value = _uiState.value.copy(wheelMaterial = material)
    }

    fun onBodyTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(bodyType = type)
    }

    fun onTotalWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(totalWeight = weight)
    }

    fun onBatteryTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(batteryType = type)
    }

    fun onBatteryBrandChange(brand: String) {
        _uiState.value = _uiState.value.copy(batteryBrand = brand)
    }

    fun onTagsChange(tags: String) {
        _uiState.value = _uiState.value.copy(tags = tags)
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.name.isBlank()) {
                _uiState.value = state.copy(nameError = "Name is required")
                return@launch
            }

            val isUnique = repository.isNameUnique(state.name, state.profileId ?: 0)
            if (!isUnique) {
                _uiState.value = state.copy(nameError = "A car with this name already exists")
                return@launch
            }

            _uiState.value = state.copy(isSaving = true)

            val profile = CarProfile(
                id = state.profileId ?: 0,
                name = state.name,
                chassis = state.selectedChassis?.let {
                    ChassisInfo(it.id, it.code, it.name, it.shaftType, it.motorPosition)
                },
                motor = state.selectedMotor?.let {
                    MotorInfo(
                        id = it.id,
                        name = it.name,
                        shaftType = it.shaftType,
                        category = it.category,
                        rpmMin = it.rpmMin,
                        rpmMax = it.rpmMax,
                        breakInStatus = state.motorBreakInStatus,
                        runCount = state.motorRunCount
                    )
                },
                gearRatio = state.selectedGearRatio?.let {
                    GearRatioInfo(it.id, it.ratio, it.ratioValue, it.shaftType, it.gear1Color, it.gear2Color)
                },
                tires = TireConfig(
                    frontType = state.tireFrontType.takeIf { it.isNotBlank() },
                    frontMaterial = state.tireFrontMaterial.takeIf { it.isNotBlank() },
                    frontDiameterMm = state.tireFrontDiameter.toFloatOrNull(),
                    rearType = state.tireRearType.takeIf { it.isNotBlank() },
                    rearMaterial = state.tireRearMaterial.takeIf { it.isNotBlank() },
                    rearDiameterMm = state.tireRearDiameter.toFloatOrNull()
                ),
                wheels = if (state.wheelType.isNotBlank() || state.wheelMaterial.isNotBlank()) {
                    WheelConfig(
                        type = state.wheelType.takeIf { it.isNotBlank() },
                        material = state.wheelMaterial.takeIf { it.isNotBlank() }
                    )
                } else null,
                customModNotes = state.customModNotes.takeIf { it.isNotBlank() },
                bodyType = state.bodyType.takeIf { it.isNotBlank() },
                totalWeightG = state.totalWeight.toFloatOrNull(),
                batteryType = state.batteryType.takeIf { it.isNotBlank() },
                batteryBrand = state.batteryBrand.takeIf { it.isNotBlank() },
                tags = state.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            )

            val result = if (state.isEditMode) {
                repository.updateProfile(profile)
            } else {
                repository.createProfile(profile).map { }
            }

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true)
                },
                onFailure = { e ->
                    val nameError = if (e is android.database.sqlite.SQLiteConstraintException) {
                        "A car with this name already exists"
                    } else null
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message,
                        nameError = nameError
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
