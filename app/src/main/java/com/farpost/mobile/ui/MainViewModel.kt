package com.farpost.mobile.ui

import androidx.lifecycle.ViewModel
import com.farpost.mobile.data.auth.AuthRepository
import com.farpost.mobile.data.auth.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel() {
    val sessionState: StateFlow<SessionState> = authRepository.sessionState
}
