package com.android.memoirapp.presentation.calendar_flow.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.R
import com.android.memoirapp.common.DateFormatter
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.navigateWithBundle
import com.android.memoirapp.presentation.journal_flow.components.JournalItem
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


@Composable
fun MainJournalListFilterPage(viewModel: JournalViewModel, navController: NavController, dayLong: Long,
                              modifier: Modifier = Modifier) {

    val journals by viewModel.journals.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier.background(colorResource(R.color.light_creme))
    ){
        Box(Modifier.fillMaxWidth().padding(12.dp)){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = colorResource(R.color.deep_blue),
                contentDescription = "Back button",
                modifier = Modifier.align(Alignment.CenterStart).clickable {
                    coroutineScope.launch {
                        navController.popBackStack()
                    }
                }
            )
            Text(
                DateFormatter.formatFullDate(dayLong),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.deep_blue),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn(Modifier.fillMaxSize().weight(1f)) {
            items(journals.filter {
                isSameDate(it.timeStamp, dayLong)
            }) {
                val bundle = Bundle()
                bundle.putParcelable("journalData", it)
                JournalItem(navController, Modifier.fillMaxWidth().clickable {
                    coroutineScope.launch {
                        navController.navigateWithBundle(Routes.JournalDetailPage, bundle)
                    } },
                    it)
            }
        }


    }
}

fun isSameDate(timestamp1: Long, timestamp2: Long): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
    val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
}

//@Preview
//@Composable
//private fun MainJournalListFilterPagePrev() {
//    MemoirAppTheme {
//        MainJournalListFilterPage(rememberNavController(), System.currentTimeMillis(),
//            Modifier.fillMaxSize())
//    }
//}