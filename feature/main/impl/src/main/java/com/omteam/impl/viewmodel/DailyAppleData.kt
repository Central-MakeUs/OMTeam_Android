package com.omteam.impl.viewmodel

import java.time.LocalDate

/**
 * 일별 사과 상태 데이터
 */
data class DailyAppleData(
    val date: LocalDate,
    val dayOfMonth: Int,
    val status: AppleStatus
)
