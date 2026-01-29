package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.repository.MissionRepository
import com.omteam.domain.usecase.GetCharacterInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

/**
 * [com.omteam.impl.screen.MainScreen], [com.omteam.impl.tab.HomeScreen]에서 쓰는 뷰모델
 *
 * 일일 미션, 캐릭터 정보 API 호출 결과, MainScreen 탭 인덱스, 날짜 값 관리
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val getCharacterInfoUseCase: GetCharacterInfoUseCase
) : ViewModel() {

    // 현재 선택된 탭의 인덱스 (0 : HOME, 1 : CHAT, 2 : REPORT, 3 : MY PAGE)
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // 리포트 화면에서 선택된 날짜 (기본값 : 오늘)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    // 일일 미션 UI State
    private val _dailyMissionUiState = MutableStateFlow<DailyMissionUiState>(DailyMissionUiState.Idle)
    val dailyMissionUiState: StateFlow<DailyMissionUiState> = _dailyMissionUiState.asStateFlow()
    
    // 캐릭터 정보 UI State
    private val _characterUiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Idle)
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()

    // 선택된 날짜를 "yyyy년 M월 n주" 형식으로 변환
    val weekDisplayText: StateFlow<String> = MutableStateFlow("").apply {
        value = formatWeekDisplayText(_selectedDate.value)
    }

    fun selectTab(index: Int) {
        if (index in 0..3) {
            Timber.d("## ${index}번 탭 선택")
            _selectedTabIndex.value = index
        } else {
            Timber.e("## 이 탭 인덱스 있는 게 맞음? : $index")
        }
    }

    fun moveToPreviousWeek() {
        _selectedDate.value = _selectedDate.value.minusWeeks(1)
        (weekDisplayText as MutableStateFlow).value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 이전 주 표시 : ${weekDisplayText.value}")
    }

    fun moveToNextWeek() {
        _selectedDate.value = _selectedDate.value.plusWeeks(1)
        (weekDisplayText as MutableStateFlow).value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 다음 주 표시 : ${weekDisplayText.value}")
    }

    fun resetToCurrentWeek() {
        _selectedDate.value = LocalDate.now()
        (weekDisplayText as MutableStateFlow).value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 현재 주로 리셋 : ${weekDisplayText.value}")
    }

    /**
     * 탭 인덱스를 초기값(0 - HOME)으로 리셋
     *
     * [com.omteam.impl.entry.mainEntry] 에서 호출해 로그아웃 후 재로그인 시 홈 화면부터 시작하게 함
     */
    fun resetTabIndex() {
        _selectedTabIndex.value = 0
        Timber.d("## 탭 인덱스 초기화")
    }

    /**
     * 날짜를 "yyyy M월 n주" 형식으로 변환
     */
    private fun formatWeekDisplayText(date: LocalDate): String {
        val year = date.year
        val month = date.monthValue
        val weekOfMonth = date.get(WeekFields.of(Locale.KOREA).weekOfMonth())

        return "${year}년 ${month}월 ${weekOfMonth}주"
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

}
