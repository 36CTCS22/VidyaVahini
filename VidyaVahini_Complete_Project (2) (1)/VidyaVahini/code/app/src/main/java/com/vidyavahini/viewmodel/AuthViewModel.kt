package com.vidyavahini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidyavahini.data.model.User
import com.vidyavahini.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val firebaseUser = repository.currentUser
        if (firebaseUser != null) {
            viewModelScope.launch {
                val isAdmin = repository.isAdmin(firebaseUser.uid)
                val userProfile = repository.getUserProfile(firebaseUser.uid)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    isAdmin = isAdmin,
                    user = userProfile
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.login(email, password)
            if (result.isSuccess) {
                val uid = result.getOrNull()!!.uid
                val isAdmin = repository.isAdmin(uid)
                val userProfile = repository.getUserProfile(uid)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    isAdmin = isAdmin,
                    user = userProfile
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.register(name, email, password)
            if (result.isSuccess) {
                val uid = result.getOrNull()!!.uid
                val userProfile = repository.getUserProfile(uid)
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    isAdmin = false,
                    user = userProfile
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }

    fun loginAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.loginAnonymously()
            if (result.isSuccess) {
                _uiState.value = AuthUiState(
                    isLoggedIn = true,
                    isAdmin = false,
                    user = User(userId = result.getOrNull()!!.uid, name = "Guest", loginType = "anonymous")
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState()
    }

    fun updateUserRoute(routeId: String) {
        val uid = repository.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.updateUserRoute(uid, routeId)
            _uiState.value = _uiState.value.copy(
                user = _uiState.value.user?.copy(routeNumber = routeId)
            )
        }
    }

    fun markReachedSafely() {
        val uid = repository.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.markReachedSafely(uid)
            _uiState.value = _uiState.value.copy(
                user = _uiState.value.user?.copy(reachedSafely = true)
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
