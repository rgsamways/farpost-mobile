package com.farpost.mobile.data.auth

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.farpost.mobile.data.remote.FarpostApi
import com.farpost.mobile.data.remote.LoginRequest
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

private val SESSION_KEY = stringPreferencesKey("user_session")

/** Real implementation against farpost-api's live `POST /api/v1/auth/login`
 * (unify-user-identity-model + fold-scout-into-professional-roles, shipped
 * 2026-07-13) — see [FarpostApi.login]. */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: FarpostApi,
    private val dataStore: DataStore<Preferences>,
    externalScope: CoroutineScope,
) : AuthRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override val sessionState: StateFlow<SessionState> = dataStore.data
        .map { prefs ->
            val stored = prefs[SESSION_KEY]?.let { json.decodeFromString<UserSession>(it) }
            if (stored != null) SessionState.SignedIn(stored) else SessionState.SignedOut
        }
        .stateIn(externalScope, SharingStarted.Eagerly, SessionState.Loading)

    override suspend fun login(email: String, password: String): Result<UserSession> = try {
        val body = api.login(LoginRequest(email, password)).data
            ?: return Result.failure(InvalidCredentialsException())

        val session = UserSession(
            userId = subClaimOf(body.token),
            token = body.token,
            capabilities = body.capabilities,
            admin = body.user?.let { AdminProfile(name = it.name, email = it.email, isSuperAdmin = it.isSuperAdmin) },
            professional = body.professional?.let {
                ProfessionalProfile(
                    id = it.id,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    company = it.company,
                    roles = it.roles,
                    slug = it.slug,
                )
            },
            owner = body.owner?.let {
                OwnerProfile(ownerId = it.ownerId, name = it.name, email = it.email, verifiedAt = it.verifiedAt, verified = it.verified)
            },
            buildingSlug = body.buildingSlug,
        )
        persist(session)
        Result.success(session)
    } catch (e: HttpException) {
        // Backend returns a generic 401 for both bad credentials and zero
        // role-memberships — don't try to distinguish those cases here either
        // (matches farpost-web's login page).
        Result.failure(InvalidCredentialsException())
    } catch (e: IOException) {
        Result.failure(e)
    }

    override suspend fun logout() {
        dataStore.edit { it.remove(SESSION_KEY) }
    }

    private suspend fun persist(userSession: UserSession) {
        dataStore.edit { prefs -> prefs[SESSION_KEY] = json.encodeToString(userSession) }
    }
}

class InvalidCredentialsException : Exception("Invalid email or password")

/** The JWT's `sub` claim (user_id) — decoded, not verified; the client never needs
 * to verify its own token, only send it back on the Authorization header. */
private fun subClaimOf(jwt: String): String {
    val payload = jwt.split(".").getOrNull(1) ?: return ""
    val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    return Json.parseToJsonElement(String(decoded, Charsets.UTF_8)).jsonObject["sub"]?.jsonPrimitive?.content ?: ""
}
