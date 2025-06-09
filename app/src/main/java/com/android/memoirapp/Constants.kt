package com.android.memoirapp

val SUPABASE_ANON_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ6cWZ6Z2l1ZWNlYWh0dWJ5Y2d6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDgxNjU0OTgsImV4cCI6MjA2Mzc0MTQ5OH0.hMKSq1BcewiUoElmmyUR1fTS3-jSqRBlz5mRBEKWkkU"
val SUPABASE_URL="https://rzqfzgiueceahtubycgz.supabase.co"

fun buildImageUrl(imageFileName: String) =
    "$SUPABASE_URL/storage/v1/object/public/${imageFileName}"