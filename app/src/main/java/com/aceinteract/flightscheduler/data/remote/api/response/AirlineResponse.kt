package com.aceinteract.flightscheduler.data.remote.api.response

import com.aceinteract.flightscheduler.data.entity.Airline
import com.google.gson.annotations.SerializedName

/**
 * Response object from airline request
 */
data class AirlineResponse(
    /**
     * Airline Resource
     */
    @field:SerializedName("AirlineResource") val resource: AirlineResource
) {

    /**
     * Response object from airline resource
     */
    data class AirlineResource(
        /**
         * Airline Resource
         */
        @field:SerializedName("Airlines") val airlineData: AirlineData
    ) {

        /**
         * Response object from airline data
         */
        data class AirlineData(
            /**
             * Returned airline
             */
            @field:SerializedName("Airline") val airline: Airline
        )

    }

}