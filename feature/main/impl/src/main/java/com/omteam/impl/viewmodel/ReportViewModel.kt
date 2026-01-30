package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omteam.domain.usecase.GetWeeklyReportUseCase
import com.omteam.impl.viewmodel.state.WeeklyReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

/**
 * 주간 리포트 조회, 날짜 관리
 */
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getWeeklyReportUseCase: GetWeeklyReportUseCase
) : ViewModel() {

    // 선택된 날짜 (기본값: 오늘)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    // 주간 리포트 UI State
    private val _weeklyReportUiState = MutableStateFlow<WeeklyReportUiState>(WeeklyReportUiState.Idle)
    val weeklyReportUiState: StateFlow<WeeklyReportUiState> = _weeklyReportUiState.asStateFlow()

    // 선택된 날짜를 "yyyy년 M월 n주" 형식으로 변환
    private val _weekDisplayText = MutableStateFlow("")
    val weekDisplayText: StateFlow<String> = _weekDisplayText.asStateFlow()
    
    init {
        _weekDisplayText.value = formatWeekDisplayText(_selectedDate.value)
    }

    /**
     * 이전 주로 이동
     */
    fun moveToPreviousWeek() {
        _selectedDate.value = _selectedDate.value.minusWeeks(1)
        _weekDisplayText.value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 이전 주 표시 : ${_weekDisplayText.value}")
    }

    /**
     * 다음 주로 이동
     */
    fun moveToNextWeek() {
        _selectedDate.value = _selectedDate.value.plusWeeks(1)
        _weekDisplayText.value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 다음 주 표시 : ${_weekDisplayText.value}")
    }

    /**
     * 현재 주로 리셋
     */
    fun resetToCurrentWeek() {
        _selectedDate.value = LocalDate.now()
        _weekDisplayText.value = formatWeekDisplayText(_selectedDate.value)
        Timber.d("## 현재 주로 리셋 : ${_weekDisplayText.value}")
    }

    /**
     * 날짜를 "yyyy년 M월 n주" 형식으로 변환
     */
    private fun formatWeekDisplayText(date: LocalDate): String {
        val year = date.year
        val month = date.monthValue
        val weekOfMonth = date.get(WeekFields.of(Locale.KOREA).weekOfMonth())

        return "${year}년 ${month}월 ${weekOfMonth}주"
    }

    /**
     * 주간 리포트 조회
     * 
     * @param useSelectedDate true인 경우 selectedDate가 속한 주의 월요일을 기준으로 조회,
     *                        false인 경우 서버에서 현재 주 기준으로 조회 (기본값: true)
     */
    fun fetchWeeklyReport(useSelectedDate: Boolean = true) = viewModelScope.launch {
        _weeklyReportUiState.value = WeeklyReportUiState.Loading
        
        // selectedDate가 속한 주의 월요일 계산 (useSelectedDate가 true인 경우에만)
        val weekStartDate = if (useSelectedDate) {
            _selectedDate.value
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .format(DateTimeFormatter.ISO_LOCAL_DATE)
        } else {
            null
        }
        
        Timber.d("## 주간 리포트 조회 시작 - weekStartDate: $weekStartDate")
        
        getWeeklyReportUseCase(weekStartDate)
            .collect { result ->
                _weeklyReportUiState.value = result.fold(
                    onSuccess = { weeklyReport ->
                        Timber.d("## 주간 리포트 조회 성공 : $weeklyReport")
                        WeeklyReportUiState.Success(weeklyReport)
                    },
                    onFailure = { error ->
                        Timber.e("## 주간 리포트 조회 실패 : ${error.message}")
                        WeeklyReportUiState.Error(error.message ?: "알 수 없는 오류")
                    }
                )
            }
    }
}