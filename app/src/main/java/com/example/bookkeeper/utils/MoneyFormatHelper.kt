package com.example.bookkeeper.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.text.DecimalFormat

class MoneyFormatHelper(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPrefMoneyString(amount : Double) : String {
        val decimalPlacement = preferences.getString("decimal_places","0")!!.toInt()
        var decimalString = ""
        if (decimalPlacement!=0){
            decimalString += "."
            for (i in 1..decimalPlacement) {
                decimalString += "#"
            }
        }
        return DecimalFormat("$#,###$decimalString").format(amount)
    }
}