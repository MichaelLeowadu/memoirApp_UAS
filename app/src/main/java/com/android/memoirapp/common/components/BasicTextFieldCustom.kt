package com.android.memoirapp.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.android.memoirapp.R

@Composable
fun BasicTextFieldCustom(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    alignment: Alignment = Alignment.Center
){
    Box(modifier = modifier) {
        BasicTextField(value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            enabled = enabled,

            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }.align(
                    alignment
                )
        )
        if(isHintVisible){
            Text(hint, style = textStyle, color = colorResource(id = R.color.gray_hint),
                modifier = Modifier.fillMaxWidth().align(
                    alignment
                ))
        }
        if(isError){
            Icon(
                Icons.Filled.Warning,"error", tint = Color.Red,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                ).padding(end = 12.dp)
            )
        }
    }

}