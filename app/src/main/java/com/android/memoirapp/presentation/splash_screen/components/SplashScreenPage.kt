package com.android.memoirapp.presentation.splash_screen.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.InsideActivity
import com.android.memoirapp.R
import com.android.memoirapp.common.Routes
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreenPage(context: Context, navController: NavController, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        delay(2500)
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(context, InsideActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }else{
            navController.navigate(Routes.LoginPage){
                popUpTo(Routes.SplashScreenPage){
                    inclusive = true
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = modifier.padding(innerPadding).background(colorResource(R.color.main_blue))
        ){
            Image(painter = painterResource(id = R.drawable.ic_logo_memoir),
                contentDescription = "Logo Image",
                modifier = Modifier
                    .size(100.dp).align(Alignment.Center)
            )
        }
    }

}

@Preview
@Composable
private fun SplashScreenPagePrev() {
    MemoirAppTheme {
//        SplashScreenPage(rememberNavController(),
//            Modifier.fillMaxSize())
    }
}