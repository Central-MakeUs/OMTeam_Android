package com.omteam.data.fcm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.omteam.datastore.PermissionDataStore
import com.omteam.domain.usecase.DeleteFcmTokenUseCase
import com.omteam.domain.usecase.RegisterFcmTokenUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@Singleton
class FcmTokenSyncManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val deleteFcmTokenUseCase: DeleteFcmTokenUseCase,
    private val permissionDataStore: PermissionDataStore
) {

    suspend fun syncCurrentToken(source: String, force: Boolean = false) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            syncToken(token = token, source = source, force = force)
        } catch (e: Exception) {
            Timber.e("## [$source] FCM 토큰 조회 실패 : ${e.message}")
        }
    }

    suspend fun syncToken(token: String, source: String, force: Boolean = false) {
        if (!isPushNotificationEnabled()) {
            Timber.d("## [FcmTokenSyncManager] $source FCM 토큰 등록 API 호출 생략 - 알림 권한 비활성화")
            unregisterTokenIfNeeded(source = source)
            return
        }

        if (!force && shouldSkipRegister(token)) {
            Timber.d("## [FcmTokenSyncManager] $source FCM 토큰 등록 API 호출 생략 - 이미 같은 토큰이 등록됨")
            return
        }

        registerFcmTokenUseCase(token).collect { result ->
            result.onSuccess { message ->
                Timber.d("## [FcmTokenSyncManager] $source FCM 토큰 등록 API 호출 성공 : $message")
                permissionDataStore.saveFcmTokenRegistered(true)
                permissionDataStore.saveLastRegisteredFcmToken(token)
            }.onFailure { error ->
                Timber.e("## [FcmTokenSyncManager] $source FCM 토큰 등록 API 호출 실패 : ${error.message}")
            }
        }
    }

    private suspend fun shouldSkipRegister(token: String): Boolean {
        val isRegistered = permissionDataStore.isFcmTokenRegistered().first()
        if (!isRegistered) {
            return false
        }

        val lastRegisteredToken = permissionDataStore.getLastRegisteredFcmToken().first()
        return lastRegisteredToken == token
    }

    private suspend fun unregisterTokenIfNeeded(source: String) {
        val isRegistered = permissionDataStore.isFcmTokenRegistered().first()
        if (!isRegistered) {
            permissionDataStore.saveLastRegisteredFcmToken("")
            return
        }

        deleteFcmTokenUseCase().collect { result ->
            result.onSuccess { message ->
                Timber.d("## [FcmTokenSyncManager] $source FCM 토큰 삭제 API 호출 성공 : $message")
                permissionDataStore.saveFcmTokenRegistered(false)
                permissionDataStore.saveLastRegisteredFcmToken("")
            }.onFailure { error ->
                Timber.e("## [FcmTokenSyncManager] $source FCM 토큰 삭제 API 호출 실패 : ${error.message}")
            }
        }
    }

    private fun isPushNotificationEnabled(): Boolean {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}