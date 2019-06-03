package com.aceinteract.flightscheduler.data.adapter

import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

/**
 * Type adapter for flight scheduler
 */
class FlightScheduleTypeAdapter : JsonDeserializer<FlightSchedule> {

    /**
     * Deserialize Json
     */
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): FlightSchedule? {

        val gson = GsonBuilder()
            .registerTypeAdapter(
                Calendar::class.java,
                CalendarTypeAdapter()
            )
            .create()

        var schedule: FlightSchedule? = null

        json?.asJsonObject?.let {
            val flights = mutableListOf<FlightSchedule.Flight>()

            it.get("Flight").let { flightJson ->

                if (flightJson is JsonArray) {
                    val listType = object : TypeToken<List<FlightSchedule.Flight>?>() {}.type
                    flights.addAll(gson.fromJson(flightJson, listType))
                } else {
                    val listType = object : TypeToken<FlightSchedule.Flight?>() {}.type
                    val flight = gson.fromJson<FlightSchedule.Flight>(flightJson, listType)
                    flights.add(flight)
                }

            }

            schedule = FlightSchedule(flights)
        }

        return schedule

    }


}