package com.android.memoirapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.memoirapp.common.Routes
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.common.ScaleTransitionDirection
import com.android.memoirapp.common.scaleIntoContainer
import com.android.memoirapp.common.scaleOutOfContainer
import com.android.memoirapp.presentation.auth_flow.components.LoginPage
import com.android.memoirapp.presentation.auth_flow.components.RegisterPage
import com.android.memoirapp.presentation.home_screen.components.MainPage
import com.android.memoirapp.presentation.splash_screen.components.SplashScreenPage

@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoirAppTheme {
                SharedTransitionLayout {
                    val navController = rememberNavController()
                    val firstRoute = Routes.SplashScreenPage
                    NavHost(
                        navController = navController,
                        startDestination = firstRoute,
                        enterTransition = {
                            scaleIntoContainer()
                        },
                        exitTransition = {
                            scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                        }
                    ) {
                        composable<Routes.SplashScreenPage> {
                            Box(
                                modifier = Modifier
                            ) {
                                SplashScreenPage(this@MainActivity, navController, Modifier.fillMaxSize())
                            }
                        }
                        composable<Routes.LoginPage> {
                            Box(
                                modifier = Modifier
                            ) {
                                LoginPage(navController, Modifier.fillMaxSize())
                            }
                        }
                        composable<Routes.MainPage> {
                            Box(
                                modifier = Modifier
                            ) {
                                MainPage(navController, Modifier.fillMaxSize())
                            }
                        }

                        composable<Routes.RegisterPage> {
                            Box(
                                modifier = Modifier
                            ) {
                                RegisterPage(navController, Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MemoirAppTheme {
        Greeting("Android")
    }
}