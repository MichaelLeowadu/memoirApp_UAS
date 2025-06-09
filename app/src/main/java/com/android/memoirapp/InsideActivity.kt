package com.android.memoirapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.ScaleTransitionDirection
import com.android.memoirapp.common.components.LoadingDialog
import com.android.memoirapp.common.components.LogoutDialog
import com.android.memoirapp.common.scaleIntoContainer
import com.android.memoirapp.common.scaleOutOfContainer
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.auth_flow.components.LoginPage
import com.android.memoirapp.presentation.auth_flow.components.RegisterPage
import com.android.memoirapp.presentation.calendar_flow.components.MainCalendarPage
import com.android.memoirapp.presentation.calendar_flow.components.MainJournalListFilterPage
import com.android.memoirapp.presentation.gallery_flow.components.MainGalleryPage
import com.android.memoirapp.presentation.home_screen.components.MainPage
import com.android.memoirapp.presentation.journal_flow.components.JournalDetailPage
import com.android.memoirapp.presentation.journal_flow.components.JournalForm
import com.android.memoirapp.presentation.journal_flow.components.MainJournalPage
import com.android.memoirapp.presentation.map_flow.components.MainMapPage
import com.android.memoirapp.presentation.splash_screen.components.SplashScreenPage
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

data class TabBarItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
)

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
class InsideActivity : ComponentActivity() {

    private val journalViewModel: JournalViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val journalTab = TabBarItem(title = "Journal",
                selectedIcon = R.drawable.ic_journal_selected,
                unselectedIcon = R.drawable.ic_journal
            )
            val calendarTab = TabBarItem(title = "Calendar",
                selectedIcon = R.drawable.ic_calendar_selected,
                unselectedIcon = R.drawable.ic_calendar)
            val mediaTab = TabBarItem(title = "Media",
                selectedIcon = R.drawable.ic_media_selected,
                unselectedIcon = R.drawable.ic_media)
            val atlasTab = TabBarItem(title = "Atlas",
                selectedIcon = R.drawable.ic_atlas_selected,
                unselectedIcon = R.drawable.ic_atlas)
            val tabBarItems = listOf(journalTab, calendarTab, mediaTab, atlasTab)

                // creating our navController
                val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showLogoutDialog = remember{
                mutableStateOf(false)
            }
            val auth = FirebaseAuth.getInstance()
            MemoirAppTheme {
                // A surface container using the 'background' color from the theme
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                val isLoading by journalViewModel.loading.collectAsState()
                val searchMode = remember{
                    mutableStateOf(false)
                }
                LoadingDialog(isLoading)
                LogoutDialog(showLogoutDialog.value, {
                    showLogoutDialog.value = false
                }) {
                    auth.signOut()
                    val intent = Intent(this@InsideActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.light_creme)
                ) {
                    Scaffold(
                        topBar = {
                            if(currentRoute == journalTab.title || currentRoute == mediaTab.title
                                || currentRoute == calendarTab.title || currentRoute == atlasTab.title){
                                CenterAlignedTopAppBar(
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = colorResource(id = R.color.main_blue),
                                        titleContentColor = colorResource(R.color.deep_blue),
                                        scrolledContainerColor = colorResource(id = R.color.main_blue)
                                    ),
                                    title = {
                                        Text(
                                            "Halo, ${FirebaseAuth.getInstance()?.currentUser?.displayName ?: "User"}",
                                            maxLines = 1,
                                            fontSize = 16.sp,
                                            color = colorResource(R.color.deep_blue),
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start
                                        )
                                    },
                                    actions = {
                                        Row{
                                            if(currentRoute == journalTab.title){
                                                Icon(Icons.Default.Search, contentDescription = "",
                                                    tint = colorResource(R.color.deep_blue),
                                                    modifier = Modifier.clickable {
                                                        searchMode.value = !searchMode.value
                                                    })
                                            }
                                            Spacer(Modifier.width(6.dp))
                                            Icon(Icons.Default.ExitToApp, contentDescription = "",
                                                tint = colorResource(R.color.deep_blue),
                                                modifier = Modifier.clickable {
                                                    showLogoutDialog.value = true
                                                })
                                            Spacer(Modifier.width(16.dp))
                                        }

                                    },
                                    scrollBehavior = scrollBehavior,
                                )
                            }
                        },
                        bottomBar = {
                            if(currentRoute == journalTab.title ||
                                currentRoute == calendarTab.title ||
                                currentRoute == mediaTab.title ||
                                currentRoute == atlasTab.title){
                                TabView(tabBarItems, navController)
                            }
                        }
                    ) { innerPadding->

                        NavHost(navController = navController, startDestination = journalTab.title) {

                            composable(journalTab.title) {
                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    MainJournalPage(journalViewModel, searchMode.value, navController, Modifier.fillMaxSize())
                                }
                            }
                            composable<Routes.JournalFormPage> {
                                val bundle = it.arguments
                                val args = it.toRoute<Routes.JournalDetailPage>()

                                val dateSelection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    bundle?.getParcelable("journalData", JournalData::class.java)
                                } else {
                                    bundle?.getParcelable("journalData") as? JournalData
                                }

                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    JournalForm(this@InsideActivity, navController, Modifier.fillMaxSize(), dateSelection)
                                }
                            }
                            composable<Routes.JournalDetailPage>{
                                val bundle = it.arguments
                                val args = it.toRoute<Routes.JournalDetailPage>()

                                val dateSelection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    bundle?.getParcelable("journalData", JournalData::class.java)
                                } else {
                                    bundle?.getParcelable("journalData") as? JournalData
                                }
                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    JournalDetailPage(navController, Modifier.fillMaxSize(), dateSelection?:JournalData())
                                }
                            }
                            composable<Routes.JournalListFilterPage>{
                                val args = it.toRoute<Routes.JournalListFilterPage>()

                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    MainJournalListFilterPage(journalViewModel, navController, args.dateLong, Modifier.fillMaxSize())
                                }

                            }
                            composable(calendarTab.title) {
                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    MainCalendarPage(journalViewModel, navController, Modifier.fillMaxSize())
                                }
                            }
                            composable(mediaTab.title) {

                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    MainGalleryPage(journalViewModel,navController, Modifier.fillMaxSize())
                                }
                            }
                            composable(atlasTab.title) {
                                Box(
                                    modifier = Modifier.padding(innerPadding))
                                {
                                    MainMapPage(journalViewModel, navController, Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(containerColor = colorResource(R.color.main_blue)) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },

                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title
                    )
                },

                label = {
                    if(selectedTabIndex == index){
                        Text(tabBarItem.title, color = colorResource(R.color.deep_blue),
                            fontFamily = FontFamily.Monospace)
                    }else{

                    }
                },
                colors = NavigationBarItemColors(Color.Transparent,
                    colorResource(R.color.deep_blue),
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    colorResource(R.color.deep_blue),
                )
            )
        }
    }
}

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: Int,
    unselectedIcon: Int,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Image(
            painter = painterResource(if (isSelected) {selectedIcon} else {unselectedIcon}),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
    }
}

// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}
// end of the reusable components that can be copied over to any new projects
// ----------------------------------------

// This was added to demonstrate that we are infact changing views when we click a new tab
@Composable
fun MoreView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Thing 1")
        Text("Thing 2")
        Text("Thing 3")
        Text("Thing 4")
        Text("Thing 5")
    }
}

@Preview(showBackground = true)
@Composable
fun InsidePreview() {
    MemoirAppTheme {
        MoreView()
    }
}
