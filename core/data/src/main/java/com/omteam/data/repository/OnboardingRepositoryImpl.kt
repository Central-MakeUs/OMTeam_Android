package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.onboarding.OnboardingInfo
import com.omteam.domain.repository.OnboardingRepository
import com.omteam.network.api.OnboardingApiService
import com.omteam.network.dto.onboarding.OnboardingRequest
import com.omteam.network.dto.onboarding.UpdateAvailableTimeRequest
import com.omteam.network.dto.onboarding.UpdateLifestyleRequest
import com.omteam.network.dto.onboarding.UpdateMinExerciseMinutesRequest
import com.omteam.network.dto.onboarding.UpdateAppGoalRequest
import com.omteam.network.dto.onboarding.UpdateNicknameRequest
import com.omteam.network.dto.onboarding.UpdatePreferredExerciseRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingApiService: OnboardingApiService
) : OnboardingRepository {

    override fun getOnboardingInfo(): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "온보딩 정보 조회",
            apiCall = { onboardingApiService.getOnboardingInfo() },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun submitOnboarding(onboardingInfo: OnboardingInfo): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "온보딩 정보 제출",
            apiCall = {
                // preferredExerciseText 문자열 -> 리스트 변환 (쉼표로 구분)
                val preferredExercises = onboardingInfo.preferredExerciseText
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                
                val request = OnboardingRequest(
                    nickname = onboardingInfo.nickname,
                    appGoalText = onboardingInfo.appGoalText,
                    workTimeType = onboardingInfo.workTimeType.name,
                    availableStartTime = onboardingInfo.availableStartTime,
                    availableEndTime = onboardingInfo.availableEndTime,
                    minExerciseMinutes = onboardingInfo.minExerciseMinutes,
                    preferredExercises = preferredExercises,
                    lifestyleType = onboardingInfo.lifestyleType.name,
                    remindEnabled = onboardingInfo.remindEnabled,
                    checkinEnabled = onboardingInfo.checkinEnabled,
                    reviewEnabled = onboardingInfo.reviewEnabled
                )
                onboardingApiService.submitOnboarding(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updateLifestyle(lifestyleType: String): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "평소 생활 패턴 수정",
            apiCall = {
                val request = UpdateLifestyleRequest(lifestyleType)
                onboardingApiService.updateLifestyle(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updatePreferredExercise(preferredExercises: List<String>): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "선호 운동 수정",
            apiCall = {
                val request = UpdatePreferredExerciseRequest(preferredExercises)
                onboardingApiService.updatePreferredExercise(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updateMinExerciseMinutes(minExerciseMinutes: Int): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "미션에 투자할 수 있는 시간 수정",
            apiCall = {
                val request = UpdateMinExerciseMinutesRequest(minExerciseMinutes)
                onboardingApiService.updateMinExerciseMinutes(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updateAvailableTime(availableStartTime: String, availableEndTime: String): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "운동 가능 시간 수정",
            apiCall = {
                val request = UpdateAvailableTimeRequest(availableStartTime, availableEndTime)
                onboardingApiService.updateAvailableTime(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updateNickname(nickname: String): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "닉네임 변경",
            apiCall = {
                val request = UpdateNicknameRequest(nickname)
                onboardingApiService.updateNickname(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )

    override fun updateAppGoal(appGoalText: String): Flow<Result<OnboardingInfo>> =
        safeApiCall(
            logTag = "앱 사용 목적 수정",
            apiCall = {
                val request = UpdateAppGoalRequest(appGoalText)
                onboardingApiService.updateAppGoal(request)
            },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}