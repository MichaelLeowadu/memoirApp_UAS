package com.android.memoirapp.presentation.journal_flow.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.R
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.components.BlueBorderWhiteEditTextBox
import com.android.memoirapp.common.navigateWithBundle
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import kotlinx.coroutines.launch


@Composable
fun MainJournalPage(viewModel: JournalViewModel, searchMode:Boolean = false, navController: NavController, modifier: Modifier = Modifier) {

    val journals by viewModel.journals.collectAsState()
    val searchText = remember{
        mutableStateOf("")
    }
    val journalDisplay = remember{
        mutableStateOf(emptyList<JournalData>())
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(searchText.value, journals) {
        if(searchText.value.isBlank()){
            journalDisplay.value = journals
        }else{
            journalDisplay.value = journals.filter { it.judul.contains(searchText.value, true) }
        }
    }
    Box(
        modifier.background(Color.White)
    ){

        Column(Modifier.fillMaxSize()) {
            if(searchMode){
                Column(Modifier.fillMaxWidth().background(colorResource(R.color.main_blue)).padding(12.dp)){
                    BlueBorderWhiteEditTextBox(
                        searchText.value,
                        "Search Journal...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        isHintVisible = searchText.value.isBlank(),
                        onValueChange = {
                            searchText.value = it
                        },
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                        singleLine = true,
                        onFocusChange = {

                        },
                        enabled = true
                    )
                }
            }
            LazyColumn(Modifier.fillMaxWidth().weight(1f)) {
                items(journalDisplay.value) {
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

        Card(
            colors = CardColors(colorResource(R.color.deep_blue),
                colorResource(R.color.deep_blue),colorResource(R.color.deep_blue),
                colorResource(R.color.deep_blue)),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).clickable {
                coroutineScope.launch {
                    navController.navigate(Routes.JournalFormPage)
                }
            }
        ) {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Image(painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("New Journal",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }


}



//@Preview
//@Composable
//private fun MainJournalPagePrev() {
//    MemoirAppTheme {
//        MainJournalPage(navController = rememberNavController(),
//            modifier = Modifier.fillMaxSize())
//    }
//}