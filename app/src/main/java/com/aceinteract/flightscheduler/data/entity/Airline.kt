package com.aceinteract.flightscheduler.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Data class for airline object.
 */
data class Airline(
    /**
     * 2-letter code for the airline
     */
    val airlineID: String,

    /**
     * Names of the airline in different locales
     */
    @field:SerializedName("Names") val names: Name.SingleName

)