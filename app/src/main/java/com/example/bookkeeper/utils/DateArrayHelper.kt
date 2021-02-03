package com.example.bookkeeper.utils

import java.text.SimpleDateFormat
import java.util.*

class DateArrayHelper {
    companion object {
        val cal: Calendar = Calendar.getInstance()
    }

    fun getYearArray(year: Int): Array<String>{
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.MONTH, 1)
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(cal.time)
        cal.set(Calendar.YEAR, year + 1)
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(cal.time)
        return arrayOf(startDate, endDate)
    }
    fun getMonthArray(year: Int, month: Int): Array<String> {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.MONTH, month)
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(cal.time)
        cal.set(Calendar.MONTH, month + 1)
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(cal.time)
        return arrayOf(startDate, endDate)
    }
}