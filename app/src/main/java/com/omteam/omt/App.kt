package com.omteam.omt

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // 릴리즈 모드에서 크래시틱스 수집이 비활성화되지 않게 명시적으로 활성화
        FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = true
            setCustomKey("build_type", BuildConfig.BUILD_TYPE)
            setCustomKey("version_name", BuildConfig.VERSION_NAME)
            setCustomKey("version_code", BuildConfig.VERSION_CODE)
        }

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        // 안드 8 이상에선 알림 채널을 생성해야 함
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            OmtFirebaseMessagingService.NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.notification_channel_description)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}