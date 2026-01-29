package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.model.mission.DailyMissionStatus
import com.omteam.domain.repository.MissionRepository
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
 * 사과 상태를 나타내는 타입
 */
enum class AppleStatus {
    DEFAULT,  // 기본 상태
    SUCCESS,  // 성공
    FAILED    // 실패
}

/**
 * 일별 사과 상태 데이터
 */
data class DailyAppleData(
    val date: LocalDate,
    val dayOfMonth: Int,
    val status: AppleStatus
)

/**
 * [com.omteam.impl.screen.MainScreen]에서 쓰는 뷰모델
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val missionRepository: MissionRepository
) : ViewModel() {

    // 현재 선택된 탭의 인덱스 (0 : HOME, 1 : CHAT, 2 : REPORT, 3 : MY PAGE)
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // 리포트 화면에서 선택된 날짜 (기본값 : 오늘)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    // 일일 미션 상태
    private val _dailyMissionStatus = MutableStateFlow<DailyMissionStatus?>(null)
    val dailyMissionStatus: StateFlow<DailyMissionStatus?> = _dailyMissionStatus.asStateFlow()
    
    // 미션 로딩 상태
    private val _isLoadingMission = MutableStateFlow(false)
    val isLoadingMission: StateFlow<Boolean> = _isLoadingMission.asStateFlow()
    
    // 미션 에러 메시지
    private val _missionError = MutableStateFlow<String?>(null)
    val missionError: StateFlow<String?> = _missionError.asStateFlow()

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
    fun fetchDailyMissionStatus() {
        viewModelScope.launch {
            _isLoadingMission.value = true
            _missionError.value = null

            missionRepository.getDailyMissionStatus()
                .collect { result ->
                    result.onSuccess { status ->
                        Timber.d("## 일일 미션 상태 조회 성공 : $status")
                        _dailyMissionStatus.value = status
                    }.onFailure { error ->
                        Timber.e("## 일일 미션 상태 조회 실패 : ${error.message}")
                        _missionError.value = error.message
                    }
                    _isLoadingMission.value = false
                }
        }
    }

    /**
     * 에러 메시지 초기화
     */
    fun clearMissionError() {
        _missionError.value = null
    }

}
