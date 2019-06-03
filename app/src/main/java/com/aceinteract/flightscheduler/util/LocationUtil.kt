package com.aceinteract.flightscheduler.util

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

/**
 * Utility class to manage location callbacks and operations
 */
class LocationUtil(private val context: Context) {

    private val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest().apply {
        interval = 5 * 1000
        fastestInterval = 1 * 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    /**
     * Checks if GPS is enabled, and shows a dialog if it is not
     */
    fun checkLocationSettings(successCallback: (Task<Location>) -> Unit) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { successCallback(locationClient.lastLocation) }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        context as Activity,
                        REQUEST_CHECK_SETTINGS

                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    companion object {
        /**
         * Request code for checking GPS settings
         */
        const val REQUEST_CHECK_SETTINGS = 420
    }

}