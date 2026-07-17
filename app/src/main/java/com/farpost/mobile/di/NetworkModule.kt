package com.farpost.mobile.di

import com.farpost.mobile.data.remote.AuthInterceptor
import com.farpost.mobile.data.remote.FarpostApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

// Plain "localhost" — works for both the emulator and a USB-attached physical
// device via `adb reverse tcp:8000 tcp:8000`, which forwards the device's own
// localhost:8000 to the host machine's. Matches farpost-web's dev fallback
// (NEXT_PUBLIC_API_URL ?? "http://localhost:8000").
// TODO: switch to the deployed Railway URL for release builds (build-type-specific,
// not hardcoded here, same as farpost-web keeps it out of source via an env var).
private const val BASE_URL = "http://localhost:8000/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Singleton
    @Provides
    fun provideFarpostApi(retrofit: Retrofit): FarpostApi = retrofit.create(FarpostApi::class.java)
}
