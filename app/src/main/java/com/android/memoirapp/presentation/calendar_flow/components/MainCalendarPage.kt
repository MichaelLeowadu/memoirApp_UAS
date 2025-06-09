package com.android.memoirapp.presentation.calendar_flow.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@Composable
fun MainCalendarPage(journalViewModel: JournalViewModel, navController: NavController, modifier: Modifier = Modifier) {

    val coroutineScope = rememberCoroutineScope()
    val journals by journalViewModel.journals.collectAsState()

    Box(
        modifier.background(Color.White)
    ){
        CalendarItem(journals, {
            coroutineScope.launch {
                navController.navigate(Routes.JournalListFilterPage(localDateToMillis(it.date)))
            }
        },
            Modifier.fillMaxSize())
    }
}

fun localDateToMillis(date: LocalDate): Long {
    return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@Preview
@Composable
private fun MainCalendarPagePrev() {
    MemoirAppTheme {
//        MainCalendarPage(rememberNavController(),
//            Modifier.fillMaxSize())
    }
}