package com.android.memoirapp.presentation.journal_flow.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.android.memoirapp.R
import com.android.memoirapp.SupabaseClient
import com.android.memoirapp.buildImageUrl
import com.android.memoirapp.common.DateFormatter
import com.android.memoirapp.common.components.BasicTextFieldCustom
import com.android.memoirapp.common.components.LoadingDialog
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import io.ktor.http.ContentType
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun JournalForm(contextActivity: Context, navController: NavController,
                modifier: Modifier = Modifier, journalDataEdit: JournalData? = null) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    val latestLatLong = remember {
        mutableStateOf(
        if(journalDataEdit != null) LatLng(journalDataEdit.lat,
            journalDataEdit.lon) else null)
    }

    val coroutineScope = rememberCoroutineScope()
    val memoTitle = remember{
        mutableStateOf(journalDataEdit?.judul ?: "")
    }
    val titleError = remember{
        mutableStateOf(false)
    }

    val memoDesc = remember{
        mutableStateOf(journalDataEdit?.desc ?: "")
    }
    val descError = remember{
        mutableStateOf(false)
    }

    val mediaPick = remember{
        mutableStateOf("")
    }

    val locationAddress = remember{
        mutableStateOf(journalDataEdit?.locationName ?: "")
    }

    val isLoading = remember{
        mutableStateOf(false)
    }

    LoadingDialog(isLoading.value)

    val db = FirebaseFirestore.getInstance()

    @Throws(IOException::class)
    fun getMoveFileToAppFolder(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        source = FileInputStream(sourceFile).getChannel()
        destination = FileOutputStream(destFile).getChannel()
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        mediaPick.value = destFile.path
        source?.close()
        destination?.close()
    }
    fun getOutputDirectory(): File? {
        val mediaDir = contextActivity.externalMediaDirs.firstOrNull()?.let {
            File(it, contextActivity.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else null
    }

    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contextActivity.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    val mediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
            val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
                .format(System.currentTimeMillis())

            val photoFile = File(getOutputDirectory(), "rmpick_$name.jpg")

            val sourcePath = getPathFromURI(uri)
            if (sourcePath != null) {
                getMoveFileToAppFolder(File(sourcePath), photoFile)
            } else {
                Toast.makeText(contextActivity, "Error processing image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(contextActivity, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && !addresses.isEmpty()) {
                val address = addresses[0]
                return address.getAddressLine(0)
            } else {
                return "Address not found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Geocoder failed"
        }
    }

    fun getLastLocation(context: Context) {
        val locationManager =
            context.getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            Toast.makeText(context, "Please activate GPS", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latestLatLong.value = LatLng(location.latitude, location.longitude)
                locationAddress.value = getAddressFromLatLng(context, location.latitude, location.longitude)
            } else {
                Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLastLocation(contextActivity)
        } else {
            Toast.makeText(contextActivity, "Please allow location permission", Toast.LENGTH_SHORT).show()
        }
    }


    Column(modifier.background(Color.White)) {
        Row(Modifier.fillMaxWidth().background(colorResource(R.color.main_blue))
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            IconButton (onClick = {
                coroutineScope.launch {
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = colorResource(R.color.deep_blue),
                    contentDescription = "Back button"
                )
            }
            Text(DateFormatter.formatFullDate(System.currentTimeMillis()),
                modifier=Modifier.weight(1f),
                fontFamily = FontFamily.Monospace,
                color = colorResource(R.color.deep_blue)
            )
            IconButton (onClick = {
                coroutineScope.launch {
                    titleError.value = false
                    descError.value = false
                    when{
                        memoTitle.value.isBlank() -> {
                            titleError.value = true
                            Toast.makeText(contextActivity, "Enter title", Toast.LENGTH_SHORT).show()
                        }
                        memoDesc.value.isBlank() -> {
                            descError.value = true
                            Toast.makeText(contextActivity, "Enter Desc", Toast.LENGTH_SHORT).show()
                        }
                        else ->  {
                            isLoading.value = true
                            val title = memoTitle.value
                            val desc = memoDesc.value
                            val journalDb = db.collection("Journal")

                            if(journalDataEdit!=null){
                                val journalRef = journalDb.document(journalDataEdit.id)

                                if(mediaPick.value.isNotBlank()){
                                    val currentFile = File(mediaPick.value)
                                    try{
                                        val id = SupabaseClient.supabase.storage.from("memoimage").upload(
                                            journalDataEdit.id +".jpg",currentFile, true
                                        )

                                        val imageLink = buildImageUrl(
                                            "memoimage/${journalDataEdit.id}.jpg")

                                        val updates = mapOf(
                                            "judul" to title,
                                            "desc" to desc,
                                            "timeStamp" to System.currentTimeMillis(),
                                            "locationName" to locationAddress.value,
                                            "lat" to latestLatLong.value?.latitude ,
                                            "lon" to latestLatLong.value?.longitude,
                                            "imagePath" to imageLink
                                        )
                                        journalRef.update(updates)
                                            .addOnSuccessListener {
                                                isLoading.value = false
                                                Toast.makeText(contextActivity, "Journal Edited", Toast.LENGTH_SHORT).show()
                                                coroutineScope.launch {
                                                    navController.popBackStack()
                                                }
                                            }
                                            .addOnFailureListener { error ->
                                                isLoading.value = false
                                                Toast.makeText(contextActivity, "Failed edit : ${error.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }catch(e: Exception){
                                        isLoading.value = false
                                        Toast.makeText(contextActivity, "Failed create : ${e.message}", Toast.LENGTH_SHORT).show()

                                    }

                                }else{
                                    val updates = mapOf(
                                        "judul" to title,
                                        "desc" to desc,
                                        "timeStamp" to System.currentTimeMillis(),
                                        "locationName" to locationAddress.value,
                                        "lat" to latestLatLong.value?.latitude ,
                                        "lon" to latestLatLong.value?.longitude,
                                    )
                                    journalRef.update(updates)
                                        .addOnSuccessListener {
                                            isLoading.value = false
                                            Toast.makeText(contextActivity, "Journal Edited", Toast.LENGTH_SHORT).show()
                                            coroutineScope.launch {
                                                navController.popBackStack()
                                            }
                                        }
                                        .addOnFailureListener { error ->
                                            isLoading.value = false
                                            Toast.makeText(contextActivity, "Failed edit : ${error.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }else{
                                val journalRef = journalDb.document()
                                val newJournalData = JournalData(
                                    id = journalRef.id,
                                    userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                    timeStamp = System.currentTimeMillis(),
                                    judul = title,
                                    desc = desc,
                                    imagePath = "",
                                    locationName = locationAddress.value,
                                    lat = latestLatLong.value?.latitude ?: 0.0,
                                    lon = latestLatLong.value?.longitude ?: 0.0,
                                )

                                if(mediaPick.value.isNotBlank()){
                                    val currentFile = File(mediaPick.value)
                                    try{
                                        val id = SupabaseClient.supabase.storage.from("memoimage").upload(
                                            journalRef.id +".jpg",currentFile, true
                                        )

                                        val imageLink = buildImageUrl(
                                            "memoimage/${journalRef.id}.jpg")
                                        val addedJournalData = newJournalData.copy(imagePath = imageLink)
                                        journalRef.set(addedJournalData)
                                            .addOnSuccessListener {
                                                isLoading.value = false
                                                Toast.makeText(contextActivity, "Journal Created", Toast.LENGTH_SHORT).show()
                                                coroutineScope.launch {
                                                    navController.popBackStack()
                                                }
                                            }
                                            .addOnFailureListener { error ->
                                                isLoading.value = false
                                                Toast.makeText(contextActivity, "Failed create : ${error.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }catch(e: Exception){
                                        isLoading.value = false
                                        Toast.makeText(contextActivity, "Failed create : ${e.message}", Toast.LENGTH_SHORT).show()

                                    }

                                }else{
                                    journalRef.set(newJournalData)
                                        .addOnSuccessListener {
                                            isLoading.value = false
                                            Toast.makeText(contextActivity, "Journal Created", Toast.LENGTH_SHORT).show()
                                            coroutineScope.launch {
                                                navController.popBackStack()
                                            }
                                        }
                                        .addOnFailureListener {
                                            isLoading.value = false
                                            Toast.makeText(contextActivity, "Failed create : ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }


                        }

                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = colorResource(R.color.deep_blue),
                    contentDescription = "Back button"
                )
            }
        }

        Column(Modifier.fillMaxWidth().weight(1f)) {
            if(mediaPick.value.isNotBlank()){
//                AsyncImage(model =
//                    ImageRequest.Builder(contextActivity)
//                        .data(mediaPick.value)
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build(),
//                    contentDescription = "",
//                    modifier = Modifier.fillMaxWidth())
                GlideImage(model = mediaPick.value,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth())
                {
                    it.load(mediaPick.value).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                }
//                Image(rememberAsyncImagePainter(
//                    ImageRequest.Builder(contextActivity)
//                        .data(mediaPick.value)
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build()),contentDescription = null,
//                    Modifier.fillMaxWidth(),)
            }else if(journalDataEdit != null && journalDataEdit.imagePath.isNotBlank()){
                GlideImage(model = journalDataEdit.imagePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth())
                {
                    it.load(journalDataEdit.imagePath).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                }
//                AsyncImage(model =
//                    ImageRequest.Builder(contextActivity)
//                        .data(journalDataEdit.imagePath)
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build(),
//                    contentDescription = "",
//                    modifier = Modifier.fillMaxWidth())
            }
            Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())) {
                if(locationAddress.value.isNotBlank()){
                    Row (Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center){
                        Image(painterResource(R.drawable.ic_atlas_selected),
                            contentDescription = null,
                            Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(locationAddress.value,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black)
                    }
                }
                BasicTextFieldCustom(
                    memoTitle.value,
                    "Enter Title...",
                    Modifier.fillMaxWidth().padding(12.dp),
                    isHintVisible = memoTitle.value.isBlank(),
                    onValueChange = {
                        memoTitle.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    singleLine = true,
                    enabled = true,
                    onFocusChange = {},
                    isError = titleError.value
                )
                BasicTextFieldCustom(
                    memoDesc.value,
                    "Enter Desc...",
                    Modifier.fillMaxWidth().padding(12.dp),
                    isHintVisible = memoDesc.value.isBlank(),
                    onValueChange = {
                        memoDesc.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    singleLine = false,
                    enabled = true,
                    onFocusChange = {},
                    isError = descError.value,
                    alignment = Alignment.TopStart
                )
            }
        }
        Row(Modifier.fillMaxWidth().background(colorResource(R.color.blue))
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Row(Modifier.weight(1f).padding(8.dp)
                .background(colorResource(R.color.blue))
                .border(1.dp, colorResource(R.color.deep_blue),
                    RoundedCornerShape(5.dp))
                .padding(8.dp).clickable {
                    mediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Image(painterResource(R.drawable.ic_media_selected),
                    contentDescription = "",
                    Modifier.size(24.dp))
                Spacer(Modifier.width(4.dp))
                Text("Media",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp)
            }
            Row(Modifier.weight(1f).padding(8.dp)
                .background(colorResource(R.color.blue))
                .border(1.dp, colorResource(R.color.deep_blue),
                    RoundedCornerShape(5.dp))
                .padding(8.dp).clickable {
                    if (isLocationPermissionGranted(contextActivity)) {
                        getLastLocation(contextActivity)
                    } else {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center){
                Image(painterResource(R.drawable.ic_atlas_selected),
                    contentDescription = "",
                    Modifier.size(24.dp))
                Spacer(Modifier.width(4.dp))
                Text("Update Loc",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp)
            }
        }
    }



}

@Preview
@Composable
private fun JournalFormPrev() {
    MemoirAppTheme {
        JournalForm(
            LocalContext.current, rememberNavController(),
            Modifier.fillMaxSize())
    }
}