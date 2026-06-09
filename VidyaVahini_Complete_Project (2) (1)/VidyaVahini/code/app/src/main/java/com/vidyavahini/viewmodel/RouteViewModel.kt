package com.vidyavahini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidyavahini.data.model.Route
import com.vidyavahini.data.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {

    private val repository = RouteRepository()

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    private val _currentRoute = MutableStateFlow<Route?>(null)
    val currentRoute: StateFlow<Route?> = _currentRoute

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadAllRoutes() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.observeAllRoutes().collect { routeList ->
                _routes.value = routeList
                _isLoading.value = false
            }
        }
    }

    fun observeRoute(routeId: String) {
        viewModelScope.launch {
            repository.observeRoute(routeId).collect { route ->
                _currentRoute.value = route
            }
        }
    }
}
