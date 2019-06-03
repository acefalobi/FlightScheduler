package com.aceinteract.flightscheduler.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Data object for containing airport response references
 */
data class AirportRemote(
    /**
     * 3-letter code for the airport
     */
    @field:SerializedName("AirportCode") val airportCode: String,

    /**
     * Positional coordinates of the airport
     */
    @field:SerializedName("Position") val position: Position,

    /**
     * Names of the airport in different locales
     */
    @field:SerializedName("Names") val names: Name.Names,

    /**
     * Distance of the airport from the given coordinates
     */
    @field:SerializedName("Distance") val distance: Distance? = null
)