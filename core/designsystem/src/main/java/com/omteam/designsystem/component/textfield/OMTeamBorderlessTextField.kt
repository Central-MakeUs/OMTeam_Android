package com.omteam.designsystem.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.omteam.designsystem.theme.*

/**
 * 테두리 없는 TextField
 */
@Composable
fun OMTeamBorderlessTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    textStyle: TextStyle = PaperlogyType.headline03,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    inputFilter: ((String) -> String)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            val filteredValue = inputFilter?.invoke(newValue) ?: newValue
            onValueChange(filteredValue)
        },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        textStyle = textStyle.copy(
            color = Gray11,
            textAlign = TextAlign.Start
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        cursorBrush = SolidColor(Gray11),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                // placeholder 표시
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle,
                        color = Gray11,
                        textAlign = TextAlign.Start
                    )
                }

                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OMTeamBorderlessTextFieldPreview() {
    OMTeamTheme {
        var text by remember { mutableStateOf("운동 습관 형성") }
        OMTeamBorderlessTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "운동 습관 형성",
            modifier = Modifier.fillMaxWidth()
        )
    }
}