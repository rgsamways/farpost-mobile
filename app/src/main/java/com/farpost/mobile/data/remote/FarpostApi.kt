package com.farpost.mobile.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FarpostApi {
    /** Connectivity/wiring proof only — see SyncWorker kdoc. */
    @GET("health")
    suspend fun health(): HealthResponse

    /** Real, live endpoint — see farpost-api's app/routes/unified_auth.py. Authenticates
     * once against `users`, then returns every capability (admin/professional/owner)
     * linked to that account; a person may hold more than one at once. */
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): LoginEnvelope

    /** Real, live endpoint — see farpost-api's app/routes/professional_scout_entries.py
     * (scout-entry-sync-endpoint). Idempotent on [ScoutEntrySubmitRequest.id] — a
     * retried offline sync resubmitting the same id upserts the same record. Requires
     * a professional JWT whose roles include "scout" (AuthInterceptor attaches the
     * token automatically); the backend returns 403 if that role is absent, or if
     * [ScoutEntrySubmitRequest.id] already belongs to a different professional. */
    @POST("api/v1/professional/scout-entries")
    suspend fun submitScoutEntry(@Body body: ScoutEntrySubmitRequest): ScoutEntryEnvelope
}

@Serializable
data class HealthResponse(val status: String)

@Serializable
data class LoginRequest(val email: String, val password: String)

/** Success responses use `data`; the backend raises a plain HTTP 401 (no envelope)
 * on bad credentials or zero role-memberships, surfaced to Retrofit as an
 * HttpException rather than a populated [error]. */
@Serializable
data class LoginEnvelope(
    val data: LoginData? = null,
    val error: String? = null,
)

@Serializable
data class LoginData(
    val token: String,
    val capabilities: List<String>,
    val user: AdminUserDto? = null,
    val professional: ProfessionalDto? = null,
    val owner: OwnerDto? = null,
    @SerialName("building_slug") val buildingSlug: String? = null,
)

@Serializable
data class AdminUserDto(
    val name: String,
    val email: String,
    @SerialName("is_super_admin") val isSuperAdmin: Boolean,
)

@Serializable
data class ProfessionalDto(
    val id: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val company: String,
    val roles: List<String>,
    val slug: String,
)

@Serializable
data class OwnerDto(
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val email: String,
    @SerialName("verified_at") val verifiedAt: String? = null,
    val verified: Boolean,
)

@Serializable
data class ScoutEntrySubmitRequest(
    val id: String,
    @SerialName("building_id") val buildingId: String?,
    val note: String,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("captured_at") val capturedAt: String,
)

/** Success responses use `data`; the backend's failure modes (missing/wrong role,
 * id owned by a different professional) are plain HTTP 403/401 with no envelope,
 * surfaced to Retrofit as an HttpException — same convention as [LoginEnvelope]. */
@Serializable
data class ScoutEntryEnvelope(
    val data: ScoutEntryData? = null,
    val error: String? = null,
)

@Serializable
data class ScoutEntryData(
    val id: String,
    @SerialName("professional_id") val professionalId: String,
    @SerialName("building_id") val buildingId: String?,
    val note: String,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("captured_at") val capturedAt: String,
    @SerialName("synced_at") val syncedAt: String,
)
