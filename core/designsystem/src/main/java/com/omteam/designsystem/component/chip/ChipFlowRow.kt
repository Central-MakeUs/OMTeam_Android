package com.omteam.designsystem.component.chip

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import com.omteam.designsystem.foundation.dp8

/**
 * FlowRow 대체용 - Chip들을 가로로 배치하다가 화면 너비 초과하면 다음 줄로 넘김
 *
 * FlowRow 쓰면 프로가드 룰 수정해도 크래시 발생해서 직접 구현
 *
 * 각 아이템의 너비 측정해서 화면 가로 길이에 맞춰 동적으로 줄바꿈
 *
 * @param items 표시할 아이템 리스트
 * @param modifier Modifier
 * @param horizontalSpacing 아이템 간 가로 간격
 * @param verticalSpacing 줄 간 세로 간격
 * @param footer 마지막 줄에 추가할 컴포저블 (예: AddCustomChip)
 * @param content 각 아이템의 컨텐츠 (람다로 받음)
 */
@Composable
fun <T> ChipFlowRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = dp8,
    verticalSpacing: Dp = dp8,
    footer: @Composable () -> Unit = {},
    content: @Composable (T) -> Unit
) {
    SubcomposeLayout(modifier = modifier.fillMaxWidth()) { constraints ->
        val maxWidth = constraints.maxWidth

        // 모든 아이템과 footer를 측정
        val itemPlaceables = items.map { item ->
            subcompose(item) {
                content(item)
            }.map { it.measure(constraints.copy(minWidth = 0)) }
        }

        val footerPlaceable = subcompose("footer") {
            footer()
        }.map { it.measure(constraints.copy(minWidth = 0)) }

        // 줄 단위로 아이템 분배 (화면 너비에 맞춰)
        val rows = mutableListOf<List<Placeable>>()
        val currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        fun addToRow(placeable: Placeable) {
            val placeableWidth = placeable.width
            val spacingPx = if (currentRow.isEmpty()) 0 else horizontalSpacing.roundToPx()

            if (currentRowWidth + spacingPx + placeableWidth <= maxWidth) {
                // 현재 줄에 추가 가능
                currentRow.add(placeable)
                currentRowWidth += spacingPx + placeableWidth
            } else {
                // 새 줄 시작
                if (currentRow.isNotEmpty()) {
                    rows.add(currentRow.toList())
                }
                currentRow.clear()
                currentRow.add(placeable)
                currentRowWidth = placeableWidth
            }
        }

        // 모든 아이템을 줄에 배치
        itemPlaceables.flatten().forEach { addToRow(it) }

        // Footer를 마지막 줄에 추가 (또는 새 줄)
        footerPlaceable.firstOrNull()?.let { addToRow(it) }

        // 마지막 줄 추가
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow.toList())
        }

        // 전체 높이 계산
        val rowHeights = rows.map { row -> row.maxOfOrNull { it.height } ?: 0 }
        val totalHeight = rowHeights.sum() + verticalSpacing.roundToPx() * (rows.size - 1).coerceAtLeast(0)

        layout(maxWidth, totalHeight) {
            var y = 0
            rows.forEachIndexed { rowIndex, row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + horizontalSpacing.roundToPx()
                }
                y += (rowHeights.getOrNull(rowIndex) ?: 0) + verticalSpacing.roundToPx()
            }
        }
    }
}

/**
 * 문자열 전용
 */
@Composable
fun ChipFlowRow(
    chips: List<String>,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = dp8,
    verticalSpacing: Dp = dp8
) {
    ChipFlowRow(
        items = chips,
        modifier = modifier,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing
    ) { chip ->
        InfoChip(text = chip)
    }
}