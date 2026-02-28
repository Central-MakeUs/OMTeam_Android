package com.omteam.data.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.omteam.datastore.PermissionDataStore
import com.omteam.domain.usecase.RegisterFcmTokenUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@Singleton
class FcmTokenSyncManager @Inject constructor(
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
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
}