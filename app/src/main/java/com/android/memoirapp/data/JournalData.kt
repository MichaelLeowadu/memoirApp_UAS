package com.android.memoirapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class JournalData(
    val id: String = "",
    val userId: String = "",
    val timeStamp: Long = 0L,
    val judul: String = "",
    val desc: String = "",
    val imagePath: String = "",
    val locationName: String ="",
    val lat: Double = 0.0,
    val lon: Double = 0.0
): Parcelable