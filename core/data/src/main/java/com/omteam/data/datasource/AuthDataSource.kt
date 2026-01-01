package com.omteam.data.datasource

import com.omteam.domain.model.UserInfo

/**
 * 인증 datasource 인터페이스
 *
 * 카카오, 구글 로그인 제공자 구현 추상화
 */
interface AuthDataSource {
    /**
     * 사용자 정보 조회
     * @return 성공 시 UserInfo, 실패 시 Exception을 포함한 Result
     */
    suspend fun getUserInfo(): Result<UserInfo>

    /**
     * 로그아웃 처리
     * @return 성공 시 Unit, 실패 시 Exception을 포함한 Result
     */
    suspend fun logout(): Result<Unit>
}
