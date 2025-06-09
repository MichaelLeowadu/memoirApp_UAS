package com.android.memoirapp.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.memoirapp.R

@Composable
fun MainBlueContainer25(modifier: Modifier = Modifier,
                         text: String,
                         resourceId: Int = -1,
                         onClick: ()-> Unit
) {
    Row(
        modifier = modifier

            .border(
                2.dp, colorResource(R.color.main_blue),
                RoundedCornerShape(25.dp)
            )
            .background(
                colorResource(R.color.main_blue),
                RoundedCornerShape(25.dp)
            )
            .clickable {
                onClick.invoke()
            }
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        if(resourceId!=-1){
            Image(painter = painterResource(id = resourceId),
                contentDescription = "Button Image",
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text, fontSize = 16.sp, color = colorResource(R.color.deep_blue), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BlueContainer25(modifier: Modifier = Modifier,
                    text: String,
                    resourceId: Int = -1,
                    onClick: ()-> Unit
) {
    Row(
        modifier = modifier

            .border(
                2.dp, colorResource(R.color.deep_blue),
                RoundedCornerShape(25.dp)
            )
            .background(
                colorResource(R.color.deep_blue),
                RoundedCornerShape(25.dp)
            )
            .clickable {
                onClick.invoke()
            }
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        if(resourceId!=-1){
            Image(painter = painterResource(id = resourceId),
                contentDescription = "Button Image",
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text, fontSize = 16.sp, color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
    }
}