package com.farpost.mobile.data.auth

import kotlinx.coroutines.flow.StateFlow

/**
 * Distinguishes "haven't finished reading the persisted session yet" from
 * "genuinely signed out" — a nullable UserSession alone can't tell those apart,
 * which would otherwise flash the signed-out screen for a moment on every cold
 * start before a persisted session loads from disk.
 */
sealed interface SessionState {
    data object Loading : SessionState
    data object SignedOut : SessionState
    data class SignedIn(val session: UserSession) : SessionState
}

interface AuthRepository {
    /** Persists across process death (see [FakeAuthRepository]). */
    val sessionState: StateFlow<SessionState>

    suspend fun login(email: String, password: String): Result<UserSession>

    suspend fun logout()
}
