package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.repository.CustomExerciseRepository
import com.omteam.domain.usecase.GetOnboardingInfoUseCase
import com.omteam.domain.usecase.UpdateAvailableTimeUseCase
import com.omteam.domain.usecase.UpdateLifestyleUseCase
import com.omteam.domain.usecase.UpdateMinExerciseMinutesUseCase
import com.omteam.domain.usecase.UpdateNicknameUseCase
import com.omteam.domain.usecase.UpdatePreferredExerciseUseCase
import com.omteam.domain.usecase.WithdrawUseCase
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getOnboardingInfoUseCase: GetOnboardingInfoUseCase,
    private val updateLifestyleUseCase: UpdateLifestyleUseCase,
    private val updatePreferredExerciseUseCase: UpdatePreferredExerciseUseCase,
    private val updateMinExerciseMinutesUseCase: UpdateMinExerciseMinutesUseCase,
    private val updateAvailableTimeUseCase: UpdateAvailableTimeUseCase,
    private val updateNicknameUseCase: UpdateNicknameUseCase,
    private val customExerciseRepository: CustomExerciseRepository,
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {

    private val _onboardingInfoState =
        MutableStateFlow<MyPageOnboardingState>(MyPageOnboardingState.Idle)
    val onboardingInfoState: StateFlow<MyPageOnboardingState> = _onboardingInfoState.asStateFlow()

    val customExercises: StateFlow<List<String>> = customExerciseRepository.getAllCustomExercises()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 온보딩 정보 조회
    fun getOnboardingInfo() = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        getOnboardingInfoUseCase.invoke().collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 온보딩 정보 조회 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.Success(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 온보딩 정보 조회 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    // 평소 생활 패턴 수정
    fun updateLifestyle(lifestyleType: String) = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        updateLifestyleUseCase.invoke(lifestyleType).collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 평소 생활 패턴 수정 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.UpdateSuccess(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 평소 생활 패턴 수정 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    // 선호 운동 수정
    fun updatePreferredExercise(preferredExercises: List<String>) = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        updatePreferredExerciseUseCase.invoke(preferredExercises).collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 선호 운동 수정 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.UpdateSuccess(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 선호 운동 수정 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    // 미션에 투자할 수 있는 시간 수정
    fun updateMinExerciseMinutes(minExerciseMinutes: Int) = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        updateMinExerciseMinutesUseCase.invoke(minExerciseMinutes).collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 미션에 투자할 수 있는 시간 수정 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.UpdateSuccess(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 미션에 투자할 수 있는 시간 수정 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    // 운동 가능 시간 수정
    fun updateAvailableTime(availableStartTime: String, availableEndTime: String) =
        viewModelScope.launch {
            _onboardingInfoState.value = MyPageOnboardingState.Loading

            updateAvailableTimeUseCase.invoke(availableStartTime, availableEndTime)
                .collect { result ->
                    result.onSuccess { onboardingInfo ->
                        Timber.d("## 운동 가능 시간 수정 성공 : $onboardingInfo")
                        _onboardingInfoState.value =
                            MyPageOnboardingState.UpdateSuccess(onboardingInfo)
                    }.onFailure { e ->
                        Timber.e("## 운동 가능 시간 수정 실패 : ${e.message}")
                        _onboardingInfoState.value =
                            MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
                    }
                }
        }

    // 닉네임 변경
    fun updateNickname(nickname: String) = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        updateNicknameUseCase.invoke(nickname).collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 닉네임 변경 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.UpdateSuccess(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 닉네임 변경 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }

    // 커스텀 운동 추가
    fun addCustomExercise(name: String) = viewModelScope.launch {
        try {
            customExerciseRepository.addCustomExercise(name)
            Timber.d("## 커스텀 운동 추가 성공 : $name")
        } catch (e: Exception) {
            Timber.e("## 커스텀 운동 추가 실패 : ${e.message}")
        }
    }

    // 커스텀 운동 삭제
    fun deleteCustomExercise(name: String) = viewModelScope.launch {
        try {
            customExerciseRepository.deleteCustomExercise(name)
            Timber.d("## 커스텀 운동 삭제 성공 : $name")
        } catch (e: Exception) {
            Timber.e("## 커스텀 운동 삭제 실패 : ${e.message}")
        }
    }

    // 회원탈퇴
    fun withdraw() = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        withdrawUseCase.invoke()
            .onSuccess { message ->
                Timber.d("## 회원탈퇴 성공 : $message")
                _onboardingInfoState.value = MyPageOnboardingState.WithdrawSuccess
            }.onFailure { e ->
                Timber.e("## 회원탈퇴 실패 : ${e.message}")
                _onboardingInfoState.value =
                    MyPageOnboardingState.Error(e.message ?: "회원탈퇴 중 오류가 발생했습니다.")
            }
    }

}