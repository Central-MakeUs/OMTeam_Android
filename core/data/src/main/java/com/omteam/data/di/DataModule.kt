package com.omteam.data.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.omteam.data.datasource.AuthDataSource
import com.omteam.data.datasource.GoogleAuthDataSource
import com.omteam.data.datasource.KakaoAuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleAuth

/**
 * DataSource 제공 모듈
 *
 * 카카오, 구글 AuthDataSource 구현체 제공
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    @KakaoAuth
    fun provideKakaoAuthDataSource(): AuthDataSource = KakaoAuthDataSource()

    @Provides
    @Singleton
    @GoogleAuth
    fun provideGoogleAuthDataSource(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager,
        @Named("google_web_client_id") webClientId: String
    ): AuthDataSource = GoogleAuthDataSource(context, credentialManager, webClientId)

    // 현재 사용할 AuthDataSource 설정. 기본값 = 카카오
    // 실제로는 DataStore 등에서 마지막 로그인 방식을 읽어와서 결정
    @Provides
    @Singleton
    fun provideAuthDataSource(
        @KakaoAuth kakaoAuthDataSource: AuthDataSource
    ): AuthDataSource = kakaoAuthDataSource
}