package com.aceinteract.flightscheduler.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Utility object for simple date and time manipulations
 */
object DateTimeUtil {

    private val dateFormat = SimpleDateFormat("YYYY-MM-DD'T'HH:mm", Locale.ENGLISH)

    /**
     * Converts string to date
     */
    fun stringToDate(s: String): Calendar? = try {
        Calendar.getInstance().apply {
            time = dateFormat.parse(s)
        }
    } catch (e: ParseException) {
        null
    }
}