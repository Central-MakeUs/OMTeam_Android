package com.omteam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.permissionDataStore: DataStore<Preferences> by preferencesDataStore(name = "permission_preferences")

/**
 * 권한 관련 데이터 저장 DataStore
 *
 * 푸시 알림 권한 거절 횟수 저장
 */
@Singleton
class PermissionDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val dataStore = context.permissionDataStore

    /**
     * 푸시 알림 권한 거절 횟수 저장
     */
    suspend fun savePushPermissionDenialCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[PUSH_PERMISSION_DENIAL_COUNT] = count
        }
    }

    /**
     * 푸시 알림 권한 거절 횟수 조회
     */
    fun getPushPermissionDenialCount(): Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[PUSH_PERMISSION_DENIAL_COUNT] ?: 0
        }

    /**
     * 푸시 알림 권한 거절 횟수 증가
     */
    suspend fun incrementPushPermissionDenialCount() {
        val currentCount = getPushPermissionDenialCount().first()
        savePushPermissionDenialCount(currentCount + 1)
    }

    /**
     * 푸시 알림 권한 거절 횟수 초기화
     */
    suspend fun resetPushPermissionDenialCount() {
        dataStore.edit { preferences ->
            preferences.remove(PUSH_PERMISSION_DENIAL_COUNT)
        }
    }

    /**
     * FCM 토큰 서버 등록 여부 저장
     *
     * 앱 재시작 후 알림 권한 취소 감지 시 불필요한 API 호출 방지에 사용
     */
    suspend fun saveFcmTokenRegistered(isRegistered: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FCM_TOKEN_REGISTERED] = isRegistered
        }
    }

    /**
     * FCM 토큰 서버 등록 여부 조회
     */
    fun isFcmTokenRegistered(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[IS_FCM_TOKEN_REGISTERED] ?: false
        }

    /**
     * 마지막으로 서버 등록에 성공한 FCM 토큰 저장
     */
    suspend fun saveLastRegisteredFcmToken(token: String) =
        dataStore.edit { preferences ->
            preferences[LAST_REGISTERED_FCM_TOKEN] = token
        }

    /**
     * 마지막으로 서버 등록에 성공한 FCM 토큰 조회
     */
    fun getLastRegisteredFcmToken(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[LAST_REGISTERED_FCM_TOKEN] ?: ""
        }

    companion object {
        private val PUSH_PERMISSION_DENIAL_COUNT = intPreferencesKey("push_permission_denial_count")
        private val IS_FCM_TOKEN_REGISTERED = booleanPreferencesKey("is_fcm_token_registered")
        private val LAST_REGISTERED_FCM_TOKEN = stringPreferencesKey("last_registered_fcm_token")
    }
}