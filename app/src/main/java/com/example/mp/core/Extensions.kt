package com.example.mp.core

import android.text.format.DateFormat
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

fun currentTime(): Long = System.currentTimeMillis()

fun getHourAndMinutesFromDate(date: Date): String {
    var result: String? = null
    result = DateFormat.format("HH", date) as String?
    result = "$result:${DateFormat.format("mm", date)}"
    return result
}

fun toDate(dateFormat: SimpleDateFormat, timestamp: Long): Date {
    val dateString: String = dateFormat.format(Date(timestamp))
    return dateFormat.parse(dateString) as Date
}

fun log(tag: String, message: Any){
    Log.d(tag, "Log: $message")
}