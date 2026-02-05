package com.omteam.impl.tab.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.stringResource
import com.omteam.designsystem.component.text.OMTeamText
import com.omteam.designsystem.foundation.*
import com.omteam.designsystem.theme.*
import com.omteam.impl.component.SubScreenHeader
import com.omteam.omt.core.designsystem.R
import timber.log.Timber

@Composable
fun OtherScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToWebView: (url: String) -> Unit = { _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(dp20)
    ) {
        // 상단 헤더
        SubScreenHeader(
            title = stringResource(R.string.other_title),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(dp50))

        // 공지사항, FAQ, 문의하기
        OtherMenuItem(
            iconRes = R.drawable.icon_announcement,
            text = stringResource(R.string.notice_title),
            onClick = { Timber.d("## 공지사항 클릭") }
        )
        OtherMenuDivider()

        OtherMenuItem(
            iconRes = R.drawable.icon_inquiry,
            text = stringResource(R.string.faq_title),
            onClick = { Timber.d("## FAQ 클릭") }
        )
        OtherMenuDivider()

        OtherMenuItem(
            iconRes = R.drawable.icon_faq,
            text = stringResource(R.string.inquiry_title),
            onClick = { Timber.d("## 문의하기 클릭") }
        )
        OtherMenuDivider()

        OtherMenuItem(
            iconRes = R.drawable.icon_check,
            text = stringResource(R.string.privacy_terms),
            onClick = {
                Timber.d("## 개인정보 정책 클릭")
                onNavigateToWebView(
                    "https://slashpage.com/omt-policy-terms/5r398nmnx6zxzmvwje7y",
                )
            }
        )
        OtherMenuDivider()

        OtherMenuItem(
            iconRes = R.drawable.icon_info,
            text = stringResource(R.string.terms_conditions),
            onClick = {
                Timber.d("## 이용약관 클릭")
                onNavigateToWebView(
                    "https://slashpage.com/omt-policy-terms",
                )
            }
        )
        OtherMenuDivider()
    }
}

@Composable
private fun OtherMenuItem(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(dp24)
        )
        
        Spacer(modifier = Modifier.width(dp4))
        
        OMTeamText(
            text = text,
            style = PretendardType.body02_2,
            color = Gray11
        )
    }

    Spacer(modifier = Modifier.height(dp18))
}

@Composable
private fun OtherMenuDivider() {
    Spacer(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(Gray03)
    )
    Spacer(modifier = Modifier.height(dp18))
}

@Preview
@Composable
private fun OtherScreenPreview() {
    OMTeamTheme {
        OtherScreen()
    }
}