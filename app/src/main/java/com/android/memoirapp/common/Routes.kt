package com.android.memoirapp.common

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.android.memoirapp.data.JournalData
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes{

    @Serializable
    data object SplashScreenPage : Routes()
    @Serializable
    data object LoginPage : Routes()
    @Serializable
    data object RegisterPage : Routes()
    @Serializable
    data object MainPage : Routes()
    @Serializable
    data object JournalFormPage : Routes()
    @Serializable
    data object JournalDetailPage : Routes()
    @Serializable
    data class JournalListFilterPage(val dateLong: Long = 0L): Routes()

}

fun NavController.navigateWithBundle(
    route: Routes,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route = route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}