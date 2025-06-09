package com.android.memoirapp.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.memoirapp.R
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme

@Composable
fun BlueBorderWhiteEditTextBox(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false
){
    BasicTextFieldCustom(
        text,
        hint,
        modifier.border(2.dp,
            color = if(isError) Color.Red else colorResource(R.color.deep_blue),
            RoundedCornerShape(25.dp)
        )
            .background(Color.White, RoundedCornerShape(25.dp)).
            padding(14.dp),
        isHintVisible, onValueChange,
        textStyle, singleLine, onFocusChange, enabled, isError)
}

@Composable
fun PasswordTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    onVisibleChange:(Boolean) -> Unit = {},
    enabled: Boolean = true,
    isError: Boolean = false
){
    Box(modifier = modifier.border(2.dp,
        color = if(isError) Color.Red else colorResource(R.color.deep_blue),
        RoundedCornerShape(25.dp))
        .background(Color.White, RoundedCornerShape(25.dp))
        .padding(14.dp)) {
        BasicTextField(value = text,
            visualTransformation = if (passwordVisibility.value)
                VisualTransformation.None else
                PasswordVisualTransformation(),
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
            ,
        )
        if(isHintVisible){
            Text(hint, style = textStyle.copy(fontSize = 16.sp), color = colorResource(id = R.color.gray_hint),
                modifier = Modifier.align(Alignment.CenterStart))
        }
        Image(painter =
            if(passwordVisibility.value) painterResource(id = R.drawable.img_hide_password) else painterResource(R.drawable.img_show_password),
            contentDescription = "Button Image",
            modifier = Modifier
                .size(24.dp).align(Alignment.CenterEnd).clickable {
                    onVisibleChange.invoke(!passwordVisibility.value)
                }
        )
        if(isError){
            Icon(
                Icons.Filled.Warning,"error", tint = Color.Red,
                modifier = Modifier.align(
                    Alignment.CenterEnd
                ).padding(end = 32.dp)
            )
        }
    }
}


@Preview
@Composable
private fun BlueBorder25TextFieldPrev() {

    val showPassword = remember {
        mutableStateOf(true)
    }

    MemoirAppTheme{

        Column(Modifier.background(Color.White).padding(16.dp)) {
            PasswordTextField(
                "",
                "Test Hint",
                onValueChange = {

                },
                onFocusChange = {

                },
                modifier = Modifier.fillMaxWidth(), passwordVisibility = showPassword,
                isError = true
            )
            Spacer(Modifier.height(12.dp))
            BlueBorderWhiteEditTextBox(
                "",
                "Test Hint",
                Modifier.fillMaxWidth(),
                onValueChange = {

                },
                onFocusChange = {

                }, isError = true)
        }


    }
}