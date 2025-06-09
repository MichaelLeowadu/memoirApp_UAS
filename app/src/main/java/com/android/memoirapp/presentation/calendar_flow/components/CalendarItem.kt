package com.android.memoirapp.presentation.calendar_flow.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarItem(journalList: List<JournalData>,onClick: (CalendarDay) -> Unit, modifier: Modifier = Modifier,
                 ){
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { daysOfWeek() } // Available from the library
//    val eventList = eventList.groupBy { it.date }
    val weekFields = WeekFields.of(Locale.getDefault())
    val coroutineScope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek.first()
    )

    val currentVisibleYearMonth = state.firstVisibleMonth

    LaunchedEffect(currentVisibleYearMonth) {

    }

    Column(modifier = modifier.background(Color.White)) {
        MonthHeader(currentVisibleYearMonth.yearMonth){ isArrowRight ->
            coroutineScope.launch {
                if(isArrowRight){
                    state.scrollToMonth(state.firstVisibleMonth.yearMonth.plusMonths(1))
                }else{
                    state.scrollToMonth(state.firstVisibleMonth.yearMonth.minusMonths(1))

                }
            }

        }
        Spacer(Modifier.height(8.dp))
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                val weekPosition =
                    if(day.position == DayPosition.OutDate || day.position == DayPosition.InDate) findCalendarDayPosition(currentVisibleYearMonth.weekDays, day)
                    else day.date.get(weekFields.weekOfMonth())-1
//                Text(weekPosition.toString())
                DayItem(day, journalList
                ){
                    onClick.invoke(it)
                } },
            monthHeader = {
                DaysOfWeekTitle(daysOfWeek = firstDayOfWeek)
                Spacer(Modifier.height(4.dp))
            }
        )
    }
}

fun findCalendarDayPosition(outerList: List<List<CalendarDay>>, targetDay: CalendarDay): Int? {
    outerList.forEachIndexed { index, innerList ->
        if (innerList.contains(targetDay)) {
            return index
        }
    }
    return null
}

@Preview
@Composable
private fun CalendarItemPrev() {
    MemoirAppTheme {
        CalendarItem(listOf(JournalData(timeStamp = System.currentTimeMillis())), {

        })
    }
}