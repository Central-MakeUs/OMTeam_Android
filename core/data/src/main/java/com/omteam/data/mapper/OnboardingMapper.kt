package com.omteam.data.mapper

import com.omteam.domain.model.onboarding.LifestyleType
import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.model.onboarding.WorkTimeType
import com.omteam.network.dto.onboarding.OnboardingData

fun OnboardingData.toDomain(): OnboardingInfo = OnboardingInfo(
    nickname = nickname,
    appGoalText = appGoalText,
    workTimeType = WorkTimeType.valueOf(workTimeType),
    availableStartTime = availableStartTime,
    availableEndTime = availableEndTime,
    minExerciseMinutes = minExerciseMinutes,
    preferredExerciseText = preferredExercises.joinToString(", "),
    lifestyleType = LifestyleType.valueOf(lifestyleType),
    remindEnabled = remindEnabled,
    checkinEnabled = checkinEnabled,
    reviewEnabled = reviewEnabled
)