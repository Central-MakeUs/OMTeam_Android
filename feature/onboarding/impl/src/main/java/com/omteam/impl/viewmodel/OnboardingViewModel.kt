package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.model.LifestyleType
import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.model.WorkTimeType
import com.omteam.domain.repository.AuthRepository
import com.omteam.impl.model.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    // 닉네임 에러 타입 관리
    private val _nicknameErrorType = MutableStateFlow(NicknameErrorType.NONE)
    val nicknameErrorType: StateFlow<NicknameErrorType> = _nicknameErrorType.asStateFlow()

    // 온보딩 제출 상태 관리
    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState.asStateFlow()

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

    fun updatePushPermission(granted: Boolean) {
        _onboardingData.update { currentData ->
            currentData.copy(pushPermissionGranted = granted)
        }
    }

    fun clearOnboardingData() {
        _onboardingData.value = OnboardingData()
    }

    /**
     * 온보딩 정보 제출
     */
    fun submitOnboarding() {
        viewModelScope.launch {
            _submitState.value = SubmitState.Loading

            try {
                val data = _onboardingData.value

                // OnboardingData를 OnboardingInfo로 변환
                val onboardingInfo = OnboardingInfo(
                    nickname = data.nickname,
                    appGoalText = data.goal,
                    workTimeType = parseWorkTimeType(data.time),
                    availableStartTime = parseStartTime(data.time),
                    availableEndTime = parseEndTime(data.time),
                    minExerciseMinutes = parseMissionTime(data.missionTime),
                    preferredExerciseText = data.favoriteExercise,
                    lifestyleType = parseLifestyleType(data.pattern),
                    remindEnabled = data.pushPermissionGranted,
                    checkinEnabled = data.pushPermissionGranted,
                    reviewEnabled = data.pushPermissionGranted
                )

                val result = authRepository.submitOnboarding(onboardingInfo)

                result.onSuccess {
                    Timber.d("## 온보딩 정보 제출 성공")
                    _submitState.value = SubmitState.Success
                }.onFailure { exception ->
                    Timber.e("## 온보딩 정보 제출 실패: ${exception.message}")
                    _submitState.value = SubmitState.Error(exception.message ?: "온보딩 정보 제출 실패")
                }
            } catch (e: Exception) {
                Timber.e(e, "## 온보딩 정보 제출 예외 발생")
                _submitState.value = SubmitState.Error(e.message ?: "온보딩 정보 제출 실패")
            }
        }
    }

    /**
     * 근무 시간 타입 파싱
     */
    private fun parseWorkTimeType(time: String): WorkTimeType =
        when {
            time.contains("고정") -> WorkTimeType.FIXED
            time.contains("교대") || time.contains("스케줄") -> WorkTimeType.SHIFT
            else -> WorkTimeType.FIXED
        }

    /**
     * 운동 가능 시작 시간 파싱 (HH:mm)
     *
     * api 요청값 스펙 정해지면 구현
     */
    private fun parseStartTime(time: String): String {
        // TODO: 실제 시간 파싱 로직 구현
        return "18:30"
    }

    /**
     * 운동 가능 종료 시간 파싱 (HH:mm)
     */
    private fun parseEndTime(time: String): String {
        // TODO: 실제 시간 파싱 로직 구현
        return "22:00"
    }

    /**
     * 미션 시간을 분 단위로 파싱
     */
    private fun parseMissionTime(missionTime: String): Int {
        // 예: "30분" -> 30
        val regex = "(\\d+)".toRegex()
        val matchResult = regex.find(missionTime)
        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    /**
     * 생활 패턴 타입 파싱
     */
    private fun parseLifestyleType(pattern: String): LifestyleType =
        when {
            pattern.contains("규칙적") && pattern.contains("주간") -> LifestyleType.REGULAR_DAYTIME
            pattern.contains("야근") || pattern.contains("불규칙") -> LifestyleType.IRREGULAR_OVERTIME
            pattern.contains("교대") || pattern.contains("밤샘") -> LifestyleType.SHIFT_NIGHT
            pattern.contains("매일") && pattern.contains("달라") -> LifestyleType.VARIABLE_DAILY
            else -> LifestyleType.REGULAR_DAYTIME
        }

    /**
     * 제출 상태 초기화
     */
    fun resetSubmitState() {
        _submitState.value = SubmitState.Idle
    }

    // API 스펙 정해지면 수정
    fun getOnboardingData(): OnboardingData = _onboardingData.value
}

/**
 * 온보딩 제출 상태
 */
sealed class SubmitState {
    object Idle : SubmitState()
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}