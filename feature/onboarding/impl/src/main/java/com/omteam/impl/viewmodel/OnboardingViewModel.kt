package com.omteam.impl.viewmodel

import androidx.lifecycle.ViewModel
import com.omteam.impl.model.OnboardingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _onboardingData = MutableStateFlow(OnboardingData())
    val onboardingData: StateFlow<OnboardingData> = _onboardingData.asStateFlow()

    fun updateNickname(nickname: String) {
        _onboardingData.update { currentData ->
            currentData.copy(nickname = nickname)
        }
    }

    fun updateGoal(goal: String) {
        _onboardingData.update { currentData ->
            currentData.copy(goal = goal)
        }
    }

    fun updateTime(time: String) {
        _onboardingData.update { currentData ->
            currentData.copy(time = time)
        }
    }

    fun updateMissionTime(missionTime: String) {
        _onboardingData.update { currentData ->
            currentData.copy(missionTime = missionTime)
        }
    }

    fun updateFavoriteExercise(favoriteExercise: String) {
        _onboardingData.update { currentData ->
            currentData.copy(favoriteExercise = favoriteExercise)
        }
    }

    fun updatePattern(pattern: String) {
        _onboardingData.update { currentData ->
            currentData.copy(pattern = pattern)
        }
    }

    fun clearOnboardingData() {
        _onboardingData.value = OnboardingData()
    }

    // API 스펙 정해지면 수정
    fun getOnboardingData(): OnboardingData = _onboardingData.value
}