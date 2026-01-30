package com.omteam.network.api

import com.omteam.network.dto.character.CharacterResponse
import retrofit2.http.GET

interface CharacterApiService {
    
    /**
     * 현재 사용자의 캐릭터 정보 조회
     * 
     * @return 캐릭터 정보 (레벨, 경험치, 격려 메시지 등)
     */
    @GET("api/character")
    suspend fun getCharacter(): CharacterResponse
}