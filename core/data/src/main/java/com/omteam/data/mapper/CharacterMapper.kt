package com.omteam.data.mapper

import com.omteam.domain.model.character.CharacterInfo
import com.omteam.network.dto.character.CharacterData

/**
 * CharacterData -> CharacterInfo 도메인 모델 변환
 */
fun CharacterData.toDomain(): CharacterInfo = CharacterInfo(
    level = level,
    experiencePercent = experiencePercent,
    successCount = successCount,
    successCountUntilNextLevel = successCountUntilNextLevel,
    encouragementTitle = encouragementTitle,
    encouragementMessage = encouragementMessage
)