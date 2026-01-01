package com.omteam.data.mapper

import com.kakao.sdk.user.model.User
import com.omteam.domain.model.UserInfo

object UserInfoMapper {
    fun toDomain(kakaoUser: User): UserInfo = UserInfo(
        id = kakaoUser.id ?: 0L,
        nickname = kakaoUser.kakaoAccount?.profile?.nickname,
        email = kakaoUser.kakaoAccount?.email
    )
}