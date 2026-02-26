package com.omteam.impl.viewmodel.state

/**
 * FCM 토큰 등록, 삭제 상태
 */
sealed interface FcmTokenState {
    /**
     * 초기 상태
     */
    data object Idle : FcmTokenState

    /**
     * 로딩 중
     */
    data object Loading : FcmTokenState

    /**
     * 등록 성공
     */
    data class RegisterSuccess(val message: String) : FcmTokenState

    /**
     * 삭제 성공
     */
    data class DeleteSuccess(val message: String) : FcmTokenState

    /**
     * 에러
     */
    data class Error(val message: String) : FcmTokenState
}