package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.repository.MissionRepository
import com.omteam.domain.usecase.GetCharacterInfoUseCase
import com.omteam.domain.usecase.GetDailyRecommendedMissionsUseCase
import com.omteam.domain.usecase.RequestDailyMissionRecommendationsUseCase
import com.omteam.domain.usecase.StartMissionUseCase
import com.omteam.impl.viewmodel.enum.AppleStatus
import com.omteam.impl.viewmodel.state.CharacterUiState
import com.omteam.impl.viewmodel.state.DailyAppleData
import com.omteam.impl.viewmodel.state.DailyMissionRecommendationUiState
import com.omteam.impl.viewmodel.state.DailyMissionUiState
import com.omteam.impl.viewmodel.state.RecommendedMissionsUiState
import com.omteam.impl.viewmodel.state.StartMissionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/**
 * [com.omteam.impl.tab.HomeScreen] 뷰모델
 * 
 * 일일 미션, 캐릭터 정보, 주간 사과 데이터 관리
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val getCharacterInfoUseCase: GetCharacterInfoUseCase,
    private val getDailyRecommendedMissionsUseCase: GetDailyRecommendedMissionsUseCase,
    private val requestDailyMissionRecommendationsUseCase: RequestDailyMissionRecommendationsUseCase,
    private val startMissionUseCase: StartMissionUseCase
) : ViewModel() {

    // 일일 미션 UI State
    private val _dailyMissionUiState = MutableStateFlow<DailyMissionUiState>(DailyMissionUiState.Idle)
    val dailyMissionUiState: StateFlow<DailyMissionUiState> = _dailyMissionUiState.asStateFlow()
    
    // 캐릭터 정보 UI State
    private val _characterUiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Idle)
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()
    
    // 추천 미션 목록 UI State
    private val _recommendedMissionsUiState = MutableStateFlow<RecommendedMissionsUiState>(RecommendedMissionsUiState.Idle)
    val recommendedMissionsUiState: StateFlow<RecommendedMissionsUiState> = _recommendedMissionsUiState.asStateFlow()
    
    // 데일리 미션 추천 받기 UI State
    private val _dailyMissionRecommendationUiState = MutableStateFlow<DailyMissionRecommendationUiState>(DailyMissionRecommendationUiState.Idle)
    val dailyMissionRecommendationUiState: StateFlow<DailyMissionRecommendationUiState> = _dailyMissionRecommendationUiState.asStateFlow()
    
    // 미션 시작하기 UI State
    private val _startMissionUiState = MutableStateFlow<StartMissionUiState>(StartMissionUiState.Idle)
    val startMissionUiState: StateFlow<StartMissionUiState> = _startMissionUiState.asStateFlow()

    /**
     * 일일 미션 상태 조회
     */
    fun fetchDailyMissionStatus() = viewModelScope.launch {
        _dailyMissionUiState.value = DailyMissionUiState.Loading

        missionRepository.getDailyMissionStatus()
            .collect { result ->
                _dailyMissionUiState.value = result.fold(
                    onSuccess = { status ->
                        Timber.d("## 일일 미션 상태 조회 성공 : $status")
                        when {
                            status.currentMission != null || status.missionResult != null -> {
                                // 진행 중 or 완료된 미션 있음
                                DailyMissionUiState.Success(status)
                            }
                            else -> {
                                // 미션 데이터 없음 (아직 생성되지 않음)
                                Timber.d("## 미션 데이터 없음 - Idle 상태 유지")
                                DailyMissionUiState.Idle
                            }
                        }
                    },
                    onFailure = { error ->
                        Timber.e("## 일일 미션 상태 조회 실패 : ${error.message}")
                        DailyMissionUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
    
    /**
     * 캐릭터 정보 조회
     */
    fun fetchCharacterInfo() = viewModelScope.launch {
        _characterUiState.value = CharacterUiState.Loading

        getCharacterInfoUseCase()
            .collect { result ->
                _characterUiState.value = result.fold(
                    onSuccess = { characterInfo ->
                        Timber.d("## 캐릭터 정보 조회 성공 : $characterInfo")
                        CharacterUiState.Success(characterInfo)
                    },
                    onFailure = { error ->
                        Timber.e("## 캐릭터 정보 조회 실패 : ${error.message}")
                        CharacterUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
    
    /**
     * 오늘의 추천 미션 목록 조회
     */
    fun fetchRecommendedMissions() = viewModelScope.launch {
        _recommendedMissionsUiState.value = RecommendedMissionsUiState.Loading
        
        getDailyRecommendedMissionsUseCase()
            .collect { result ->
                _recommendedMissionsUiState.value = result.fold(
                    onSuccess = { missions ->
                        Timber.d("## 추천 미션 목록 조회 성공 : $missions")
                        RecommendedMissionsUiState.Success(missions)
                    },
                    onFailure = { error ->
                        Timber.e("## 추천 미션 목록 조회 실패 : ${error.message}")
                        RecommendedMissionsUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
    
    /**
     * 데일리 미션 추천 받기
     */
    fun requestDailyMissionRecommendations() = viewModelScope.launch {
        _dailyMissionRecommendationUiState.value = DailyMissionRecommendationUiState.Loading
        
        requestDailyMissionRecommendationsUseCase()
            .collect { result ->
                _dailyMissionRecommendationUiState.value = result.fold(
                    onSuccess = { recommendation ->
                        Timber.d("## 데일리 미션 추천 받기 성공 : $recommendation")
                        DailyMissionRecommendationUiState.Success(recommendation)
                    },
                    onFailure = { error ->
                        Timber.e("## 데일리 미션 추천 받기 실패 : ${error.message}")
                        DailyMissionRecommendationUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
    
    /**
     * 미션 시작하기
     */
    fun startMission(recommendedMissionId: Int) = viewModelScope.launch {
        _startMissionUiState.value = StartMissionUiState.Loading
        
        startMissionUseCase(recommendedMissionId)
            .collect { result ->
                _startMissionUiState.value = result.fold(
                    onSuccess = { currentMission ->
                        Timber.d("## 미션 시작하기 성공 : $currentMission")
                        // 미션 시작 성공 시 일일 미션 상태 재조회
                        fetchDailyMissionStatus()
                        StartMissionUiState.Success(currentMission)
                    },
                    onFailure = { error ->
                        Timber.e("## 미션 시작하기 실패 : ${error.message}")
                        StartMissionUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }

    /**
     * 주어진 날짜가 속한 주의 일요일부터 토요일까지 7일간의 데이터를 반환
     */
    fun getCurrentWeekDays(baseDate: LocalDate = LocalDate.now()): List<DailyAppleData> {
        // 주의 시작(일요일)을 찾음
        val startOfWeek = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        
        // 7일간의 데이터 생성 (임시로 DEFAULT 상태 사용, 추후 API 연동 시 변경)
        return (0..6).map { dayOffset ->
            val date = startOfWeek.plusDays(dayOffset.toLong())
            DailyAppleData(
                date = date,
                dayOfMonth = date.dayOfMonth,
                status = getAppleStatusForDate(date) // 임시 데이터
            )
        }
    }

    /**
     * 임시로 사과 상태를 반환하는 함수
     * TODO: API 연동 후 실제 데이터로 교체
     */
    private fun getAppleStatusForDate(date: LocalDate): AppleStatus {
        // 임시 로직: 날짜에 따라 다른 상태 반환
        return when (date.dayOfMonth % 3) {
            0 -> AppleStatus.SUCCESS
            1 -> AppleStatus.FAILED
            else -> AppleStatus.DEFAULT
        }
    }
}