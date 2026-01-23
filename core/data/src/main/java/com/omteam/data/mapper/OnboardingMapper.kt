package com.omteam.data.mapper

import com.omteam.domain.model.LifestyleType
import com.omteam.domain.model.OnboardingInfo
import com.omteam.domain.model.WorkTimeType
import com.omteam.network.dto.OnboardingData

fun OnboardingData.toDomain(): OnboardingInfo = OnboardingInfo(
    nickname = nickname,
    appGoalText = appGoalText,
    workTimeType = WorkTimeType.valueOf(workTimeType),
    availableStartTime = availableStartTime,
    availableEndTime = availableEndTime,
    minExerciseMinutes = minExerciseMinutes,
    preferredExerciseText = preferredExerciseText,
    lifestyleType = LifestyleType.valueOf(lifestyleType),
    remindEnabled = remindEnabled,
    checkInEnabled = checkinEnabled,
    reviewEnabled = reviewEnabled
)