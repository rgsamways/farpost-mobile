package com.farpost.mobile.ui.screens.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farpost.mobile.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/** Owns the login form's local state only — a successful [signIn] just persists the
 * session; FarpostNavGraph reacts to that (via AuthRepository.sessionState) and
 * routes by capability, so this view model never navigates directly. */
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isSigningIn by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun signIn() {
        if (isSigningIn) return
        isSigningIn = true
        errorMessage = null
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            isSigningIn = false
            result.onFailure { errorMessage = "Invalid email or password." }
        }
    }
}
