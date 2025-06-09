package com.android.memoirapp.presentation.calendar_flow.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.memoirapp.R
import com.android.memoirapp.data.JournalData
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import timber.log.Timber
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DayItem(
    day: CalendarDay,
    journalList: List<JournalData>,
    onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(59 / 81f)
            .padding(top = 4.dp)
            .clickable(
                indication = null,
                enabled = day.position == DayPosition.MonthDate,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick(day)
            }
        ,
        contentAlignment = Alignment.TopCenter
    ) {
        val alpha = if (day.position == DayPosition.MonthDate) 1f else 0.3f
        if(isSameDate(day.date, System.currentTimeMillis())){
            Image(painter = painterResource(R.drawable.ic_circle_yellow),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentScale = ContentScale.Fit)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = day.date.dayOfMonth.toString(),
                fontFamily = FontFamily.Monospace,
                color = Color.Black, modifier = Modifier.alpha(alpha))
            val currentFilter = journalList.firstOrNull { isSameDate(day.date, it.timeStamp) }
            if(currentFilter != null){
                Image(Icons.Filled.DateRange,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(R.color.deep_blue)),
                    modifier = Modifier.weight(1f))
            }
        }



    }
}

private fun isSameDate(localDate: LocalDate, timestamp: Long): Boolean {
    val dateFromTimestamp = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return localDate == dateFromTimestamp
}

fun <T> MutableList<T>.moveElement(fromIndex: Int, toIndex: Int) {
    if (fromIndex !in 0 until size || toIndex !in 0 until size) {
        throw IndexOutOfBoundsException("Invalid index")
    }
    val element = removeAt(fromIndex)
    add(toIndex, element)
}

fun Modifier.noRippleClickable(onClick: () -> Unit, enabled: Boolean): Modifier = composed {
    this.clickable(
        indication = null,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}