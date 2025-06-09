package com.android.memoirapp.presentation.gallery_flow.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.memoirapp.R
import com.android.memoirapp.common.JournalViewModel
import com.android.memoirapp.common.Routes
import com.android.memoirapp.common.navigateWithBundle
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainGalleryPage(viewModel: JournalViewModel,  navController: NavController, modifier: Modifier = Modifier) {

    val journals by viewModel.journals.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier.background(Color.White)
    ){
        Text("Media",
            fontWeight = FontWeight.Black,
            color = colorResource(R.color.blue),
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Start,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(journals){ journal ->
                val bundle = Bundle()
                bundle.putParcelable("journalData", journal)

                GlideImage(model = journal.imagePath.ifBlank { R.drawable.ic_logo_memoir },
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                        .border(1.dp, Color.Black).clickable {
                            coroutineScope.launch {
                                navController.navigateWithBundle(Routes.JournalDetailPage, bundle)
                            }
                        }
                )
                {
                    it.load(journal.imagePath.ifBlank { R.drawable.ic_logo_memoir }).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .fitCenter()
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun MainGalleryPagePrev() {
//    MemoirAppTheme {
//        MainGalleryPage(rememberNavController(),
//            Modifier.fillMaxSize())
//    }
//}