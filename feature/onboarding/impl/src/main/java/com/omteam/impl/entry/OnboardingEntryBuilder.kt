package com.omteam.impl.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omteam.api.OnboardingNavKey
import com.omteam.impl.screen.OnboardingScreen
import timber.log.Timber

/**
 * 온보딩 화면 entry builder
 *
 * @param onNavigateToNextStep 다음 단계 이동
 * @param onNavigateToMain 메인 화면 이동 (온보딩 완료)
 */
fun EntryProviderScope<NavKey>.onboardingEntry(
    onNavigateToNextStep: (currentStep: Int) -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    entry<OnboardingNavKey> { navKey ->
        Timber.e("## 온보딩 화면 - Step ${navKey.step}")

        OnboardingScreen(
            currentStep = navKey.step,
            onNext = {
                if (navKey.step < 7) {
                    // 다음 단계로
                    onNavigateToNextStep(navKey.step)
                } else {
                    // 마지막 단계 완료 → 메인 화면
                    onNavigateToMain()
                }
            },
            onSkip = {
                // 건너뛰기 → 메인 화면
                onNavigateToMain()
            },
            onBack = {
                // 이전 온보딩 화면 이동
                onNavigateBack()
            },
            onNavigateToMain = onNavigateToMain
        )
    }
}