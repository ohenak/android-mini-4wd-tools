package com.mini4wd.lab.feature.profile.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.database.repository.CarProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileDetailUiState(
    val profile: CarProfile? = null,
    val isLoading: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CarProfileRepository
) : ViewModel() {

    private val profileId: Long = checkNotNull(savedStateHandle.get<Long>("profileId"))

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.getProfileById(profileId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
                }
                .collect { profile ->
                    _uiState.value = _uiState.value.copy(profile = profile, isLoading = false)
                }
        }
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun dismissDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun deleteProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showDeleteDialog = false)
            repository.deleteProfile(profileId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isDeleted = true)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )
        }
    }
}
