package com.omteam.domain.repository

import com.omteam.domain.model.character.CharacterInfo
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    /**
     * 현재 사용자의 캐릭터 정보 조회
     * 
     * @return 캐릭터 정보 Flow (레벨, 경험치, 격려 메시지 등)
     */
    fun getCharacter(): Flow<Result<CharacterInfo>>
}