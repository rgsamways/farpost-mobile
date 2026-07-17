package com.farpost.mobile.data.remote

import com.farpost.mobile.data.auth.AuthRepository
import com.farpost.mobile.data.auth.SessionState
import dagger.Lazy
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/** Attaches `Authorization: Bearer <jwt>` — the real token from
 * `POST /api/v1/auth/login` ({sub: user_id, capabilities: [...], exp}).
 *
 * [authRepository] is `dagger.Lazy`, not injected directly: AuthRepositoryImpl
 * calls FarpostApi to log in, and FarpostApi is built from the same OkHttpClient
 * this interceptor rides on — a direct AuthRepository dependency here would be a
 * Dagger dependency cycle. Lazy defers resolution to first use (well after the
 * graph itself is built), which breaks it. */
class AuthInterceptor @Inject constructor(
    private val authRepository: Lazy<AuthRepository>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val sessionState = authRepository.get().sessionState.value
        val token = (sessionState as? SessionState.SignedIn)?.session?.token

        val authorized = if (token != null) {
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            request
        }
        return chain.proceed(authorized)
    }
}
