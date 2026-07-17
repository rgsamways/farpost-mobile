package com.farpost.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farpost.mobile.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/** Sign-out is identical across every signed-in destination (AdminHome/
 * ProfessionalHub/OwnerHome), so they all share this instead of each owning a
 * near-duplicate view model. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun signOut() {
        viewModelScope.launch { authRepository.logout() }
    }
}
