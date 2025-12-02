package com.mini4wd.lab.feature.profile.ui.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mini4wd.lab.core.common.model.CarProfile
import com.mini4wd.lab.core.database.repository.CarProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GarageUiState(
    val profiles: List<CarProfile> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val error: String? = null
)

enum class ViewMode { GRID, LIST }

sealed class GarageEvent {
    data class DeleteProfile(val id: Long) : GarageEvent()
    data class NavigateToProfile(val id: Long) : GarageEvent()
    data object NavigateToCreateProfile : GarageEvent()
}

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val repository: CarProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GarageUiState())
    val uiState: StateFlow<GarageUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<GarageEvent>()
    val events: SharedFlow<GarageEvent> = _events.asSharedFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        observeProfiles()
    }

    private fun observeProfiles() {
        viewModelScope.launch {
            combine(
                searchQuery,
                searchQuery.flatMapLatest { query ->
                    if (query.isBlank()) {
                        repository.getAllProfiles()
                    } else {
                        repository.searchProfiles(query)
                    }
                }
            ) { query, profiles ->
                _uiState.value.copy(
                    profiles = profiles,
                    searchQuery = query,
                    isLoading = false
                )
            }.catch { e ->
                emit(_uiState.value.copy(error = e.message, isLoading = false))
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onViewModeToggle() {
        _uiState.value = _uiState.value.copy(
            viewMode = if (_uiState.value.viewMode == ViewMode.GRID) ViewMode.LIST else ViewMode.GRID
        )
    }

    fun onDeleteProfile(id: Long) {
        viewModelScope.launch {
            repository.deleteProfile(id)
        }
    }

    fun onProfileClick(id: Long) {
        viewModelScope.launch {
            _events.emit(GarageEvent.NavigateToProfile(id))
        }
    }

    fun onAddProfileClick() {
        viewModelScope.launch {
            _events.emit(GarageEvent.NavigateToCreateProfile)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
