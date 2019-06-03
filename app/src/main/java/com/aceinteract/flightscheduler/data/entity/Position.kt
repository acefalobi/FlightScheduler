package com.aceinteract.flightscheduler.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Data class for position containing coordinated.
 */
data class Position(
    /**
     * Coordinates of position.
     */
    @field:SerializedName("Coordinate") val coordinate: Coordinate
) {

    /**
     * Data class for geo coordinates.
     */
    @Parcelize
    data class Coordinate (
        /**
         * Latitude of coordinates.
         */
        @field:SerializedName("Latitude") val lat: Double,
        /**
         * Longitude of coordinates.
         */
        @field:SerializedName("Longitude") val long: Double
    ) : Parcelable

}