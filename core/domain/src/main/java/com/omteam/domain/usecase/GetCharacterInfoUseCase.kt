package com.omteam.domain.usecase

import com.omteam.domain.model.character.CharacterInfo
import com.omteam.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharacterInfoUseCase(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(): Flow<Result<CharacterInfo>> =
        characterRepository.getCharacter()
}