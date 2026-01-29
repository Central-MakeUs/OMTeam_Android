package com.omteam.data.repository

import com.omteam.data.mapper.toDomain
import com.omteam.data.util.ErrorInfo
import com.omteam.data.util.safeApiCall
import com.omteam.domain.model.character.CharacterInfo
import com.omteam.domain.repository.CharacterRepository
import com.omteam.network.api.CharacterApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterApiService: CharacterApiService
) : CharacterRepository {

    override fun getCharacter(): Flow<Result<CharacterInfo>> =
        safeApiCall(
            logTag = "캐릭터 정보 조회",
            defaultErrorMessage = "캐릭터 정보를 불러올 수 없습니다",
            apiCall = { characterApiService.getCharacter() },
            transform = { response -> response.data?.toDomain() },
            getErrorInfo = { response -> ErrorInfo(response.error?.code, response.error?.message) }
        )
}