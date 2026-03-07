package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.omteam.datastore.PermissionDataStore
import com.omteam.domain.repository.CustomExerciseRepository
import com.omteam.domain.usecase.DeleteFcmTokenUseCase
import com.omteam.domain.usecase.GetOnboardingInfoUseCase
import com.omteam.domain.usecase.RegisterFcmTokenUseCase
import com.omteam.domain.usecase.UpdateAvailableTimeUseCase
import com.omteam.domain.usecase.UpdateLifestyleUseCase
import com.omteam.domain.usecase.UpdateMinExerciseMinutesUseCase
import com.omteam.domain.usecase.UpdateAppGoalUseCase
import com.omteam.domain.usecase.UpdateNicknameUseCase
import com.omteam.domain.usecase.UpdatePreferredExerciseUseCase
import com.omteam.domain.usecase.WithdrawUseCase
import com.omteam.impl.viewmodel.state.FcmTokenState
import com.omteam.impl.viewmodel.state.MyPageOnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    private val updateAppGoalUseCase: UpdateAppGoalUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val deleteFcmTokenUseCase: DeleteFcmTokenUseCase,
    private val permissionDataStore: PermissionDataStore,
) : ViewModel() {

    private val _onboardingInfoState =
        MutableStateFlow<MyPageOnboardingState>(MyPageOnboardingState.Idle)
    val onboardingInfoState: StateFlow<MyPageOnboardingState> = _onboardingInfoState.asStateFlow()

    private val _fcmTokenState = MutableStateFlow<FcmTokenState>(FcmTokenState.Idle)
    val fcmTokenState: StateFlow<FcmTokenState> = _fcmTokenState.asStateFlow()

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

    // 앱 사용 목적 수정
    fun updateAppGoal(appGoalText: String) = viewModelScope.launch {
        _onboardingInfoState.value = MyPageOnboardingState.Loading

        updateAppGoalUseCase.invoke(appGoalText).collect { result ->
            result.onSuccess { onboardingInfo ->
                Timber.d("## 앱 사용 목적 수정 성공 : $onboardingInfo")
                _onboardingInfoState.value = MyPageOnboardingState.UpdateSuccess(onboardingInfo)
            }.onFailure { e ->
                Timber.e("## 앱 사용 목적 수정 실패 : ${e.message}")
                _onboardingInfoState.value = MyPageOnboardingState.Error(e.message ?: "알 수 없는 오류")
            }
        }
    }
    
    /**
     * one-shot 업데이트 결과 상태를 소비 처리해서
     *
     * 화면 재진입 시 같은 성공 or 에러 이벤트가 재실행되지 않게 함
     */
    fun consumeOnboardingUpdateState() {
        when (val state = _onboardingInfoState.value) {
            is MyPageOnboardingState.UpdateSuccess -> {
                _onboardingInfoState.value = MyPageOnboardingState.Success(state.data)
            }
            is MyPageOnboardingState.Error -> {
                _onboardingInfoState.value = MyPageOnboardingState.Idle
            }
            else -> Unit
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

        // 회원탈퇴 전 FCM 토큰 삭제
        deleteFcmToken()

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

    /**
     * FCM 토큰 등록
     *
     * Firebase에서 현재 기기의 FCM 토큰을 가져와 서버에 등록.
     * 성공 시 DataStore에 등록 완료 플래그를 저장하여 앱 재시작 후 상태 추적에 사용.
     */
    fun registerFcmToken() = viewModelScope.launch {
        _fcmTokenState.value = FcmTokenState.Loading
        try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            Timber.d("## FCM 토큰 : $fcmToken")

            registerFcmTokenUseCase(fcmToken).collect { result ->
                result.onSuccess { message ->
                    Timber.d("## FCM 토큰 등록 성공 : $message")
                    permissionDataStore.saveFcmTokenRegistered(true)
                    _fcmTokenState.value = FcmTokenState.RegisterSuccess(message)
                }.onFailure { e ->
                    Timber.e("## FCM 토큰 등록 실패 : ${e.message}")
                    _fcmTokenState.value = FcmTokenState.Error(e.message ?: "FCM 토큰 등록 실패")
                }
            }
        } catch (e: Exception) {
            Timber.e("## FCM 토큰 등록 중 예외 발생 : ${e.message}")
            _fcmTokenState.value = FcmTokenState.Error(e.message ?: "FCM 토큰 등록 중 오류 발생")
        }
    }

    /**
     * FCM 토큰 삭제
     *
     * 알림 권한 취소, 회원탈퇴 전 서버에서 FCM 토큰을 제거
     *
     * 성공 시 DataStore의 등록 완료 플래그를 해제
     */
    fun deleteFcmToken() = viewModelScope.launch {
        _fcmTokenState.value = FcmTokenState.Loading

        deleteFcmTokenUseCase().collect { result ->
            result.onSuccess { message ->
                Timber.d("## FCM 토큰 삭제 성공 : $message")
                permissionDataStore.saveFcmTokenRegistered(false)
                permissionDataStore.saveLastRegisteredFcmToken("")
                _fcmTokenState.value = FcmTokenState.DeleteSuccess(message)
            }.onFailure { e ->
                Timber.e("## FCM 토큰 삭제 실패 : ${e.message}")
                _fcmTokenState.value = FcmTokenState.Error(e.message ?: "FCM 토큰 삭제 실패")
            }
        }
    }

    /**
     * 앱 재시작 후 알림 권한 취소 감지 시 FCM 토큰 삭제
     *
     * 알림 설정 화면에서 권한을 끄면 앱이 재시작돼서 OnResume으로 감지할 수 없음
     *
     * 대신 MyPageScreen 진입 시 푸시 알림 권한이 없는 상태면서 DataStore에 FCM이 등록된 상태면 삭제 처리
     *
     * @param isPermissionGranted 현재 푸시 알림 권한 허가 여부
     */
    fun deleteFcmTokenIfRegistered(isPermissionGranted: Boolean) = viewModelScope.launch {
        if (!isPermissionGranted) {
            val isRegistered = permissionDataStore.isFcmTokenRegistered().first()
            if (isRegistered) {
                Timber.d("## 앱 재시작 후 알림 권한 없음 감지 → FCM 토큰 삭제")
                deleteFcmToken()
            }
        }
    }

}