package com.android.memoirapp.presentation.map_flow.components

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.navigateWithBundle
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.journal_flow.components.JournalItem
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MainMapPage(viewModel: JournalViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val journals by viewModel.journals.collectAsState()

    val cameraPositionState = rememberCameraPositionState()

    var selectedMarkerId by remember { mutableStateOf<JournalData?>(null) }

    val validLocations = remember(journals) {
        journals.filter { it.lat != 0.0 && it.lon != 0.0 }
    }

    var isMapLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(validLocations) {
        Log.d("MainMapPage", "validLocations: ${validLocations.size}")
        validLocations.firstOrNull()?.let {
            Log.d("MainMapPage", "validLocations Lat: ${it.lat}, Lon: ${it.lon}, Judul: ${it.judul}")

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lon), 15f)
            )
        }
    }

    Box(
        modifier.background(Color.White)
    ){

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
            },
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            Log.d("MainMapPage", "Jumlah valid location: ${validLocations.size}")
            validLocations.forEach { location ->
                Log.d("MainMapPage", "Lat: ${location.lat}, Lon: ${location.lon}, Judul: ${location.judul}")
                Marker(
                    state = MarkerState(position = LatLng(location.lat, location.lon)),
                    title = location.judul,
                    onClick = {
                        val bundle = Bundle()
                        bundle.putParcelable("journalData", location)
                        coroutineScope.launch {
                            navController.navigateWithBundle(Routes.JournalDetailPage, bundle)
                        }
                        true // return true to consume the event
                    }
                )
            }
        }
    }
}
