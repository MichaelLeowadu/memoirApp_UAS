package com.android.memoirapp.presentation.journal_flow.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.android.memoirapp.data.JournalData
import com.android.memoirapp.presentation.ui.theme.MemoirAppTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun JournalItem(navController: NavController, modifier: Modifier = Modifier, journalData: JournalData) {

    val context = LocalContext.current
    Card(modifier.padding(12.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardColors(colorResource(R.color.creme),
            colorResource(R.color.creme),
            colorResource(R.color.creme),
            colorResource(R.color.creme)),
        border = BorderStroke(2.dp, colorResource(R.color.deep_blue))
    ){
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(Modifier.weight(1f)) {
                Text(journalData.judul,
                    Modifier.fillMaxWidth().padding(end = 8.dp),
                    fontFamily = FontFamily.Monospace,
                    color = colorResource(R.color.deep_blue),
                    fontWeight = FontWeight.Black
                )
                Text(DateFormatter.formatDateWithShortDay(journalData.timeStamp),
                    Modifier.fillMaxWidth().padding(end = 8.dp),
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }
            GlideImage(model = journalData.imagePath.ifBlank { R.drawable.ic_logo_memoir },
                contentDescription = null,
                modifier = Modifier.weight(0.22f).aspectRatio(1f),
                contentScale = ContentScale.Crop)
            {
                it.load(journalData.imagePath.ifBlank { R.drawable.ic_logo_memoir })
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            }
//            AsyncImage(model =
//                ImageRequest.Builder(context)
//                    .data(journalData.imagePath.ifBlank { R.drawable.ic_logo_memoir })
//                    .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                    .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                    .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                    .build(),
//                contentDescription = "",
//                modifier = Modifier.fillMaxWidth())
//            Image(
//                rememberAsyncImagePainter(
//                    ImageRequest.Builder(context)
//                        .data(journalData.imagePath.ifBlank { R.drawable.ic_logo_memoir })
//                        .memoryCachePolicy(CachePolicy.DISABLED) // no memory cache
//                        .diskCachePolicy(CachePolicy.DISABLED)   // no disk cache
//                        .networkCachePolicy(CachePolicy.DISABLED) // (optional)
//                        .build()),
//                contentDescription = null,
//                modifier = Modifier.weight(0.22f).aspectRatio(1f),
//                contentScale = ContentScale.Crop
//            )
        }
    }
}

@Preview
@Composable
private fun JournalItemPrev() {
    MemoirAppTheme {
        JournalItem(rememberNavController(), Modifier.fillMaxWidth(),
            JournalData("-1","",
                System.currentTimeMillis(),
                "Judul test note",
                "Deskripsi blablabla",
                "https://images.ctfassets.net/ihx0a8chifpc/gPyHKDGI0md4NkRDjs4k8/36be1e73008a0181c1980f727f29d002/avatar-placeholder-generator-500x500.jpg?w=1920&q=60&fm=webp",
                ""))
    }
}