package com.omteam.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    companion object {
        private val PUSH_PERMISSION_DENIAL_COUNT = intPreferencesKey("push_permission_denial_count")
    }
}