package com.omteam.omt

import androidx.compose.runtime.MutableState
import androidx.navigation3.runtime.NavKey
import com.omteam.api.*

class AppNavigator(
    private val backStackState: MutableState<List<NavKey>>
) {
    private val backStack: List<NavKey>
        get() = backStackState.value

    fun navigate(destination: NavKey, popUpTo: NavKey? = null, inclusive: Boolean = false) {
        backStackState.value = if (popUpTo != null) {
            val index = backStack.indexOfLast { it == popUpTo }
            val newStack = if (index >= 0) {
                if (inclusive) {
                    backStack.subList(0, index)
                } else {
                    backStack.subList(0, index + 1)
                }
            } else {
                backStack
            }
            newStack + destination
        } else {
            backStack + destination
        }
    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStackState.value = backStack.dropLast(1)
        }
    }

    fun navigateToAccountLinkComplete() =
        navigate(
            destination = AccountLinkCompleteNavKey,
            popUpTo = LoginNavKey,
            inclusive = false
        )

    fun navigateToOnboarding() =
        navigate(
            destination = OnboardingNavKey(step = 1),
            popUpTo = AccountLinkCompleteNavKey,
            inclusive = true,
        )
    
    /**
     * 로그인 스택 제거 후 메인 화면 이동
     */
    fun navigateToMain() {
        backStackState.value = listOf(MainNavKey)
    }
    
    /**
     * 로그아웃 시 모든 스택 제거 후 로그인 화면 이동
     */
    fun navigateToLogin() {
        backStackState.value = listOf(LoginNavKey)
    }
    
    /**
     * 기타 화면 이동
     */
    fun navigateToOther() = navigate(destination = OtherNavKey)
    
    /**
     * 나의 목표 수정하기 화면 이동
     */
    fun navigateToEditMyGoal() = navigate(destination = EditMyGoalNavKey)
    
    /**
     * 내 정보 수정하기 화면 이동
     */
    fun navigateToEditMyInfo() = navigate(destination = EditMyInfoNavKey)
    
    /**
     * 운동 가능 시간 수정 화면 이동
     */
    fun navigateToEditExerciseTime() = navigate(destination = EditExerciseTimeNavKey)
    
    /**
     * 미션 투자 시간 수정 화면 이동
     */
    fun navigateToEditMissionTime() = navigate(destination = EditMissionTimeNavKey)
    
    /**
     * 선호 운동 수정 화면 이동
     */
    fun navigateToEditFavoriteExercise() = navigate(destination = EditFavoriteExerciseNavKey)
    
    /**
     * 생활 패턴 수정 화면 이동
     */
    fun navigateToEditPattern() = navigate(destination = EditPatternNavKey)
    
    /**
     * 웹뷰 화면 이동
     * 
     * @param url 표시할 웹페이지 URL
     */
    fun navigateToWebView(url: String) = navigate(destination = WebViewNavKey(url = url))
}