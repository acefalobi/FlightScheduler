package com.aceinteract.flightscheduler.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Airport data class
 */
@Parcelize
data class Airport(
    /**
     * City where the airport is located.
     */
    val city: String,

    /**
     * 3-digit code for the airport.
     */
    val code: String,

    /**
     * Country where the airport is located.
     */
    val country: String,

    /**
     * Latitude coordinate of airport.
     */
    val latitude: String,

    /**
     * Longitude coordinate of airport.
     */
    val longitude: String,

    /**
     * Name of airport.
     */
    val name: String,

    /**
     * State where the airport is located.
     */
    val state: String
) : Parcelable