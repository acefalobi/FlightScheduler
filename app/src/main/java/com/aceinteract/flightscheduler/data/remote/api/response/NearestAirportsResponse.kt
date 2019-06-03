package com.aceinteract.flightscheduler.data.remote.api.response

import com.aceinteract.flightscheduler.data.entity.AirportRemote
import com.google.gson.annotations.SerializedName

/**
 * Response object from nearest airports request
 */
data class NearestAirportsResponse(
    /**
     * Nearest Airports Resource
     */
    @field:SerializedName("NearestAirportResource") val resource: AirportsResource
) {

    /**
     * Response object from nearest airports resource
     */
    data class AirportsResource(
        /**
         * Airports Resource
         */
        @field:SerializedName("Airports") val airportsData: AirportData
    ) {

        /**
         * Response object from airport data
         */
        data class AirportData(
            /**
             * List of returned airports
             */
            @field:SerializedName("Airport") val airports: List<AirportRemote>
        )

    }

}