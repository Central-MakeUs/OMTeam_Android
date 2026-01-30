package com.omteam.domain.model.character

/**
 * 캐릭터 정보
 * 
 * @property level 현재 레벨
 * @property experiencePercent 현재 레벨 내 경험치 퍼센트 (0-100)
 * @property successCount 누적 성공 횟수
 * @property successCountUntilNextLevel 다음 레벨까지 필요한 성공 횟수
 * @property encouragementTitle 오늘의 격려 제목
 * @property encouragementMessage 오늘의 격려 메시지
 */
data class CharacterInfo(
    val level: Int,
    val experiencePercent: Int,
    val successCount: Int,
    val successCountUntilNextLevel: Int,
    val encouragementTitle: String,
    val encouragementMessage: String
)