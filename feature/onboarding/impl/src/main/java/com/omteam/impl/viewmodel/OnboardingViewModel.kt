package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import com.omteam.impl.model.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

enum class NicknameErrorType {
    // 에러 없음
    NONE,
    // 글자수 초과
    LENGTH_EXCEED,
    // 특수문자 사용
    SPECIAL_CHAR,
}

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    // 닉네임 에러 타입 관리
    private val _nicknameErrorType = MutableStateFlow(NicknameErrorType.NONE)
    val nicknameErrorType: StateFlow<NicknameErrorType> = _nicknameErrorType.asStateFlow()

    fun updateNickname(nickname: String) {
        _onboardingData.update { currentData ->
            currentData.copy(nickname = nickname)
        }
        // 닉네임 유효성 검증
        _nicknameErrorType.value = validateNickname(nickname)
    }

    /**
     * 닉네임 유효성 검증
     *
     * - 8자 이하
     * - 한글(완성형 + 자모음), 영어 대소문자, 숫자만 허용
     * @return 에러 타입
     */
    private fun validateNickname(nickname: String): NicknameErrorType {
        if (nickname.isEmpty()) return NicknameErrorType.NONE
        
        if (nickname.length > 8) return NicknameErrorType.LENGTH_EXCEED
        
        // 한글 자모음(ㄱ-ㅎ, ㅏ-ㅣ), 완성형 한글(가-힣), 영어 대소문자(a-zA-Z), 숫자(0-9)
        if (!nickname.matches(Regex("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]*$"))) {
            return NicknameErrorType.SPECIAL_CHAR
        }
        
        return NicknameErrorType.NONE
    }

    fun updateGoal(goal: String) {
        _onboardingData.update { currentData ->
            currentData.copy(goal = goal)
        }
    }

    fun updateTime(time: String) {
        _onboardingData.update { currentData ->
            currentData.copy(time = time)
        }
    }

    fun updateMissionTime(missionTime: String) {
        _onboardingData.update { currentData ->
            currentData.copy(missionTime = missionTime)
        }
    }

    fun updateFavoriteExercise(favoriteExercise: String) {
        _onboardingData.update { currentData ->
            currentData.copy(favoriteExercise = favoriteExercise)
        }
    }

    fun updatePattern(pattern: String) {
        _onboardingData.update { currentData ->
            currentData.copy(pattern = pattern)
        }
    }

    fun clearOnboardingData() {
        _onboardingData.value = OnboardingData()
    }

    // API 스펙 정해지면 수정
    fun getOnboardingData(): OnboardingData = _onboardingData.value
}