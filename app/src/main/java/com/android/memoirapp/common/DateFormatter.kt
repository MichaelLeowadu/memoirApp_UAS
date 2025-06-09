package com.android.memoirapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    fun formatDateWithShortDay(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy - E", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun formatTimeOnly(timestamp: Long): String {
        val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun formatFullDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun formatFullDayAndTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, h:mm a", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

}