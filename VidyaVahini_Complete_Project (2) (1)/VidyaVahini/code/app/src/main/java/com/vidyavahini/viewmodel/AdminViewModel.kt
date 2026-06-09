package com.vidyavahini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidyavahini.data.model.Route
import com.vidyavahini.data.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminUiState(
    val isLoading: Boolean = false,
    val updateSuccess: Boolean = false,
    val error: String? = null
)

class AdminViewModel : ViewModel() {

    private val repository = RouteRepository()

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        loadRoutes()
    }

    private fun loadRoutes() {
        viewModelScope.launch {
            repository.observeAllRoutes().collect { routeList ->
                _routes.value = routeList
            }
        }
    }

    fun updateRouteStatus(
        routeId: String,
        currentLocation: String,
        eta: String,
        isDelayed: Boolean,
        isCancelled: Boolean,
        delayReason: String
    ) {
        viewModelScope.launch {
            _uiState.value = AdminUiState(isLoading = true)
            try {
                repository.updateRouteStatus(
                    routeId, currentLocation, eta, isDelayed, isCancelled, delayReason
                )
                _uiState.value = AdminUiState(updateSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AdminUiState(error = e.message ?: "Update failed")
            }
        }
    }

    fun addNewRoute(routeId: String, routeName: String) {
        viewModelScope.launch {
            try {
                val route = Route(
                    routeId = routeId,
                    routeName = routeName,
                    currentLocation = "Not started",
                    eta = "Unknown"
                )
                repository.addRoute(route)
                _uiState.value = AdminUiState(updateSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AdminUiState(error = e.message ?: "Failed to add route")
            }
        }
    }

    fun clearState() {
        _uiState.value = AdminUiState()
    }
}
