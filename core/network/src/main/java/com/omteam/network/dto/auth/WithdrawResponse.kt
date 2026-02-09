package com.omteam.network.dto.auth

import kotlinx.serialization.Serializable

/**
 * 회원탈퇴 응답
 *
 * @property success 성공 여부
 * @property data 탈퇴 성공 시 메시지
 * @property error 탈퇴 실패 시 에러 정보
 */
@Serializable
data class WithdrawResponse(
    val success: Boolean,
    val data: String? = null,
    val error: WithdrawError? = null
)

/**
 * 회원탈퇴 에러 정보
 *
 * @property code 에러 코드
 * @property message 에러 메시지
 */
@Serializable
data class WithdrawError(
    val code: String,
    val message: String
)