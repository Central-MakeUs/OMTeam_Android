package com.omteam.impl.viewmodel.state

import com.omteam.domain.model.character.CharacterInfo

/**
 * 캐릭터 정보 UI 상태
 */
sealed class CharacterUiState {
    /**
     * 초기 상태 (아직 로드하지 않음)
     */
    data object Idle : CharacterUiState()
    
    /**
     * 로딩 중
     */
    data object Loading : CharacterUiState()
    
    /**
     * 로딩 성공
     */
    data class Success(val data: CharacterInfo) : CharacterUiState()
    
    /**
     * 로딩 실패
     */
    data class Error(val message: String) : CharacterUiState()
}