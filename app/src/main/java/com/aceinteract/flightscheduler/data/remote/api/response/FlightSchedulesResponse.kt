package com.aceinteract.flightscheduler.data.remote.api.response

import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.google.gson.annotations.SerializedName

/**
 * Response object from flight schedules request
 */
data class FlightSchedulesResponse(

    /**
     * Flight Schedules Resource
     */
    @field:SerializedName("ScheduleResource") val scheduleResource: FlightScheduleResource

) {

    /**
     * Response object containing schedules
     */
    data class FlightScheduleResource(

        /**
         * List of returned schedules
         */
        @field:SerializedName("Schedule") val schedules: List<FlightSchedule>

    )

}