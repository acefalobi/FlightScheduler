package com.aceinteract.flightscheduler.data.adapter

import com.aceinteract.flightscheduler.util.DateTimeUtil
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

/**
 * Type adapter for flight scheduler
 */
class CalendarTypeAdapter : JsonDeserializer<Calendar> {

    /**
     * Deserialize Json
     */
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar? {

        var calendar: Calendar? = null

        json?.asString?.let { calendar = DateTimeUtil.stringToDate(it) }

        return calendar

    }


}