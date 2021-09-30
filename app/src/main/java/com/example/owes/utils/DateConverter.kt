package com.example.owes.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    fun convertDateToSimpleFormatString(date: Date): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}