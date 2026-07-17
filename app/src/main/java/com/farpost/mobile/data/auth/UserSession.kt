package com.farpost.mobile.data.auth

import kotlinx.serialization.Serializable

/**
 * Mirrors the real, live `POST /api/v1/auth/login` response (unify-user-identity-model
 * + fold-scout-into-professional-roles, both shipped 2026-07-13): one login can hold
 * more than one capability at once, and each capability carries its own payload.
 * Scout is not a capability — it only ever shows up inside [ProfessionalProfile.roles].
 */
@Serializable
data class UserSession(
    val userId: String,
    val token: String,
    val capabilities: List<String>,
    val admin: AdminProfile? = null,
    val professional: ProfessionalProfile? = null,
    val owner: OwnerProfile? = null,
    val buildingSlug: String? = null,
)

@Serializable
data class AdminProfile(
    val name: String,
    val email: String,
    val isSuperAdmin: Boolean,
)

/** roles is a FK list into the backend's role_types registry (sprint F17); roles[0]
 * is primary. Kept as raw strings, not a closed enum, so a new role type added on
 * the backend doesn't require an app release just to stop being dropped/misread. */
@Serializable
data class ProfessionalProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val company: String,
    val roles: List<String>,
    val slug: String,
)

@Serializable
data class OwnerProfile(
    val ownerId: String,
    val name: String,
    val email: String,
    val verifiedAt: String?,
    val verified: Boolean,
)

/** Well-known capability values, for UI gating — not an exhaustive/closed set. */
object Capabilities {
    const val ADMIN = "admin"
    const val PROFESSIONAL = "professional"
    const val OWNER = "owner"
}

/** Role keys inside [ProfessionalProfile.roles] that this app has a real screen for. */
object ProfessionalRoles {
    const val SCOUT = "scout"
}

fun UserSession.has(capability: String): Boolean = capabilities.contains(capability)
