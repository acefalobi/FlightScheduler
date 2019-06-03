package com.aceinteract.flightscheduler.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.gms.maps.model.LatLng

/**
 * Utility class for management of Shared Preferences storage
 */
class StorageUtil(context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)

    /**
     * Access token saved in and retrieved from shared preferences
     */
    var accessToken: String
        get() = preferences.getString(ACCESS_TOKEN, "") ?: ""
        set(value) = preferences.edit { putString(ACCESS_TOKEN, value) }

    /**
     * Access token saved in and retrieved from shared preferences
     */
    var lastCoordinate: LatLng
        get() {
            val lat = preferences.getFloat(LAST_LATITUDE, 0f)
            val long = preferences.getFloat(LAST_LONGITUDE, 0f)
            return LatLng(lat.toDouble(), long.toDouble())
        }
        set(value) {
            preferences.edit {
                putFloat(LAST_LATITUDE, value.latitude.toFloat())
                putFloat(LAST_LONGITUDE, value.latitude.toFloat())
            }
        }

    companion object {
        private const val STORAGE = "com.aceinteract.flightscheduler.util.STORAGE"

        private const val ACCESS_TOKEN = "access_token"

        private const val LAST_LONGITUDE = "last_long"
        private const val LAST_LATITUDE = "last_lat"
    }
}