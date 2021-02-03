package com.example.bookkeeper.utils

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class MyValueFormatter(context: Context): ValueFormatter() {
    private val moneyFormatHelper = MoneyFormatHelper(context)

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return moneyFormatHelper.getPrefMoneyString(value.toDouble())
    }

}