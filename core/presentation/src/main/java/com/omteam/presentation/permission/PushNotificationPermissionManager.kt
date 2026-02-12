package com.omteam.presentation.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.omteam.datastore.PermissionDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 푸시 알림 권한 상태
 */
enum class PushPermissionStatus {
    /** 권한 허용됨 */
    GRANTED,

    /** 권한 거부됨 (아직 2회 미만) */
    DENIED,

    /** 영구적으로 거부됨 (2회 이상) - 설정에서만 변경 가능 */
    PERMANENTLY_DENIED
}

/**
 * 푸시 알림 권한 매니저
 *
 * Android 13+에서 POST_NOTIFICATIONS 런타임 권한 관리
 *
 * 거절 횟수 추적해서 2회 이상 거절 시 시스템 설정 화면 이동
 *
 * @param context 애플리케이션 컨텍스트
 * @param permissionDataStore 권한 데이터 저장소
 */
class PushNotificationPermissionManager(
    private val context: Context,
    private val permissionDataStore: PermissionDataStore? = null
) {

    /**
     * 현재 권한 상태 확인
     */
    suspend fun getPermissionStatus(): PushPermissionStatus {
        // Android 13 미만은 런타임 권한 필요 없음
        // minSdk 28이므로 항상 안드 8(오레오) 이상
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                PushPermissionStatus.GRANTED
            } else {
                PushPermissionStatus.DENIED
            }
        }

        val permission = Manifest.permission.POST_NOTIFICATIONS
        return when {
            ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED -> {
                PushPermissionStatus.GRANTED
            }

            isPermanentlyDenied() -> PushPermissionStatus.PERMANENTLY_DENIED

            else -> PushPermissionStatus.DENIED
        }
    }

    /**
     * 권한 요청이 가능한지 확인 (2회 미만 거절)
     */
    suspend fun canRequestPermission(): Boolean = (getPermissionStatus() == PushPermissionStatus.DENIED)

    /**
     * 시스템 설정 화면 이동
     */
    fun openNotificationSettings() {
        val intent = Intent().apply {
            // minSdk 28이라서 항상 안드 8(오레오) 이상
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 거절 횟수 증가
     */
    suspend fun incrementDenialCount() = permissionDataStore?.incrementPushPermissionDenialCount()

    /**
     * 거절 횟수 초기화 (권한 허용 시 호출)
     */
    suspend fun resetDenialCount() {
        permissionDataStore?.resetPushPermissionDenialCount()
    }

    /**
     * 영구적으로 거부되었는지 확인 (2회 이상)
     */
    private suspend fun isPermanentlyDenied(): Boolean {
        val denialCount = permissionDataStore?.getPushPermissionDenialCount()?.first() ?: 0
        return denialCount >= 2
    }
}

/**
 * Composable에서 푸시 알림 권한 요청하기 위한 유틸 함수
 *
 * @param permissionDataStore 권한 데이터 저장소
 * @param onResult 권한 요청 결과 콜백 (granted: Boolean, isPermanentlyDenied: Boolean)
 * @return Pair of (PermissionManager, requestPermission function)
 */
@Suppress("MemberVisibilityCanBePrivate")
@Composable
fun rememberPushPermissionLauncher(
    permissionDataStore: PermissionDataStore? = null,
    onResult: (granted: Boolean, isPermanentlyDenied: Boolean) -> Unit
): Pair<PushNotificationPermissionManager, () -> Unit> {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val manager = remember { PushNotificationPermissionManager(context, permissionDataStore) }
    var denialCount by remember { mutableIntStateOf(0) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        scope.launch {
            if (isGranted) {
                manager.resetDenialCount()
                onResult(true, false)
            } else {
                manager.incrementDenialCount()
                denialCount += 1
                onResult(false, denialCount >= 2)
            }
        }
    }

    val requestPermission: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            launcher.launch(permission)
        } else {
            // 안드 13 미만은 자동 허용
            scope.launch {
                val isEnabled =
                    NotificationManagerCompat.from(context).areNotificationsEnabled()
                onResult(isEnabled, false)
            }
        }
    }

    return manager to requestPermission
}