package com.omteam.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.omteam.network.api.AuthApiService
import com.omteam.network.api.CharacterApiService
import com.omteam.network.api.ChatApiService
import com.omteam.network.api.MissionApiService
import com.omteam.network.api.OnboardingApiService
import com.omteam.network.api.ReportApiService
import com.omteam.network.interceptor.AuthResponseInterceptor
import com.omteam.network.interceptor.TokenInterceptor
import com.omteam.omt.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        authResponseInterceptor: AuthResponseInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor) // Authorization 헤더 자동 추가
            .addInterceptor(authResponseInterceptor) // 401, 403 응답 시 토큰 갱신 및 재시도
            .addInterceptor(loggingInterceptor) // HTTP 로깅
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)
    
    @Provides
    @Singleton
    fun provideMissionApiService(retrofit: Retrofit): MissionApiService =
        retrofit.create(MissionApiService::class.java)
    
    @Provides
    @Singleton
    fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
        retrofit.create(CharacterApiService::class.java)
    
    @Provides
    @Singleton
    fun provideReportApiService(retrofit: Retrofit): ReportApiService =
        retrofit.create(ReportApiService::class.java)
    
    @Provides
    @Singleton
    fun provideOnboardingApiService(retrofit: Retrofit): OnboardingApiService =
        retrofit.create(OnboardingApiService::class.java)
    
    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)
}