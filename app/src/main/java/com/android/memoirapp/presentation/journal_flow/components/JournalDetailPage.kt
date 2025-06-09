package com.android.memoirapp.presentation.journal_flow.components

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.android.memoirapp.R
import com.android.memoirapp.common.DateFormatter
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.components.DeleteDialog
import com.android.memoirapp.common.components.LoadingDialog
import com.android.memoirapp.common.navigateWithBundle
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun JournalDetailPage(navController: NavController,
                      modifier: Modifier = Modifier, journalData: JournalData) {
    val coroutineScope = rememberCoroutineScope()

    val isLoading = remember {
        mutableStateOf(false)
    }
    val isDeleteDialog = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    DeleteDialog(isDeleteDialog.value, {
        isDeleteDialog.value = false
    }) {
        isLoading.value = true
        FirebaseFirestore.getInstance()
            .collection("Journal")
            .document(journalData.id)
            .delete()
            .addOnSuccessListener {
                isDeleteDialog.value = false
                isLoading.value = false
                Toast.makeText(context, "Journal Deleted", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    navController.popBackStack()
                }
            }
            .addOnFailureListener {
                isLoading.value = false
                Toast.makeText(context, "Error delete : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    LoadingDialog(isLoading.value)

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
            Text(
                journalData.judul,
                modifier=Modifier.weight(1f),
                fontFamily = FontFamily.Monospace,
                color = colorResource(R.color.deep_blue)
            )
            IconButton (onClick = {
                coroutineScope.launch {
                    val bundle = Bundle()
                    bundle.putParcelable("journalData", journalData)
                    navController.navigateWithBundle(Routes.JournalFormPage, bundle)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = colorResource(R.color.deep_blue),
                    contentDescription = "Edit button"
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton (onClick = {
//                coroutineScope.launch {
//                    navController.popBackStack()
//                }
                isDeleteDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = colorResource(R.color.deep_blue),
                    contentDescription = "Delete button"
                )
            }
        }
        Column(Modifier.fillMaxWidth().weight(1f)) {
            if(journalData.imagePath.isNotBlank()){
                GlideImage(model = journalData.imagePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth())
                {
                    it.load(journalData.imagePath).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                }
//                AsyncImage(model =
//                    ImageRequest.Builder(context)
//                        .data(journalData.imagePath)
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build(),
//                    contentDescription = "",
//                    modifier = Modifier.fillMaxWidth())
//                Image(rememberAsyncImagePainter(
//                    ImageRequest.Builder(context)
//                        .data(journalData.imagePath)
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build())
//                    ,contentDescription = null,
//                    Modifier.fillMaxWidth(),)
            }

            Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState())) {
                if(journalData.locationName.isNotBlank()){
                    Row (Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center){
                        Image(
                            painterResource(R.drawable.ic_atlas_selected),
                            contentDescription = null,
                            Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(journalData.locationName,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily.Monospace,
                            color = Color.Black)
                    }
                }
                Text(journalData.judul,
                    Modifier.fillMaxWidth().padding(12.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
                Text(journalData.desc,
                    Modifier.fillMaxWidth().padding(12.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
private fun JournalDetailPagePrev() {
    MemoirAppTheme {
        JournalDetailPage(rememberNavController(), Modifier.fillMaxSize(),
            JournalData("-1","",
                System.currentTimeMillis(),
                "Judul test note",
                "Deskripsi blablabla",
                "https://images.ctfassets.net/ihx0a8chifpc/gPyHKDGI0md4NkRDjs4k8/36be1e73008a0181c1980f727f29d002/avatar-placeholder-generator-500x500.jpg?w=1920&q=60&fm=webp",
                "Jalan delapan delapan no 12"))
    }
}