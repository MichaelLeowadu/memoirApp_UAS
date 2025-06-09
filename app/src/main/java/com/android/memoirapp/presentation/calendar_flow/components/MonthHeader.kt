package com.android.memoirapp.presentation.calendar_flow.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.memoirapp.R
import com.kizitonwose.calendar.core.CalendarDay
import java.time.YearMonth

@Composable
fun MonthHeader(currentVisibleYearMonth : YearMonth, onClick: (Boolean) -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(3.dp), verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(12.dp))
        Icon(
            Icons.Filled.KeyboardArrowLeft,
            tint = colorResource(R.color.deep_blue),
            contentDescription = null,
            modifier = Modifier.clickable {
                onClick.invoke(false)
            }
            )
        Spacer(Modifier.width(16.dp))
        Text(
            text = currentVisibleYearMonth.month.value.toString(),
            fontSize = 37.sp,
            color = colorResource(R.color.deep_blue),
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = currentVisibleYearMonth.year.toString(),
                fontSize = 13.sp,
                color = colorResource(R.color.black),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = currentVisibleYearMonth.month.name, fontSize = 13.sp,
                color = colorResource(R.color.black),
                fontFamily = FontFamily.Monospace
            )
        }
        Icon(
            Icons.Filled.KeyboardArrowRight,
            tint = colorResource(R.color.deep_blue),
            contentDescription = null,
            modifier = Modifier.clickable {
                onClick.invoke(true)
            }
        )
        Spacer(Modifier.width(12.dp))
    }
}