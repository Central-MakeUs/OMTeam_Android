package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
class MainViewModel @Inject constructor() : ViewModel() {

    // 현재 선택된 탭의 인덱스 (0 : HOME, 1 : CHAT, 2 : REPORT, 3 : MY PAGE)
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // 리포트 화면에서 선택된 날짜 (기본값 : 오늘)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

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
}
