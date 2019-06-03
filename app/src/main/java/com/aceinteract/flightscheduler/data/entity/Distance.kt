package com.aceinteract.flightscheduler.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Data object for containing distance references
 */
data class Distance(
    /**
     * Value of distance
     */
    @field:SerializedName("Value") val value: Int,

    /**
     * Unit of measurement of value
     */
    @field:SerializedName("UOM") val uom: String
) {

    override fun toString(): String = "$value${uom.toLowerCase(Locale.getDefault())}"

}