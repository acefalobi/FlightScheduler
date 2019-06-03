package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.data.entity.AirportRemote
import com.aceinteract.flightscheduler.data.remote.api.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Source class for getting airport data from remote data sets.
 */
class AirportRemoteRepository(
    private val jsonSource: String, accessToken: String,
    override val apiService: ApiService = ApiService(accessToken),
    private var gson: Gson = Gson()
) : BaseRemoteRepository(accessToken) {

    /**
     * Gets and returns Access Token from API.
     */
    suspend fun getNearestAirports(latitude: Double, longitude: Double): List<AirportRemote>? {

        val response = safeApiCall { apiService.getNearestAirportsAsync(latitude, longitude).await() }

        return response?.resource?.airportsData?.airports

    }

    /**
     * Searches through json data for an airport code and returns the airport.
     */
    fun getAirport(code: String): Airport? {
        val airports = gson.fromJson<List<Airport>>(jsonSource, object : TypeToken<List<Airport>>() {}.type)

        return try {
            airports.single { it.code.equals(code, true) }
        } catch (e: NoSuchElementException) {
            null
        }

    }

    /**
     * Searches the list of all airports from json data.
     */
    fun searchAirports(queryString: String): List<Airport> {

        val airports = gson.fromJson<List<Airport>>(jsonSource, object : TypeToken<List<Airport>>() {}.type)

        return airports.filter {
            it.city.contains(
                queryString,
                true
            ) || it.country.contains(
                queryString,
                true
            ) || it.state.contains(
                queryString,
                true
            ) || it.name.contains(
                queryString,
                true
            ) || it.code.contains(
                queryString,
                true
            )
        }

    }
}