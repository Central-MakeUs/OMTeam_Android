package com.omteam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")

/**
 * 토큰 저장, 관리하는 dataStore
 *
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 */
@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.tokenDataStore

    /**
     * accessToken, refreshToken 저장
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    /**
     * accessToken 조회
     */
    fun getAccessToken(): Flow<String?> =
        dataStore.data.map { preferences -> preferences[ACCESS_TOKEN_KEY] }

    /**
     * refreshToken 조회
     */
    fun getRefreshToken(): Flow<String?> =
        dataStore.data.map { preferences -> preferences[REFRESH_TOKEN_KEY] }

    /**
     * 저장된 모든 토큰 삭제
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}