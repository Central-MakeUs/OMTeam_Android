package com.omteam.omt

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.omteam.domain.usecase.RegisterFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OmtFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var registerFcmTokenUseCase: RegisterFcmTokenUseCase

    // 서비스 생명주기에 맞는 독립 CoroutineScope
    // SupervisorJob으로 만들어서 자식 실패가 전파되지 않게
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * FCM 토큰이 새로 발급되거나 갱신될 때 호출
     *
     * 앱 설치 직후, 토큰 만료, 기기 복원 등 다양한 상황에서 호출되고
     *
     * 로그인 여부와 상관없이 항상 서버에 최신 토큰 등록 시도
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("## [FCM] 토큰 갱신됨 : $token")

        serviceScope.launch {
            registerFcmTokenUseCase(token).collect { result ->
                result.onSuccess { message ->
                    Timber.d("## [FCM] 갱신된 토큰 서버 등록 성공 : $message")
                }.onFailure { error ->
                    // 로그인 전이거나 네트워크 오류일 수 있음 — 다음 로그인/자동로그인 시 재등록됨
                    Timber.w("## [FCM] 갱신된 토큰 서버 등록 실패 (무시됨) : ${error.message}")
                }
            }
        }
    }

    /**
     * 앱이 포그라운드 상태일 때 FCM 메시지 수신 시 호출
     *
     * notification payload가 있어도 포그라운드에서는 자동 표시되지 않아서
     *
     * 직접 NotificationCompat으로 알림을 빌드해야 함
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("## [FCM] 메시지 수신 - from : ${message.from}")

        if (message.data.isNotEmpty()) {
            Timber.d("## [FCM] data 페이로드 : ${message.data}")
        }

        // notification 페이로드가 있을 때 포그라운드 알림 표시
        message.notification?.let { notification ->
            Timber.d("## [FCM] notification 페이로드 : title = ${notification.title}, body=${notification.body}")
            showNotification(
                title = notification.title ?: getString(R.string.app_name),
                body = notification.body ?: ""
            )
        }
    }

    /**
     * 포그라운드 상태에서 알림을 직접 빌드해 표시
     *
     * @param title 알림 제목
     * @param body  알림 본문
     */
    private fun showNotification(title: String, body: String) {
        // 알림 탭하면 메인 액티비티 이동
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // 탭하면 알림 자동 제거
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 서비스가 종료될 때 CoroutineScope도 같이 취소
        serviceScope.cancel()
    }

    companion object {
        // App 클래스에서 생성하는 알림 채널 ID와 반드시 일치해야 함
        const val NOTIFICATION_CHANNEL_ID = "omt_push_channel"
    }
}